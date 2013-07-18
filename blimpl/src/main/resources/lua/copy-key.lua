local data = redis.call('dump', KEYS[1])
return redis.call('restore', KEYS[2], 0, data)