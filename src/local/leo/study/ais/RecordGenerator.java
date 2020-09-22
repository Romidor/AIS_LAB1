package local.leo.study.ais;

import java.util.Date;
import java.util.Random;

public class RecordGenerator {

    private final float p1min = 0.01f;
    private final float p1max = 1.0f;
    private final float p2min = 1.0f;
    private final float p2max = 300.0f;

    private Random rnd;

    public RecordGenerator() {
        rnd = new Random(new Date().getTime());
    }

    public Record generate() {
        float p1;
        float p2;
        int p3;

        int cluster = rnd.nextInt(3);
        //System.out.println("cluster: " + cluster);

        switch (cluster) {
            case 0: {
                p2 = rnd.nextFloat() * 80.0f; //0-80
                break;
            }
            case 1: {
                p2 = rnd.nextFloat() * 80.0f + 110.0f; //110-190
                break;
            }
            case 2: {
                p2 = rnd.nextFloat() * 100.0f + 200.0f; //200-300
                break;
            }
            default: {
                System.out.println("cluster number is not in [0;2]");
                p2 = rnd.nextFloat() * (p2max - p2min) + p2min;
            }
        }
        p1 = (rnd.nextFloat() * (p1max - p1min) + p1min);
        if (p1 > p1max) {
            p1 = p1max;
        }
        if (p1 < p1min) {
            p1 = p1min;
        }
        p3 = rnd.nextInt(3);
        //System.out.println("p3: " + p3);
        Param3 param3 = Param3.values()[p3];
        return new Record(p1, p2, param3, cluster);
    }
}
