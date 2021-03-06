package com.shirly.eureka.model;

/**
 * @Author shirly
 * @Date 2020/2/8 1:30
 * @Description
 */
public class EurekaClient {

    private String serverName;
    private String host;
    private int port;
    /**
     * 状态码，定义上下线
     */
    private String status;
    /**
     * 最后一次注册时间
     */
    private long isDirtyWithTime;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getIsDirtyWithTime() {
        return isDirtyWithTime;
    }

    public void setIsDirtyWithTime(long isDirtyWithTime) {
        this.isDirtyWithTime = isDirtyWithTime;
    }
}
