
public enum Maps {
	
	// Maps
	// 0 - No wall
	// 1 - Wall (lvl1)
	// 2 - Wall (lvl2)
	// 3 - Wall (lvl3)
	
	// 4 - Minotaur
	
	// 5 - Wall (lvl1, additive)
	
//	Map1(new int[][] { (TESTMAP)
//		{1,1,1,1,1,1,1,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,1,1,1,1,1,1,1}
//		}); // semi-colon ends the number of variables!!!

	Map1(new int[][] { 
		{1,1,1,1,1,1,1,1},
		{1,0,0,0,0,0,0,1},
		{1,0,1,1,0,1,0,1},
		{1,0,1,0,0,1,0,1},
		{1,0,1,0,1,1,0,1},
		{1,0,1,0,0,0,0,1},
		{1,0,0,0,0,0,1,1},
		{1,1,1,1,1,1,1,1}
		}), // semi-colon ends the number of variables!!!
	Map2(new int[][] { 
		{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
		{2,0,0,2,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,2},
		{2,0,0,2,0,2,2,2,2,2,0,2,0,2,2,2,2,0,0,2},
		{2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
		{2,0,0,2,0,2,0,0,0,2,0,2,0,0,0,0,0,2,0,2},
		{2,0,0,2,0,2,2,2,2,2,0,2,2,2,2,2,2,2,0,2},
		{2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
		{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2},
		}), // semi-colon ends the number of variables!!!
	
	Map3(new int[][] { 
		{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3},
		{3,3,0,0,3,0,0,3,0,3,0,0,0,0,0,0,0,3,0,0,0,3},
		{3,0,0,0,0,0,0,0,0,0,0,3,0,0,3,0,0,0,0,0,3,3},
		{3,0,0,3,0,0,0,3,0,0,3,0,0,0,0,0,0,3,0,0,0,3},
		{3,0,0,0,3,0,0,3,0,0,0,3,0,0,0,0,0,0,0,3,0,3},
		{3,0,0,0,0,3,0,0,3,0,0,0,3,0,0,0,0,3,0,3,0,3},
		{3,0,3,0,0,0,0,3,0,0,0,0,0,0,0,0,0,3,0,3,0,3},
		{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0,3,3,3},
		}); // semi-colon ends the number of variables!!!

//	Map2(new int[][] { 
//		{ false, false, false, false, false, false, false, false, false, true },
//		{ false, false, false, false, false, false, false, false, false, true },
//		{ false,  true,  true,  true, false, false, false, false, false, true },
//		{ false, false, false, true, false, false, false, false, false, true },
//		{ false, false, false, true, false, false, false, false, false, true },
//		{ false, false, false, true, false, false, false, false, false, true },
//		{ false, true, 	true,  true,  true,  true,  true,  true,  true,  true },
//		{ false, true, false, false, false, false, false, false, false, false },
//		{ false, true, false, false, false, false, false, false, false, false },
//		{ false, true, false, false, false, false, false, false, false, false } 
//		});
	
	private int[][] _map;

	public int[][] getArr() {
		return _map;
	}

	private Maps(int[][] map) {
		_map = map;
	}
}
