package com.example.mq.mqserver.datacenter;

import com.example.mq.common.BinaryTool;
import com.example.mq.common.MqException;
import com.example.mq.mqserver.core.MSGQueue;
import com.example.mq.mqserver.core.Message;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/*

 */
public class MessageFileManager {
    //定义一个内部类表示队列进行统计信息
    //有限考虑使用静态内部类
    // 2000/t1500
    static public class Stat {
        public int totalCount;  //总的消息数量
        public int validCount;  //有效消息量
    }

    //暂时不需要初始化操作
    public void init() {
        //以备后续扩展
    }

    //约定消息文件所在的目录及其文件名
    //用来获取指定队列对应的消息文件所在的路径
    private String getQueueDir(String queueName) {
        return "./data/" + queueName;
    }

    //这个方法用来获取队列的消息数据文件路径
    //二进制文件使用txt作为后缀不太合适 应该使用.dat/.bin
    private String getQueueDataPath(String queueName) {
        return getQueueDir(queueName) + "/queue_data.txt";
    }

    //这个方法用来获取该消息队列的消息统计文件路径
    private String getQueueStatPath(String queueName) {
        return getQueueDir(queueName) + "/queue_stat.txt";
    }

    private Stat readStat(String queueName) {
        Stat stat = new Stat();
        try(InputStream inputStream = new FileInputStream(getQueueStatPath(queueName))) {
            Scanner scanner = new Scanner(inputStream);
            stat.totalCount = scanner.nextInt();
            stat.validCount = scanner.nextInt();
            return stat;
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeStat(String queueName,Stat stat) {
        //outputstream会把旧的文件删除覆盖成新的文件
        try(OutputStream outputStream = new FileOutputStream(getQueueStatPath(queueName))) {
            PrintWriter printWriter = new PrintWriter(outputStream);
            // 2000\t1500
            printWriter.write(stat.totalCount + "\t" + stat.validCount);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建队列对应的文件和目录
    public void createQueueFiles(String queueName) throws IOException {
        //1.先创建队列对应的消息目录
        File baseDir = new File(getQueueDir(queueName));
        if(!baseDir.exists()) {
            //如果不存在的话就创建这个目录
            boolean ok = baseDir.mkdirs();
            if(!ok) {
                throw new IOException("创建目录失败! baseDir " + baseDir.getAbsolutePath());
            }
        }
        //2.创建队列数据文件
        File queueDataFile = new File(getQueueDataPath(queueName));
        if(!queueDataFile.exists()) {
            boolean ok = queueDataFile.createNewFile();
            if(!ok) {
                throw new IOException("创建文件失败! queueDataFile " + queueDataFile.getAbsolutePath());
            }

        }
        //3.创建消息统计文件
        File queueStatFile = new File(getQueueStatPath(queueName));
        if(!queueStatFile.exists()) {
            boolean ok = queueStatFile.createNewFile();
            if(!ok) {
                throw new IOException("创建文件失败! queueStatFile " + queueStatFile.getAbsolutePath());
            }
        }
        //4.给消息统计文件设定初始值 0\t0
        Stat stat = new Stat();
        stat.totalCount = 0;
        stat.validCount = 0;
        writeStat(queueName,stat);
    }

    //删除队列的目录和文件
    //队列也是可以被删除的,当队列删除之后,对应的消息文件啥的也会随之删除
    public void destroyQueueFiles(String queueName) throws IOException {
        //先删除文件 在删除目录
        File queueDataFile = new File(getQueueDataPath(queueName));
        boolean ok1 = queueDataFile.delete();
        File queueStatFile = new File(getQueueStatPath(queueName));
        boolean ok2 = queueStatFile.delete();
        File baseDir = new File(getQueueDir(queueName));
        boolean ok3 = baseDir.delete();
        if(!ok1 || !ok2 || !ok3) {
            //有任意删除失败都算整体删除失败
            throw new IOException("删除队列目录和文件失败! baseDir = " + baseDir.getAbsolutePath());
        }
    }

    //检查队列的目录和文件是否存在
    //比如后续有生产者给broker server生产消息了 这个消息就可能记录在文件上(取决于消息是否需要持久化)
    public boolean checkFileExists(String queueName) {
        //判定队列的数据文件和统计文件是否都存在
        File dataFile = new File(getQueueDataPath(queueName));
        if(!dataFile.exists()) {
            return false;
        }
        File statFile = new File(getQueueStatPath(queueName));
        if(!statFile.exists()) {
            return false;
        }
        return true;
    }

    //这个方法用来把一个新的消息放到队列对应的文件中
    //queue表示要把消息写入的队列 message是要写的消息
    public void sendMessage(MSGQueue queue, Message message) throws MqException, IOException {
        // 1. 检查一下当前要写入的队列对应的文件是否存在.
        if (!checkFileExists(queue.getName())) {
            throw new MqException("[MessageFileManager] 队列对应的文件不存在! queueName=" + queue.getName());
        }
        // 2. 把 Message 对象, 进行序列化, 转成二进制的字节数组.
        byte[] messageBinary = BinaryTool.toBytes(message);
        synchronized (queue) {
            // 3. 先获取到当前的队列数据文件的长度, 用这个来计算出该 Message 对象的 offsetBeg 和 offsetEnd
            // 把新的 Message 数据, 写入到队列数据文件的末尾. 此时 Message 对象的 offsetBeg , 就是当前文件长度 + 4
            // offsetEnd 就是当前文件长度 + 4 + message 自身长度.
            File queueDataFile = new File(getQueueDataPath(queue.getName()));
            // 通过这个方法 queueDataFile.length() 就能获取到文件的长度. 单位字节.
            message.setOffsetBeg(queueDataFile.length() + 4);
            message.setOffsetEnd(queueDataFile.length() + 4 + messageBinary.length);
            // 4. 写入消息到数据文件, 注意, 是追加写入到数据文件末尾.
            try (OutputStream outputStream = new FileOutputStream(queueDataFile, true)) {
                try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                    // 接下来要先写当前消息的长度, 占据 4 个字节的~~
                    dataOutputStream.writeInt(messageBinary.length);
                    // 写入消息本体
                    dataOutputStream.write(messageBinary);
                }
            }
            // 5. 更新消息统计文件
            Stat stat = readStat(queue.getName());
            stat.totalCount += 1;
            stat.validCount += 1;
            writeStat(queue.getName(), stat);
        }
    }

    //参数分别代表哪个消息队列以及具体的消息是什么
    //删除是逻辑删除 1->0
    //这里是删除数据
    //1.先把文件中数据读取出来 还原回message对象
    //2.把isValid改为0  只是在文件中标识消息
    //3.把上述数据重新写回到文件
    //此处message对象必须包含offsetBeg offsetEnd
    //此处需要的是在文件的指定位置进行读写 随机访问 此处用到的类是RandomAccessFile (很像游戏中的闪现功能)
    //seek调整文件光标移动
    public void deleteMessage(MSGQueue queue, Message message) throws IOException, ClassNotFoundException {
        synchronized (queue) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(getQueueDataPath(queue.getName()), "rw")) {
                // 1. 先从文件中读取对应的 Message 数据.
                byte[] bufferSrc = new byte[(int) (message.getOffsetEnd() - message.getOffsetBeg())];
                randomAccessFile.seek(message.getOffsetBeg());
                randomAccessFile.read(bufferSrc);
                // 2. 把当前读出来的二进制数据, 转换回成 Message 对象
                Message diskMessage = (Message) BinaryTool.fromBytes(bufferSrc);
                // 3. 把 isValid 设置为无效.
                diskMessage.setIsValid((byte) 0x0);
                // 此处不需要给参数的这个 message 的 isValid 设为 0, 因为这个参数代表的是内存中管理的 Message 对象
                // 而这个对象马上也要被从内存中销毁了.
                // 4. 重新写入文件
                byte[] bufferDest = BinaryTool.toBytes(diskMessage);
                // 虽然上面已经 seek 过了, 但是上面 seek 完了之后, 进行了读操作, 这一读, 就导致, 文件光标往后移动, 移动到
                // 下一个消息的位置了. 因此要想让接下来的写入, 能够刚好写回到之前的位置, 就需要重新调整文件光标.
                randomAccessFile.seek(message.getOffsetBeg());
                randomAccessFile.write(bufferDest);
                // 通过上述这通折腾, 对于文件来说, 只是有一个字节发生改变而已了~~
            }
            // 不要忘了, 更新统计文件!! 把一个消息设为无效了, 此时有效消息个数就需要 - 1
            Stat stat = readStat(queue.getName());
            if (stat.validCount > 0) {
                stat.validCount -= 1;
            }
            writeStat(queue.getName(), stat);
        }
    }

    //使用这个方法从文件中读取所有的消息内容 加载到内存中(放入链表中)
    //程序启动时进行调用  使用LinkedList是为了后续进行头删头插 所以不适用ArrayList
    public LinkedList<Message> loadAllMessageFromQueue(String queueName) throws IOException, MqException,ClassNotFoundException {
        LinkedList<Message> messages = new LinkedList<>();
        //使用以下对象进行读操作
        try(InputStream inputStream = new FileInputStream(getQueueDataPath(queueName))) {
            try(DataInputStream dataInputStream = new DataInputStream(inputStream)) {
                //文件中包含很多消息 所以需要循环读取
                long currentOffset = 0;
                while(true) {
                    //1.读取当前消息的长度 readInt可能会读到文件末尾 读到末尾会抛出异常(EOF)
                    int messageSize = dataInputStream.readInt();
                    //2.按照这个长度 读取消息的内容
                    byte[] buffer = new byte[messageSize];
                    //这个表示读取的实际长度
                    int actualSize = dataInputStream.read(buffer);
                    if(messageSize != actualSize) {
                        //不匹配说明文件有问题 格式错乱了
                        throw new MqException("[MessageFileManager 文件格式错误! queueName=]" + queueName);
                    }
                    //3.将读取的二进制数据反序列化回message对象
                    Message message = (Message)BinaryTool.fromBytes(buffer);
                    //4.判定一下这个消息对象是不是无效消息对象
                    if(message.getIsValid() != 0x1) {
                        //无效数据直接跳过
                        currentOffset += (messageSize + 4);
                        continue;
                    }
                    //5.有效数据需要把message对象加入到链表中 加之前还需要填写offsetBeg和offsetEnd
                    //计算的时候需要知道当前的光标位置 需要手动计算文件光标的位置
                    //初始为0 读了一次所以+4
                        message.setOffsetBeg(currentOffset + 4);
                        message.setOffsetEnd(currentOffset + 4 + messageSize);
                        currentOffset += (4 + messageSize);
                        messages.add(message);

                }
            } catch (EOFException e) {
               //此处的catch并非真的处理异常 而是处理正常的业务逻辑 文件读到末尾的时候
               //会抛出readInt的异常
                System.out.println("[MessageFileManager] 恢复Message数据完成!");
            }
        }
        return messages;
    }

    //检查当前是否要针对该队列的消息数据文件进行GC
    public boolean checkGC(String queueName) {
        //判断是否要GC是根据总消息数和有效消息数
        Stat stat = readStat(queueName);
        if(stat.totalCount > 2000 && (double)(stat.validCount / stat.totalCount) < 0.5) {
            return true;
        }
        return false;
    }

    private String getQueueDataNewPath(String queueName) {
        return getQueueDir(queueName) + "/queue_data_new.txt";
    }
    //通过这个方法 真正执行消息数据文件的垃圾回收操作
    //使用复制算法完成这个操作
    //创建一个新的文件 文件的名称是queue_data_new.txt
    //把之前的消息数据文件的有效消息都读取出来 写到新的文件中
    //删除旧的文件 再把新的文件名称改为queue_data.txt
    //同时要记得更新消息统计文件
    public void gc(MSGQueue queue) throws IOException, ClassNotFoundException, MqException {
//        //gc时候要对消息数据文件进行大洗牌 在这个过程其他线程不能针对该队列进行修改所以要进行加锁操作
//        synchronized (queue) {
//            long gcBeg = System.currentTimeMillis();
//            //1.创建一个新的文件
//            File queueDataNewFile = new File(getQueueDataNewPath(queue.getName()));
//            if(queueDataNewFile.exists()) {
//                //如果文件存在就是意外了
//                throw new MqException("[MessageFileManager] gc时发现该队列的queue_data_new已经存在了! queueName =" + queue.getName());
//            }
//            boolean ok = queueDataNewFile.createNewFile();
//            if(!ok) {
//                throw new MqException("[MessageFileManager] 创建新的文件失败! queueDataNewFile =" + queueDataNewFile.getAbsolutePath());
//            }
//            //2.从旧的文件中读取出有效消息对象
//            LinkedList<Message> messages = loadAllMessageFromQueue(queue.getName());
//            //3.把有效消息写入新的文件中
//            try(OutputStream outputStream = new FileOutputStream(queueDataNewFile)) {
//                try(DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
//                    for(Message message : messages) {
//                        byte[] buffer = BinaryTool.toBytes(message);
//                        //先写四个字节消息的长度
//                        dataOutputStream.writeInt(buffer.length);
//                        dataOutputStream.write(buffer);
//                    }
//                }
//            }
//            //4.删除旧的文件 并把新的文件进行重命名
//            File queueDataOldFile = new File(getQueueDataPath(queue.getName()));
//            ok = queueDataOldFile.delete();
//            if(!ok) {
//                throw new MqException("[MessageFileManager] 删除旧的文件失败! queueDataOldFile =" + queueDataOldFile.getAbsolutePath());
//            }
//            ok = queueDataNewFile.renameTo(queueDataOldFile);
//            if(!ok) {
//                throw new MqException("[MessageFileManager] 文件重命名失败! queueDataNewFile =" + queueDataNewFile.getAbsolutePath() +
//                        ", queueDataOldFile" + queueDataOldFile.getAbsolutePath());
//            }
//            //5.更新统计文件
//            Stat stat = readStat(queue.getName());
//            stat.totalCount = messages.size();
//            stat.validCount = messages.size();
//            writeStat(queue.getName(),stat);
//
//            long gcEnd = System.currentTimeMillis();
//            System.out.println("[MessageManager] gc执行完毕! queueName=" + queue.getName() +
//                    ",time=" + (gcEnd -gcBeg) + "ms");
//        }
        // 进行 gc 的时候, 是针对消息数据文件进行大洗牌. 在这个过程中, 其他线程不能针对该队列的消息文件做任何修改.
        synchronized (queue) {
            // 由于 gc 操作可能比较耗时, 此处统计一下执行消耗的时间.
            long gcBeg = System.currentTimeMillis();

            // 1. 创建一个新的文件
            File queueDataNewFile = new File(getQueueDataNewPath(queue.getName()));
            if (queueDataNewFile.exists()) {
                // 正常情况下, 这个文件不应该存在. 如果存在, 就是意外~~ 说明上次 gc 了一半, 程序意外崩溃了.
                throw new MqException("[MessageFileManager] gc 时发现该队列的 queue_data_new 已经存在! queueName=" + queue.getName());
            }
            boolean ok = queueDataNewFile.createNewFile();
            if (!ok) {
                throw new MqException("[MessageFileManager] 创建文件失败! queueDataNewFile=" + queueDataNewFile.getAbsolutePath());
            }

            // 2. 从旧的文件中, 读取出所有的有效消息对象了. (这个逻辑直接调用上述方法即可, 不必重新写了)
            LinkedList<Message> messages = loadAllMessageFromQueue(queue.getName());

            // 3. 把有效消息, 写入到新的文件中.
            try (OutputStream outputStream = new FileOutputStream(queueDataNewFile)) {
                try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                    for (Message message : messages) {
                        byte[] buffer = BinaryTool.toBytes(message);
                        // 先写四个字节消息的长度
                        dataOutputStream.writeInt(buffer.length);
                        dataOutputStream.write(buffer);
                    }
                }
            }

            // 4. 删除旧的数据文件, 并且把新的文件进行重命名
            File queueDataOldFile = new File(getQueueDataPath(queue.getName()));
            ok = queueDataOldFile.delete();
            if (!ok) {
                throw new MqException("[MessageFileManager] 删除旧的数据文件失败! queueDataOldFile=" + queueDataOldFile.getAbsolutePath());
            }
            // 把 queue_data_new.txt => queue_data.txt
            ok = queueDataNewFile.renameTo(queueDataOldFile);
            if (!ok) {
                throw new MqException("[MessageFileManager] 文件重命名失败! queueDataNewFile=" + queueDataNewFile.getAbsolutePath()
                        + ", queueDataOldFile=" + queueDataOldFile.getAbsolutePath());
            }

            // 5. 更新统计文件
            Stat stat = readStat(queue.getName());
            stat.totalCount = messages.size();
            stat.validCount = messages.size();
            writeStat(queue.getName(), stat);

            long gcEnd = System.currentTimeMillis();
            System.out.println("[MessageFileManager] gc 执行完毕! queueName=" + queue.getName() + ", time="
                    + (gcEnd - gcBeg) + "ms");
        }
    }
}
