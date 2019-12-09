package com.shirly.neteasemaster.function.hystrix;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/9 15:41
 * @description 远程资源
 */
@Repository
public class QueryServiceRemoteCall {

    /**
     * 单个信息伪数据
     * @param movieCode 查询code
     * @return 信息
     */
    public Map<String, Object> queryCommodityByCode(String movieCode) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", movieCode);
        result.put("name", "shirly");
        return result;
    }

    /**
     * 批量信息伪数据
     * @param codes 多个code
     * @return 信息集合
     */
    public List<Map<String, Object>> queryCommodityByCodeBatch(List<String> codes) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", codes.get(i));
            result.put("name", "shirly");
            results.add(result);
        }
        return results;
    }
}
