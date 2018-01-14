package jetpack_joyride;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ConcurrentModificationException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import physics_simulator.*;

public class JetPack_JoyRide {

	public static final String version = "1.5";
	
	static JPJR_frame frame = new JPJR_frame();
	private static JPJR_GUI GUI = new JPJR_GUI();
	
	static JPJR_Shop shop = new JPJR_Shop();
	
	static int coins = 0, coinsEarned = 0, game_over = 0;  // 0 is false, 1 is true, and 2 & 3 are other
	
	static double jetpack_speed = 2, distance = 0, distanceHighScore = 0, responsiveness_timer = 0, frames;
	
	static final boolean pictureGraphics = false;
	
	static boolean pause = false, error = false;
	
	static object_draw drawer = new object_draw(frame);

	static ImageIcon jetpack_img = new ImageIcon("jetpack.txt"), coin_img = new ImageIcon("coin.txt");
	static border_bounce boundries;
	static JetPack jetpack;
	static Laser laser1,laser2;
	static Missile Missile1;
	static ScoreBoard coinScore,distanceScore,distanceHighScoreBoard;
	static Coin coin1,coin2,coin3;
	
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		init();
	}
	
	public static void playSound(File soundFile) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(soundFile));
			clip.start();
		}catch(Exception e) {
			
		}
	}
	
	public static void resetGame() {
		
		jetpack.setPos(Settings.width/2,Settings.height/2, 0);
		jetpack.setSpeed(0, 0, 0);
		jetpack.setAccel(0,0,0);
		jetpack.applyComponentForce(0, 9.8, 0);
		
		laser1.setPos(100,200,0);
		laser2.setPos(2*Settings.width/3,200,0);
		
		Missile1.setPos(0,200,0);
		
		coin1.setPos(350, 270,0);
		coin2.setPos(530, 300,0);
		coin3.setPos(760, 230,0);
		
		distance = 0;		
		game_over = 0;
		jetpack_speed = 3;
		coinsEarned = 0;
		
		distanceHighScoreBoard.setScore(distanceHighScore);	
		
		drawer.doFrame(50);
	}
	
	public static void init() throws FileNotFoundException, IOException, ClassNotFoundException {
				
		boundries = new border_bounce();
		boundries.name = "boundries";
		boundries.drawMethod = "listedPointsAlgorithm";
		boundries.setColor(Color.black);
		boundries.setPos(boundries.getXReal(),boundries.getYReal()-100,10);
		boundries.setSize(Settings.width *1.06, Settings.height*1.15,10);
		
		coinScore = new ScoreBoard();
		coinScore.setScore(coins);
		coinScore.setScorePhrase("Coins:");
		coinScore.setColor(Color.WHITE);
		
		distanceScore = new ScoreBoard(0.7 * Settings.width,Settings.height-100,"Distance:",distance);
		distanceScore.setColor(Color.WHITE);
		distanceScore.setEndPhrase("m");
		
		distanceHighScoreBoard = new ScoreBoard(0.4 * Settings.width,Settings.height-100,"HighScore:",distance);
		distanceHighScoreBoard.setColor(Color.WHITE);
		distanceHighScoreBoard.setEndPhrase("m");
		
		jetpack = new JetPack(Settings.width/2,Settings.height-150, 0, 20,200);
		jetpack.setColor(Color.blue);
		jetpack.name = "jetpack";
		
		laser1 = new Laser(100,200,10,100,0,0,0);
		laser2 = new Laser(2*Settings.width/3,200,10,100,0,0,0);
	
		Missile1 = new Missile(0,200);
		Missile1.setName("thing",1);
		
		coin1 = new Coin(350, 270);
		coin2 = new Coin(530, 300);
		coin3 = new Coin(760, 230);
		
	
		
		//mouseListener +==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
				MouseAdapter mouse =  new MouseAdapter() {

				public void mouseClicked(MouseEvent arg0) {
					responsiveness_timer = 0;
				}
				
				public void mousePressed(MouseEvent arg0) {
					responsiveness_timer = 0;
					jetpack.applyComponentForce(0, -25, 0);
					jetpack.fireSize = 0.75;
				}


				public void mouseReleased(MouseEvent arg0) {
					responsiveness_timer = 0;
					jetpack.applyComponentForce(0, 25, 0);
					jetpack.fireSize = 0.35;
			
				}};
				
				
			//==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
				
			drawer.addMouseListener(mouse);
		
			//key listener
			drawer.addKeyListener(new KeyListener() {
				   
	              @Override
	              public void keyPressed(KeyEvent e) {
	            	  responsiveness_timer = 0;
	            	  jetpack.setAccel(0, 0, 0);
	            	  jetpack.applyComponentForce(0, -jetpack.power, 0);
	            	  jetpack.fireSize = 0.75;
	              }

				@Override
				public void keyReleased(KeyEvent arg0) {
					responsiveness_timer = 0;
					jetpack.setAccel(0, 0, 0);
					jetpack.applyComponentForce(0, jetpack.power, 0);
					jetpack.applyComponentForce(0, 9.8, 0);
					jetpack.fireSize = 0.35;
					
				}
				@Override
				public void keyTyped(KeyEvent arg0) {
					responsiveness_timer = 0;
				}
	          });

	          drawer.setFocusable(true);
	          drawer.requestFocusInWindow();			  
					
				
		frame.cp.add(drawer);
		
		object_draw.objects.add(jetpack);
		object_draw.objects.add(boundries);
		object_draw.objects.add(coinScore);
		object_draw.objects.add(distanceScore);
		object_draw.objects.add(distanceHighScoreBoard);
		object_draw.objects.add(laser1);
		object_draw.objects.add(laser2);
		object_draw.objects.add(Missile1);
		object_draw.objects.add(coin1);
		object_draw.objects.add(coin2);
		object_draw.objects.add(coin3);
	
		
		
		
	
		jetpack.applyComponentForce(0, 9.8, 0);
		
		
		//load the game
			try {
				loadGame(); 
			}catch(FileNotFoundException e) {
				System.out.println("Missing Save File");
			}
		
		//initializing the GUIs
		GUI.init();
		shop.init();
			
			
		distanceHighScoreBoard.setScore(distanceHighScore);	
			
		
		frames = 0.5;
		
		error = false;
		
		do {
			try {
				drawer.doFrame(100);
				error = false;
			}catch(ConcurrentModificationException e) {
				System.out.println("ConcModError");
				error = true;
			}
		}while(error);
		
		frame.setVisible(true);
		GUI.setVisible(true);
		
		jetpack_speed = 3;
		distance = 0;	
		game_over = 0;
		run();
	}
	
	public static void run() throws FileNotFoundException, IOException {
		responsiveness_timer = 0;
		while (game_over == 0) {
			if (! pause) {
				do {
					try {			
						drawer.doFrame(frames);
						if ((jetpack.getY() < -40) || (jetpack.getY() > Settings.height*1.25)){
							jetpack.setPos(Settings.width/2, Settings.height - 100, 0);
						}
						
						if (responsiveness_timer >= 2000) {
							System.out.println("User is unresponsive so the game is terminating.");
							game_over = 2;
						}
						
						coinScore.setScore(coins);
						distanceScore.setScore(Math.round(distance));
						
						distance += jetpack_speed;
						responsiveness_timer += frames;
						
								
						error = false;				
					
					
					
					
					
					}catch(ConcurrentModificationException e) {
						System.out.println("ConcModError");
						error = true;
					}
				}while (error);
				
					jetpack_speed += (Math.pow(object_draw.current_frame,1/100) / 900) * frames;
			}else {
				GUI.repaint();
			}
		}
		
		if ((game_over == 1) || (game_over == 4)) {
			
			String mes = "";
			if (distance > distanceHighScore) {
				mes = "\n You beat the high score!";
			}
			
			if (coins == 1) {
				JOptionPane.showMessageDialog(drawer, "GAME OVER\nYou earned " + coinsEarned + " coin!\nYou traveled " + Math.round(distance) + " meters!" + mes);
			} else {
				JOptionPane.showMessageDialog(drawer, "GAME OVER\nYou earned " + coinsEarned + " coins!\nYou traveled " + Math.round(distance) + " meters!" + mes);
			}
			
			
				
			
			saveGame(); //save the game
			
			if (game_over == 1) {
				//See if user wants to play another game and if so, starting another game  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
				int another = 2;
				while (another == 2) {
					another = JOptionPane.showConfirmDialog(null,"Do you want to play another game?", "Another?", 1, 1, null);
				}
				
				if (another == 0) {
					resetGame();
					try {
						loadGame();
					} catch (ClassNotFoundException e) {
						System.out.println("Missing Save_file");
					} catch (InvalidClassException e) {
						System.out.println("Corrupted Save_file");
					}
					
					run();
				}
				//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			}
			
		}

		
		frame.dispose();
		GUI.dispose();
		shop.dispose();
		
		//end
	
	}

	public static void loadGame() throws ClassNotFoundException, IOException{
		
		try {
			System.out.println("Loading from save_file...");
			ObjectInputStream loader = new ObjectInputStream(new FileInputStream("JetPack_JoyRide_save.txt"));
			JetPack_JoyRide_SaveFIle save = (JetPack_JoyRide_SaveFIle) loader.readObject();
			coins = save.coins;
			distanceHighScore = save.distanceHighScore;
			shop.boughtButtons = save.boughtButtons;
			loader.close();
		}catch(InvalidClassException e) {
			System.out.println("Corrupted Save_file");
		}
	}
	
	public static void saveGame() throws FileNotFoundException, IOException {
		System.out.println("Saving in progress...");
		ObjectOutputStream saver = new ObjectOutputStream(new FileOutputStream("JetPack_JoyRide_save.txt"));
		
		if (distance > distanceHighScore) {
			distanceHighScore = distance;
		}
		
		
		JetPack_JoyRide_SaveFIle save = new JetPack_JoyRide_SaveFIle();
		save.distanceHighScore = distanceHighScore;
		save.coins = coins;
		save.boughtButtons = shop.boughtButtons;
		
		saver.writeObject(save);
		saver.close();
		System.out.println("Save Complete");
		
	}
	
}
