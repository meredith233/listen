package com.example.listen.common;

import lombok.Data;

@Data
public class MyResponse<T> {

    private String code;

    private String message;

    private T object;
}
