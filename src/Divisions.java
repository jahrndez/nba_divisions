import com.google.code.ekmeans.EKmeans;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Divisions {
    public static void main(String[] args) {
        kmeans();
        System.out.println("ekmeans:\n");
        ekmeans();
    }

    public static void kmeans() {
        try {
            Dataset data = FileHandler.loadDataset(new File("Divisions.csv"), 0, ",");
            System.out.println(data.noAttributes());
            for (int i = 0; i < data.size(); i++) {
                System.out.println(data.instance(i).classValue() + ": " + data.instance(i).value(0) + ", " + data.instance(i).value(1));
            }

            Clusterer km = new KMeans(8);

            Dataset[] clusters = km.cluster(data);

            System.out.println();

            for (int i = 0; i < clusters.length; i++) {
                Dataset cluster = clusters[i];
                for (int j = 0; j < cluster.size(); j++) {
                    System.out.println(cluster.instance(j).classValue());
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ekmeans() {
        int n = 32;
        int k = 8;
        Random random = new Random(System.currentTimeMillis());

        String[] names = new String[n];
        double[][] points = new double[n][2];
        double[][] centroids = new double[k][2];

        try {
            File file = new File("Divisions.csv");
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");

                double lat = Double.parseDouble(tokens[1]);
                double lon = Double.parseDouble(tokens[2]);
                names[i] = tokens[0];
                points[i][0] = lat;
                points[i][1] = lon;
                i++;
            }

            // min lat = 26, max lat = 47, range = 21
            // min lon = 71, max lon = 122, range = 51
            for (i = 0; i < k; i++) {
                int rand1 = random.nextInt();
                int rand2 = random.nextInt();

                // mod range + 1
                centroids[i][0] = (((rand1 % 22) + rand1) % 22) + 26;
                centroids[i][1] = (((rand2 % 52) + rand2) % 52) + 71;
            }

            EKmeans eKmeans = new EKmeans(centroids, points);
            eKmeans.setEqual(true);

            eKmeans.run();

            int[] assignments = eKmeans.getAssignments();
            Map<Integer, List<String>> map = new HashMap<>();
            for (i = 0; i < assignments.length; i++) {
                int division = assignments[i];
                if (!map.containsKey(division)) {
                    map.put(division, new ArrayList<>());
                }

                List<String> list = map.get(division);
                list.add(names[i]);
            }

            for (List<String> list : map.values()) {
                list.forEach(System.out::println);
                System.out.println();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
