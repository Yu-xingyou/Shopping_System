package com.xingyou.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component //注入到spring容器中
@ConfigurationProperties(prefix = "aliyun.oss") //自动配置文件
public class AliyunOSSProperties {
    private String endpoint;
    private String bucketName;
    private String region;
}
