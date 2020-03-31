package com.example.listen.entity;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return id.equals(material.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
