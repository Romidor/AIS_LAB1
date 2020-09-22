package local.leo.study.ais;

public class EuclidMetric implements Metric {

    @Override
    public float distanceBetween(Record a, Record b) {
        float s1, s2;
        int s3;
        s1 = a.getParam1() - b.getParam1();
        s2 = a.getParam2() - b.getParam2();
        s3 = a.differenceFunction(b);
        return (float) Math.sqrt(s1*s1 + s2*s2 + s3*s3);
    }

}
