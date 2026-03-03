package com.sky.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.sky.properties.OSSProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class OSSUtils {
    @Autowired
    private OSSProperties ossProperties;

    private static String ENDPOINT;
    private static String BUCKET_NAME;
    private static String REGION;

    @PostConstruct
    public void init() {
        if (ossProperties != null) {
            ENDPOINT = ossProperties.getEndpoint();
            BUCKET_NAME = ossProperties.getBucketName();
            REGION = ossProperties.getRegion();
        }
    }

    public static String upload(byte[] content, String originalFilename) throws Exception {
        String endpoint = ENDPOINT;
        String bucketName = BUCKET_NAME;
        String region = REGION;

        // 从环境变量中获取访问凭证。运行本代码之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如example-dir/example-object.txt。
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = dir + "/" + newFileName;

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create().endpoint(endpoint).credentialsProvider(credentialsProvider).clientConfiguration(clientBuilderConfiguration).region(region).build();

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content));
            // 创建PutObject请求。
            ossClient.putObject(putObjectRequest);
        } finally {
            ossClient.shutdown();
        }

        return "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectName;
    }
}