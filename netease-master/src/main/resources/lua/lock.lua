-- Set a lock
-- 如果获取锁成功，则返回1
local key = KEYS[1]
local content = KEYS[2]
local ttl = ARGV[1]
local lockSet = redis.call('setnx', key, content)
if lockSet == 1 then
    redis.call('pexpire', key, ttl)
-- redis.call('incr', "content")
else
    -- 如果value相同，则认为是重入锁
    local value = redis.call('get', key)
    if(value == content) then
        lockSet = 1;
        redis.call('pexpire', key, ttl)
    end
end
return lockSet