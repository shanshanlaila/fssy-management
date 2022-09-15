package com.fssy.shareholder.management.pojo.system.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author TerryZeng
 * @title: AttachmentAuth
 * @description: 销售合同、采购合同、计划价等 保密附件
 * @date 2021/10/29
 */
@TableName("bs_common_attachment_secret")
public class AttachmentSecret extends BaseModel {

    private static final long serialVersionUID = 0x4b91a62812c9e09cL;
    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer status;

    private String md5Path;

    private String filename;

    private String path;

    private Integer importStatus;

    private Integer module;

    private String note;

    /*
     * DataTimeFormat是往数据库走时转换，时区指定在启动类中
     * JsonFormat是数据往客户端时转换
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date importDate;

    private String conclusion;


    public AttachmentSecret(Long id, Integer status, String md5Path, String filename, String path, Integer importStatus, Integer module, String note, Date importDate, String conclusion) {
        this.id = id;
        this.status = status;
        this.md5Path = md5Path;
        this.filename = filename;
        this.path = path;
        this.importStatus = importStatus;
        this.module = module;
        this.note = note;
        this.importDate = importDate;
        this.conclusion = conclusion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMd5Path() {
        return md5Path;
    }

    public void setMd5Path(String md5Path) {
        this.md5Path = md5Path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(Integer importStatus) {
        this.importStatus = importStatus;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    @Override
    public String toString() {
        return "AttachmentSecret{" +
                "id=" + id +
                ", status=" + status +
                ", md5Path='" + md5Path + '\'' +
                ", filename='" + filename + '\'' +
                ", path='" + path + '\'' +
                ", importStatus=" + importStatus +
                ", module=" + module +
                ", note='" + note + '\'' +
                ", importDate=" + importDate +
                ", conclusion=" + conclusion +
                '}';
    }
}
