package com.shirly.neteasemaster.function.aop.myaop;

/**
 * @Author shirly
 * @Date 2020/1/28 16:33
 * @Description 定义切入点
 */
public class Pointcut {

    // 类名匹配模式（正则表达式）
    private String classPattern;

    // 方法名匹配模式（正则表达式）
    private String methodPattern;

    public Pointcut(String classPattern, String methodPattern) {
        this.classPattern = classPattern;
        this.methodPattern = methodPattern;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public String getMethodPattern() {
        return methodPattern;
    }

    public void setMethodPattern(String methodPattern) {
        this.methodPattern = methodPattern;
    }
}
