package com.lee.myfileserver.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.GeneratedValue;
import java.util.Arrays;
import java.util.Date;

@Document  //采用的是 Spring Data MongoDB 中的注解，用于标识这是个 NoSQL 中的文档概念。
public class File {

    @Id // 主键
    @GeneratedValue
    private String id;
    private String name; // 文件名称
    private String contentType; // 文件类型
    private long size;
    private Date uploadDate;  //文件上传日期
    private String md5;
    private byte[] content; // 文件内容
    private String path; // 文件路径
    private String userId;  //记录头像文件所属用户,若非头像文件则为null
    private String blogFileId;  //记录图片属于哪篇博文,博主请求删除博文的时候应请求文件服务器删除此图片文件,若非博文文件则为null
    private int isHeadImg;


    protected File() {
    }

    public File(String name, String contentType, long size, byte[] content,String userId,String blogId,int isHeadImg) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.uploadDate = new Date();
        this.content = content;
        this.userId = userId;
        this.blogFileId = blogId;
        this.isHeadImg = isHeadImg;
    }
    public File(String name, String contentType, long size, byte[] content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.uploadDate = new Date();
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        if (size != file.size) return false;
        if (!id.equals(file.id)) return false;
        if (!name.equals(file.name)) return false;
        if (!contentType.equals(file.contentType)) return false;
        if (!uploadDate.equals(file.uploadDate)) return false;
        if (!md5.equals(file.md5)) return false;
        if (!Arrays.equals(content, file.content)) return false;
        return path.equals(file.path);
    }

    @Override
    public int hashCode() {
        //为一系列的输入值生成哈希码，该方法的参数是可变参数。
        return java.util.Objects.hash(name,contentType,size,content);
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", uploadDate=" + uploadDate +
                ", md5='" + md5 + '\'' +
                ", content=" + Arrays.toString(content) +
                ", path='" + path + '\'' +
                ", userId='" + userId + '\'' +
                ", blogFileId='" + blogFileId + '\'' +
                ", isHeadImg=" + isHeadImg +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlogFileId() {
        return blogFileId;
    }

    public void setBlogFileId(String blogFileId) {
        this.blogFileId = blogFileId;
    }

    public int isHeadImg() {
        return isHeadImg;
    }

    public void setHeadImg(int headImg) {
        isHeadImg = headImg;
    }
}
