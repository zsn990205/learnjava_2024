package com.example.demo.controller;

import com.example.demo.model.MessageInfo;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/message")
@RestController
public class MessageController {
    //private List<MessageInfo> messageInfos = new ArrayList<>();
    @Autowired
    private MessageService messageService;
    //1.提交留言
    @RequestMapping("/publish")
    public Boolean publishMessage(MessageInfo messageInfo) {
        //进行参数校验
        if(!StringUtils.hasLength(messageInfo.getFrom()) ||
           !StringUtils.hasLength(messageInfo.getTo()) ||
                !StringUtils.hasLength(messageInfo.getMessage())) {
            return false;
        }
        //添加留言
        messageService.addMessage(messageInfo);

        return true;
    }

    //2.查看所有留言
    @RequestMapping("/getMessageList")
    public List<MessageInfo> messageList() {
        return messageService.getmessageInfos();
    }
}
