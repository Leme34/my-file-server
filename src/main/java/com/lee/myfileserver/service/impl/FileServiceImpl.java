package com.lee.myfileserver.service.impl;

import com.lee.myfileserver.entity.File;
import com.lee.myfileserver.repository.FileRepository;
import com.lee.myfileserver.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    FileRepository fileRepository;


    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.delete(id);
    }

    @Override
    public File getFileById(String id) {
        return fileRepository.findOne(id);
    }

    @Override
    public List<File> listFilesByPage(int pageIndex, int pageSize) {
        //根据文件上传日期排序
        Sort sort = new Sort(Sort.Direction.DESC,"uploadDate");

        Pageable pageable = new PageRequest(pageIndex,pageSize,sort);

        //按排序和分页规则查询所有
        Page<File> filePage = fileRepository.findAll(pageable);

        List<File> fileList = filePage.getContent();

        return fileList;
    }

    @Override
    public void removeFileByBlogFileId(String blogFileId) {
        fileRepository.deleteAllByBlogFileId(blogFileId);
    }

    @Override
    public File getFileByUserIdAndIsHeadImg(String userId, int isHeadImg) {
        return fileRepository.findByUserIdAndIsHeadImg(userId,isHeadImg);
    }

}
