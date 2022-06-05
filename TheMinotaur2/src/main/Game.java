package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

// implements GUI, main game mechanics

public class Game extends JFrame implements Runnable {
	
	public static Game game;
	//public static Audio audio;
	private static BufferedImage iconimg;
	private static final long serialVersionUID = 1L;
	
	private static String Version = "Version 2.1 (16/DEC/21)";
	
	//private static Thread tweenClassThread;
	
	private JPanel gamePanel;
	private JLabel gameTitle;
	private JButton playButton;
	private JButton helpButton;
		private JLabel helpButtonTitle;
		private JLabel helpButtonTitle2;
		private JButton helpButtonBack;
	private JButton cSkipButton;
		
	private JTextField nameField;
		private JLabel nameFieldText;
		
	// GAME STATEMENTS
		
	private boolean running;
	private boolean pMovingDebounce; // the debounce for when a player decides to move (so that the minotaurs can move)
	private Thread thread;
	private BufferedImage image;
	public int[] pixels;
	
	private int gameTick = 0;
	
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
	
	private JLabel levelText;
	
	private JLabel MINIMAPTEXT;
	private JLabel PLAYERACTIONTEXT1; // sword
	private JLabel PLAYERACTIONTEXT2; // key
	private JLabel GAMEOVERTEXT;
	
	// INGAME UI
	
	private JLabel dtext;
	private JLabel gametitle;
	private JLabel gamed1;
	private JLabel gamed2;
	
	// ESSENTIAL METHODS
	
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int lerp(double a, double b, double f) {
		double ret = a + f * (b - a);
        return (int)ret;
    }
	
	// i could have maybe made a class to make this simpler but idc
	
