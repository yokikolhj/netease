package com.shirly.neteasemaster.function.aop.myaop;

import com.shirly.neteasemaster.function.aop.service.KTVPrincess;
import com.shirly.neteasemaster.function.aop.service.KTVService;

/**
 * @Author shirly
 * @Date 2020/1/28 20:42
 * @Description
 */
public class ProxyMain {
    public static void main(String[] args) {
        // ----------------------------------静态代理---------------------------------
        KTVService pb = new KTVPrincess();
        KTVService proxy = new StaticProxy(pb);
        proxy.sing("shirly");
        // ----------------------------------动态代理---------------------------------
    }
}
