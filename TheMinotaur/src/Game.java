
// acts as Overseer class

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;

//import mod.Direction;
//import mod.Maze;

import java.awt.BorderLayout;

import java.io.IOException;
import java.net.MalformedURLException;

public class Game extends JFrame implements Runnable {
	
	public static Game game;
	
	private static final long serialVersionUID = 1L;
	
	private boolean running;
	private boolean pMovingDebounce; // the debounce for when a player decides to move (so that the minotaurs can move)
	private Thread thread;
	private BufferedImage image;
	private static BufferedImage iconimg;
	public int[] pixels;
	
	private Maze maze;
	private Player plr; // uses Player class (duh)
	private ArrayList<Minotaur> minlist; // ArrayList is more flexible IMO
	
	private Controller cnt; // Controls the player
	
	public ArrayList<Texture> textures;
	public Camera camera;
	public CameraDir camdir;
	public Screen screen;
	
	public String playerName;
	private int mapSelected = 0;
	
	private JLabel MINIMAPTEXT;
	private JLabel PLAYERACTIONTEXT;
	private JLabel GAMEOVERTEXT;
	
//	public int[][] TESTMAP = {
//		{1,1,1,1,1,1,1,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,0,0,0,0,0,0,1},
//		{1,1,1,1,1,1,1,1}
//	};
	
	// 1 - Easy
	// 2 - Normal
	// 3 - Challenger
	
	
	// Game Settings (set-up)
	
	public void setup() {
		//
		JFrame panel = new JFrame();
	    panel.setBounds(0, 0, 640, 480);
	    panel.setBackground(Color.gray);
		panel.setLayout(null);
		panel.setResizable(false);
		panel.setTitle("The Minotaur");
		panel.setIconImage(iconimg);
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(Color.black);
		panel.setLocationRelativeTo(null);
	    //
	    
	    // Text/Buttons
		JLabel title1 = new JLabel("Welcome adventurer!");
		title1.setBounds(15, 15, 640, 32);
		title1.setFont(new Font("Arial", Font.BOLD, 25));
		title1.setVerticalAlignment(SwingConstants.CENTER);
		title1.setVisible(true); panel.add(title1);
		JLabel title2 = new JLabel("Please enter your username to continue as well as your preferred map.");
		title2.setBounds(15, 40, 640, 32);
		title2.setFont(new Font("Arial", Font.PLAIN, 15));
		title2.setVerticalAlignment(SwingConstants.CENTER);
		title2.setVisible(true); panel.add(title2);
		JLabel title3 = new JLabel("Good luck.");
		title3.setBounds(15, 60, 640, 32);
		title3.setFont(new Font("Arial", Font.BOLD, 20));
		title3.setVerticalAlignment(SwingConstants.CENTER);
		title3.setVisible(true); panel.add(title3);
		JLabel title4 = new JLabel("Project made by Bruno Amprimo");
		title4.setBounds(15, 80, 640, 32);
		title4.setFont(new Font("Arial", Font.PLAIN, 12));
		title4.setVerticalAlignment(SwingConstants.CENTER);
		title4.setVisible(true); panel.add(title4);
		JTextField name = new JTextField("");
		name.setBounds(210, 150, 200, 32);
		name.setFont(new Font("Arial", Font.BOLD, 25));
		name.setVisible(true); panel.add(name);
		JButton map1 = new JButton("Map 1");
		map1.setBounds(210, 220, 200, 32);
		map1.setFont(new Font("Arial", Font.PLAIN, 25));
		map1.setToolTipText("Features a simpler map with only 1 minotaur");
		map1.setVisible(true); panel.add(map1);
		JButton map2 = new JButton("Map 2");
		map2.setBounds(210, 260, 200, 32);
		map2.setFont(new Font("Arial", Font.PLAIN, 25));
		map2.setToolTipText("Features a more intermediate dungeon-like map with 3 minotaurs");
		map2.setVisible(true); panel.add(map2);
		JButton map3 = new JButton("Map 3");
		map3.setBounds(210, 300, 200, 32);
		map3.setFont(new Font("Arial", Font.PLAIN, 25));
		map3.setToolTipText("Features a difficult checkers-like map with 5 minotaurs. Good luck on this one");
		map3.setVisible(true); panel.add(map3);
		JButton proceed = new JButton("Deploy");
		proceed.setBounds(400, 380, 200, 32);
		proceed.setFont(new Font("Arial", Font.BOLD, 25));
		proceed.setVisible(true); panel.add(proceed);
		panel.setVisible(true);
	    //

		// ADD EVENTS WHEN BUTTON IS CLICKED:
		// Event when a map button is chosen to change that value
		// When "Deploy" button is clicked, checks to see if there is a username and a map.
		// If there is, then the game starts. The first panel is closed, and a second one is opened.
		
		// 1 - Map1, 2 - Map2, 3 - Map3, 4 - Proceed
		
		final JButton[] buttonList = {map1, map2, map3, proceed};
		for(int i = 0; i < buttonList.length; i++) {
			final int ic = i + 1;
			final JTextField namec = name;
			final JFrame panelc = panel;
			final JLabel title2c = title2;
			buttonList[i].addActionListener(new ActionListener() {
				// When button is pressed
		    	public void actionPerformed(ActionEvent e) {
		    		if(ic != 4) { // Map1/Map2/Map3 (Select map, bold text)
		    			mapSelected = ic;
		    			//
		    			for(int b = 0; b < 3; b++) {
		    				if(!(b == ic - 1)) {
		    					buttonList[b].setFont(new Font("Arial", Font.PLAIN, 25));
		    				}
		    			}
		    			buttonList[ic - 1].setFont(new Font("Arial", Font.BOLD, 25));
		    			//
		    		}else{ // Deploy button
		    			String inputName = namec.getText();
		    			System.out.println(inputName.length());
		    			System.out.println(mapSelected);
		    			if(inputName.length() > 0 && mapSelected != 0) { // At least one character for name is inputted and a map is selected
		    				playerName = inputName;
		    				System.out.println("Game is starting");
		    				panelc.dispose();
		    				start();
		    			}else{
		    				title2c.setFont(new Font("Arial", Font.BOLD, 15));
		    			}
		    		}
		    	}
		    	//
		    });
		}
		
		
	}
	
