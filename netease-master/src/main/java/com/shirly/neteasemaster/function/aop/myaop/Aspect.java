package com.shirly.neteasemaster.function.aop.myaop;

/**
 * @Author shirly
 * @Date 2020/1/28 16:46
 * @Description 切面
 */
public class Aspect {
    private Advice advice;
    private Pointcut pointcut;

    public Aspect(Advice advice, Pointcut pointcut) {
        this.advice = advice;
        this.pointcut = pointcut;
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }
}
