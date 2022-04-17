package com.toozy.community.controller;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.PutObjectResponse;
import com.toozy.community.dto.FileDTO;
import com.toozy.community.provider.BOSProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class FileController {
    @Autowired
    BOSProvider bosProvider;


    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("editormd-image-file");

        String originalFilename = UUID.randomUUID().toString()+file.getOriginalFilename();//设置文件名

        bosProvider.putObjectSimple(originalFilename, file.getInputStream());


        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(bosProvider.generatePresignedUrl(originalFilename,-1));
        return fileDTO;
    }


}