	// GAME METHODS
	
	public void KR_Method(KeyEvent key) {
		//System.out.println("hi");
		if(running && (key.getKeyCode() == KeyEvent.VK_UP) && !pMovingDebounce && cnt.move(camdir.getDir())) {
			pMovingDebounce = false;
			movePlayer(camdir.getDir());
			if(plr.getLoc().getRow() == maze.getEnd().getRow() && plr.getLoc().getCol() == maze.getEnd().getCol()) {
				gameOver(0);
			}
			for(int i = 0; i < maze.getSwords().length; i++) {
				//
//				System.out.println(plr.getLoc().getRow());
//				System.out.println(plr.getLoc().getCol());
//				System.out.println(maze.getSwords()[i].getRow());
//				System.out.println(maze.getSwords()[i].getCol());
				//
				if(maze.getSwords()[i] != null && plr.getLoc().getRow() == maze.getSwords()[i].getRow() && plr.getLoc().getCol() == maze.getSwords()[i].getCol()) {
					plr.setSword(true);
					PLAYERACTIONTEXT.setText("You have a sword!");
					maze.removeSword(i);
					break;
				}
			}
			if(maze.getTraps() != null) {
				for(int i = 0; i < maze.getTraps().length; i++) {
					if(plr.getLoc().getRow() == maze.getTraps()[i].getRow() && plr.getLoc().getCol() == maze.getTraps()[i].getCol()) {
						gameOver(2);
						break;
					}
				}
			}
			moveMinotaurs();
		}
	}
	
	private void movePlayer(Direction d) {
		//System.out.println("MovePlayer");
		if(d == Direction.UP)
			plr.moveUp();
		else if(d == Direction.DOWN) 
			plr.moveDown();
		else if(d == Direction.LEFT) 
			plr.moveLeft();
		else 
			plr.moveRight();
		camera.moveCam(d);
		MINIMAPTEXT.setText(cnt.drawMap());
	}
	
	private void moveMinotaurs() {
		for(int i = 0; i < minlist.size(); i++) {
			maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 0; // changes Minotaurs
			moveMinotaur(minlist.get(i));
			if(plr.getLoc().getRow() == minlist.get(i).getLoc().getRow() && plr.getLoc().getCol() == minlist.get(i).getLoc().getCol()) {
				if(!plr.hasSword()) {
					gameOver(1);
				}else {
					if(Math.random() > 0.5) { // 50% chance to break
						plr.setSword(false);
						PLAYERACTIONTEXT.setText("You have no sword.");
					}
					minlist.remove(i);
				}
			}else {
				maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 4;
			}
		}
		MINIMAPTEXT.setText(cnt.drawMap());
	}
	
