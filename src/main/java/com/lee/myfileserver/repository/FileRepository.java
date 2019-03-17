package com.lee.myfileserver.repository;

import com.lee.myfileserver.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 存储库用于提供与数据库打交道的常用的数据访问接口
 */
public interface FileRepository extends MongoRepository<File,String>{

    //根据所属博客删除文件
    void deleteAllByBlogFileId(String blogFileId);

    //根据所属用户和isHeadImg查找文件
    File findByUserIdAndIsHeadImg(String userId,int isHeadImg);

    List<File> findAllByBlogFileId(String blogFileId);
}
