// Location

public class Location {

	private int _row, _col;
	
	public int getRow() { return _row; }
	public int getCol() { return _col; }
	
	public void moveDown() { _row++; }
	public void moveUp() { _row--; }
	public void moveLeft() { _col--; }
	public void moveRight() { _col++; }
	
	public Location(int row, int col) {
		_row = row;
		_col = col;
	}
}