	private void moveMinotaur(Minotaur min) {
		int _minRow = min.getLoc().getRow();
		int _minCol = min.getLoc().getCol();
		int[][] map = maze.getMap().getArr();
		
		int rowDist = min.getLoc().getRow() - plr.getLoc().getRow();
		int colDist = min.getLoc().getCol() - plr.getLoc().getCol();
		
		if(rowDist > 0){ // Min Row > Ply Row
			if(map[_minRow - 1][_minCol] == 0){
				min.moveUp();
			}
			else{
				if(colDist > 0 && map[_minRow][_minCol - 1] == 0){ // Move Left
					min.moveLeft();
				}
				else if(colDist < 0 && map[_minRow][_minCol + 1] == 0){ 
					min.moveRight();
				}
			}
		}
		else if(rowDist < 0){ // Min Row < Ply Row
			if(map[_minRow + 1][_minCol] == 0){
				min.moveDown();
			}
			else{
				if(colDist > 0 && map[_minRow][_minCol - 1] == 0){
					min.moveLeft();
				}
				else if(colDist < 0 && map[_minRow][_minCol + 1] == 0){
					min.moveRight();
				}
			}
		}
		else {
			if(colDist > 0 && map[_minRow][_minCol - 1] == 0){
				min.moveLeft();
			}
			else if(colDist < 0 && map[_minRow][_minCol + 1] == 0){
				min.moveRight();
			}
		}
	}
	
	private void gameOver(int ending) {
		System.out.println("GAME OVER");
		running = false;
		setBackground(Color.gray);
		PLAYERACTIONTEXT.setVisible(false);
		MINIMAPTEXT.setVisible(false);
		if(ending == 0) {
			//playSound("res/Congrats2.wav");
			GAMEOVERTEXT.setText("Congratulations! You have escaped the maze! Try some of the other maps!");
		}else if(ending == 1) {
			GAMEOVERTEXT.setText("You have succumbed to the Minotaurs. Try again!");
		}else if(ending == 2) {
			GAMEOVERTEXT.setText("You have succumbed to the Dungeon's many explosive traps. Try again!");
		}
		GAMEOVERTEXT.setVisible(true);
	}
	
	//
	
	public Game() {
		thread = new Thread(this);
		// GUI data
		image = new BufferedImage(1080, 720, BufferedImage.TYPE_INT_RGB); // Basis for GUI
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		// Setup game GUI
		setup(); // settings GUI before the game is made
		setSize(1080, 720);
		setResizable(false);
		setTitle("The Minotaur");
		setIconImage(iconimg);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		//
		//start(); deprecated
	}
	
