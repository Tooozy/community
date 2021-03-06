package com.toozy.community;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.toozy.community.dto.QuestionDTO;
import com.toozy.community.entity.Question;
import com.toozy.community.entity.User;
import com.toozy.community.mapper.QuestionMapper;
import com.toozy.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    public QuestionMapper questionMapper;
    @Autowired
    public UserMapper userMapper;

    @Test
    void contextLoads() {
        List<Question> questions = questionMapper.selectList(null);
        Long aLong = questionMapper.selectCount(null);
        Integer i = aLong.intValue();

        Double totalCommentCount = 0d;
        Double totalViewCount = 0d;

        for (Question question : questions) {
            totalCommentCount = totalCommentCount+question.getCommentCount();
            totalViewCount = totalViewCount+question.getViewCount();
        }
        Double meanCommentCount = totalCommentCount/i;
        Double meanViewCount = totalViewCount/i;

        for (Question question : questions) {
            Double d = ((question.getCommentCount()/meanCommentCount) + (question.getViewCount()/meanViewCount));
            question.setLikeCount(d.intValue());
            questionMapper.updateById(question);
        }



        List<Question> questionsFinal = questionMapper.selectList(new QueryWrapper<Question>().orderByDesc("like_count"));


        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionsFinal) {
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.selectById(question.getCreator());
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }


    }


    @Test
    public void simpleModuleCodeGenerator() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/BBS?useSSL=false&useUnicode=true&characterEncoding=UTF-8", "root", "xstjy123")
                .globalConfig(builder -> {
                    builder.author("baomidou") // ????????????
//                                .enableSwagger() // ?????? swagger ??????
                            .fileOverride() // ?????????????????????
                            .outputDir("/Users/tjy/Downloads/community/src/main/java"); // ??????????????????
                })
                .packageConfig(builder -> {
                    builder.parent("com.toozy") // ???????????????
                            .moduleName("community") // ?????????????????????
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/tjy/Downloads/community/src/main/resources/mapper")); // ??????mapperXml????????????
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude("comment", "nav", "notification", "question", "user") // ???????????????????????????
                            .addTablePrefix("t_", "c_")
                            .entityBuilder()
                            .enableLombok()
                            .versionColumnName("version")
                            .logicDeleteColumnName("deleted")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImp"); // ?????????????????????
                })
                .templateEngine(new FreemarkerTemplateEngine()) // ??????Freemarker???????????????????????????Velocity????????????
                .execute();
    }

    @Test
    // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????Object??????
    public  void putObjectSimple() throws FileNotFoundException {
        String ACCESS_KEY_ID = "29ca9431de1f4616b77c9a870d631f15";             // ?????????Access Key ID
        String SECRET_ACCESS_KEY = "c927cf8563f445afada65dc8b51f3352";         // ?????????Secret Access Key
        String ENDPOINT = "su.bcebos.com";                                     // ????????????????????????????????????????????????

        // ???????????????BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);

        // ??????????????????
//        File file = new File("/fonts/????????????1.jpg");
        // ???????????????
        InputStream inputStream = new FileInputStream("/Users/tjy/Downloads/community/src/main/resources/static/fonts/????????????2.jpg");
//        byte[] byte0 = new byte[0];

        // ?????????????????????Object
//        PutObjectResponse putObjectFromFileResponse =
//                client.putObject("bucketName", "file-objectKey", file);
        // ????????????????????????Object
        PutObjectResponse putObjectResponseFromInputStream =
                client.putObject("cbucket", "file_1", inputStream);
        // ?????????????????????Object
//        PutObjectResponse putObjectResponseFromByte =
//                client.putObject("bucketName", "byte-objectKey", byte0);
        // ??????????????????Object
//        PutObjectResponse putObjectResponseFromString =
//                client.putObject("bucketName", "string-objectKey", "hello world");

        // ??????ETag
//        System.out.println(putObjectFromFileResponse.getETag());
//        System.out.println(putObjectResponseFromInputStream.getETag());
//        System.out.println(putObjectResponseFromByte.getETag());
//        System.out.println(putObjectResponseFromString.getETag());

        // ???????????????
        client.shutdown();
    }

    @Test
    public void putObjectWithMeta() {
        String ACCESS_KEY_ID = "akxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";             // ?????????Access Key ID
        String SECRET_ACCESS_KEY = "skxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";         // ?????????Secret Access Key
        String ENDPOINT = "bj.bcebos.com";                                     // ????????????????????????????????????????????????

        // ???????????????BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);

        // ??????????????????
        File file = new File("/path/to/file.zip");

        // ????????????????????????
        ObjectMetadata meta = new ObjectMetadata();
        // ??????ContentLength??????
        meta.setContentLength(1000);
        // ??????ContentType
        meta.setContentType("application/json");
        // ??????cache-control
        meta.setCacheControl("no-cache");
        // ??????x-bce-content-crc32
        meta.setxBceCrc("crc");

        // ????????????????????????name?????????my-data
        meta.addUserMetadata("name", "my-data");

        // ??????Object
        client.putObject("bucketName", "objectKey", file, meta);

        // ???????????????
        client.shutdown();
    }


}


