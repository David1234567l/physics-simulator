package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import physics_simulator.*;

public class Coin extends Square {
	
	public Coin(int x, int y) {
		super(x, y, 1, 20, 0.1);
		affectedByBorder = false;
		name = "thing";
		setColor(Color.YELLOW);
		drawMethod = "paint";
		
	}

	public void coinReLocate() {
		setPos(Settings.width * 2 * Math.random() + (Settings.width+100), Math.random() * (Settings.height-xSize-150), getZReal());
		setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
	}
	
	public Object checkForCollision(physics_object current_object,ArrayList<physics_object> objects) {
		if ( (Math.abs(getCenterX() - current_object.getCenterX()) < (current_object.getXSize()/2+xSize/2)) && (Math.abs(getCenterY() - current_object.getCenterY()) < (current_object.getYSize()/2+ySize/2)) /* && (Math.abs(centerZ - current_object.centerZ) < (current_object.getZSize()/2+zSize/2))*/ ) {	
			if (current_object.name == "jetpack") {
				coinReLocate();
				JetPack_JoyRide.coins++;
				JetPack_JoyRide.coinsEarned++;
			}
		}
		return current_object;
	}
	
	
	public void Update(ArrayList<physics_object> objects) {
		
			if (getXReal() < 0) {
				setPos(Settings.width+100, Math.random() * (Settings.height-getXSize()-150), getZReal());
				setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
			}else if ( (getXReal()+10 < JetPack_JoyRide.jetpack.getXReal()) || (getXReal()-50 >JetPack_JoyRide.jetpack.getXReal()) ) {
				isTangible = false;
			}else {
				isTangible = true;
			}
		
			setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
		
		 //updating the pos and speed based on the accel
				//update speed
				xSpeed += xAccel;
				ySpeed += yAccel;
				zSpeed += zAccel;
				
				checkForCollisions(objects);
				
				//update real pos
				xReal += xSpeed;
				yReal += ySpeed;
				zReal += zSpeed;
				
			
				
				
					
				
				//updating relative values
				updatePos();//update int values of pos
				updateCenter();
				updateSize();
				updatePoints();//set the points based on the x and y values  --MUST BE REWRITTEN AFTER ROTATION--
				
			
		}
		
		
	public void Update(ArrayList<physics_object> objects,double frames) { //frames is the number of frames the object should update (can be a decimal)
			

		if (getXReal() < 0) {
			setPos(Settings.width+100, Math.random() * (Settings.height-getXSize()-150), getZReal());
			setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
		}else if ( (getXReal()+10 < JetPack_JoyRide.jetpack.getXReal()) || (getXReal()-50 >JetPack_JoyRide.jetpack.getXReal()) ) {
			isTangible = false;
		}else {
			isTangible = true;
		}
			

		setSpeed(-JetPack_JoyRide.jetpack_speed, 0,0);
		
	 //updating the pos and speed based on the accel
			//update speed
			xSpeed += (xAccel * frames);
			ySpeed += (yAccel * frames);
			zSpeed += (zAccel * frames);
				
			checkForCollisions(objects);
			
			//update real pos
			xReal += (xSpeed * frames);
			yReal += (ySpeed * frames);
			zReal += (zSpeed * frames);
			
		
		
				
			//updating relative values
			updatePos();//update int values of pos
			updateCenter();
			updateSize();
			updatePoints();//set the points based on the x and y values  --MUST BE REWRITTEN AFTER ROTATION--	
		}
		

	
	public void paint(Graphics page) {
		if (JetPack_JoyRide.pictureGraphics) {
		
		}else {
			page.setColor(color);
			page.fillOval(x, y, (int) Math.round(xSize),(int) Math.round(ySize));
		}
	}
}
