
// main graphics controller. Controls player "movement", but not technically.

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {	
	
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean left, right, forward, back;
	public final double MOVE_SPEED = .08; // Default move speed
	public final double ROTATION_SPEED = .045; // Default rotation speed
	
	public CameraDir camdir;
	public Game gameinst;
	
	public Camera(double x, double y, double xd, double yd, double xp, double yp, CameraDir camdirn, Game gamec) { // initialize the camera
		xPos = x + 0.5;
		yPos = y + 0.5;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
		camdir = camdirn;
		gameinst = gamec;
	}
	
	public void keyPressed(KeyEvent key) { // event where it tracks a key press
		if((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = true;
		if((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = true;
		/////////////////////////
//		if((key.getKeyCode() == KeyEvent.VK_UP))
//			forward = true;
//		if((key.getKeyCode() == KeyEvent.VK_DOWN))
//			back = true;
	}
	
	public void keyReleased(KeyEvent key) { // event where it tracks a key release
		//////////////
		gameinst.KR_Method(key);
		//////////////
		
		if((key.getKeyCode() == KeyEvent.VK_LEFT))
			left = false;
		if((key.getKeyCode() == KeyEvent.VK_RIGHT))
			right = false;
		/////////////////////////
//		if((key.getKeyCode() == KeyEvent.VK_UP))
//			forward = false;
//		if((key.getKeyCode() == KeyEvent.VK_DOWN))
//			back = false;
	}
	
	public void moveCam(Direction d) { // Moves camera whenever player moves
		if(d == Direction.UP) {
			xPos -= 1;
		}else if(d == Direction.DOWN) {
			xPos += 1;
		}else if(d == Direction.RIGHT) {
			yPos += 1;
		}else if(d == Direction.LEFT) {
			yPos -= 1;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent key) { // default method
		
	}
	
	public void update(int[][] map) {
		// basically gets the x and y positions and determines if the space in the 2D array is 0 (which means you can pass through it)
		// forward/back adds and removes x/y position respectively
		if(forward) {
			if(map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) {
				xPos+=xDir*MOVE_SPEED;
			}
			if(map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] ==0)
				yPos+=yDir*MOVE_SPEED;
		}
		if(back) {
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
				xPos-=xDir*MOVE_SPEED;
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)]==0)
				yPos-=yDir*MOVE_SPEED;
		}
		// determines camera rotation. the placeholder values are for so that yDir and yPlane are calculated accordingly
		if(right) {
			double oldxDir=xDir;
			xDir=xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED);
			yDir=oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
		}
		if(left) {
			double oldxDir=xDir;
			xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
			yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
		}
		// this basically gets the angle and if it's past a certain point, then it's considered as the player wanting to move down/left/up/right.
		Direction newdir = camdir.getDir();
		if(yPlane <= -0.34) { // Down
			camdir.setDir(Direction.DOWN);
		}else if(xPlane <= -0.34) { // Left
			camdir.setDir(Direction.LEFT);
		}else if(yPlane >= 0.34) { // Up
			camdir.setDir(Direction.UP);
		}else if(xPlane >= 0.34) { // Right
			camdir.setDir(Direction.RIGHT);
		}
		if(camdir.getDir() != newdir) {
			camdir.changeText();
		}
		//System.out.println(xPos + ", " + yPos); debug
	}
	
}
