package main;

// Stores player data

public class Player {

	private Location _position;
	private boolean _isDead;
	private boolean _hasSword;
	private boolean _hasKey;
	
	public Location getLoc() { return _position; }
	public boolean isDead() { return _isDead; }
	public boolean hasSword() { return _hasSword; }
	public boolean hasKey() { return _hasKey; }
	
	public void setSword(boolean t) {
		_hasSword = t;
	}
	
	public void setKey(boolean t) {
		_hasKey = t;
	}
	
	public void moveUp() { _position.moveUp(); }
	public void moveDown() { _position.moveDown(); }
	public void moveLeft() { _position.moveLeft(); }
	public void moveRight() { _position.moveRight(); }
	
	public void kill() { _isDead = true; }
	public void revive() { _isDead = false; }
	
	public void setup(Location pos) {
		_position = pos;
		_isDead = false;
		_hasSword = false;
		_hasKey = false;
	}
	
	public Player() {}
}
