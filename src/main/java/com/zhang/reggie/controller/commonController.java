package com.zhang.reggie.controller;

import com.zhang.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class commonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        log.info(file.toString());

        //获取原始文件名
        String originalFilename = file.getOriginalFilename();

        //获取文件类型例如   abc.jpg   (.jpg)
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //利用uuid代替文件名，防止重复
        String filename = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断basePath路径是否存在
        if (!dir.exists()) {
            //不存在就创建
            dir.mkdirs();
        }

        try {
            //换文件位置
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 文件下载，回显到浏览器
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            //响应类型  图片类型
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len); //把bytes数组中的内容从0到最后写入浏览器
                outputStream.flush();//刷新
            }
            //关闭流
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
