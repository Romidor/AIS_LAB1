package local.leo.study.ais;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static int RECORD_NUMBER = 100;
    private static String FILE = "data.dat";

    public static void load(Record[] records) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            for (int i = 0; i < RECORD_NUMBER; i++) {
                //records[i] = recordGenerator.generate();
                records[i] = (Record) objectInputStream.readObject();
                System.out.println(records[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save(Record[] records) {
        try {
            FileOutputStream outputStream = new FileOutputStream("data.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            for (Record r : records) {
                objectOutputStream.writeObject(r);
            }

            //закрываем поток и освобождаем ресурсы
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void normalize(Record[] records) {
        float newValue;
        for (Record r : records) {
            newValue = (r.getParam2() - 1.0f) / 299;
            r.setParam2(newValue);
            newValue = (r.getParam1() - 0.01f) / 0.99f;
            if (newValue < 0) {
                System.out.println("not normalized value = " + r.getParam1() + ", normalized = " + newValue);
            }
            r.setParam1(newValue);
        }
    }

    public static Record[] kmeansWithEuclidMetric(Record[] records, int k) {
        KMeans kmeans = new KMeans();
        return kmeans.cluster(new EuclidMetric(), records, k);
    }

    public static Record[] kmeansWithManhattanMetric(Record[] records, int k) {
        KMeans kmeans = new KMeans();
        return kmeans.cluster(new ManhattanMetric(), records, k);
    }

    public static void drawRecords(Record[] records, JComponent painter) {
        JFrame frame = new JFrame("K-means");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(painter);
        frame.setVisible(true);
    }

    public static void printSortedByAssignedCluster(Record[] records) {
        Arrays.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Integer.compare(o1.getAssignedCluster(), o2.getAssignedCluster());
            }
        });
        for (Record r : records) {
            System.out.println(r);
        }
    }

    public static void printSortedByKMeansCluster(Record[] records) {
        Arrays.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Integer.compare(o1.getKmeansCluster(), o2.getKmeansCluster());
            }
        });
        for (Record r : records) {
            System.out.println(r);
        }
    }

    public static void main(String[] args) {
        RecordGenerator recordGenerator = new RecordGenerator();
        Record[] records = new Record[RECORD_NUMBER];

        File file = new File(FILE);
        if (file.exists()) {
            load(records);
        } else {
            for (int i = 0; i < RECORD_NUMBER; i++) {
                records[i] = recordGenerator.generate();
                System.out.println(records[i]);
            }
            save(records);
        }

        normalize(records);
        drawRecords(records, new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(2));
                for (Record r : records) {
                    switch (r.getParam3()) {
                        case Tolyatty: {
                            g2d.setPaint(Color.green);
                            break;
                        }
                        case Samara: {
                            g2d.setPaint(Color.blue);
                            break;
                        }
                        default: {
                            g2d.setPaint(Color.RED);
                        }
                    }
                    //рисует точку
                    g2d.drawLine(20 + (int) (r.getParam2() * 299 + 1), 10 + (int) (r.getParam1() * 100), // * 299 + 1
                            20 + (int) (r.getParam2() * 299 + 1), 10 + (int) (r.getParam1() * 100));
                }
                //draw legend
                g2d.setPaint(Color.black);
                g2d.drawLine(20, 5, 20, 110);
                g2d.drawLine(20, 110, 330, 110);
                g2d.drawString("P", 8, 30);
                g2d.drawString("a", 8, 40);
                g2d.drawString("r", 8, 50);
                g2d.drawString("a", 8, 60);
                g2d.drawString("m", 8, 70);
                g2d.drawString("1", 8, 80);
                g2d.drawString("Param2", 170, 125);
                g2d.drawString("1", 8, 10);
                g2d.drawLine(18, 5, 22, 5);
                g2d.drawString("0", 8, 125);
                g2d.drawString("300", 315, 125);
                g2d.drawLine(330, 108, 330, 112);
                super.repaint();
            }
        });

        Record[] centers;
        //centers = kmeansWithEuclidMetric(records, 3);
        centers = kmeansWithManhattanMetric(records, 3);
        drawRecords(records, new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                g2d.setStroke(new BasicStroke(2));
                for (int i = 0; i < centers.length; i++) {
                    switch (centers[i].getParam3()) {
                        case Tolyatty: {
                            g2d.setPaint(Color.green);
                            break;
                        }
                        case Samara: {
                            g2d.setPaint(Color.blue);
                            break;
                        }
                        default: {
                            g2d.setPaint(Color.RED);
                        }
                    }
                    g2d.drawString("X", (int) (centers[i].getParam2() * 299 + 1), (int) (centers[i].getParam1() * 100));
                    for (Record r : records) {
                        if (r.getKmeansCluster() == centers[i].getKmeansCluster()) {
                            if (r.getKmeansCluster() == 0) {
                                g2d.drawString("1", 20 + (int) (r.getParam2() * 299 + 1), 10 + (int) (r.getParam1() * 100));
                            } else if (r.getKmeansCluster() == 1) {
                                g2d.drawString("2", 20 + (int) (r.getParam2() * 299 + 1), 10 + (int) (r.getParam1() * 100));
                            } else {
                                g2d.drawString("3", 20 + (int) (r.getParam2() * 299 + 1), 10 + (int) (r.getParam1() * 100));
                            }
                        }
                    }
                }
                //draw legend
                g2d.setPaint(Color.black);
                g2d.drawLine(20, 5, 20, 110);
                g2d.drawLine(20, 110, 330, 110);
                g2d.drawString("P", 8, 30);
                g2d.drawString("a", 8, 40);
                g2d.drawString("r", 8, 50);
                g2d.drawString("a", 8, 60);
                g2d.drawString("m", 8, 70);
                g2d.drawString("1", 8, 80);
                g2d.drawString("Param2", 170, 125);
                g2d.drawString("1", 8, 10);
                g2d.drawLine(18, 5, 22, 5);
                g2d.drawString("0", 8, 125);
                g2d.drawString("300", 315, 125);
                g2d.drawLine(330, 108, 330, 112);
                super.repaint();
            }
        });
        //printSortedByAssignedCluster(records);
        printSortedByKMeansCluster(records);
    }


}
