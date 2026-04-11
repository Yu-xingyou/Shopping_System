package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Integer id;
    
    private String userId;
    
    private Integer productId;
    
    private String productName;
    
    private String content;
    
    private Integer isRead;
    
    private LocalDateTime createTime;
}
