-- capture arguments
local ttl = ARGV[1]
local queryFlag = ARGV[2]

local insertedIds = {}
local staleIds = {}

local result = {}
table.insert(result, insertedIds)
table.insert(result, staleIds)

for keyIdx, key in ipairs(KEYS) do

  local itemIdx = 2 + keyIdx

  local item = redis.call('get', key)
  if item == queryFlag then
    redis.log(redis.LOG_NOTICE, "Updating item '" .. key .. "' with queried value")
    redis.call('setex', key, ttl, ARGV[itemIdx])
    table.insert(insertedIds, key)
  else
    redis.log(redis.LOG_NOTICE, "Stale item '" .. key .. "'")
    table.insert(staleIds, key)
  end

end

return result