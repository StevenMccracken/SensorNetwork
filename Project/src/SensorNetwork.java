import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Solves the problem of finding the largest group of sensors in a
 * forest that are all within a certain distance of each other.
 */
public class SensorNetwork {
    public static double distance;
    public static ArrayList<Point> sensors;

    /**
     * Computes the largest subset of (x,y) points that are within a distance d of each other.
     * The distance d should be a double value in the first line of the input file.
     * The (x,y) coordinates for the "sensors" should be on the subsequent lines, one per line.
     * Pipe the input file into the program for correct functionality.
     * @param args Arguments for this program determine whether output should be written to a file
     *             as well as printed out to the console. Only enter boolean values for the arguments,
     *             "true" or "false". args[0] determines whether or not to write the result to a file.
     *             Args[1] determines whether or not to print the result to the screen.
     */
    public static void main(String[] args) {
        boolean writeToFile = args.length >= 1 ? Boolean.parseBoolean(args[0]) : true;
        boolean printResult = args.length >= 2 ? Boolean.parseBoolean(args[1]) : true;

        long t1 = System.nanoTime();
        getInput(); // Populates sensors array list
        long t2 = System.nanoTime();
        double secondsElapsed = ((double)t2 - t1) / 1000000000;
        System.out.printf("Processing the %d sensors and plotting their coordinates from the input file took %.4f seconds%n", sensors.size(), secondsElapsed);

        t1 = System.nanoTime();
        sensors.sort((p,q) -> p.compareTo(q)); // Sort the 2D points based on the x-coordinate in ascending order
        t2 = System.nanoTime();
        secondsElapsed = ((double)t2 - t1) / 1000000000;
        System.out.printf("Sorting the sensor coordinates took %.4f seconds%n", secondsElapsed);

        ArrayList<Point> currSet = new ArrayList<>(), maxCount = new ArrayList<>();
        currSet.add(sensors.get(0));

        /*
            If the current sensor is within distance d of all the other sensors in the current set,
            then add that sensor to the current set and check the next sensor. Also, if the current
            set accumulates more sensors than the count of the max set, update the max set.
         */
        t1 = System.nanoTime();
        for(int i = 1; i < sensors.size(); i++) {
            Point p = sensors.get(i);
            boolean isValid = true;
            for(Point q : currSet) {
                if(distance(p,q) > distance) {
                    isValid = false;
                    break;
                }
            }
            if(isValid) currSet.add(p);
            if(currSet.size() > maxCount.size()) maxCount = currSet;
        }
        t2 = System.nanoTime();
        secondsElapsed = ((double)t2 - t1) / 1000000000;
        System.out.printf("Finding the most populated subset of sensors took %.4f seconds%n", secondsElapsed);

        if(writeToFile) writeAnswer(maxCount);
        if(printResult) {
            System.out.printf("Here is the largest subset of sensors within %.1f distance of each other (there are %d of them)%n", distance, maxCount.size());
            for(Point p : maxCount)
                System.out.printf("(%s)%n", p.toString());
        }
    }

    /**
     * Computes the Euclidean distance between two 2-D points
     * @param p the first point
     * @param q the second point
     * @return the euclidean distance between p and q
     */
    public static double distance(Point p, Point q) {
        double x = Math.pow(p.x - q.x, 2);
        double y = Math.pow(p.y - q.y, 2);
        return Math.sqrt(x + y);
    }

    /**
     * Gets distance and sensor coordinates from input file
     */
    public static void getInput() {
        sensors = new ArrayList<>();

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        try {
            // Get distance from first line of file
            distance = Double.parseDouble(input.readLine());

            // Read all sensor coordinates from file
            while((line = input.readLine()) != null) {
                double[] coords = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                sensors.add(new Point(coords[0], coords[1]));
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                input.close();
            } catch(IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Writes the sensors in the input parameter "points" to a file
     * @param points the largest subset of points within a distance d of each other
     */
    public static void writeAnswer(ArrayList<Point> points) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter("chosen.txt");
            bw = new BufferedWriter(fw);
            for(Point p : points)
                bw.write(p.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int compareTo(Point p) {
        if(this.x > p.x) return 1;
        else if(this.x < p.x) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
}