package com.example.onlinemusic.controller;

import com.example.onlinemusic.mapper.LoveMusicMapper;
import com.example.onlinemusic.model.Music;
import com.example.onlinemusic.model.User;
import com.example.onlinemusic.tools.Constant;
import com.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {
    @Resource
    private LoveMusicMapper loveMusicMapper;

    @RequestMapping("/likemusic")
    public ResponseBodyMessage<Boolean> likeMusic(@RequestParam String id, HttpServletRequest request) {
        //转换成int类型的Id
        int musicId = Integer.parseInt(id);
        //1.查询这个音乐之前是否被收藏过
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY ) == null) {
            System.out.println("当前未登录");
            return new ResponseBodyMessage<>(-1,"当前未登录!",false);
        }
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY );
        int userId = user.getId();
        Music music = loveMusicMapper.findLoveMusic(userId,musicId);
        if(music != null) {
            //取消收藏!
            return new ResponseBodyMessage<>(-1,"早先收藏过音乐!",false);
        } else {
            boolean flg = loveMusicMapper.insertLoveMusic(userId,musicId);
            if(flg) {
                return new ResponseBodyMessage<>(0,"收藏喜欢的音乐成功!",true);
            }
            return new ResponseBodyMessage<>(-1,"收藏喜欢的音乐失败!",false);
        }
    }

    @RequestMapping("/findlovemusic")
    public ResponseBodyMessage<List<Music>> findLoveMusic(@RequestParam(required = false) String musicName,
                                                          HttpServletRequest request) {
        //1.查询当前是否登录
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY ) == null) {
            System.out.println("当前未登录");
            return new ResponseBodyMessage<>(-1,"当前未登录!",null);
        }
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY );
        int userId = user.getId();
        System.out.println("userId: " + userId);

        List<Music> musicList = null;
        if(musicName == null) {
            musicList = loveMusicMapper.findLoveMusicByUserId(userId);
        } else {
            musicList = loveMusicMapper.findLoveMusicByKeyAndUId(musicName,userId);
        }
        return new ResponseBodyMessage<>(0,"查询到了歌曲信息!",musicList);
    }

    @RequestMapping("/deletelovemusic")
    public ResponseBodyMessage<Boolean> deleteLoveMusic(@RequestParam String id,HttpServletRequest request) {
        int musicId = Integer.parseInt(id);

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            System.out.println("当前没登录!");
            return new ResponseBodyMessage<>(-1,"当前未登录!",false);
        }
        User user = (User)session.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        int ret = loveMusicMapper.deleteLoveMusic(userId,musicId);
        if(ret == 1) {
            return new ResponseBodyMessage<>(0,"当前取消收藏成功!",true);
        } else {
            return new ResponseBodyMessage<>(-1,"当前取消收藏失败!",false);
        }
    }

}
