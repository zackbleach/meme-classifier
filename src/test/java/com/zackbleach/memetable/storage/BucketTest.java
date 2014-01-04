package com.zackbleach.memetable.storage;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.zackbleach.memetable.bucket.Bucket;

public class BucketTest {

    private static final Log log =
        LogFactory.getLog(BucketTest.class);

    @Test
    public void storeAndRetrieveTest() {
        BucketDAO bucketDAO = new BucketDAO();
        Bucket bucket = new Bucket();
        bucket.setName("Advice Mallard");
        bucket.setPrototype(new double[]{1.0, 2.0, 4.5, 5.6});
        bucket.setImagePath("TestMemes/advicemallard.jpg");
        bucket = bucketDAO.save(bucket);
        Bucket retrievedBucket = bucketDAO.retrieveSingle(bucket.getId());
        log.warn(retrievedBucket.getName());
        bucketDAO.delete(bucket.getId());
    }

}
