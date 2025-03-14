package com.example.onlinemusic.controller;

import com.example.onlinemusic.mapper.LoveMusicMapper;
import com.example.onlinemusic.mapper.MusicMapper;
import com.example.onlinemusic.model.Music;
import com.example.onlinemusic.model.User;
import com.example.onlinemusic.tools.Constant;
import com.example.onlinemusic.tools.ResponseBodyMessage;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {
    @Value("${music.local.path}")
    private String SAVE_PATH;

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> insertMusic(@RequestParam String singer,
                                                    @RequestParam("filename") MultipartFile file,
                                                    HttpServletRequest request, HttpServletResponse resp) throws IOException {
        //1、检查是否登录了
        HttpSession httpSession = request.getSession(false);
        if(httpSession == null || httpSession.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            System.out.println("没有登录！");
            return new ResponseBodyMessage<>(-1,"请登录后上传",false);
        }

        //2、先查询数据库当中 是否有当前音乐【title歌曲名+singer歌手】
        //一样的时候不能上传
        String allTitle = file.getOriginalFilename();
        int i = allTitle.lastIndexOf(".");
        String t = allTitle.substring(0,i);
        Music music = musicMapper.selectByNameAndSinger(t,singer);
        if(music != null) {
            return new ResponseBodyMessage<>(-1,"当前歌曲已经存在!请勿重复上传!",false);
        }

        //2、上传到了服务器
        String fileNameAndType = file.getOriginalFilename(); //xxx.mp3
        System.out.println("fileNameAndType: " + fileNameAndType);
        String path = SAVE_PATH + fileNameAndType;

        File dest = new File(path);
        if(!dest.exists()) {
            dest.mkdir();
        }
        try {
            file.transferTo(dest);
            //return new ResponseBodyMessage<>(0,"服务器上传成功！",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBodyMessage<>(-1,"服务器上传失败!",false);
        }

        //进行数据库的上传
        //准备数据 调用insert
        int index = fileNameAndType.lastIndexOf(".");
        String title = fileNameAndType.substring(0,index);

        User user = (User)httpSession.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userid = user.getId();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sf.format(new Date());

        String url = "/music/get?path="+title;
        try{
            int ret = musicMapper.insert(title,singer,time,url,userid);
            //ret表示的是当前插入影响的行数
            //如果当前影响的行数为1表示插入成功 反之表示插入失败
            if(ret == 1) {
                resp.sendRedirect("/list.html");
                return new ResponseBodyMessage<>(0,"数据库上传成功！",true);
            } else {
                return new ResponseBodyMessage<>(-1,"数据库上传失败！",false);
            }
        } catch (BindingException e) {
            //失败的话对服务器的数据进行删除!
            dest.delete();
            return new ResponseBodyMessage<>(-1,"数据库上传失败！",false);
        }
    }

    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(@RequestParam("path") String path) {
        File file = new File(SAVE_PATH+path);
        byte[] a = null;
        try {
            a = Files.readAllBytes(file.toPath());
            if(a == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }


    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteMusic(@RequestParam String id) {
        //1.查找当前要删除的音乐是否存在
        int iid = Integer.parseInt(id);
        Music music = musicMapper.findMusicById(iid);
        if(music == null) {
            System.out.println("没有你要删除的音乐!");
            return new ResponseBodyMessage<>(-1,"当前查找的音乐不存在!",false);
        } else {
            //查找的音乐存在
            //对音乐进行删除
            int ret = musicMapper.deleteMusicById(iid);
            if(ret == 1) {
                //删除服务器上的数据
                int index = music.getUrl().lastIndexOf("=");
                String fileName = music.getUrl().substring(index + 1);
                //此处的SAVE_PATH和fileName之间不要加空格 会导致路径错误
                File file = new File(SAVE_PATH +fileName+".mp3");
                System.out.println("当前的路径为: " + file.getPath());
                if(file.delete()) {
                    //同步删除lovemusic中的id
                    loveMusicMapper.deleteLoveMusicByMusicId(iid);
                    return new ResponseBodyMessage<>(0,"服务器音乐删除成功!",true);
                } else {
                    return new ResponseBodyMessage<>(-1,"服务器音乐删除失败!",false );
                }
            } else {
                return new ResponseBodyMessage<>(-1,"数据库音乐未成功删除!",false);
            }
        }
    }

    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteSelMusic(@RequestParam("id[]") List<Integer> id) {
        int sum = 0;
        for(int i = 0; i < id.size(); i++) {
            Music music = musicMapper.findMusicById(id.get(i));
            if(music == null) {
                System.out.println("没有你要删除的音乐!");
                return new ResponseBodyMessage<>(-1,"当前查找的音乐不存在!",false);
            }
                int ret = musicMapper.deleteMusicById(id.get(i));
                if(ret == 1) {
                    int index = music.getUrl().lastIndexOf("=");
                    String fileName = music.getUrl().substring(index + 1);
                    File file = new File(SAVE_PATH +fileName+".mp3");
                    System.out.println("当前的路径为: " + file.getPath());
                    if(file.delete()) {
                        //同步检查lovemusic中的music是否被删除
                        loveMusicMapper.deleteLoveMusicByMusicId(id.get(i));
                        sum += ret;
                    } else {
                            return new ResponseBodyMessage<>(-1,"服务器音乐删除失败!",false);
                        }
                    } else {
                        return new ResponseBodyMessage<>(-1,"数据库音乐删除失败!",false);
                    }
                }
                if(sum == id.size()) {
                    System.out.println("当前音乐全部删除成功!");
                    return new ResponseBodyMessage<>(0,"服务器音乐全部删除成功!",true);
            } else {
                    System.out.println("当前音乐删除失败!");
                    return new ResponseBodyMessage<>(-1,"删除失败!",false);
            }
    }

    @RequestMapping("/findmusic")
    public ResponseBodyMessage<List<Music>> findMusic(@RequestParam(required = false) String musicName) {
        List<Music> musicList = null;
        if(musicName != null) {
            //当参数中的musicName不为空的时候 此时查询到数据库中指定名字的   音乐
            musicList = musicMapper.findMusicByName(musicName);
        } else {
            //当参数中的musicName为空的时候 此时查询到数据库中的所有音乐
            musicList = musicMapper.findMusic();
        }
        return new ResponseBodyMessage<>(0,"查询到了音乐!",musicList);
    }
}
