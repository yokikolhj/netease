package com.shirly.tomcat.util;

import javax.servlet.Servlet;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author shirly
 * @Date 2020/2/4 15:56
 * @Description web.xml文件对应的java对象
 */
public class WebXml {
    public String projectPath = null;
    // name:class
    public Map<String, Object> servlets = new HashMap<>();
    // url:servletname
    public Map<String, String> servletMapping = new HashMap<>();
    // Servletname:实例对象
    public Map<String, Servlet> servletInstances = new HashMap<>();

    // 写一个class加载示例
    public void loadService() throws Exception {
        // JVM加载class，得告诉它class在哪里，jar在哪里
        URL url = new URL("file:" + projectPath + "\\WEB-INF\\classes\\");
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}); // 类加载的工具
        for (Map.Entry<String, Object> entry : servlets.entrySet()) {
            String servletName = entry.getKey(); // servlet名称
            String servletClassName = entry.getValue().toString(); // 全类名

            // 加载
            Class<?> aClass = classLoader.loadClass(servletClassName);
            // 反射 创建对象
            Servlet servlet = (Servlet) aClass.newInstance();
            servletInstances.put(servletName, servlet);
        }
    }
}
