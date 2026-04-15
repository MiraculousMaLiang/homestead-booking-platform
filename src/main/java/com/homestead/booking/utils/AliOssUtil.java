package com.homestead.booking.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class AliOssUtil {

    // 从配置文件注入（与 yml 下划线 key 对应）
    @Value("${homestead.oss.endpoint}")
    private String endpoint;

    @Value("${homestead.oss.access-key-id}")
    private String accessKeyId;

    @Value("${homestead.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${homestead.oss.bucket-name}")
    private String bucketName;

    // 上传文件（去掉 V4 签名，兼容老 SDK，无需 region 配置）
    public String uploadFile(String objectName, InputStream in, String contentType) throws Exception {
        OSS ossClient = null;
        try {
            // 1. 构建 OSSClient（仅用 Endpoint+AK/SK，老版本自动识别地域）
            // 去掉 V4 签名配置，用 SDK 默认签名（V2，更兼容老版本）
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 2. 配置文件元数据（公共读权限，前端可访问）
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType); // 必须设置文件类型，避免下载异常
            metadata.setObjectAcl(CannedAccessControlList.PublicRead); // 公共读权限

            // 3. 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, in, metadata);
            ossClient.putObject(putObjectRequest);

            // 4. 拼接公共访问 URL（固定格式，避免错误）
            String endpointDomain = endpoint.replace("https://", ""); // 得到 oss-cn-beijing.aliyuncs.com
            return String.format("https://%s.%s/%s", bucketName, endpointDomain, objectName);

        } catch (OSSException oe) {
            // OSS 服务端异常（如 Bucket 不存在、权限不足）
            throw new RuntimeException("OSS上传失败：" + oe.getErrorMessage() + "（错误码：" + oe.getErrorCode() + "）");
        } catch (ClientException ce) {
            // 客户端异常（如网络不通、AK/SK 错误）
            throw new RuntimeException("OSS客户端异常：" + ce.getMessage());
        } finally {
            // 5. 关闭资源，避免泄露
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
