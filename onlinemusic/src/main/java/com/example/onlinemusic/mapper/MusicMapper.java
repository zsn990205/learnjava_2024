package com.example.onlinemusic.mapper;

import com.example.onlinemusic.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicMapper {
    int insert(String title, String singer, String time, String url, int userid);

    Music selectByNameAndSinger(String title,String singer);

    //此处的id是musicid
    Music findMusicById(int id);

    int deleteMusicById(int id);

    //查询所有的音乐
    List<Music> findMusic();

    //模糊查询音乐
    List<Music> findMusicByName(String musicName);
}
