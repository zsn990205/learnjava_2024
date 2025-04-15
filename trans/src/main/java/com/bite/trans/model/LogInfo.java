package com.bite.trans.model;

import lombok.Data;

import java.util.Date;

@Data
public class LogInfo {
    private Integer id;
    private String userName;
    private String op;
    private Date creteTime;
    private Date updateTime;
}
