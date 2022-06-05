import java.util.Arrays;

public class Maze {

	// declarations and getter methods
	
	private Maps map;
	private Location spawn;
	private Location end;
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
	
	public Location[] getSwords() {
		return swords;
	}
	
	public void removeSword(int i) { // removes sword option by determining if there is an enemy in the player's position
		Location[] swordsC = new Location[swords.length - 1];
		for(int b = 0; b < swordsC.length; b++) {
			if(b != i) {
				swordsC[b] = swords[b];
			}
		}
		swords = swordsC;
	}
	
	public Location[] getTraps() {
		return explosivetrap;
	}

	public Maze(int x) { // crappily made but works. it initializes the locations of the spawn, goal, and swords based on the map selected from the x int
		if(x == 1){
			map = Maps.Map1;
			spawn = new Location(6, 4); // 6, 4
			end = new Location(1, 6); // 3, 1
			Location[] swordsC = {new Location(3, 4)};
			swords = swordsC;
//			Location[] explosivetrapC = {new Location (1, 6)};
//			explosivetrap = explosivetrapC;
		}else if(x == 2) {
			map = Maps.Map2;
			spawn = new Location(6, 1); // 6, 4
			end = new Location(1, 18); // 3, 1
			Location[] swordsC = {new Location(3, 6), new Location(3, 12)};
			swords = swordsC;
			Location[] explosivetrapC = {new Location (1, 1), new Location(1, 14)};
			explosivetrap = explosivetrapC;
		}else if(x == 3) {
			map = Maps.Map3;
			spawn = new Location(6, 10); // 6, 4
			end = new Location(1, 20); // 3, 1
			Location[] swordsC = {new Location(4, 5), new Location(2, 10), new Location(2, 15), new Location(5, 10)};
			swords = swordsC;
			Location[] explosivetrapC = {new Location (4, 17), new Location(4, 18), new Location (5, 18)};
			explosivetrap = explosivetrapC;
		}
	}

}
