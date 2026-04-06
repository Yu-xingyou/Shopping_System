package com.xingyou.entity.people;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    private Integer staffId;
    private String name;
    private String password;
    private Integer status;
}
