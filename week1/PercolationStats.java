import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.Scanner;

public class PercolationStats {

    private int[] threshold;
    private double size;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        StdOut.println("Run PercolationStats " + n + " , " + trials);
        threshold = new int[trials];
        size = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            /* random index */
            int x, y;
            /* need at least n site open to be percolate */
            for (int k = 0; k < n; k++) {
                do {
                    x = StdRandom.uniform(0, n);
                    y = StdRandom.uniform(0, n);
                } while (p.isOpen(x, y));
                p.open(x, y);
            }

            if (p.percolates()) {
                threshold[i] = n;
                StdOut.println("[" + i + "]: percolates at " + n);
                break;
            }

            for (int k = n + 1; k < n * n + 1; k++) {
                do {
                    x = StdRandom.uniform(0, n);
                    y = StdRandom.uniform(0, n);
                } while (p.isOpen(x, y));
                p.open(x, y);
                if (p.percolates()) {
                    threshold[i] = k;
                    StdOut.println("[" + i + "]: percolates at " + k);
                    break;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold) / size;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold) / size;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(threshold.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(threshold.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();
        int m = input.nextInt();

        PercolationStats pS = new PercolationStats(n, m);

        StdOut.println("mean                    = " + pS.mean());
        StdOut.println("stddev                  = " + pS.stddev());
        StdOut.println("95% confidence interval = [" + pS.confidenceLo()
                               + "," + pS.confidenceLo() + "]");
    }
}
