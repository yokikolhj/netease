package com.shirly.neteasemaster.function.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/9 15:39
 * @description 商品信息服务
 */
@Service
public class CommodityService {

    /**
     * 存放请求参数
     */
    class Request {
        String movieCode;
        CompletableFuture<Map<String, Object>> future;
    }

    BlockingQueue<Request> queue = new LinkedBlockingDeque<>(); //队列用来存放请求

    @Autowired
    QueryServiceRemoteCall queryServiceRemoteCall; // 调用全程服务

    @PostConstruct
    public void init() {
        // 类被初始化时执行
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        scheduledExecutorService.scheduleAtFixedRate(() ->{
            int size = queue.size();
            if (size == 0) {
               return;
            }

            ArrayList<Request> requests = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Request request = queue.poll();
                requests.add(request);
            }
            System.out.println("批量处理了：" + size + "条请求");
            // 将请求参数合并执行
            List<String> codes = new ArrayList<>();
            for (Request request : requests) {
                codes.add(request.movieCode);
            }
            List<Map<String, Object>> responses = queryServiceRemoteCall.queryCommodityByCodeBatch(codes);
            // spring-cloud的hystrix有请求合并的请求方案

            // 结果集完成，把请求分发给每一个具体的request
            // 返回结果集的map的key为movieCode,value为改movieCOde查询结果
            HashMap<String, Map<String, Object>> responseMap = new HashMap<>();
            for (Map<String, Object> response : responses) {
                String code = response.get("code").toString();
                responseMap.put(code, response);
            }
            // 返回请求-> 线程之间能返回return吗？？？ JDK-callable-future<T>-T就是返回的结果->get()阻塞，直到返回为止
            for (Request request : requests) {
                Map<String, Object> result = responseMap.get(request.movieCode);
                request.future.complete(result);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    // 请求合并，1000次并发同时合并
    public Map<String, Object> queryCommodity(String movieCode) throws ExecutionException, InterruptedException {
        // 1、拦截请求
        Request request = new Request();
        request.movieCode = movieCode;
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        request.future = future;
        queue.add(request);
        return future.get();

        /*return queryServiceRemoteCall.queryCommodityByCode(movieCode);*/
    }
}
