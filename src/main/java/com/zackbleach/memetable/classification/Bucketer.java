package com.zackbleach.memetable.classification;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zackbleach.memetable.bucket.Bucket;
@Component
public class Bucketer{

   // TODO: there will be another app in the background
   // that temporarily scrapes templates from popular
   // meme sites - those will be saved in to a database
   // and used to populate the initial buckets
   //
   // There may have to be a different type of storage for
   // clusters found that don't currently have a matching
   // template

   // @Autowire
   // TemplateStorage templateStorage;
   Set<Bucket> buckets = new HashSet<Bucket>();

    public Bucketer() {
    }

    public Set<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(Set<Bucket> buckets) {
        this.buckets = buckets;
    }

}

