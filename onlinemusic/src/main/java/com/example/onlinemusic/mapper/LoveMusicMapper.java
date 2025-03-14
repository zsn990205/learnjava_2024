package com.example.onlinemusic.mapper;

import com.example.onlinemusic.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoveMusicMapper {
    //查询喜欢的音乐
     Music findLoveMusic(int userId,int musicId);

    //收藏音乐
     boolean insertLoveMusic(int userId,int musicId);

     //查询这个用户收藏到的所有音乐
     List<Music> findLoveMusicByUserId(int userId);

    //查询当前用户,指定为musicName的音乐,支持模糊查询
     List<Music> findLoveMusicByKeyAndUId(String musicName,int userId);

     //移除某个用户收藏中喜欢的音乐
     int deleteLoveMusic(int userId,int musicId);

     //根据音乐的Id进行删除
     int deleteLoveMusicByMusicId(int musicId);
}
