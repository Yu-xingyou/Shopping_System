package com.xingyou.entity.people;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
    private Integer adminId;
    private String name;
    private String password;
}
