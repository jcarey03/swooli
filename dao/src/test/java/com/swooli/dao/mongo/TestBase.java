package com.swooli.dao.mongo;

import com.mongodb.DB;
import com.mongodb.MongoException;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TestBase {

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Before
    public void setup() {
        try {
            final ApplicationContext ctx = new ClassPathXmlApplicationContext("/dao-context.xml");
            ctx.getBean("mongoTemplate", MongoTemplate.class).execute(new DbCallback<Void>() {
                @Override
                public Void doInDB(DB db) throws MongoException, DataAccessException {
                    final String filename = TestBase.class.getResource("/testdata.js").getFile().toString();
                    db.eval(String.format("load('%s')", filename), (Object) null);
                    return null;
                }
            });
        } catch(final Exception ex) {
            throw new RuntimeException("Failed to load test data due to: " + ex);
        }
    }

}