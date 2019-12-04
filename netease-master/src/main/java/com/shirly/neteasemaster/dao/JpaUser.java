package com.shirly.neteasemaster.dao;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/3 15:58
 */
public class JpaUser {
    private Integer id;
    private String lastName;
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
