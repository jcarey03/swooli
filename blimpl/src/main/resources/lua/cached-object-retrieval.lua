-- capture arguments
local ttl = ARGV[1]
local queryFlagPrefix = ARGV[2]
local queryFlagSuffix = ARGV[3]

local queryFlagPrefixLen = string.len(queryFlagPrefix)
local queryFlagMarker = queryFlagPrefix .. queryFlagSuffix;
local found = {}
local waitingIds = {}
local missingIds = {}

local result = {}
table.insert(result, found)
table.insert(result, waitingIds)
table.insert(result, missingIds)

for keyIdx, key in ipairs(KEYS) do

  local item = redis.call('get', key)
  redis.log(redis.LOG_DEBUG, "Key '" .. key .. "' = '" .. tostring(item) .. "'")

  -- check if item is in the cache
  if item then

    local itemPrefix = string.sub(item, 1, queryFlagPrefixLen)
    redis.log(redis.LOG_DEBUG, "Item Prefix '" .. itemPrefix .. "' = '" .. queryFlagPrefix .. "'")

    -- check if item is currently being queried by another thread
    if itemPrefix == queryFlagPrefix then
      redis.log(redis.LOG_NOTICE, "Waiting for item '" .. key .. "'")
      table.insert(waitingIds, key)
    else
      redis.log(redis.LOG_DEBUG, "Found item '" .. key .. "'")
      table.insert(found, item)
    end

  else
      redis.log(redis.LOG_NOTICE, "Missing item '" .. key .. "'. Setting query flag to " .. queryFlagMarker)
      redis.call('setex', key, ttl, queryFlagMarker)
      table.insert(missingIds, key)
  end

end

return result