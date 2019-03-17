package com.lee.myfileserver.service;

import com.lee.myfileserver.entity.File;
import java.util.List;

public interface FileService {

    /**
     * 保存文件 * @param File * @return
     */
    File saveFile(File file);

    /**
     * 删除文件 * @param File * @return
     */
    void removeFile(String id);

    /**
     * 根据id获取文件 * @param File * @return
     */
    File getFileById(String id);

    /**
     * 分页查询，按上传时间降序 * @param pageIndex * @param pageSize * @return
     */
    List<File> listFilesByPage(int pageIndex, int pageSize);

    /**
     * 根据所属博文查找插入的图片文件
     */
    void removeFileByBlogFileId(String blogFileId);

    /**
     * 根据所属用户和isHeadImg查找文件
     */
    File getFileByUserIdAndIsHeadImg(String userId,int isHeadImg);



}
