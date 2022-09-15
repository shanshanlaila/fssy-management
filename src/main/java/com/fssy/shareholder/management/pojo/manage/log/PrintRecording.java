package com.fssy.shareholder.management.pojo.manage.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author QinHui
 * @title: PrintRecording
 * @description: 打印日志记录
 * @date 2021/11/2
 */
@TableName("basic_print_recording")
public class PrintRecording implements Serializable
{
    private static final long serialVersionUID = 3172478184750497567L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String moduleName;
    private Long relatedId;
    private LocalDateTime printTime;
    private Long userId;
    private String userName;
    private Integer time;

    public PrintRecording(){
        super();
    }

    public PrintRecording(Long id, String moduleName, Long relatedId, LocalDateTime printTime, Long userId, String userName, Integer time)
    {
        this.id = id;
        this.moduleName = moduleName;
        this.relatedId = relatedId;
        this.printTime = printTime;
        this.userId = userId;
        this.userName = userName;
        this.time = time;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }

    public Long getRelatedId()
    {
        return relatedId;
    }

    public void setRelatedId(Long relatedId)
    {
        this.relatedId = relatedId;
    }

    public LocalDateTime getPrintTime()
    {
        return printTime;
    }

    public void setPrintTime(LocalDateTime printTime)
    {
        this.printTime = printTime;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Integer getTime()
    {
        return time;
    }

    public void setTime(Integer time)
    {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return "PrintRecording{" +
                "id=" + id +
                ", moduleName='" + moduleName + '\'' +
                ", relatedId=" + relatedId +
                ", printTime=" + printTime +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", time=" + time +
                '}';
    }
}
