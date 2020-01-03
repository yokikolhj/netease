package com.shirly.neteasemaster.function.high_quality;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 16:52
 * @description 描述
 */
public class Email {

    private String title;

    private String content;

    private String userId;

    public Email(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
