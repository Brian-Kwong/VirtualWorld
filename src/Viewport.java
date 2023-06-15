public final class Viewport {
  private int row;
  private int col;
  private int numRows;
  private int numCols;

  public Viewport(int numRows, int numCols) {
    this.numRows = numRows;
    this.numCols = numCols;
  }

  /* Getters and Setters */
  public double getNumRows() { return numRows; }

  public double getNumCols() { return numCols; }

  private void shift(int col, int row) {
    this.col = col;
    this.row = row;
  }

  public boolean contains(Point p) {
    return p.y >= row && p.y < row + numRows && p.x >= col &&
        p.x < col + numCols;
  }

  public void shiftView(int worldNumRoms, int worldNumCols, int colDelta,
                        int rowDelta) {
    int newCol = clamp(col + colDelta, 0, worldNumCols - numCols);
    int newRow = clamp(row + rowDelta, 0, worldNumRoms - numRows);
    shift(newCol, newRow);
  }

  public  Point worldToViewport( int col, int row) {
    return new Point(col - this.col, row - this.row);
  }

  public  Point viewportToWorld( int col, int row) {
    return new Point(col + this.col, row + this.row);
  }

  private int clamp(int value, int low, int high) {
    return Math.min(high, Math.max(value, low));
  }
}
