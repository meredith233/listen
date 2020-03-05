package com.example.listen.entity;

import lombok.Data;

@Data
public class Material {

    private Long id;

    private String name;

    private Long typeId;

    private String coverId;

    private Long fileId;

    /**
     * 文件大小，单位byte
     */
    private Integer size;

    private String lyricsContent;

    private Integer sort;
}
