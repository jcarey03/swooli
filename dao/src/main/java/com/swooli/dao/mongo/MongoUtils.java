package com.swooli.dao.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public final class MongoUtils {

    public static final String MAX_OBJECT_ID = "ffffffffffffffffffffffff";

    public static boolean exists(final String id, Class<?> clazz, final MongoTemplate mongoTemplate) {
        final Criteria criteria = Criteria.where("id").is(id);
        final Query query = new Query(criteria);
        return mongoTemplate.count(query, clazz) > 0;
    }

    public static boolean updateField(final String field, final Object value, final Update update) {
        if(value != null) {
            update.set(field, value);
            return true;
        }
        return false;
    }

}