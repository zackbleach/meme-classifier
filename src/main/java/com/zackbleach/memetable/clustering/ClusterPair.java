package com.zackbleach.memetable.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterPair implements Comparable<ClusterPair> {

    private Cluster[] clusters = new Cluster[2];
    private float distance;

    private static final Logger logger = LoggerFactory
            .getLogger(ClusterPair.class);

    public ClusterPair(Cluster left, Cluster right) {
        clusters[0] = left;
        clusters[1] = right;
        distance = left.getFeature().getDistance(right.getFeature());
    }

    public float getDistance() {
        return distance;
    }

    public Cluster[] getClusters() {
        return clusters;
    }

    public Cluster agglomerate() {
        Cluster left = clusters[0];
        Cluster right = clusters[1];
        Cluster cluster = new Cluster();
        cluster = setClusterName(left, right, cluster);
        cluster = setClusterChildren(left, right, cluster);
        cluster = setClusterMemeFeature(left, right, cluster);
        cluster = setClusterHeight(left, right, cluster);
        cluster.setDifference(getDistance());
        return cluster;
    }

    private Cluster setClusterMemeFeature(Cluster left, Cluster right,
            Cluster cluster) {
        MemeFeature m = left.getFeature().averageFeature(right.getFeature());
        cluster.setFeature(m);
        return cluster;
    }

    private Cluster setClusterChildren(Cluster left, Cluster right,
            Cluster cluster) {
        Cluster[] children = new Cluster[2];
        children[0] = left;
        children[1] = right;
        cluster.setChildren(children);
        return cluster;
    }

    private Cluster setClusterName(Cluster left, Cluster right, Cluster cluster) {
        if (StringUtils.isBlank(left.getName()) && StringUtils.isBlank(right.getName()))  {
            return cluster;
        }
        if (StringUtils.isBlank(left.getName())) {
            cluster.setName(right.getName());
            return cluster;
        }

        if (StringUtils.isBlank(right.getName())) {
            cluster.setName(left.getName());
            return cluster;
        }
        String longestSubstring = StringUtils.trim(longestSubstring(
                left.getName(), right.getName()));
        // TODO: If the substring does not appear in the split array of either
        // name then forget about it (probably a single letter or something
        // uninteresting).
        String[] nameLeftSplit = StringUtils.split(left.getName(), " ");
        String[] nameRightSplit = StringUtils.split(right.getName(), " ");
        List<String> possibleNames = new ArrayList<String>();
        possibleNames.addAll(Arrays.asList(nameLeftSplit));
        possibleNames.addAll(Arrays.asList(nameRightSplit));
        if (possibleNames.containsAll(Arrays.asList(StringUtils.split(
                longestSubstring, " ")))) {
            cluster.setName(longestSubstring);
            return cluster;
        }
        if (!StringUtils.isEmpty(left.getName())
                && !StringUtils.isEmpty(right.getName())) {
            if (left.getName().equals(right.getName())) {
                cluster.setName(left.getName());
            } else {
                cluster.setName(left.getName() + "+" + right.getName());
            }
        } else if (left.getName() != null) {
            cluster.setName(left.getName());
        } else if (right.getName() != null) {
            cluster.setName(right.getName());
        }
        return cluster;
    }

    private Cluster setClusterHeight(Cluster left, Cluster right,
            Cluster cluster) {
        int height = Math.max(left.getHeight(), right.getHeight()) + 1;
        cluster.setHeight(height);
        return cluster;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((clusters == null) ? 0 : clusters.hashCode());
        result = prime * result + Float.floatToIntBits(distance);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClusterPair other = (ClusterPair) obj;
        if (clusters == null) {
            if (other.clusters != null)
                return false;
        } else if (!clusters.equals(other.clusters))
            return false;
        if (Float.floatToIntBits(distance) != Float
                .floatToIntBits(other.distance))
            return false;
        return true;
    }

    @Override
    public int compareTo(ClusterPair otherClusterPair) {
        if (otherClusterPair.getDistance() == this.getDistance()) {
            return 0;
        }
        return (this.getDistance() > otherClusterPair.getDistance()) ? 1 : -1;
    }

    // Taken from: http://java.dzone.com/articles/longest-common-substring
    // TODO: This is a dynamic programming approach. Try suffix trees
    public String longestSubstring(String str1, String str2) {

        StringBuilder sb = new StringBuilder();
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
            return "";

        // ignore case
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();

        // java initializes them already with 0
        int[][] num = new int[str1.length()][str2.length()];
        int maxlen = 0;
        int lastSubsBegin = 0;

        for (int i = 0; i < str1.length(); i++) {
            for (int j = 0; j < str2.length(); j++) {
                if (str1.charAt(i) == str2.charAt(j)) {
                    if ((i == 0) || (j == 0))
                        num[i][j] = 1;
                    else
                        num[i][j] = 1 + num[i - 1][j - 1];

                    if (num[i][j] > maxlen) {
                        maxlen = num[i][j];
                        // generate substring from str1 => i
                        int thisSubsBegin = i - num[i][j] + 1;
                        if (lastSubsBegin == thisSubsBegin) {
                            // if the current LCS is the same as the last time
                            // this block ran
                            sb.append(str1.charAt(i));
                        } else {
                            // this block resets the string builder if a
                            // different LCS is found
                            lastSubsBegin = thisSubsBegin;
                            sb = new StringBuilder();
                            sb.append(str1.substring(lastSubsBegin, i + 1));
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

}
