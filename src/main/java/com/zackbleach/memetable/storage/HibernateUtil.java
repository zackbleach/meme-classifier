package com.zackbleach.memetable.storage;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.zackbleach.memetable.classification.bucketer.Bucket;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure()
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
