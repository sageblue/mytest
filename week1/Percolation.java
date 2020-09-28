import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private boolean[][] siteOpen;
    private int[] parent;
    private final int size;
    private int openSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    String.format("Percolation size n (%d) must be greater than 0", n));
        }
        size = n;
        siteOpen = new boolean[n][n];
        parent = new int[n];
        uf = new WeightedQuickUnionUF(n * n);

        for (int i = 0; i < n; i++) {
            /* since 0 is a valid index.
             * set to any value that is outside of index range
             *  of a valid site
             *  */
            parent[i] = size * size;
            for (int j = 0; j < n; j++) {
                siteOpen[i][j] = false;
            }
        }

        openSite = 0;
    }

    private void checkRowColIndex(int row, int col) {
        if (row < 1 || row > size) {
            throw new IllegalArgumentException(
                    String.format(
                            "Percolation.Open() index must be within [1,%d], row index %d out of range",
                            size, row));
        }
        if (col < 1 || col > size) {
            throw new IllegalArgumentException(
                    String.format(
                            "Percolation.Open() index must be within [1,%d], row index %d out of range",
                            size, col));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRowColIndex(row, col);
        row--;
        col--;

        if (siteOpen[row][col])
            return;

        siteOpen[row][col] = true;
        openSite++;

        /* if up site is also open, connect to it*/
        if ((row > 0) && siteOpen[row - 1][col]) {
            uf.union((row - 1) * size + col, row * size + col);
        }

        /* if left site is also open, connect to it*/
        if ((col > 0) && siteOpen[row][col - 1]) {
            uf.union(row * size + col - 1, row * size + col);
        }

        /* if right site is also open, connect to it*/
        if ((col < size - 1) && siteOpen[row][col + 1]) {
            uf.union(row * size + col, row * size + col + 1);
        }

        /* if down site is also open, connect to it*/
        if ((row < size - 1) && siteOpen[row + 1][col]) {
            uf.union(row * size + col, (row + 1) * size + col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowColIndex(row, col);
        row--;
        col--;
        return siteOpen[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowColIndex(row, col);
        row--;
        col--;

        if (!siteOpen[row][col])
            return false;

        if (row == 0)
            return true;

        int t = uf.find(row * size + col);

        /* check parent of first row */
        for (int a = 0; a < size; a++) {
            if (siteOpen[0][a]) {
                if (uf.find(a) == t)
                    return true;
            }
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSite;
    }

    // does the system percolate?
    public boolean percolates() {
        /* check parent of first row */
        for (int a = 0; a < size; a++) {
            if (siteOpen[0][a]) {
                parent[a] = uf.find(a);
            }
        }

        /* check parent of last row */
        for (int b = 0; b < size; b++) {
            int t;
            if (siteOpen[size - 1][b]) {
                t = uf.find((size - 1) * size + b);
                /* search top row's parent */
                for (int a = 0; a < size; a++) {
                    if (parent[a] == t) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        /* empty function */


    }
}