	// Boot up game (threads)
	private synchronized void start() {
		//running = true;
		textures = new ArrayList<Texture>();
		textures.add(Texture.lvl1wall);
		textures.add(Texture.lvl2wall);
		textures.add(Texture.lvl3wall);
		textures.add(Texture.MINOTAUR);
		// In-game UI stuff (priority)
		JLabel dtext = new JLabel("Current Direction: ");
		dtext.setBounds(875, 55, 180, 120);
		dtext.setFont(new Font("Arial", Font.PLAIN, 15));
		dtext.setVerticalAlignment(SwingConstants.TOP);
		dtext.setVisible(true); add(dtext);
		// Class Data (super important, also very inefficient)
		if(mapSelected == 1) {
			maze = new Maze(mapSelected); 
			plr = new Player(maze.getSpawn());
			// Minotaurs in levels
			minlist = new ArrayList<Minotaur>();
			minlist.add(new Minotaur(new Location(1, 1)));
			for(int i = 0; i < minlist.size(); i++) {
				maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 4; // Change map tile to a Minotaur
			}
			//
			cnt = new Controller(plr, minlist, maze, playerName, "hi");
			camdir = new CameraDir(dtext);
			camera = new Camera(maze.getSpawn().getRow(), maze.getSpawn().getCol(), 1, 0, 0, -.66, camdir, game);
			screen = new Screen(maze.getMap().getArr(), 8, 8, textures, 1080, 720, mapSelected); 
			addKeyListener(camera);
		}else if(mapSelected == 2) {
			maze = new Maze(mapSelected); 
			plr = new Player(maze.getSpawn());
			// Minotaurs in levels
			minlist = new ArrayList<Minotaur>();
			minlist.add(new Minotaur(new Location(6, 15)));
			minlist.add(new Minotaur(new Location(4, 10)));
			minlist.add(new Minotaur(new Location(1, 17)));
			for(int i = 0; i < minlist.size(); i++) {
				maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 4; // Change map tile to a Minotaur
			}
			//
			cnt = new Controller(plr, minlist, maze, playerName, "hi");
			camdir = new CameraDir(dtext);
			camera = new Camera(maze.getSpawn().getRow(), maze.getSpawn().getCol(), 1, 0, 0, -.66, camdir, game);
			screen = new Screen(maze.getMap().getArr(), 8, 20, textures, 1080, 720, mapSelected); 
			addKeyListener(camera);
		}else if(mapSelected == 3) {
			maze = new Maze(mapSelected); 
			plr = new Player(maze.getSpawn());
			// Minotaurs in levels
			minlist = new ArrayList<Minotaur>();
			minlist.add(new Minotaur(new Location(2, 1)));
			minlist.add(new Minotaur(new Location(6, 1)));
			minlist.add(new Minotaur(new Location(1, 6)));
			minlist.add(new Minotaur(new Location(1, 19)));
			minlist.add(new Minotaur(new Location(6, 20)));
			for(int i = 0; i < minlist.size(); i++) {
				maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 4; // Change map tile to a Minotaur
			}
			//
			cnt = new Controller(plr, minlist, maze, playerName, "hi");
			camdir = new CameraDir(dtext);
			camera = new Camera(maze.getSpawn().getRow(), maze.getSpawn().getCol(), 1, 0, 0, -.66, camdir, game);
			screen = new Screen(maze.getMap().getArr(), 8, 22, textures, 1080, 720, mapSelected); 
			addKeyListener(camera);
		}
		// In-game UI stuff
		JLabel gametitle = new JLabel("The Minotaur - Map #" + mapSelected + " - Avoid the minotaurs and retrieve the sword!");
		gametitle.setBounds(15, 15, 1080, 25);
		gametitle.setFont(new Font("Arial", Font.BOLD, 20));
		gametitle.setVerticalAlignment(SwingConstants.TOP);
		gametitle.setVisible(true); add(gametitle);
		JLabel gamed1 = new JLabel("<- and -> arrow keys to change directions");
		gamed1.setBounds(15, 45, 1080, 30);
		gamed1.setFont(new Font("Arial", Font.PLAIN, 25));
		gamed1.setVerticalAlignment(SwingConstants.TOP);
		gamed1.setVisible(true); add(gamed1);
		JLabel gamed2 = new JLabel("Up arrow key to move in your direction"); // "W (and respective arrow key) to move in your direction"
		gamed2.setBounds(15, 80, 1080, 30);
		gamed2.setFont(new Font("Arial", Font.PLAIN, 25));
		gamed2.setVerticalAlignment(SwingConstants.TOP);
		gamed2.setVisible(true); add(gamed2);
		PLAYERACTIONTEXT = new JLabel("You have no sword.");
		PLAYERACTIONTEXT.setBounds(875, 80, 1080, 30);
		PLAYERACTIONTEXT.setFont(new Font("Arial", Font.ITALIC, 20));
		PLAYERACTIONTEXT.setVerticalAlignment(SwingConstants.TOP);
		PLAYERACTIONTEXT.setVisible(true); add(PLAYERACTIONTEXT);
		MINIMAPTEXT = new JLabel();
		MINIMAPTEXT.setBounds(700, 0, 380, 120);
		MINIMAPTEXT.setFont(new Font("Consolas", Font.PLAIN, 12));
		MINIMAPTEXT.setVerticalAlignment(SwingConstants.TOP);
		MINIMAPTEXT.setText(cnt.drawMap());
		MINIMAPTEXT.setVisible(true); add(MINIMAPTEXT);
		GAMEOVERTEXT = new JLabel();
		GAMEOVERTEXT.setBounds(0, 0, 1080, 720);
		GAMEOVERTEXT.setFont(new Font("Arial", Font.BOLD, 25));
		GAMEOVERTEXT.setVerticalAlignment(SwingConstants.CENTER);
		GAMEOVERTEXT.setHorizontalAlignment(SwingConstants.CENTER);
		add(GAMEOVERTEXT);
		//
		JLabel FILLER = new JLabel(""); // for some reason the UI lining breaks, but it fixes itself with a filler text?
		FILLER.setBounds(15, 120, 1080, 30);
		FILLER.setFont(new Font("Arial", Font.PLAIN, 25));
		FILLER.setVerticalAlignment(SwingConstants.TOP);
		FILLER.setVisible(true); add(FILLER);
		//
		setVisible(true);
		pMovingDebounce = false;
		running = true;
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	//
	
	// Rendering
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(1);
			return;
		}
		//
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 150, image.getWidth(), image.getHeight() - 150, null);
		bs.show();
	}
	
	public void run() { // basically renders the 3D objects as well as the GUI
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;// 60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)// Make sure update is only happening 60 times a second
			{
				// handles all of the logic restricted time
				screen.update(camera, pixels);
				camera.update(maze.getMap().getArr());
				delta--;
			}
			render();// displays to the screen unrestricted time
		}
	}
	//
	
	public static void main(String [] args) {
		try {
			iconimg = ImageIO.read(Game.class.getResourceAsStream("/tml.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		game = new Game();
	}

}


