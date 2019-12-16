local key = KEYS[1]              --限流key(一秒一个)
local limit = tonumber(ARGV[1])  --容量
--获取当前可用令牌数
local current = tonumber(redis.call('get', key) or "0") --没有值为0
if current + 1 > limit then --超出容量
    return 0
else
    redis.call("INCRBY", key, "1") --令牌数+1
    return 1 --返回1代表不限流
end
