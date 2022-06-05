
// Stores minotaur data

public class Minotaur {
	
	private Location _position;
	private boolean _isDead;
	
	public Location getLoc() { return _position; }
	public boolean isDead() { return _isDead; }
	
	public void moveUp() { _position.moveUp(); }
	public void moveDown() { _position.moveDown(); }
	public void moveLeft() { _position.moveLeft(); }
	public void moveRight() { _position.moveRight(); }
	
	public void kill() { _isDead = true; }
	public void revive() { _isDead = false; }
	
	public Minotaur(Location pos) {
		_position = pos;
		_isDead = false;
	}
	
}
