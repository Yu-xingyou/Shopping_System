package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    private Integer id;
    private String userId;
    private Integer productId;
    private LocalDateTime createTime;
}
