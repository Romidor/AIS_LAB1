package local.leo.study.ais;

public class ManhattanMetric implements Metric {

    @Override
    public float distanceBetween(Record a, Record b) {
        float s1, s2;
        int s3;
        s1 = Math.abs(a.getParam1() - b.getParam1());
        s2 = Math.abs(a.getParam2() - b.getParam2());
        s3 = Math.abs(a.differenceFunction(b));
        return s1 + s2 + s3;
    }

}
