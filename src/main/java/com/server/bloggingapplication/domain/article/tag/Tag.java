package com.server.bloggingapplication.domain.article.tag;

public class Tag {

    private Integer id;
    private String value;

    public Tag(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

}
