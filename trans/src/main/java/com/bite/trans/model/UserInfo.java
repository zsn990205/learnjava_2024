package com.bite.trans.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private Integer id;
    private String userName;
    private String password;
    private Date creteTime;
    private Date updateTime;
}
