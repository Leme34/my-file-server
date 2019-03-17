package com.lee.myfileserver.controller;

import com.lee.myfileserver.entity.File;
import com.lee.myfileserver.service.FileService;
import com.lee.myfileserver.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

//注解标识了 API 可以被跨域请求。为了能够启用该注解，仍然需要安全配置类的支持。
@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有域名访问
@Controller
public class FileController {

    //配置文件注入的属性
    @Value("${ipAddress}")
    private String serverAddress;
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    FileService fileService;

    /**
     * 首页展示最新二十条数据
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileService.listFilesByPage(0, 20));
        return "index";
    }

    /**
     * 分页查询文件
     */
    public List<File> listFilesByPage(@PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 下载文件
     */
    @GetMapping("/files/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id) {
        //查到此文件
        File file = fileService.getFileById(id);
        if (file != null) {
            //返回体
            return ResponseEntity.ok()
                    //返回体中添加文件信息(下载文件时的一些标识字段)并放入文件
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize() + "")
                    .header("Connection", "close")
                    .body(file.getContent());
        } else {
            //返回404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }

    /**
     * 在线显示文件的接口,用于预览图片文件
     */
    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) {

        File file = fileService.getFileById(id);
        if (file != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize() + "")
                    .header("Connection", "close")
                    .body(file.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }
    }


    /**
     * 主页表单的Upload按钮上传文件,会重定向主页
     */
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {  //重定向时用于保存信息
        try {
            //获取上传的文件的信息
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes());
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            //文件入库
            fileService.saveFile(f);
        } catch (Exception e) {
            e.printStackTrace();
            //记录出错信息并重定向到首页
            redirectAttributes.addFlashAttribute("message", "Your " + file.getOriginalFilename() + " is wrong!");
            return "redirect:/";
        }

        //重定向到首页
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }


    /**
     * 上传文件的接口
     * 若传入了userId且isHeadImg=true说明是上传头像文件，根据userId查询isHeadImg的文件若不为null则已上传过头像，则替换原头像
     * 若传入了blogId说明是上传插入博文的图片文件，则为此文件setBlogId再保存
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam(value = "userId",required = false) String userId,
                                                   @RequestParam(value = "blogFileId",required = false)String blogFileId,
                                                   @RequestParam(value = "isHeadImg",required = false,defaultValue = "0") int isHeadImg, //默认不是头像文件
                                                   @RequestParam("file") MultipartFile file) {
        //因为获取新增博文页面的时候传入前端的是空的博客对象所以上传图片时blogId也为null,所以改为使用blogFileId
//        System.out.println("blogFileId:"+blogFileId);

        //若是上传头像文件
        if (isHeadImg==1) {
            File userHeadImg = fileService.getFileByUserIdAndIsHeadImg(userId, 1);
            // 查出此用户的头像文件, 如果此用户已上传过头像文件，则先删除原有文件
            if (userHeadImg != null) {
                System.out.println("上传头像文件,此用户之前已上传过头像");
                fileService.removeFile(userHeadImg.getId());
            }
        }
        //若是个人主页背景文件
        if (isHeadImg==2) {
            File userBgImg = fileService.getFileByUserIdAndIsHeadImg(userId, 2);
            if (userBgImg != null) {
                System.out.println("上传头像文件,此用户之前已上传过头像");
                fileService.removeFile(userBgImg.getId());
            }
        }

        File returnFile = null;
        try {
            File f = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes(),userId,blogFileId,isHeadImg);
            f.setMd5(MD5Util.getMD5(file.getInputStream()));
            returnFile = fileService.saveFile(f);
            //文件服务器在线显示(预览)文件的url
            String path = "http://" + serverAddress + ":" + serverPort + "/view/" + returnFile.getId();
            return ResponseEntity.ok(returnFile.getId());
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * 根据id删除文件的接口
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@PathVariable String id) {
        try {
            fileService.removeFile(id);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE Success!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 根据请求参数blogId删除所有插入该博文的图片文件
     */
    @DeleteMapping("/delete/{blogFileId}")
    @ResponseBody
    public ResponseEntity deleteFileByBlogId(@PathVariable("blogFileId")String blogFileId){
        System.out.println("请求删除文件：blogFileId="+blogFileId);
        try {
            fileService.removeFileByBlogFileId(blogFileId);
            System.out.println("删除文件成功：blogFileId="+blogFileId);
            return ResponseEntity.status(HttpStatus.OK).body("DELETE Success!");
        } catch (Exception e) {
            System.out.println("异常信息："+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