	public void labelTweenColor(final JLabel text, final Color first, final Color last, final int type, final int delayms) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JLabel textUse = text;
				Color set = first;
				int R = set.getRed();
				int G = set.getGreen();
				int B = set.getBlue();
				while(!set.equals(last)) {
					set = new Color(lerp(R, last.getRed(), 0.2), lerp(G, last.getGreen(), 0.2), lerp(B, last.getBlue(), 0.2));
					R = set.getRed();
					G = set.getGreen();
					B = set.getBlue();
					if(type == 1)
						textUse.setForeground(set);
					else
						textUse.setBackground(set);
					sleep(delayms);
				}
			}
		}).start();
	}
	
	public void labelTweenColorButton(final JButton text, final Color first, final Color last, final int type, final int delayms) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JButton textUse = text;
				Color set = first;
				int R = set.getRed();
				int G = set.getGreen();
				int B = set.getBlue();
				while(!set.equals(last)) {
					set = new Color(lerp(R, last.getRed(), 0.2), lerp(G, last.getGreen(), 0.2), lerp(B, last.getBlue(), 0.2));
					R = set.getRed();
					G = set.getGreen();
					B = set.getBlue();
					if(type == 1)
						textUse.setForeground(set);
					else
						textUse.setBackground(set);
					sleep(delayms);
				}
			}
		}).start();
	}
	
	public void labelTweenPosition(final JLabel text, final int x, final int y, final int delayms) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JLabel textUse = text;
				int x1F = textUse.getLocation().x;
				int y1F = textUse.getLocation().y;
				while(!(x1F == x && y1F == y)) {
					x1F = lerp(x1F, x, 0.2);
					y1F = lerp(y1F, y, 0.2);
					text.setBounds(x1F, y1F, text.getSize().width, text.getSize().height);
					sleep(delayms);
				}
			}
		}).start();
	}
	
	public void labelTweenPositionButton(final JButton text, final int x, final int y, final int delayms) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JButton textUse = text;
				int x1F = textUse.getLocation().x;
				int y1F = textUse.getLocation().y;
				while(!(x1F == x && y1F == y)) {
					x1F = lerp(x1F, x, 0.2);
					y1F = lerp(y1F, y, 0.2);
					text.setBounds(x1F, y1F, text.getSize().width, text.getSize().height);
					sleep(delayms);
				}
			}
		}).start();
	}
	
	//
	
	public void selectMap(int mapTag) {
		mapSelected = mapTag;
		System.out.println("start");
		new Thread(new Runnable() {
			@Override
			public void run() {
				start();
			}
		}).start();
	}
	
	public void adjustMenu(int menuTag) {
		if(menuTag == 1) { // playButton
			////////////////
			// START GAME //
			String inputName = nameField.getText();
			playerName = inputName.toLowerCase();
			if(playerName.equals("rico")) {
				mapSelected = 3;
			}else {
				mapSelected = 1;
			}
			selectMap(mapSelected);
			////////////////
		}else if(menuTag == 2) { // helpButton
			//
			helpButtonTitle.setBounds(275, (720/2)-60, 1080, 60);
			helpButtonTitle.setFont(new Font("Arial", Font.BOLD, 20));
			helpButtonTitle.setVerticalAlignment(SwingConstants.TOP);
			helpButtonTitle.setHorizontalAlignment(SwingConstants.LEFT);
			helpButtonTitle.setVisible(true);
			//
			helpButtonTitle2.setBounds(250, (720/2), 1080, 60);
			helpButtonTitle2.setFont(new Font("Arial", Font.BOLD, 20));
			helpButtonTitle2.setVerticalAlignment(SwingConstants.TOP);
			helpButtonTitle2.setHorizontalAlignment(SwingConstants.LEFT);
			helpButtonTitle2.setVisible(true);
			//
			//
			helpButtonBack.setText("Back");
			helpButtonBack.setBounds(450, 700, 150, 30);
			helpButtonBack.setFont(new Font("Arial", Font.BOLD, 30));
			helpButtonBack.setForeground(new Color(0, 0, 0));
			helpButtonBack.setBackground(new Color(0, 0, 0));
			helpButtonBack.setVisible(true);
			//
			labelTweenColor(helpButtonTitle, new Color(255, 255, 255), new Color(150, 150, 150), 1, 25);
			labelTweenPosition(helpButtonTitle, 275, 100, 25);
			labelTweenColor(helpButtonTitle2, new Color(255, 255, 255), new Color(150, 150, 150), 1, 25);
			labelTweenPosition(helpButtonTitle2, 250, 160, 25);
			labelTweenPositionButton(helpButtonBack, 450, 600, 25);
			labelTweenColorButton(helpButtonBack, new Color(0, 0, 0), new Color(255, 255, 255), 1, 25);
		}
	}
	
	public void openMainMenu() {
		cSkipButton.setVisible(false);
		gameTitle.setBounds(325, (720/2)-60, 1080, 60);
		gameTitle.setVisible(true);
		labelTweenColor(gameTitle, new Color(255, 255, 255), new Color(100, 100, 100), 1, 25);
		labelTweenPosition(gameTitle, 325, 100, 25);
		sleep(500);
		playButton.setBounds(375, 400, 250, 32);
		playButton.setVisible(true);
		labelTweenPositionButton(playButton, 375, 300, 25);
		labelTweenColorButton(playButton, new Color(0, 0, 0), new Color(255, 255, 255), 1, 25);
		sleep(300);
		helpButton.setBounds(375, 475, 250, 32);
		helpButton.setVisible(true);
		labelTweenPositionButton(helpButton, 375, 375, 25);
		labelTweenColorButton(helpButton, new Color(0, 0, 0), new Color(255, 255, 255), 1, 25);
		sleep(300);
		nameField.setVisible(true);
		nameFieldText.setVisible(true);
	}
	
	//
	
	public void closeMainMenu() {
		gameTitle.setVisible(false);
		playButton.setVisible(false);
		helpButton.setVisible(false);
		nameField.setVisible(false);
		nameFieldText.setVisible(false);
	}
	
	public void closeHelpMenu() {
		helpButtonBack.setVisible(false);
		helpButtonTitle.setVisible(false);
		helpButtonTitle2.setVisible(false);
	}
	
	//
	
	private boolean skippedIntro = false;
	
	public void addButtonFunctionality() {
		//////////////////////////////
		// ADD BUTTON FUNCTIONALITY //
		//////////////////////////////
		
		final JButton[] buttonList = {playButton, helpButton, helpButtonBack, cSkipButton};
		for(int i = 0; i < buttonList.length; i++) {
			final int ic = i + 1;
			final JPanel panelc = gamePanel;
			buttonList[i].addActionListener(new ActionListener() {
				// When button is pressed
		    	public void actionPerformed(ActionEvent e) {
		    		//audio.playAudio("buttonSelect.wav");
		    		if(ic == 1 && nameField.getText().length() > 0) { // playButton
		    			closeMainMenu();
		    			adjustMenu(1);
		    		}else if(ic == 2) { // helpButton
		    			closeMainMenu();
		    			adjustMenu(2);
		    		}else if(ic == 3) { // helpButtonBack
		    			closeHelpMenu();
		    			openMainMenu();
		    		}else if(ic == 4) { // skipButton
		    			skippedIntro = true;
		    			openMainMenu();
		    		}
		    	}
		    	//
		    });
		}
	}
	
	public void initgame() {
		if(skippedIntro == false) {
			// STARTUP TEXT //
			JLabel credit1 = new JLabel("A long awaited sequel to The Minotaur...");
			credit1.setBounds(0, (720/2)-50, 1024, 100);
			credit1.setFont(new Font("Arial", Font.BOLD, 35));
			credit1.setForeground(new Color(255, 255, 255));
			credit1.setVerticalAlignment(SwingConstants.TOP);
			credit1.setHorizontalAlignment(SwingConstants.CENTER);
			credit1.setVisible(false); gamePanel.add(credit1);
			//
			JLabel credit2 = new JLabel("The Hog Ridaaas have since recaptured you..");
			credit2.setBounds(0, (720/2)+200, 1024, 100);
			credit2.setFont(new Font("Arial", Font.BOLD, 35));
			credit2.setForeground(new Color(255, 255, 255));
			credit2.setVerticalAlignment(SwingConstants.TOP);
			credit2.setHorizontalAlignment(SwingConstants.CENTER);
			credit2.setVisible(false); gamePanel.add(credit2);
			//
			JLabel credit3 = new JLabel("You are now trapped in the maximum security incarceration of the mighty Level 7 Clan Castle.");
			credit3.setBounds(0, (720/2)+200, 1024, 100);
			credit3.setFont(new Font("Arial", Font.BOLD, 18));
			credit3.setForeground(new Color(255, 255, 255));
			credit3.setVerticalAlignment(SwingConstants.TOP);
			credit3.setHorizontalAlignment(SwingConstants.CENTER);
			credit3.setVisible(false); gamePanel.add(credit3);
			//
			JLabel credit4 = new JLabel("There is only a Hard difficulty. Will you escape and slay the Hog Ridaaa Coalition?");
			credit4.setBounds(0, (720/2)+200, 1024, 100);
			credit4.setFont(new Font("Arial", Font.BOLD, 18));
			credit4.setForeground(new Color(255, 255, 255));
			credit4.setVerticalAlignment(SwingConstants.TOP);
			credit4.setHorizontalAlignment(SwingConstants.CENTER);
			credit4.setVisible(false); gamePanel.add(credit4);
			//
			BufferedImage hogriderimage1 = null;
			BufferedImage hogriderimage2 = null;
			try {
				hogriderimage1 = ImageIO.read(Game.class.getResourceAsStream("/hogriderclash.png"));
				hogriderimage2 = ImageIO.read(Game.class.getResourceAsStream("/hogriderclash2.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			ImageIcon icon = new ImageIcon(hogriderimage1);
			JLabel hogriderstory = new JLabel();
			hogriderstory.setBounds(780/2-250, 75, 1080-300, 720-300);
			hogriderstory.setIcon(icon);
			hogriderstory.setVisible(false); gamePanel.add(hogriderstory);
			
			//////////////////////
			System.out.println(hogriderstory.getBounds());
			sleep(500);
			credit1.setVisible(true);
			sleep(75);
			labelTweenColor(credit1, new Color(255, 255, 255), new Color(150, 150, 150), 1, 75);
			sleep(2500);
			labelTweenColor(credit1, new Color(150, 150, 150), new Color(0, 0, 0), 1, 50);
			sleep(3000);
			credit1.setVisible(false);
			hogriderstory.setVisible(true);
			credit2.setVisible(true);
			labelTweenColor(credit2, new Color(0, 0, 0), new Color(255, 255, 255), 1, 100);
			sleep(6000);
			credit2.setVisible(false);
			hogriderstory.setVisible(false);
			sleep(250);
			hogriderstory.setIcon(new ImageIcon(hogriderimage2));
			hogriderstory.setVisible(true);
			credit3.setVisible(true);
			labelTweenColor(credit3, new Color(0, 0, 0), new Color(255, 255, 255), 1, 80);
			sleep(7000);
			credit3.setVisible(false);
			hogriderstory.setVisible(false);
			sleep(250);
			credit4.setVisible(true);
			labelTweenColor(credit4, new Color(0, 0, 0), new Color(255, 255, 255), 1, 25);
			sleep(4000);
			hogriderstory.setVisible(false);
			credit4.setVisible(false);
			sleep(500);
			openMainMenu();
			//////////////////////
		}
	}
	
	public void setup() {
		// Initalize Audio
		//audio = new Audio();
		// Initalize window
		setTitle("The Minotaur 2");
		setResizable(false);
		setIconImage(iconimg);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		// Initalize panel
		gamePanel = new JPanel();
		gamePanel.setSize(1080, 720);
		gamePanel.setBounds(0, 0, 1080, 720);
	    gamePanel.setLayout(null);
	    gamePanel.setBackground(Color.black);
	    // Initalize Text/Buttons
	    //
		gameTitle = new JLabel("The Minotaur 2");
		gameTitle.setBounds(325, (720/2)-60, 1080, 60);
		gameTitle.setFont(new Font("Arial", Font.BOLD, 50));
		gameTitle.setVerticalAlignment(SwingConstants.TOP);
		gameTitle.setHorizontalAlignment(SwingConstants.LEFT);
		gameTitle.setVisible(false); gamePanel.add(gameTitle);
		//
		JLabel gameVersion = new JLabel(Version);
		gameVersion.setBounds(750, 660, 300, 20);
		gameVersion.setFont(new Font("Arial", Font.BOLD, 10));
		gameVersion.setForeground(new Color(255, 255, 255));
		gameVersion.setVerticalAlignment(SwingConstants.TOP);
		gameVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		gameVersion.setVisible(true); gamePanel.add(gameVersion);
		//
		JLabel gameCredit = new JLabel("Made by Bruno Amprimo");
		gameCredit.setBounds(750, 640, 300, 20);
		gameCredit.setFont(new Font("Arial", Font.BOLD, 15));
		gameCredit.setForeground(new Color(255, 255, 255));
		gameCredit.setVerticalAlignment(SwingConstants.TOP);
		gameCredit.setHorizontalAlignment(SwingConstants.RIGHT);
		gameCredit.setVisible(true); gamePanel.add(gameCredit);
		//
		cSkipButton = new JButton("Skip (3s)");
		cSkipButton.setBounds(375, 400, 250, 32);
		cSkipButton.setFont(new Font("Arial", Font.BOLD, 30));
		cSkipButton.setForeground(new Color(255, 255, 255));
		cSkipButton.setBackground(new Color(0, 0, 0));
		cSkipButton.setVisible(true); gamePanel.add(cSkipButton);
		//
		playButton = new JButton("Start");
		playButton.setBounds(375, 400, 250, 32);
		playButton.setFont(new Font("Arial", Font.BOLD, 30));
		playButton.setForeground(new Color(0, 0, 0));
		playButton.setBackground(new Color(0, 0, 0));
		playButton.setVisible(false); gamePanel.add(playButton);
		//
		helpButton = new JButton("Help");
		helpButton.setBounds(375, 475, 250, 32);
		helpButton.setFont(new Font("Arial", Font.BOLD, 30));
		helpButton.setForeground(new Color(0, 0, 0));
		helpButton.setBackground(new Color(0, 0, 0));
		helpButton.setVisible(false); gamePanel.add(helpButton);
		//
		nameField = new JTextField("");
		nameField.setBounds(750, 500, 200, 32);
		nameField.setFont(new Font("Arial", Font.BOLD, 25));
		nameField.setForeground(new Color(255, 255, 255));
		nameField.setBackground(new Color(100, 100, 100));
		nameField.setVisible(false); gamePanel.add(nameField);
		//
		nameFieldText = new JLabel("Input your name here!");
		nameFieldText.setBounds(750, 470, 200, 32);
		nameFieldText.setFont(new Font("Arial", Font.BOLD, 15));
		nameFieldText.setForeground(new Color(255, 255, 255));
		nameFieldText.setVerticalAlignment(SwingConstants.CENTER);
		nameFieldText.setHorizontalAlignment(SwingConstants.CENTER);
		nameFieldText.setVisible(false); gamePanel.add(nameFieldText);
		//
		levelText = new JLabel("");
		levelText.setBounds(0, (720/2)-50, 1024, 100);
		levelText.setFont(new Font("Arial", Font.BOLD, 35));
		levelText.setForeground(new Color(255, 255, 255));
		levelText.setVerticalAlignment(SwingConstants.TOP);
		levelText.setHorizontalAlignment(SwingConstants.CENTER);
		levelText.setVisible(false); gamePanel.add(levelText);
		//
		helpButtonBack = new JButton("Back"); gamePanel.add(helpButtonBack);
		helpButtonTitle = new JLabel("Get the key to open the door in each level!"); gamePanel.add(helpButtonTitle);
		helpButtonTitle2 = new JLabel("Watch out for traps and all the hog ridaaaaaaa!!!!!!!!!"); gamePanel.add(helpButtonTitle2);
		//
		gamePanel.setVisible(true);
		// Finalize panel
		add(gamePanel, BorderLayout.CENTER);
	    setSize(1080, 720);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
	    // Skip Mechanic
	    addButtonFunctionality();
	    sleep(1000);
	    cSkipButton.setText("Skip (2s)");
	    sleep(1000);
	    cSkipButton.setText("Skip (1s)");
	    sleep(1000);
	    cSkipButton.setVisible(false);
	    initgame();
	}
	
	///////////////////////////////////////////////////////////////////////////////
	// GAME METHODS ///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	public void KR_Method(KeyEvent key) {
		//System.out.println("hi");
		if(running && (key.getKeyCode() == KeyEvent.VK_UP) && !pMovingDebounce && cnt.move(camdir.getDir())) {
			pMovingDebounce = false;
			movePlayer(camdir.getDir());
			Location mk = maze.getKey();
			if(plr.hasKey() && plr.getLoc().getRow() == maze.getEnd().getRow() && plr.getLoc().getCol() == maze.getEnd().getCol()) {
				gameOver(0);
			}
			if(mk != null && plr.getLoc().getRow() == mk.getRow() && plr.getLoc().getCol() == mk.getCol()) {
				plr.setKey(true);
				PLAYERACTIONTEXT2.setText("Key: YES");
				maze.removeKey();
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
					PLAYERACTIONTEXT1.setText("Sword: YES");
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
					if(!playerName.equals("rico")) {
						plr.setSword(false);
						PLAYERACTIONTEXT1.setText("Sword: NO");
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
	
	private void gameOver(final int ending) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("GAME OVER");
				removeKeyListener(camera);
				running = false;
				gameTick++;
				setBackground(Color.gray);
				GAMEOVERTEXT.setVisible(true);
				PLAYERACTIONTEXT1.setVisible(false);
				PLAYERACTIONTEXT2.setVisible(false);
				MINIMAPTEXT.setVisible(false);
				if(ending == 0) {
					//playSound("res/Congrats2.wav");
					if(mapSelected < 3) {
						GAMEOVERTEXT.setText("Well done, but the Clan Castle continues on...");
						sleep(3000);
						GAMEOVERTEXT.setVisible(false);
						System.out.println("attempting to continue game");
						mapSelected++;
						selectMap(mapSelected);
					}else {
						GAMEOVERTEXT.setText("Congratulations! You have escaped the Level 7 Clan Castle! More updates soon!");
						sleep(3000);
						if(dtext != null) {dtext.setVisible(false);}
						if(gametitle != null) {gametitle.setVisible(false);}
						if(gamed1 != null) {gamed1.setVisible(false);}
						if(gamed2 != null) {gamed2.setVisible(false);}
						openMainMenu();
					}
				}else if(ending == 1) {
					GAMEOVERTEXT.setText("You have succumbed to the Minotaurs...");
					sleep(3000);
					GAMEOVERTEXT.setVisible(false);
					System.out.println("attempting to restart game");
					mapSelected = 1;
					selectMap(mapSelected);
				}else if(ending == 2) {
					GAMEOVERTEXT.setText("You have succumbed to the Dungeon's many explosive traps...");
					sleep(3000);
					GAMEOVERTEXT.setVisible(false);
					System.out.println("attempting to restart game");
					System.out.println("GAME OVER");
					if(mapSelected != 1) {
						mapSelected--;
					}
					selectMap(mapSelected);
				}
			}
		}).start();
	}
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	// Boot up game (threads)
	private synchronized void start() {
		
		// PRE START
		System.out.println("starting map");
		gameTick++;
		if(dtext != null) {dtext.setVisible(false);}
		if(gametitle != null) {gametitle.setVisible(false);}
		if(gamed1 != null) {gamed1.setVisible(false);}
		if(gamed2 != null) {gamed2.setVisible(false);}
		//
		levelText.setText("Level " + mapSelected);
		levelText.setVisible(true);
		sleep(3000);
		levelText.setVisible(false);
		//
		
		//running = true;
		textures = null; textures = new ArrayList<Texture>();
		textures.add(Texture.lvl1wall);
		textures.add(Texture.lvl2wall);
		textures.add(Texture.lvl3wall);
		textures.add(Texture.MINOTAUR);
		// In-game UI stuff (priority)
		dtext = new JLabel("");
		dtext.setBounds(875, 40, 180, 120);
		dtext.setFont(new Font("Arial", Font.BOLD, 15));
		dtext.setVerticalAlignment(SwingConstants.TOP);
		dtext.setForeground(new Color(255, 255, 255));
		dtext.setVisible(true); gamePanel.add(dtext);
		dtext.setText("Current Direction: ");
		// Class Data (super important, also very inefficient)
		if(mapSelected == 1) {
			maze.setup(mapSelected);
			plr.setup(maze.getSpawn());
			// Minotaurs in levels
			minlist = new ArrayList<Minotaur>();
			minlist.add(new Minotaur(new Location(1, 10)));
			minlist.add(new Minotaur(new Location(4, 5)));
			minlist.add(new Minotaur(new Location(6, 10)));
			for(int i = 0; i < minlist.size(); i++) {
				maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 4; // Change map tile to a Minotaur
			}
			//
			cnt.setup(plr, minlist, maze, playerName, "hi");
			camdir.setup(dtext);
			camera.setup(maze.getSpawn().getRow(), maze.getSpawn().getCol(), 1, 0, 0, -.66, camdir, game);
			screen.setup(maze.getMap().getArr(), 8, 8, textures, 1080, 720, mapSelected);
			addKeyListener(camera);
		}else if(mapSelected == 2) {
			maze.setup(mapSelected);
			plr.setup(maze.getSpawn());
			// Minotaurs in levels
			minlist = new ArrayList<Minotaur>();
			minlist.add(new Minotaur(new Location(6, 15)));
			minlist.add(new Minotaur(new Location(4, 10)));
			minlist.add(new Minotaur(new Location(1, 17)));
			for(int i = 0; i < minlist.size(); i++) {
				maze.getMap().getArr()[minlist.get(i).getLoc().getRow()][minlist.get(i).getLoc().getCol()] = 4; // Change map tile to a Minotaur
			}
			//
			cnt.setup(plr, minlist, maze, playerName, "hi");
			camdir.setup(dtext);
			camera.setup(maze.getSpawn().getRow(), maze.getSpawn().getCol(), 1, 0, 0, -.66, camdir, game);
			screen.setup(maze.getMap().getArr(), 8, 20, textures, 1080, 720, mapSelected);
			addKeyListener(camera);
		}else if(mapSelected == 3) {
			maze.setup(mapSelected);
			plr.setup(maze.getSpawn());
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
			cnt.setup(plr, minlist, maze, playerName, "hi");
			camdir.setup(dtext);
			camera.setup(maze.getSpawn().getRow(), maze.getSpawn().getCol(), 1, 0, 0, -.66, camdir, game);
			screen.setup(maze.getMap().getArr(), 8, 22, textures, 1080, 720, mapSelected);
			addKeyListener(camera);
		}
		if(playerName.equals("rico")) {
			plr.setSword(true);
		}
		// In-game UI stuff
		gametitle = new JLabel("");
		gametitle.setBounds(15, 15, 1080, 25);
		gametitle.setFont(new Font("Arial", Font.BOLD, 20));
		gametitle.setVerticalAlignment(SwingConstants.TOP);
		gametitle.setForeground(new Color(255, 255, 255));
		gametitle.setVisible(true); gamePanel.add(gametitle);
		gametitle.setText("The Minotaur 2 - Level " + mapSelected);
		gamed1 = new JLabel("");
		gamed1.setBounds(15, 45, 1080, 30);
		gamed1.setFont(new Font("Arial", Font.PLAIN, 25));
		gamed1.setVerticalAlignment(SwingConstants.TOP);
		gamed1.setForeground(new Color(255, 255, 255));
		gamed1.setVisible(true); gamePanel.add(gamed1);
		gamed1.setText("<- and -> arrow keys to change directions");
		gamed2 = new JLabel(""); // "W (and respective arrow key) to move in your direction"
		gamed2.setBounds(15, 80, 1080, 30);
		gamed2.setFont(new Font("Arial", Font.PLAIN, 25));
		gamed2.setVerticalAlignment(SwingConstants.TOP);
		gamed2.setForeground(new Color(255, 255, 255));
		gamed2.setVisible(true); gamePanel.add(gamed2);
		gamed2.setText("Up arrow key to move in your direction");
		PLAYERACTIONTEXT1 = new JLabel("");
		PLAYERACTIONTEXT1.setBounds(875, 60, 1080, 30);
		PLAYERACTIONTEXT1.setFont(new Font("Arial", Font.ITALIC, 20));
		PLAYERACTIONTEXT1.setVerticalAlignment(SwingConstants.TOP);
		PLAYERACTIONTEXT1.setForeground(new Color(255, 255, 255));
		PLAYERACTIONTEXT1.setVisible(true); gamePanel.add(PLAYERACTIONTEXT1);
		if(playerName.equals("rico")) {
			PLAYERACTIONTEXT1.setText("Sword: YES");
		}else {
			PLAYERACTIONTEXT1.setText("Sword: NO");
		}
		PLAYERACTIONTEXT2 = new JLabel("");
		PLAYERACTIONTEXT2.setBounds(875, 80, 1080, 30);
		PLAYERACTIONTEXT2.setFont(new Font("Arial", Font.ITALIC, 20));
		PLAYERACTIONTEXT2.setVerticalAlignment(SwingConstants.TOP);
		PLAYERACTIONTEXT2.setForeground(new Color(255, 255, 255));
		PLAYERACTIONTEXT2.setVisible(true); gamePanel.add(PLAYERACTIONTEXT2);
		PLAYERACTIONTEXT2.setText("Key: NO");
		MINIMAPTEXT = new JLabel("");
		MINIMAPTEXT.setBounds(700, 0, 380, 120);
		MINIMAPTEXT.setFont(new Font("Consolas", Font.PLAIN, 12));
		MINIMAPTEXT.setVerticalAlignment(SwingConstants.TOP);
		MINIMAPTEXT.setForeground(new Color(255, 255, 255));
		MINIMAPTEXT.setVisible(true); gamePanel.add(MINIMAPTEXT);
		MINIMAPTEXT.setText(cnt.drawMap());
		GAMEOVERTEXT = new JLabel();
		GAMEOVERTEXT.setBounds(0, 0, 1080, 720);
		GAMEOVERTEXT.setFont(new Font("Arial", Font.BOLD, 25));
		GAMEOVERTEXT.setVerticalAlignment(SwingConstants.CENTER);
		GAMEOVERTEXT.setForeground(new Color(255, 255, 255));
		GAMEOVERTEXT.setHorizontalAlignment(SwingConstants.CENTER);
		gamePanel.add(GAMEOVERTEXT);
		//
		JLabel FILLER = new JLabel(""); // for some reason the UI lining breaks, but it fixes itself with a filler text?
		FILLER.setBounds(15, 120, 1080, 30);
		FILLER.setFont(new Font("Arial", Font.PLAIN, 25));
		FILLER.setVerticalAlignment(SwingConstants.TOP);
		FILLER.setVisible(true); gamePanel.add(FILLER);
		//
		setVisible(true);
		pMovingDebounce = false;
		running = true;
		run();
		//thread.start();
		
		// Move all minotaurs
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				int oldtick = gameTick;
//				while(oldtick == gameTick) {
//					sleep(2000);
//					moveMinotaurs();
//				}
//			}
//		}).start();
	}
		
	public synchronized void stop() {
		running = false;
//		try {
//			thread.join();
//		} catch(InterruptedException e) {
//			e.printStackTrace();
//		}
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
		new Thread(new Runnable() {
		@Override
		public void run() {
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
		}).start();
	}
	//
	
	public Game() {
		// Initialize Thread
		//thread = new Thread(this);
		// GUI data
		image = new BufferedImage(1080, 720, BufferedImage.TYPE_INT_RGB); // Basis for GUI
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		//
		maze = new Maze(); 
		plr = new Player();
		cnt = new Controller();
		camdir = new CameraDir();
		camera = new Camera();
		screen = new Screen(); 
		//
		setup(); // setup UI
		//
	}
	
	public static void main(String [] args) {
		try {
			iconimg = ImageIO.read(Game.class.getResourceAsStream("/TheMinotaurLogo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		game = new Game();
	}

}
