package com.zackbleach.memetable.clustering;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class HierarchyBuilder {

    private Cluster hierarchy;
    private static final Log logger = LogFactory.getLog(HierarchyBuilder.class);

    // TODO: delete this?
    private final static int MAGICAL_MEME_DISTANCE = 10;

    public HierarchyBuilder() {
        // retrieve previous hierarchy - if one doesn't exist, start a new one?
    }

    /**
     * This is way to complicated - at this point I think I'm going to go for the bucket approach
     */
    //public Cluster addToHeirarchy(Cluster c) {
    //look at each node in a breadth first manner
    //create a cluster pair for each node and the node we're adding
    //get the children of the pair with the shortest distance
    //repeat process
    //
    //stop when no children are present
    //When you've found the child node with the least distance there are the following options:
    //      *MMD > MAX_MMD - create new cluster **NOTE - COULD THIS BE DONE AT THE TOP LEVEL?**
    //      *MMD < MAX_MMD
    //          *Create a new cluser
    //}

    // TODO: add to hierarchy method
    // This should really be replaced to incremental calls to the add method
    public Cluster buildHeirarchy(List<Cluster> clusters) {
        if (clusters.isEmpty()) {
            throw new IllegalArgumentException("Hierarchy can not be built from supplied data set");
        }
        Set<ClusterPair> pairs = getPairings(clusters);
        while (pairs.size() > 0) {
            ClusterPair[] orderedPairs = pairs.toArray(new ClusterPair[pairs
                    .size()]);
            Arrays.sort(orderedPairs);
            ClusterPair minDistance = orderedPairs[0];
            Cluster newCluster = minDistance.agglomerate();
            clusters.removeAll(Arrays.asList(minDistance.getClusters()));
            Iterator<ClusterPair> it = pairs.iterator();
            while (it.hasNext()) {
                ClusterPair p = it.next();
                if (!Collections.disjoint(Arrays.asList(p.getClusters()),
                        Arrays.asList(minDistance.getClusters()))) {
                    it.remove();
                }
            }
            for (Cluster c : clusters) {
                pairs.add(new ClusterPair(newCluster, c));
            }
            clusters.add(newCluster);
        }
       setHierarchy(clusters.get(0));
       return hierarchy;
    }

    // group all clusters in to pairs and calculate distance between them
    public Set<ClusterPair> getPairings(List<Cluster> clusters) {
        Set<ClusterPair> pairings = new HashSet<ClusterPair>();
        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);
            for (int j = i; j < clusters.size(); j++) {
                Cluster otherCluster = clusters.get(j);
                if (cluster.getFeature().getDistance(otherCluster.getFeature()) != 0) {
                    pairings.add(new ClusterPair(cluster, otherCluster));
                }
            }
        }
        return pairings;
    }

    public Cluster getHierarchy() {
        return hierarchy;
    }

    private void setHierarchy(Cluster hierarchy) {
        this.hierarchy = hierarchy;
    }

}
