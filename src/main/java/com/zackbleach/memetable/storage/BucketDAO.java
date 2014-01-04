package com.zackbleach.memetable.storage;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.zackbleach.memetable.bucket.Bucket;

@Repository
public class BucketDAO {

    SessionFactory sessionFactory = HibernateUtilTest.getSessionFactory();

    public Bucket save(Bucket bucket) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Integer bucketId = null;
        try {
            transaction = session.beginTransaction();
            bucketId = (Integer) session.save(bucket);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return retrieveSingle(bucketId);
    }

    public Bucket retrieveSingle(int bucketId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Bucket bucket = null;
        try {
            transaction = session.beginTransaction();
            bucket = (Bucket) session.get(Bucket.class, bucketId);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return bucket;
    }

    @SuppressWarnings("unchecked")
    public List<Bucket> retrieveList() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Bucket> buckets = null;
        try {
            transaction = session.beginTransaction();
            buckets = session.createQuery("from Bucket").list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return buckets;
    }

    public void delete(int bucketId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Bucket bucket = (Bucket) session.get(Bucket.class, bucketId);
            session.delete(bucket);
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
