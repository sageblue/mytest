import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int[] threshold;
    private final double size;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    String.format("Percolation size n (%d) must be greater than 0", n));
        }
        if (trials <= 0) {
            throw new IllegalArgumentException(
                    String.format("Percolation trial count (%d) must be greater than 0", trials));
        }

        threshold = new int[trials];
        size = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            /* random index */
            int x, y;
            /* need at least n site open to be percolate */
            for (int k = 0; k < n; k++) {
                do {
                    x = StdRandom.uniform(1, n + 1);
                    y = StdRandom.uniform(1, n + 1);
                } while (p.isOpen(x, y));
                p.open(x, y);
            }

            if (p.percolates()) {
                threshold[i] = n;
                continue;
            }

            for (int k = n + 1; k < n * n + 1; k++) {
                do {
                    x = StdRandom.uniform(1, n + 1);
                    y = StdRandom.uniform(1, n + 1);
                } while (p.isOpen(x, y));
                p.open(x, y);
                if (p.percolates()) {
                    threshold[i] = k;
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
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(threshold.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(threshold.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }

        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        PercolationStats pS = new PercolationStats(n, m);

        StdOut.println("mean                    = " + pS.mean());
        StdOut.println("stddev                  = " + pS.stddev());
        StdOut.println("95% confidence interval = [" + pS.confidenceLo()
                               + "," + pS.confidenceLo() + "]");
    }
}
