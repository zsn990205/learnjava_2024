package com.example.demo.mapper;

import com.example.demo.model.MessageInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//操作数增删改查
public interface MessageMapper {
    @Insert("insert into message_info(`from`,`to`,message) values(#{from},#{to},#{message})")
    void insertMessage(MessageInfo messageInfo);

    @Select("select * from message_info where delete_flag = 0")
    List<MessageInfo> selectAllMapper();


}
