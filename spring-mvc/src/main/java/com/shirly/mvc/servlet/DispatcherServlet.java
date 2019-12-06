package com.shirly.mvc.servlet;

import com.shirly.mvc.annotation.Controller;
import com.shirly.mvc.annotation.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/5 14:36
 */
public class DispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    private Map<String, Object> controllerMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) {
        // 1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        // 2.扫描的类
        doScanner(properties.getProperty("scanPackage"));
        // 3.拿到扫描的类，通过反射机制实例化，并以Map的形式放到IOC容器中
        doInstance();
        // 4.初始化HandlerMapping(url[login]->method[login()])
        initHandlerMapping();
    }

    /**
     * 获取servlet初始化时配置文件
     * @param contextConfigLocation 文件位置
     */
    private void doLoadConfig(String contextConfigLocation) {
        System.out.println("调用了doLoadConfig...");
        System.out.println("配置文件位置：" + contextConfigLocation);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归
     * @param packageName 包路径
     */
    private void doScanner(String packageName) {
        System.out.println("调用了doScanner...");
        System.out.println("扫描的包路径" + packageName);
        // 把所有的“.”替换成“/”
        // com.shirly.controller -> c://com/shirly/controller
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        if (url != null) {
            File dir = new File(url.getFile());
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归读取包
                        doScanner(packageName + "." + file.getName());
                    } else {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        classNames.add(className);
                        System.out.println("Spring容器扫描到的类有：" + packageName + "." + file.getName());
                    }
                }
            }
        }
    }

    /**
     * 实例化
     */
    private void doInstance() {
        System.out.println("调用了doInstance...");
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)){ // (userService, new UserService)
                    ioc.put(toLowerFirstWord(clazz.getSimpleName()), clazz.newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 字符串首字母小写
     * @param name 字符串
     * @return 首字母小写字符串
     */
    private String toLowerFirstWord(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * handlerMapping-method
     */
    private void initHandlerMapping() {
        System.out.println("调用了initHandlerMapping...");
        try {
            for (Map.Entry entry : ioc.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                if (!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                // 拼接url时，RequestMapping头的url和方法上的url
                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = annotation.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String url = annotation.value();
                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    handlerMapping.put(url, method);
                    controllerMap.put(url, clazz.newInstance());
                    System.out.println(url + "," + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 处理请求
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500! Server Exception");
        }
    }

    /**
     * 方法请求入口
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     */
    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (handlerMapping.isEmpty()) {
            return;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        // 拼接url，并把多个/替换成一个
        url = url.replace(contextPath, "").replaceAll("/+","/");

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 NOT FOUND!");
            return;
        }

        Method method = this.handlerMapping.get(url);
        // 获取方法中的参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 获取请求参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 保存参数值
        Object[] paramValues = new Object[parameterTypes.length];
        // 方法参数列表
        for (int i = 0; i < parameterTypes.length; i++) {
            // 根据参数名称，做处理
            String requestParam = parameterTypes[i].getSimpleName();
            if ("HttpServletRequest".equals(requestParam)) {
                // 参数类型已明确，强制转化类型
                paramValues[i] = req;
                continue;
            }
            if ("HttpServletResponse".equals(requestParam)) {
                paramValues[i] = resp;
                continue;
            }
            if ("String".equals(requestParam)) {
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "");
                    paramValues[i] = value;
                }
            }
        }

        // 利用反射机制来调用方法
        try {
            method.invoke(this.controllerMap.get(url), paramValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

