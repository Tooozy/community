package com.toozy.community.provider;

import com.baidubce.BceServiceException;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class BOSProvider {
    String ACCESS_KEY_ID = "29ca9431de1f4616b77c9a870d631f15";             // 用户的Access Key ID
    String SECRET_ACCESS_KEY = "c927cf8563f445afada65dc8b51f3352";         // 用户的Secret Access Key
    String ENDPOINT = "su.bcebos.com";                                     // 用户自己指定的域名，参考说明文档


    public void putObjectSimple(String fileName,InputStream inputStream) throws FileNotFoundException {


        // 初始化一个BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);
        // 以数据流形式上传Object
        PutObjectResponse putObjectResponseFromInputStream =
                client.putObject("cbucket", fileName, inputStream);

        // 关闭客户端
        client.shutdown();
    }


    public  void getObjectToStream(String fileName) {

        // 初始化一个BosClient
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);

        // 获取Object，返回结果为BosObject对象
        BosObject object = client.getObject("cbucket", fileName);



        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();

        // 处理这个流...

        // 关闭流
        try {
            if (objectContent != null) {
                objectContent.close();
            }
        } catch (IOException e) {
            throw new BceServiceException("", e);
        }

        // 关闭客户端
        client.shutdown();
    }

    public String generatePresignedUrl(String objectKey, int expirationInSeconds) {
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        BosClient client = new BosClient(config);

        URL url = client.generatePresignedUrl("cbucket",objectKey, expirationInSeconds);
        //指定用户需要获取的Object所在的Bucket名称、该Object名称、时间戳、URL的有效时长

        return url.toString();
    }


}
