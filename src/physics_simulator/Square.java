package physics_simulator;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import physics_simulator.physics_object.faces;



public class Square extends physics_object{
	
	public Square(double x, double y, double z, double size, double mass) {
		
		setPos(x,y,z);
		setSize(size,size,0);
		setMass(mass);
		setRotation(0,0,0);
		
		name = "square1";
		
		pointRenderOrder = new int[] {0,1,2,3,0};
		point[] points1 = new point[4];
		
		points1[0] = new point(xReal,yReal,zReal); 
		points1[0].setAngle(3*Math.PI/4, 0,Math.PI/2);
		
		points1[1] = new point(xReal+xSize,yReal,zReal);
		points1[1].setAngle(Math.PI/4, Math.PI,Math.PI/2);
		
		points1[2] = new point(xReal+xSize,yReal+ySize,zReal);
		points1[2].setAngle(-Math.PI/4, Math.PI,-Math.PI/2);
		
		points1[3] = new point(xReal,yReal+ySize,zReal);
		points1[3].setAngle(-3*Math.PI/4, 0,-Math.PI/2);
	
		
		setPoints(points1);
		
	
		
		
		
		double r = Math.sqrt(Math.pow(xSize,2) + Math.pow(ySize,2) + Math.pow(zSize,2))/2;
		for (point cP : points) {
			cP.setR(r);
		}
		
		calculateCenter();
	
//		calculatePointValues();  //THIS METHOD IS EVIL SPAWN OF SATAN! |:<
	
		updatePointOfRotation();
	


		setPos(x,y,z);
		
		if ( (Settings.rotationAlgorithm == 2) && (isRotatable) ) {
			System.out.println("creatingAxis");
			for (point p : points) {
				p.setR(Math.sqrt(xSize + ySize));
			}
			axis = new Coordinate_Axis(this);
			axis.setName(name + "_axis");
			object_draw.objects.add(axis);
		}
		
	}
	
	
	public Object checkForCollision(physics_object current_object,ArrayList<physics_object> objects) {
			
			if ((current_object.isTangible) && (! this.equals(current_object))) {
				faces side = faces.none;
				
				if (Settings.collision_algorithm == 4) {// v 4.2 collision algorithm (simplified)
					
					//is there a collision?
					if ( (Math.abs(getCenterX() - current_object.getCenterX()) < (current_object.xSize/2+xSize/2)) && (Math.abs(getCenterY() - current_object.getCenterY()) < (current_object.ySize/2+ySize/2)) /* && (Math.abs(centerZ - current_object.centerZ) < (current_object.zSize/2+zSize/2))*/ ) {	
					
						//set up some vars
						double[] trajectory_this = getTrajectory();
						double[] trajectory_current_ob = current_object.getTrajectory();
						
						double xyTragThis = trajectory_this[0];
						double xyTragCOb = trajectory_current_ob[0];
						
						
						//which face did it collide with?
						if (  Math.abs(getCenterX()+xSize/2 - current_object.x) < (xSize * 0.1)) { //object hit left face of object
							side = faces.left;
						} else if (  Math.abs(getCenterY()+ySize/2 - current_object.y) < (ySize * 0.1)) { //object hit top face
							side = faces.top;
						} else if (  Math.abs( (getCenterX() - xSize/2) - (current_object.x + current_object.xSize) ) < (xSize * 0.1 )) { //ball hit right face
							side = faces.right;
						}else if (  Math.abs( (getCenterY() - ySize/2) - (current_object.y+current_object.ySize) ) < (ySize * 0.1) ) {
						 side = faces.bottom;
						}
						
						if ( !(side.equals(faces.none)) ) {
							isCollided(current_object,side);
						}
							
					//output
						if (! side.equals(faces.none)) System.out.println(side);
					
					if (Settings.forceMethod == 1) {
						// sspeeed method
						//bouncing!  this algorithm needs to be a lot more advanced. This just tests the collision algorithm
						if (side.equals(faces.top) || side.equals(faces.bottom)) {
							System.out.println("top/bottom");
							ySpeed = - Settings.elasticity * ySpeed;
							current_object.setSpeed(current_object.xSpeed,-current_object.ySpeed,current_object.zSpeed);
						}else if (side.equals(faces.left) || side.equals(faces.right)) {
							System.out.println("left/right");
							xSpeed = -Settings.elasticity * xSpeed;
							current_object.setSpeed(-current_object.xSpeed,current_object.ySpeed,current_object.zSpeed);
						}else {
				
						}
						
				
						
					}else if (Settings.forceMethod == 0) {
							//momentum method ->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
							double momentum1 = mass * xSpeed;
							double momentum2 = current_object.mass * current_object.xSpeed;
							
							double time = 0.5;
							
							double force = (momentum1+momentum2)/(4*time);
							force *= Settings.elasticity;
							System.out.println("Force: " + force);
							
							double[] thisDeflectionAnglePack = current_object.calculateDeflectionAngle(this);
							double[] current_obDeflectionAnglePack = calculateDeflectionAngle(current_object);
							
							double thisDeflectionAngle = thisDeflectionAnglePack[0];
							double thisZReflect = thisDeflectionAnglePack[1];
							
							double current_obDeflectionAngle = current_obDeflectionAnglePack[0];
							double current_obZReflect = current_obDeflectionAnglePack[1];
							
	//public force(physics_object object1, double r, double theta, double zComponent1, double time1, double frame1, boolean isPolar) {
							
							object_draw.scheduled_forces.add(new force(this,force,thisDeflectionAngle,thisZReflect,time,-1,true));
							object_draw.scheduled_forces.add(new force(this,force,current_obDeflectionAngle,current_obZReflect,time,-1,true));
						}	
						
						
					}
					
				
				} else if (Settings.collision_algorithm == 3) { // v 3.0 collision algorithm (predictions)
					
					boolean hit = false;
					
					physics_object prediction_this = new physics_object();
					prediction_this = copy(prediction_this);
					
					physics_object prediction_current_ob = new physics_object();
					prediction_current_ob = current_object.copy(prediction_current_ob);
					
					
					System.out.println("old");
					System.out.println(xSpeed);
					System.out.println(current_object.xSpeed);
					
					System.out.println("copy");
					System.out.println(prediction_this.xSpeed);
					System.out.println(prediction_current_ob.xSpeed);
					
					
					double collision_frame,momentum1,momentum2,force,thisDeflectionAngle,current_obDeflectionAngle,thisZReflect,current_obZReflect;
					int time = 2;
					double[] thisDeflectionAnglePack, current_obDeflectionAnglePack;
					//prediction number is a measurement of time in frames from the current one
//					for (double prediction_number = 0; prediction_number <= Settings.collision_check_distance / Settings.prediction_step; prediction_number+= Settings.prediction_step) {
						
//						if (hit) break; //if the code finds a collision, break out
						
//						if ( (Math.abs(prediction_current_ob.getCenterX() - prediction_this.getCenterX()) < (Settings.prediction_step + (current_object.xSize+xSize)/2 ))) {
//							if  ( Math.abs(prediction_current_ob.getCenterY() - prediction_this.getCenterY()) < (Settings.prediction_step + (current_object.ySize+ySize)/2 )) {
								hit = true;
								
	//							collision_frame  = prediction_number * Settings.prediction_step + object_draw.current_frame;
								
								//momentum method ->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
								momentum1 = mass * xSpeed;
								momentum2 = current_object.mass * current_object.xSpeed;
								
								time = 2;
								
								force = (momentum1+momentum2)/(4*time);
								force = 10;
								System.out.println("Force: " + force);
								
								thisDeflectionAnglePack = current_object.calculateDeflectionAngle(this);
								current_obDeflectionAnglePack = calculateDeflectionAngle(current_object);
								
								thisDeflectionAngle = thisDeflectionAnglePack[0];
								thisZReflect = thisDeflectionAnglePack[1];
								
								current_obDeflectionAngle = current_obDeflectionAnglePack[0];
								current_obZReflect = current_obDeflectionAnglePack[1];
								
								object_draw.objects.add(new SpeedTimer(time,force*Math.cos(thisDeflectionAngle), force*Math.sin(thisDeflectionAngle),thisZReflect,this));
								object_draw.objects.add(new SpeedTimer(time,force*Math.cos(current_obDeflectionAngle), force*Math.sin(current_obDeflectionAngle),current_obZReflect,current_object));
								
								
								
								
//								object_draw.scheduled_forces.add(new force(this,force,thisDeflectionAngle,0,time,collision_frame));
//								object_draw.scheduled_forces.add(new force(current_object,-force,current_obDeflectionAngle,0,time,collision_frame));
								
								//->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
					
//							}
							
//					}
						
					//moving prediction markers to the next sub-frame interval
//						prediction_this.UpdateWithoutCollision(objects, Settings.prediction_step);
//						prediction_current_ob.UpdateWithoutCollision(objects, Settings.prediction_step);
						
						
//					}
					
					
				}else {
					assert Settings.collision_algorithm == 1;
					
				
					
					
					// v 1.10 collision algorithm (works ~70% of the time)
					if (Math.abs(getCenterX() - current_object.getCenterX()) < (current_object.xSize/2+xSize/2)) {
						if (Math.abs(getCenterY() - current_object.getCenterY()) < (current_object.ySize/2+ySize/2)) {						
								
							//figure out which way the object bounces
							if (  Math.abs(getCenterX()+xSize/2 - current_object.x) < (xSpeed*1.1)) { //object hit left face of object
								
								if (Settings.forceMethod == 0) {
									//momentum method ->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
									double momentum1 = mass * xSpeed;
									double momentum2 = current_object.mass * current_object.xSpeed;
									
									int time = 10;
									
									double force = (momentum1+momentum2)/(4*time);
								
									System.out.println("Force: " + force);
									
									object_draw.scheduled_forces.add(new force(this,-force,0,0,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,force,0,0,time,-1));
									
									//->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
								
								}else if (Settings.forceMethod == 1) {
									int time = 10;
									object_draw.scheduled_forces.add(new force(this,-Math.abs(current_object.xSpeed/mass),current_object.ySpeed/mass,current_object.zSpeed/mass,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,- Math.abs(xSpeed/current_object.mass),ySpeed/current_object.mass,zSpeed/current_object.mass,time,-1));
									xReal += 1.1 * xSpeed;
								}else {
									assert Settings.forceMethod == 2;
									
			
									//speed method
									xSpeed = - Math.abs(xSpeed);
									current_object.xSpeed = - Math.abs(current_object.xSpeed);
							//		x = (int) Math.round(xReal + xSpeed);
									xReal += 1.1 * xSpeed;
									
								
								}
			
								
							} else if (  Math.abs(getCenterY()+ySize/2 - current_object.y) < (ySpeed*1.1)) { //object hit top face
								
								if (Settings.forceMethod == 0) {
									//momentum method ->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
									double momentum1 = mass * xSpeed;
									double momentum2 = current_object.mass * current_object.xSpeed;
									
									int time = 2;
									
									double force = (momentum1+momentum2)/(2*time);
									System.out.println("Force: " + force);
									
									object_draw.scheduled_forces.add(new force(this,0,-force,0,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,0, force, 0,time,-1));
									
									//->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
								}else if (Settings.forceMethod == 1) {
									int time = 10;
									object_draw.scheduled_forces.add(new force(this,current_object.xSpeed/mass, - Math.abs(current_object.ySpeed/mass),-current_object.zSpeed/mass,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,xSpeed/current_object.mass,ySpeed/current_object.mass,-zSpeed/current_object.mass,time,-1));
									yReal += 1.1 * ySpeed;
								}else {
									assert Settings.forceMethod == 2;
									// speed method
									ySpeed = -Math.abs(ySpeed);
									current_object.ySpeed = - Math.abs(current_object.ySpeed);
									//y = (int) Math.round(yReal + ySpeed);
									yReal += 1.1 * ySpeed;
						 
								}
							
							} else if (  Math.abs( (getCenterX() - xSize/2) - (current_object.x + current_object.xSize) ) < (xSpeed*1.1 + current_object.xSize )) { //ball hit right face
								if (Settings.forceMethod == 0) {
									//force method ->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
									double momentum1 = mass * xSpeed;
									double momentum2 = current_object.mass * current_object.xSpeed;
									
									int time = 2;
									
									double force = (momentum1+momentum2)/(2*time);
									System.out.println("Force: " + force);
									
									object_draw.scheduled_forces.add(new force(this,force,0,0,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,-force,0,0,time,-1));
									xReal += 1.1 * xSpeed;
									
									//->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
								}else if (Settings.forceMethod == 1) {
									int time = 10;
									object_draw.scheduled_forces.add(new force(this,-current_object.xSpeed/mass,current_object.ySpeed/mass,-current_object.zSpeed/mass,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,-xSpeed/current_object.mass,ySpeed/current_object.mass,-zSpeed/current_object.mass,time,-1));
								
								}else {
									assert Settings.forceMethod == 2;	
									// speed method
									xSpeed = Math.abs(xSpeed);
									current_object.xSpeed = - Math.abs(current_object.xSpeed);
								
									xReal += 1.1 * xSpeed;
		
								}
								
							}else { // object hit bottom face
								assert (  Math.abs( (centerY - ySize/2) - (current_object.y+current_object.ySize) ) < (current_object.ySize + ySpeed*1.1) ); 
								
								if (Settings.forceMethod == 0) {
									//force method ->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
									double momentum1 = mass * xSpeed;
									double momentum2 = current_object.mass * current_object.xSpeed;
									
									int time = 2;
									
									double force = (momentum1+momentum2)/(2*time);
									System.out.println("Force: " + force);
									
									object_draw.scheduled_forces.add(new force(this,0,force,0,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,0,-force,0,time,-1) );
									
									
									//->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->
								}else if (Settings.forceMethod == 1) {
									int time = 10;
									object_draw.scheduled_forces.add(new force(this,-current_object.xSpeed/mass,current_object.ySpeed/mass,-current_object.zSpeed/mass,time,-1));
									object_draw.scheduled_forces.add(new force(current_object,-xSpeed/current_object.mass,ySpeed/current_object.mass,-zSpeed/current_object.mass,time,-1));
									yReal += 1.1 * ySpeed;
								}else {
									assert Settings.forceMethod == 2;
								
									// speed method
									ySpeed = Math.abs(ySpeed);
									current_object.ySpeed = - Math.abs(current_object.ySpeed);
								
									yReal += 1.1 * ySpeed;
				 
								}
							}
						} 
					
					}
				
			}//end of V1 collison algoritm
			
		}
			return current_object;
				
	}
	
	
	
	
}
