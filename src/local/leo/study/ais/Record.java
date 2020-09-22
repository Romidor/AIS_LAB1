package local.leo.study.ais;

import java.io.Serializable;

public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    private float param1; //[0.01; 1]
    private float param2; //[1; 300]
    private Param3 param3; //[Tolyatti, Samara, Chapaevsk]
    private float param3Normalized;
    private int assignedCluster; //[0; 2]
    private int kmeansCluster; //cluster that the k-means algorithm calculates

    public Record() {
    }

    public Record(float param1, float param2, Param3 param3, int cluster) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.assignedCluster = cluster;
    }

    public float getParam1() {
        return param1;
    }

    public void setParam1(float param1) {
        this.param1 = param1;
    }

    public float getParam2() {
        return param2;
    }

    public void setParam2(float param2) {
        this.param2 = param2;
    }

    public Param3 getParam3() {
        return param3;
    }

    public void setParam3(Param3 param3) {
        this.param3 = param3;
    }

    public int getAssignedCluster() {
        return assignedCluster;
    }

    public int getKmeansCluster() {
        return kmeansCluster;
    }

    public void setKmeansCluster(int kmeansCluster) {
        this.kmeansCluster = kmeansCluster;
    }

    public int differenceFunction(Record b) {
        return this.param3.ordinal() != b.getParam3().ordinal() ? 1 : 0;
    }

    @Override
    public String toString() {
        return "Record(" + param1 +
                ", " + param2 +
                ", " + param3 +
                ')' +
                " assigned cluster: " + assignedCluster +
                ", k-means cluster: " + kmeansCluster;
    }

}
