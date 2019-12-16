local key = KEYS[1]              --限流key(一秒一个)
--获取当前可用令牌数
local current = tonumber(redis.call('get', key) or "0") --没有值为0
if current <= 0 then --没有令牌
    return 0
else
    redis.call("DECRBY", key, "1") --令牌数-1
    return 1 --返回1代表不限流
end