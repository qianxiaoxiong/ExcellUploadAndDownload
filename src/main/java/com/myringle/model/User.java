package com.myringle.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
public class User {

    private BigDecimal id;
    private String UserName;
    private String Phone;
    private String Email;
    private Integer age;
    private String sex;
}
