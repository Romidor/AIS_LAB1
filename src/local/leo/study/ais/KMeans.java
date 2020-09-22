package local.leo.study.ais;

import java.util.*;

public class KMeans {

    private final int MAX_ITERATIONS_COUNT = 1000;

    public Record[] cluster(Metric metric, Record[] set, int k) {
        Random rnd = new Random(new Date().getTime());
        Record[] centers = new Record[k];
        Set<Integer> centersIndexes = new HashSet<>(k);
        while (centersIndexes.size() < k) {
            centersIndexes.add(rnd.nextInt(set.length));
        }
        Iterator<Integer> iter = centersIndexes.iterator();
        //выбор начальных центров кластеров
        for (int i = 0; i < k && iter.hasNext(); i++) {
            Record r = set[iter.next()];
            centers[i] = new Record(r.getParam1(), r.getParam2(), r.getParam3(), r.getAssignedCluster());
            centers[i].setKmeansCluster(i);
            System.out.println("Start center of cluster " + i + ": " + centers[i].toString());
            iter.remove();
        }

        //формирование кластеров
        double e = Double.MAX_VALUE;
        for (int l = 0; l < MAX_ITERATIONS_COUNT; l++) {
            double newE = 0.0;
            for (int i = 0; i < set.length; i++) {
                float d, min;
                int cluster = 0;
                d = min = metric.distanceBetween(set[i], centers[cluster]);
                for (int j = 1; j < k; j++) {
                    d = metric.distanceBetween(set[i], centers[j]);
                    if (d < min) {
                        min = d;
                        cluster = j;
                    }
                }
                newE += min * min;
                set[i].setKmeansCluster(cluster);
            }

            //if (Math.abs(e - newE) <= newE / 100000) {
            if (e == newE) {
                System.out.println("Iteration: " + l + ". E = " + newE + '.');
                return centers;
            } else {
                System.out.println("E = " + newE);
                e = newE;
            }

            //перераспределение центров кластеров
            for (int i = 0; i < k; i++) {
                float param1avg = 0;
                float param2avg = 0;
                int p3T = 0;
                int p3C = 0;
                int p3S = 0;
                int clusterVolume = 0;
                for (int j = 0; j < set.length; j++) {
                    if (set[j].getKmeansCluster() == i) {
                        param1avg += set[j].getParam1();
                        param2avg += set[j].getParam2();
                        //param3avg += set[j].differenceFunction(centers[i]);
                        switch (set[j].getParam3()) {
                            case Tolyatty: p3T++; break;
                            case Chapaevsk: p3C++; break;
                            case Samara: p3S++; break;
                        }
                        clusterVolume++;
                    }
                }
                if (clusterVolume > 0) {
                    param1avg /= clusterVolume;
                    param2avg /= clusterVolume;
                    Param3 p3;
                    int m = Math.max(Math.max(p3C, p3T), p3S);
                    if (m == p3T) {
                        p3 = Param3.Tolyatty;
                    } else if (m == p3C) {
                        p3 = Param3.Chapaevsk;
                    } else {
                        p3 = Param3.Samara;
                    }
                    centers[i].setParam1(param1avg);
                    centers[i].setParam2(param2avg);
                    centers[i].setParam3(p3);
                    System.out.println("Center of cluster " + i + ": " + centers[i].toString());
                } else {
                    System.out.println("cluster " + i + " is empty!");
                }
            }
            //System.out.println("Iteration: " + l + ". E = " + newE + '.');
        }
        return centers;
    }
}
