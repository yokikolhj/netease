package com.shirly.neteasemaster.decoupingDemo.model;

import java.io.Serializable;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 10:37
 * @description 描述
 */
public class User implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
