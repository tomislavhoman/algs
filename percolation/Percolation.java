/**------------------------------------------------------------
 * Author:         Tomislav Homan
 * Coursera login: tomislav.homan@gmail.com
 * Written:        4.2.2014.
 *
 * Compilation:    javac Percolation.java
 * 
 * Impelements percolation model 
 *  
 *------------------------------------------------------------*/
public class Percolation {
    
    private static final int TOP_SITE_OFFSET = 1;
    private static final int BOTTOM_SITE_OFFSET = 2;
    
    private boolean[][] openedSpots; //Holds "openess" value of the system
    
    //Size of the system and openedSpots grids (Not counting virtual spots)
    private int n; 
    
    //Connected only to top virtual spot, used to check is the spot full
    private WeightedQuickUnionUF topSystem;
    //Connected to both virtual spots, used for percolation checking
    private WeightedQuickUnionUF topBottomSystem; 
    
    /**------------------------------------------------------------
     * Create N-by-N grid, with all sites blocked
     *------------------------------------------------------------*/
    public Percolation(int N) {
        this.n = N;
        this.openedSpots = new boolean[N][N];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //Initially the all spots are is closed
                openedSpots[i][j] = false; 
            }
        }
        
        //Extra spaces for virtual sites
        this.topSystem = new WeightedQuickUnionUF(n * n + 3); 
        this.topBottomSystem = new WeightedQuickUnionUF(n * n + 3); 
    }
    
    /**------------------------------------------------------------
     * Open site (row i, column j) if it is not already
     *------------------------------------------------------------*/
    public void open(int i, int j) {
        checkBounds(i, j);
        
        int rowIndex = convertToArrayIndex(i);
        int columnIndex = convertToArrayIndex(j);
        
        //The spot is already opened
        if (openedSpots[rowIndex][columnIndex]) {
            return;
        }
        
        openedSpots[rowIndex][columnIndex] = true;
        
        //Connect to the top neighbour if not in the top row
        if (rowIndex > 0 && openedSpots[rowIndex - 1][columnIndex]) {
            connectSpots(rowIndex, columnIndex, rowIndex - 1, columnIndex);
        }
        
        
        //Connect to the bottom neighbour if not in the bottom row
        if (rowIndex < n - 1 && openedSpots[rowIndex + 1][columnIndex]) {
            connectSpots(rowIndex, columnIndex, rowIndex + 1, columnIndex);      
        }
        
        //Connect to the left neighbour if not in the first column
        if (columnIndex > 0 && openedSpots[rowIndex][columnIndex - 1]) {
            connectSpots(rowIndex, columnIndex, rowIndex, columnIndex - 1);
        }
        
        //Connect to the right neighbour if not in the last column
        if (columnIndex < n - 1 && openedSpots[rowIndex][columnIndex + 1]) {
            connectSpots(rowIndex, columnIndex, rowIndex, columnIndex + 1);
        }
        
        //Connect top row to top virtual site
        if (rowIndex == 0) {
            topSystem.union(convert2DTo1DArrayIndex(rowIndex, columnIndex), 
                         getTopSiteArrayIndex());
            topBottomSystem.union(convert2DTo1DArrayIndex(rowIndex, columnIndex), 
                         getTopSiteArrayIndex());
        }
        
        //Connect bottom row to bottom virtual site
        if (rowIndex == n - 1) {
            topBottomSystem.union(convert2DTo1DArrayIndex(rowIndex, columnIndex), 
                         getBottomSiteArrayIndex());
        }
    }
    
    private void connectSpots(int spot1Row, int spot1Column, 
                              int spot2Row, int spot2Column) {
        topSystem.union(convert2DTo1DArrayIndex(spot1Row, spot1Column), 
                         convert2DTo1DArrayIndex(spot2Row, spot2Column));
        topBottomSystem.union(convert2DTo1DArrayIndex(spot1Row, spot1Column), 
                         convert2DTo1DArrayIndex(spot2Row, spot2Column));
    }
    
    /**------------------------------------------------------------
     * Returns true if site (row i, column j) is opened
     *------------------------------------------------------------*/
    public boolean isOpen(int i, int j) {
        checkBounds(i, j);
        
        int rowIndex = convertToArrayIndex(i);
        int columnIndex = convertToArrayIndex(j);
        return openedSpots[rowIndex][columnIndex];
    }
     
    /**------------------------------------------------------------
     * Returns true if site (row i, column j) is full
     *------------------------------------------------------------*/
    public boolean isFull(int i, int j) {
        checkBounds(i, j);
        
        int rowIndex = convertToArrayIndex(i);
        int columnIndex = convertToArrayIndex(j);
        return topSystem.connected(convert2DTo1DArrayIndex(rowIndex, columnIndex), 
                                getTopSiteArrayIndex());
    }
    
    /**------------------------------------------------------------
     * Returns true if the system percolates
     *------------------------------------------------------------*/
    public boolean percolates() {    
        return topBottomSystem.connected(getTopSiteArrayIndex(), 
                                getBottomSiteArrayIndex());
    }
    
    /**------------------------------------------------------------
     * Converts assigment (1 based) indexing to 0 based Java array
     * indexing
     *------------------------------------------------------------*/
    private int convertToArrayIndex(int index) {
        return index - 1;
    }
    
    /**------------------------------------------------------------
     * Checks if input parameters are within system defined bounds
     * and throws IndexOutOfBoundsException if they are not
     *------------------------------------------------------------*/
    private void checkBounds(int i, int j) {
        if (i < 1 || i > this.n) {
            throw new IndexOutOfBoundsException("Row index i out of bounds\n");
        }
        
        if (j < 1 || j > this.n) {
            throw new IndexOutOfBoundsException("Column index j out of bounds\n");
        }
    }
    
    /**------------------------------------------------------------
     * Converts percolation model 2D indexing to 1D indexing 
     * used by WeightedQuickUnionUF class
     *------------------------------------------------------------*/
    private int convert2DTo1DArrayIndex(int i, int j) {
        return i * n + j;
    }
    
    /**------------------------------------------------------------
     * Returns index designated for top virtual site
     *------------------------------------------------------------*/
    private int getTopSiteArrayIndex() {
        return n * n + TOP_SITE_OFFSET;
    }
    
    /**------------------------------------------------------------
     * Returns index designated for bottom virtual site
     *------------------------------------------------------------*/
    private int getBottomSiteArrayIndex() {
        return n * n + BOTTOM_SITE_OFFSET;
    }
}