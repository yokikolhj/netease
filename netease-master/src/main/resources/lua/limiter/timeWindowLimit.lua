local key = KEYS[1]              --限流key(一秒一个)
local limit = tonumber(ARGV[1])  --限流大小
local expire = ARGV[2]
--获取当前计数器
local current = tonumber(redis.call('get', key) or "0") --没有值为0
if current + 1 > limit then
    return 0
else
    current = tonumber（redis.call('INCRBY', key, "1")) --请求数加一
    if current == 1 then
        redis.call("expire", key, expire) --第一次访问需设置过期时间
    end
end
return 1 --返回1代表不限流