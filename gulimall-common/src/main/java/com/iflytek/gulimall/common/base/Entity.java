package com.iflytek.gulimall.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@Accessors(chain = true)
public class Entity implements Serializable {
    public static final String UPDATE_TIME = "updateTime";
    public static final String CREATE_TIME = "createTime";
    private static final long serialVersionUID = 5169873634279173683L;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

}
