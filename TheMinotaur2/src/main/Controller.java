package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Controller {
	
	private Player player;
	private ArrayList<Minotaur> minlist;
	private Maze map;
	private String playerName;
	private String messageSend;
	
	public void setup(Player player1, ArrayList<Minotaur> minlist1, Maze map1, String playerName1, String messageSend1) {
		player = player1;
		minlist = minlist1;
		map = map1;
		playerName = playerName1;
		messageSend = messageSend1;
	}
	
	public Controller() {}
	
	public boolean move(Direction d) {
		if(!isValid(d)) {
			return false;
		}
		return true;
	}
	
	private boolean isValid(Direction d) {
		int[][] mapUse = map.getMap().getArr();
		int row = player.getLoc().getRow();
		int col = player.getLoc().getCol();
		
		if(d == Direction.UP) {
			if(row == 0)
				return false;
			row--;
		}
		else if(d == Direction.DOWN) {
			if(row == mapUse.length - 1)
				return false;
			row++;
		}
		else if(d == Direction.LEFT) {
			if(col == 0)
				return false;
			col--;
		}
		else {
			if(col == mapUse[row].length - 1)
				return false;
			col++;
		}
		System.out.println(mapUse[row][col]);
		if(mapUse[row][col] == 0 || mapUse[row][col] == 4) {
			return true; // good job
		}else {
			return false;
		}
	}
	
//	private Direction convertInput(String s) {
//		if(s.equals("w")) {
//			return Direction.UP;			
//		}
//		else if(s.equals("a")) {
//			return Direction.LEFT;
//		}
//		else if(s.equals("s")) {
//			return Direction.DOWN;
//		}
//		else if(s.equals("d")) {
//			return Direction.RIGHT;
//		}
//		else {
//			return Direction.UP;
//		}
//	}
	
	public void victory() {
		JOptionPane.showMessageDialog(null, drawMap() + "\n" + "Congratulations, you won the game!" + "\n");
	}
	
	public void defeat() {
		JOptionPane.showMessageDialog(null, drawMap() + "\n" + "WASTED!" + "\n");
	}

	
	public String drawMap() {
		String MAP = "<html>";
		String ply = "P";
		String wall = "□";
		String path = "·";
		String space = "";
		String min = "M";
		String end = "E";
		String sword = "S";
		String trap = "T";
		String key = "K";
		
		int pRow = player.getLoc().getRow();
		int pCol = player.getLoc().getCol();
		int eRow = map.getEnd().getRow();
		int eCol = map.getEnd().getCol();
		
		int[][] mapUse = map.getMap().getArr();
		Location[] swordsL = map.getSwords();
		Location[] trapsL = map.getTraps();
		Location keyL = map.getKey();
		
		System.out.println(playerName);
		
		for(int r = 0; r < mapUse.length; r++) {
			for(int c = 0; c < mapUse[r].length; c++) {
				// Minotaur Area
				boolean minspacefound = false;
				String adding = "";
				
				for(int mc = 0; mc < minlist.size(); mc++) {
					if(r == minlist.get(mc).getLoc().getRow() && c == minlist.get(mc).getLoc().getCol()) {
						adding = min + space;
						minspacefound = true;
						break;
					}
				}
				for(int sc = 0; sc < swordsL.length; sc++) {
					if(swordsL[sc] != null && r == swordsL[sc].getRow() && c == swordsL[sc].getCol()) {
						adding = sword + space;
						minspacefound = true;
						break;
					}
				}
				if(keyL != null && r == keyL.getRow() && c == keyL.getCol()) {
					adding = key + space;
					minspacefound = true;
				}
				if(playerName.toLowerCase().equals("rico")) {
					System.out.println("HI HELLO");
					for(int tc = 0; tc < trapsL.length; tc++) {
						if(trapsL[tc] != null && r == trapsL[tc].getRow() && c == trapsL[tc].getCol()) {
							adding = trap + space;
							minspacefound = true;
							break;
						}
					}
				}
				MAP += adding;
				//
				if(!minspacefound) {
					if(r == pRow && c == pCol) {
						MAP += ply + space;
					}
					else if(r == eRow && c == eCol) {
						MAP += end + space;
					}
					else if(mapUse[r][c] == 0) {
						MAP += path + space;
					}
					else {
						MAP += wall + space;
					}
				}
			}
			MAP += "<br/>";
		}
		
		MAP += "<html>";
		return MAP;
	}

	///
	
	
}