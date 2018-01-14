package physics_simulator;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class object_draw extends Canvas{
	
	public static ArrayList<physics_object> objects = new ArrayList<physics_object>();
	
	public static ArrayList<force> scheduled_forces = new ArrayList<force>(); //list of forces for the maintenance bot to apply
	
	public static double current_frame = 0; //what frame we are on
	private static long frameStartTime;
	private static long frameEndTime;
	private static long wait_time;
	
	public int inactivity_timer = 0;
	
	public static Physics_frame frame;
	
	public object_draw(Physics_frame frame1) {
		frame = frame1;
		frame.getContentPane().add(this);
		
		

	}
	
	public void updateObjects() {
		
		for (physics_object current_object : objects) {				
			current_object.Update(objects);
		}

		for (force current_force : scheduled_forces) { //applying the scheduled forces
			current_force.apply();
		}
	
	}
	
	public void updateObjects(double frames) {
		for (physics_object current_object : objects) {				
			current_object.Update(objects,frames);
		}
		
		if ( Math.abs(current_frame - (int)current_frame ) < 0.00001) {
			for (force current_force : scheduled_forces) { //applying the scheduled forces
				current_force.apply();
			}
		}
	}
	
	public static void Wait(double time) { // Wait(1000) evaluates to about 20 seconds
		double Time = time * 10000000;
		double t = 0;
		while (t < Time)
			t++;
	}
	
	public static void WaitNanotime(long time) { //more accurate wait method for nanotime
		long StartTime = System.nanoTime();
		long CurrentTime;	
		do {
			CurrentTime = System.nanoTime();
		}while ( (CurrentTime-StartTime) <= time);
	}
	

	
	public void doFrame() {
		frameStartTime = System.nanoTime();
		current_frame++;
		updateObjects();
		repaint();
		frameEndTime = System.nanoTime();
		wait_time = Settings.frameTime - (frameEndTime - frameStartTime);
		WaitNanotime(wait_time);
	
	}
	
	public void doFrame(double frames) {
	
		frameStartTime = System.nanoTime();
		current_frame += frames;
		updateObjects(frames);
		
		if ( Math.abs(current_frame - (int)current_frame ) < Settings.frameStep) { 
	
			repaint(); 
			frameEndTime = System.nanoTime();
			wait_time = Settings.frameTime - (frameEndTime - frameStartTime);
			
			if (wait_time < 0) {
				Exception ex = new Exception("Wait time is less than 0! wait_time: " + wait_time);
				ex.printStackTrace();
			}
			
			WaitNanotime(wait_time);
		}
	}
	
	public void doFrame(String key) {
		assert key == "step";
		for (double i = 0; i < 1; i+= Settings.frameStep) {
			doFrame(Settings.frameStep);
		}
	}
	
	public void paint(Graphics page) {
		
		//sorting objects by z distance ----------------------------------
		Collections.sort(objects, new Comparator<physics_object>() {
	     
	        public int compare(physics_object o1, physics_object o2) {
	            return Double.compare(o2.getZReal(), o1.getZReal());
	        }
	    });
		//----------------------------------------------------------------
		
		
		point[] points;
		point current_point;
		point next_point;
		for (physics_object current_object : objects) {
			
			if (current_object.isVisible) {
				
				page.setColor(current_object.getColor());
				
			
			
				points = current_object.getPoints();
				
				switch (current_object.drawMethod) {
				
					case("fillRect"):
						if (Settings.displayObjectNames) page.drawString(current_object.name,(int) Math.round(current_object.getCenterX()), (int) Math.round(current_object.getCenterY()));
						page.fillRect(current_object.x,current_object.y, (int) Math.round(current_object.xSizeAppearance),(int) Math.round(current_object.ySizeAppearance));
						break;
					
					case("ListedPointAlgorithm"):
						int pointKey;
						int next_pointKey;
						if (Settings.displayObjectNames) page.drawString(current_object.name,(int) Math.round(current_object.getCenterX()), (int) Math.round(current_object.getCenterY()));
						for (int i = 0; i < current_object.pointRenderOrder.length-1 ; i++) {
						
							//calculate the pointkeys
							pointKey = current_object.pointRenderOrder[i];
							
							if (i == current_object.pointRenderOrder.length-1) {
								next_pointKey = 0;
								
							}else {
								next_pointKey = current_object.pointRenderOrder[i+1];
							}
							
							
					//		System.out.println(pointKey + "," + next_pointKey);
							
							//get the points
							current_point = points[pointKey];
							next_point = points[next_pointKey];
							
							if (Settings.showPointNumbers) page.drawString("" + i, current_point.getX(), current_point.getY()); //display the point numbers
							
					//		System.out.println(pointKey + "," + next_pointKey);
							
							/*
							System.out.print("Point" + i + ": (");
							System.out.print(current_point.x);
							System.out.print(",");
							System.out.print(current_point.x);
							System.out.println(")");
							*/
						
							//draw line between points
							page.drawLine(current_point.x, current_point.y, next_point.x, next_point.y);
						}
						break;
					
					case("paint"):
						current_object.paint(page);
						break;
						
					default:
						current_object.paint(page);
						break;
				} //switch drawmethod
			}
			
		}
	}
}
