package com.zackbleach.memetable.storage;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.zackbleach.memetable.classification.bucketer.Bucket;

public class HibernateUtilTest {

    /**
     * This is a potential way of using an in memory test database.
     * TODO: come back to this
     */

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate-test.cfg.xml")
                    .addPackage("com.zackbleach.bucket") // the fully qualified
                    .addAnnotatedClass(Bucket.class).buildSessionFactory();
                                                    // package name

        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
