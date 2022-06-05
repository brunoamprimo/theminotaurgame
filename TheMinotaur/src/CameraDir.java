
// Used to calculate direction with camera using x and y planes

import javax.swing.JLabel;

public class CameraDir {
	
	public Direction d;
	public JLabel st;
	
	public CameraDir(JLabel setText) {
		d = Direction.DOWN;
		st = setText;
	}
	
	public Direction getDir() {
		return d;
	}
	
	public String getDirByString() {
		if(d == Direction.UP) {
			return "w";
		}else if(d == Direction.LEFT) {
			return "a";
		}else if(d == Direction.DOWN) {
			return "s";
		}else if (d == Direction.RIGHT) {
			return "d";
		}else {
			return "w";
		}
	}
	
	public void setDir(Direction dn) {
		d = dn;
	}
	
	public void changeText() {
		st.setText("Current Direction: " + d);
	}
	
}
