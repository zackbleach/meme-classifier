package com.zackbleach.memetable.util;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.zackbleach.memetable.clustering.Cluster;

@Component
public class ClusteringUtils {

    private static final Logger log = Logger
            .getLogger(ClusteringUtils.class);


    public void printHeirarchy(Cluster root) {
        Deque<Cluster> q = new ArrayDeque<Cluster>();
        if (root == null) {
            return;
        }
        q.add(root);
        while (!q.isEmpty()) {
            Cluster c = q.remove();
            System.out.println(c);
            if (!c.isLeafNode()) {
                for (Cluster child : c.getChildren()) {
                    q.add(child);
                }
            }
        }
    }


}
