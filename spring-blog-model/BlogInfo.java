package com.bite.blog.model;

import com.bite.blog.utils.DateUtils;
import lombok.Data;
import java.util.Date;

@Data
public class BlogInfo {
    private Integer id;
    private String title;
    private String content;
    private Integer UserId;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;
    private boolean isLoginUser;

    private String getCreateTime() {
        return DateUtils.formatDate(createTime);
    }
}
