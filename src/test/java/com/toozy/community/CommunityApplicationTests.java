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
                    builder.author("baomidou") // 设置作者
//                                .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/tjy/Downloads/community/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.toozy") // 设置父包名
                            .moduleName("community") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/tjy/Downloads/community/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude("comment", "nav", "notification", "question", "user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")
                            .entityBuilder()
                            .enableLombok()
                            .versionColumnName("version")
                            .logicDeleteColumnName("deleted")
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImp"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    @Test
    // 简单上传，支持以指定文件形式、以数据流方式、以二进制串方式、以字符串方式执行Object上传
    public  void putObjectSimple() throws FileNotFoundException {
        String ACCESS_KEY_ID = "29ca9431de1f4616b77c9a870d631f15";             // 用户的Access Key ID
        String SECRET_ACCESS_KEY = "c927cf8563f445afada65dc8b51f3352";         // 用户的Secret Access Key
        String ENDPOINT = "su.bcebos.com";                                     // 用户自己指定的域名，参考说明文档

        // 初始化一个BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);

        // 获取指定文件
//        File file = new File("/fonts/视觉中国1.jpg");
        // 获取数据流
        InputStream inputStream = new FileInputStream("/Users/tjy/Downloads/community/src/main/resources/static/fonts/视觉中国2.jpg");
//        byte[] byte0 = new byte[0];

        // 以文件形式上传Object
//        PutObjectResponse putObjectFromFileResponse =
//                client.putObject("bucketName", "file-objectKey", file);
        // 以数据流形式上传Object
        PutObjectResponse putObjectResponseFromInputStream =
                client.putObject("cbucket", "file_1", inputStream);
        // 以二进制串上传Object
//        PutObjectResponse putObjectResponseFromByte =
//                client.putObject("bucketName", "byte-objectKey", byte0);
        // 以字符串上传Object
//        PutObjectResponse putObjectResponseFromString =
//                client.putObject("bucketName", "string-objectKey", "hello world");

        // 打印ETag
//        System.out.println(putObjectFromFileResponse.getETag());
//        System.out.println(putObjectResponseFromInputStream.getETag());
//        System.out.println(putObjectResponseFromByte.getETag());
//        System.out.println(putObjectResponseFromString.getETag());

        // 关闭客户端
        client.shutdown();
    }

    @Test
    public void putObjectWithMeta() {
        String ACCESS_KEY_ID = "akxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";             // 用户的Access Key ID
        String SECRET_ACCESS_KEY = "skxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";         // 用户的Secret Access Key
        String ENDPOINT = "bj.bcebos.com";                                     // 用户自己指定的域名，参考说明文档

        // 初始化一个BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);

        // 获取指定文件
        File file = new File("/path/to/file.zip");

        // 初始化上传输入流
        ObjectMetadata meta = new ObjectMetadata();
        // 设置ContentLength大小
        meta.setContentLength(1000);
        // 设置ContentType
        meta.setContentType("application/json");
        // 设置cache-control
        meta.setCacheControl("no-cache");
        // 设置x-bce-content-crc32
        meta.setxBceCrc("crc");

        // 设置自定义元数据name的值为my-data
        meta.addUserMetadata("name", "my-data");

        // 上传Object
        client.putObject("bucketName", "objectKey", file, meta);

        // 关闭客户端
        client.shutdown();
    }


}


