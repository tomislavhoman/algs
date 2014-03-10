/**------------------------------------------------------------
 * Author:         Tomislav Homan
 * Coursera login: tomislav.homan@gmail.com
 * Written:        4.2.2014.
 *
 * Compilation:    javac PercolationStats.java
 * Execution:      java PercolationStats GridSize NoSamples
 * 
 * Tests working of Percolation model and calculates mean 
 * percolation probability for which system percolates, 
 * standard deviation and confindence bounds.
 * 
 *------------------------------------------------------------*/
public class PercolationStats {
    
    private int n;
    private int t;
    private double[] samples;
    
    private double mean = 0.0d;
    private double stddev = 0.0d;
    private double confidenceLo = 0.0d;
    private double confidenceHi = 0.0d;
    
    /**------------------------------------------------------------
     * Performs T independent computational 
     * experiments on an N-by-N grid
     *------------------------------------------------------------*/
    public PercolationStats(int N, int T) { 
        
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        
        this.n = N;
        this.t = T;
        this.samples = new double[T];
        
        for (int i = 0; i < t; i++) {
            samples[i] = performMeasurment();
        }
        
        calculateStatistics();
    }
    
    /**------------------------------------------------------------
     * Uses StdStats class to calculate mean, stddev, 
     * confidenceLo and confidenceHi values
     *------------------------------------------------------------*/
    private void calculateStatistics() {
        mean = StdStats.mean(samples);
        stddev = StdStats.stddev(samples);
        
        double confidenceOffset = (1.96d * stddev) / Math.sqrt(t);
        confidenceLo = mean - confidenceOffset;
        confidenceHi = mean + confidenceOffset;
    }
    
    /**------------------------------------------------------------
     * Crates percolation object and estimates probabilty p via 
     * Monte Carlo method
     *------------------------------------------------------------*/
    private double performMeasurment() {
        Percolation percolation = new Percolation(n);
        int numberOfOpenedSpots = 0;
        while (!percolation.percolates()) {
            int i = StdRandom.uniform(n) + 1;
            int j = StdRandom.uniform(n) + 1;
            
            if (!percolation.isOpen(i, j)) {
                percolation.open(i, j);
                numberOfOpenedSpots++;
            }
        }
        return (double) numberOfOpenedSpots / (double) (n * n);
    }
    
    /**------------------------------------------------------------
     * Samples mean of percolation threshold
     *------------------------------------------------------------*/
    public double mean() {
        return mean;
    }
    
    /**------------------------------------------------------------
     * Samples standard deviation of percolation threshold
     *------------------------------------------------------------*/
    public double stddev() {
        return stddev;
    }                   
    
    /**------------------------------------------------------------
     * Returns lower bound of the 95% confidence interval
     *------------------------------------------------------------*/
    public double confidenceLo() {
        return confidenceLo;
    }             
    
    /**------------------------------------------------------------
     * Returns upper bound of the 95% confidence interval
     *------------------------------------------------------------*/
    public double confidenceHi() {
        return confidenceHi;
    }          
    
    /**------------------------------------------------------------
     * Tests the Percolation class
     *------------------------------------------------------------*/
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
        
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t); 
        
        StdOut.printf("mean                    = %g\n", stats.mean());
        StdOut.printf("stddev                  = %g\n", stats.stddev());
        StdOut.printf("95%% confidence interval = %g, %g\n", 
                      stats.confidenceLo(), stats.confidenceHi());
    }
}