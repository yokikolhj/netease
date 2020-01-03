package com.shirly.neteasemaster.function.high_quality;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 16:57
 * @description 描述
 */
public class Msg {

    private String userId;

    private String telephone;

    private String content;

    public Msg(String userId, String telephone, String content) {
        this.userId = userId;
        this.telephone = telephone;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
