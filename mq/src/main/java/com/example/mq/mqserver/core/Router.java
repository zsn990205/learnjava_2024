package com.example.mq.mqserver.core;

import com.example.mq.common.MqException;

/*
实现交换机的转发规则
验证BindingKey是否是合法的
 */
public class Router {
    // bindingKey 的构造规则:
    // 1. 数字, 字母, 下划线
    // 2. 使用 . 分割成若干部分
    // 3. 允许存在 * 和 # 作为通配符. 但是通配符只能作为独立的分段.
    public boolean checkBindingKey(String bindingKey) {
        if(bindingKey.length() == 0) {
            //空字符串 合法情况->使用direct/fanout交换机的时候
            return true;
        }
        for(int i = 0; i < bindingKey.length(); i++) {
            char ch = bindingKey.charAt(i);
            if(ch >= '0' && ch <= '9') {
                continue;
            }
            if(ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if(ch >= 'a' && ch <= 'z') {
                continue;
            }
            if(ch == '.' || ch == '*' || ch == '#' || ch == '_') {
                continue;
            }
            return false;
        }
        //检查* # 是否是独立的部分(独立的部分才行)
        // aaa.*.bbb 合法情况;  aaa.a*.bbb 非法情况.
        //.在正则表达式是一个特殊的符号 此处是想要用.分割
        //要想使用.的原始文本就需要转义 正则表达式中就需要使用\.的方式表示
        String[] words = bindingKey.split("\\.");
        for(String word : words) {
            // 检查 word 长度 > 1 并且包含了 * 或者 # , 就是非法的格式了.
            if(word.length() > 1 && (word.contains("*") || word.contains("#"))) {
                return false;
            }
        }
        // 约定一下, 通配符之间的相邻关系(人为(俺)约定的).
        // 为啥这么约定? 因为前三种相邻的时候, 实现匹配的逻辑会非常繁琐, 同时功能性提升不大~~
        // 1. aaa.#.#.bbb    => 非法
        // 2. aaa.#.*.bbb    => 非法
        // 3. aaa.*.#.bbb    => 非法
        //因为前三种方式实现起来过于复杂所以不使用

        // 4. aaa.*.*.bbb    => 合法
        for(int i = 0; i < words.length - 1; i++) {
            //两个连着的#
            if(words[i].equals("#") && words[i + 1].equals("#")) {
                return false;
            }
            //前面是#后面是*
            if(words[i].equals("#") && words[i + 1].equals("*")) {
                return false;
            }
            //前面是*后面是#
            if(words[i].equals("*") && words[i + 1].equals("#")) {
                return false;
            }
        }
        return true;
    }

    // routingKey 的构造规则:
    //    // 1. 数字, 字母, 下划线
    //    // 2. 使用 . 分割成若干部分
    public boolean checkRoutingKey(String routingKey) {
        if(routingKey.length() == 0) {
            //空字符串 合法情况->使用fanout交换机的时候
            return true;
        }
        for(int i = 0; i < routingKey.length(); i++) {
            char ch = routingKey.charAt(i);
            //判定该字符是否是大写字符
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            //判定该字符是否是小写字符
            if(ch >= 'a' && ch <= 'z') {
                continue;
            }
            //判定该字符是否是数字
            if(ch >= '0' && ch <= '9') {
                continue;
            }
            //判定该字符是否是下划线 或者.
            if(ch == '_' || ch == '.') {
                continue;
            }
            return false;
        }
        //字符串全部检查完毕 在返回true
        return true;
    }

    // 这个方法用来判定该消息是否可以转发给这个绑定对应的队列.
    public boolean route(ExchangeType exchangeType, Binding binding, Message message) throws MqException {
        //根据不同的exchangeType使用不同的转发规则
        if(exchangeType == ExchangeType.FANOUT) {
            //如果是Fanout交换机 无脑转发
            return true;
        } else if(exchangeType == ExchangeType.TOPIC) {
            //topic主题交换机 更复杂一点
            return routeTopic(binding,message);
        } else {
            //其他情况不应该存在的
            throw new MqException("[Router] 交换机类型非法! exchangeType=" + exchangeType);
        }
    }

    // [测试用例]
    // binding key          routing key         result
    // aaa                  aaa                 true
    // aaa.bbb              aaa.bbb             true
    // aaa.bbb              aaa.bbb.ccc         false
    // aaa.bbb              aaa.ccc             false
    // aaa.bbb.ccc          aaa.bbb.ccc         true
    // aaa.*                aaa.bbb             true
    // aaa.*.bbb            aaa.bbb.ccc         false
    // *.aaa.bbb            aaa.bbb             false
    // #                    aaa.bbb.ccc         true
    // aaa.#                aaa.bbb             true
    // aaa.#                aaa.bbb.ccc         true
    // aaa.#.ccc            aaa.ccc             true
    // aaa.#.ccc            aaa.bbb.ccc         true
    // aaa.#.ccc            aaa.aaa.bbb.ccc     true
    // #.ccc                ccc                 true
    // #.ccc                aaa.bbb.ccc         true

    //针对routeTopic 进行测试

    private boolean routeTopic(Binding binding, Message message) {
        //先按照.对字符串进行分割 分割成单个部分的数组
        String[] bindingTokens = binding.getBindingKey().split("\\.");
        String[] routingTokens = message.getRoutingKey().split("\\.");

        //引入两个下标 指向上述两个数组 初始情况下均为0
        int bindingIndex = 0;
        int routingIndex = 0;
        //此处使用while更为稳妥一点 因为每次移动的下标不一定 + 1  不适合使用for
        while (bindingIndex < bindingTokens.length && routingIndex < routingTokens.length) {
            if(bindingTokens[bindingIndex].equals("*")) {
                //如果匹配到 * 直接进入下一轮 *可以匹配任何一个部分
                bindingIndex++;
                routingIndex++;
                continue;
            } else if(bindingTokens[bindingIndex].equals("#")) {
                //如果匹配到 # 就要判断 # 后面还有没有内容 如果没有直接返回true
                bindingIndex++;
                if(bindingIndex == bindingTokens.length) {
                    return true;
                }
                //#后面还有内容 拿着这个内容去routingKey中找 找到相应的位置
                //findNextMatch 这个方法用来查找该部分在routingKey中的位置 返回该下标 没找到的话 返回-1
                routingIndex = findNextMatch(routingTokens,routingIndex,bindingTokens[bindingIndex]);
                if(routingIndex == -1) {
                    //没找到匹配的结果 匹配失败
                    return false;
                }
                //找到匹配的情况继续往后匹配
                bindingIndex++;
                routingIndex++;
            } else {
                //如果遇到普通字符串 要求两边的内容是一样的
                if(!(bindingTokens[bindingIndex].equals(routingTokens[routingIndex]))) {
                    //如果两个字符串的内容不相等 直接return false
                    return false;
                }
                //此时两个内容是相同的 此时继续往后找下面的部分
                bindingIndex++;
                routingIndex++;
            }
        }
        //移动的时候两个同时到达末尾 返回true 反之false
        //比如 aaa bbb ccc 和 aaa bbb 匹配失败
        if(bindingIndex == bindingTokens.length && routingIndex == routingTokens.length) {
            return true;
        }
        return false;
    }

    private int findNextMatch(String[] routingTokens, int routingIndex, String bindingToken) {
        //必须得从匹配之后的位置开始找
        //在前面找到也是没有用的
        //比如aaa bbb # ccc
        //
        for(int i = routingIndex; i < routingTokens.length; i++) {
            if(routingTokens[i].equals(bindingToken)) {
                return i;
            }
        }
        return -1;
    }
}
