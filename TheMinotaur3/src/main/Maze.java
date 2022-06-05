package main;
import java.util.Arrays;

public class Maze {

	private Maps map;
	private Location spawn;
	private Location end;
	private Location key;
	private Location[] swords;
	private Location[] explosivetrap; 

	public Maps getMap() {
		return map;
	}

	public Location getSpawn() {
		return spawn;
	}
	
	public Location getEnd() {
		return end;
	}
	
	public Location getKey() {
		return key;
	}
	
	public Location[] getSwords() {
		return swords;
	}
	
	public void removeSword(int i) {
		Location[] swordsC = new Location[swords.length - 1];
		for(int b = 0; b < swordsC.length; b++) {
			if(b != i) {
				swordsC[b] = swords[b];
			}
		}
		swords = swordsC;
	}
	
	public void removeKey() {
		key = null;
	}
	
	public Location[] getTraps() {
		return explosivetrap;
	}
	
	public void setup(int x) {
		if(x == 1){
			map = Maps.Map1;
			spawn = new Location(4, 9);
			end = new Location(2, 10);
			key = new Location(6, 7);
			Location[] swordsC = {new Location(1, 1), new Location(2, 4), new Location(6, 4), new Location(4, 7)};
			swords = swordsC;
			Location[] explosivetrapC = {new Location(4, 1)};
			explosivetrap = explosivetrapC;
		}else if(x == 2) {
			map = Maps.Map2;
			spawn = new Location(6, 1); // 6, 4
			end = new Location(1, 18); // 3, 1
			key = new Location(1, 6);
			Location[] swordsC = {new Location(3, 6), new Location(3, 12)};
			swords = swordsC;
			Location[] explosivetrapC = {new Location (1, 1), new Location(1, 14)};
			explosivetrap = explosivetrapC;
		}else if(x == 3) {
			map = Maps.Map3;
			spawn = new Location(6, 10); // 6, 4
			end = new Location(1, 20); // 3, 1
			key = new Location(1, 8);
			Location[] swordsC = {new Location(4, 5), new Location(2, 10), new Location(2, 15), new Location(5, 10)};
			swords = swordsC;
			Location[] explosivetrapC = {new Location (4, 17), new Location(4, 18), new Location (5, 18)};
			explosivetrap = explosivetrapC;
		}
		// Reset Minotaur movement
		for(int r = 0; r < getMap().getArr().length; r++){
			for(int c = 0; c < getMap().getArr()[0].length; c++){
				if(getMap().getArr()[r][c] == 4){
					getMap().getArr()[r][c] = 0;
				}
			}
		}
	}

	public Maze() {}

}
