package jetpack_joyride;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import physics_simulator.*;
import physics_simulator.physics_object.faces;

public class Laser extends rectangle{

	public Laser(int x, int y, int xSize, int ySize, double xRotation1, double yRotation1, double zRotation1) {
		super(x, y, 0, xSize, ySize, 1);
		setColor(Color.red);
		drawMethod = "paint";
		affectedByBorder = false;
		name = "thing";
		
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
			
		if (! isAnchored) { //updating the pos and speed based on the accel
			//update speed
			xSpeed += (xAccel * frames);
			ySpeed += (yAccel * frames);
			zSpeed += (zAccel * frames);
				
			checkForCollisions(objects);
			
			//update real pos
			xReal += (xSpeed * frames);
			yReal += (ySpeed * frames);
			zReal += (zSpeed * frames);
			
		}else { //object is anchored and shouldn't move
			xSpeed = 0;
			ySpeed = 0;
			zSpeed = 0;
		}
				
	
				
			//updating relative values
			updatePos();//update int values of pos
			updateCenter();
			updateSize();
			updatePoints();//set the points based on the x and y values  --MUST BE REWRITTEN AFTER ROTATION--	
		}
		
	
	
	public void paint(Graphics page) {
		page.fillRect(x,y, (int) Math.round(xSizeAppearance),(int) Math.round(ySizeAppearance));
//		page.setColor(Color.ORANGE);
//		page.fillRect(x + (int) Math.round(xSizeAppearance/4),y + (int) Math.round(ySizeAppearance/4), (int) Math.round(xSizeAppearance/2),(int) Math.round(ySizeAppearance/2));
	}
}
