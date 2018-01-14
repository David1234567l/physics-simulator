package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;


import physics_simulator.*;
import physics_simulator.physics_object.faces;

public class JetPack extends rectangle {
	
	public double fireSize = 0.4;
	public double power = 40;
	
	public JetPack(int x, int y, int z, int size, double mass) {
		super(x, y, z, size*2,size, mass);
		drawMethod = "paint";
		
	}
	
	public void isCollided(physics_object object, faces side) {
		if (object != null) {
			
			try {
			//this try/catch checks if the object is of the Laser class
			    Laser c = (Laser) object;
			    System.out.println("game over");
				JetPack_JoyRide.game_over = 1;
			} catch (ClassCastException e) {}
			
			try {
				//this try/catch checks if the object is of the Missile class
			    Missile c = (Missile) object;
			    System.out.println("game over");
				JetPack_JoyRide.game_over = 1;
			} catch (ClassCastException e) {}
			
			//this try/catch checks if the object is of the Coin class
			try {
			    Coin coin = (Coin) object;
			    JetPack_JoyRide.coins++;
			    coin.coinReLocate();
			} catch (ClassCastException e) {}
			
	
		}
	}
	
	
	
	public void paint(Graphics page) {
		page.setColor(color);
		page.fillRect(x, y, (int) Math.round(ySize/2), (int) Math.round(ySize));
		page.fillRect((int) Math.round(x + ySize/2) - 3, y, (int) Math.round(ySize/2), (int) Math.round(ySize));
		
		page.setColor(Color.DARK_GRAY);
		page.drawRect(x, y, (int) Math.round(ySize/2), (int) Math.round(ySize));
		page.drawRect((int) Math.round(x + ySize/2) , y, (int) Math.round(ySize/2)-3, (int) Math.round(ySize));
		
		if (fireSize > 0.4) {
			page.setColor(Color.yellow);
			page.fillRect(x+3,(int) ( y+Math.round(ySize)), (int) Math.round(ySize/2)-6, (int) Math.round(fireSize*ySize/2));
			page.fillRect((int) Math.round(ySize/2)+x+1,(int) ( y+Math.round(ySize)), (int) Math.round(ySize/2)-6, (int) Math.round(fireSize*ySize/2));
		}
		
		page.setColor(Color.orange);
		page.fillRect(x+2,(int) ( y+Math.round(ySize)), (int) Math.round(ySize/2)-4, (int) Math.round(0.45*ySize/2));
		page.fillRect((int) Math.round(ySize/2)+x,(int) ( y+Math.round(ySize)), (int) Math.round(ySize/2)-4, (int) Math.round(0.45*ySize/2));
		
		}

}
