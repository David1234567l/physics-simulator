package physics_simulator;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class physics_object extends Canvas {
	
	public boolean isVisible = true, isTangible = true, isAnchored = false, isFilled = false, affectedByBorder = true,        isRotatable = true;
	//				will the object |will the object	|if it's anchored  | is it an outline | will the object be affected |  can the object be rotated?
	//				be drawn		|collide with others|it  wont move	   | or is it filled? | by a border_bounce?         |   
	
	public String name = "unNamed"; //the name of the object
	protected int x,y,z; 
	protected double xReal,yReal,zReal; //x, y, and z SizeAppearance is the how big the object would appear to be (taking into account how far away it is)

	protected double centerX,centerY, centerZ,xSpeed,ySpeed,zSpeed,xAccel,yAccel,zAccel,xSize, ySize, zSize,xSizeAppearance, ySizeAppearance, zSizeAppearance, axisThetaXY = 0,axisThetaZX = 0, axisThetaZY = 0,mass,friction_coefficient;
	protected double xRotation,yRotation,zRotation,angularVelocityX, angularVelocityY, angularVelocityZ, angularAccelX, angularAccelY, angularAccelZ;
	protected point center;
	
	protected point pointOfRotation; //the point that the object rotates around
	private pointOfRotationPlaces pointOfRotationPlace = pointOfRotationPlaces.center;  //the place that that point is
	public enum pointOfRotationPlaces {center,parentCenter,parentsPlace,custom};
	
	public String drawMethod = "ListedPointAlgorithm"; //the method the drawer (object_draw) will use to render the object
	public point[] points = {}; //all of the points in the object
	protected int[] pointXs = {}; //all of the x coordinates of the points in the object
	protected int[] pointYs = {}; //all of the y coordinates of the points in the object
	protected int[] pointRenderOrder = {}; //the order in which lines will be drawn from point to point (listedPointAlgorithm)
	protected Color color = Color.BLACK; //the color of the object
	
	private physics_object parent_object; //this object will move and act relative to it's parent object (usefull for making complex objects out of multiple shapes)
	private boolean hasParentObject = false; //if the object is linked to a parent object
	
	protected Coordinate_Axis axis;
	
	public enum faces {left,right,top,bottom,far,near,none};
	
	
	
	public void paint(Graphics page) {
		
		if (Settings.displayObjectNames) page.drawString(name,(int) Math.round(points[0].getXReal()), (int) Math.round(points[0].getYReal())); //displaying the name of the object
		
		
		if (Settings.showPointNumbers) {
			point current_point;
			for (int i = 0; i < points.length; i++) {
				current_point = points[i];
				page.drawString("" + i, current_point.getX(), current_point.getY()); //display the point numbers
			}
		}
		
		
		
		
		updatePointXandYs();
		
		if (isFilled) {
			page.fillPolygon(pointXs, pointYs, points.length);
		}else {
			page.drawPolygon(pointXs, pointYs, points.length);
		}
	}

	
	public void checkForCollisions(ArrayList<physics_object> objects) { // calls the checkForCollision method for every object in the objects list
		
		if (isTangible) {
			for (physics_object current_pObject : objects) {
				
				if ( (current_pObject.isTangible) && (isTangible) ) current_pObject.checkForCollision(this,objects);
	
			}	
		}
	}
	
	public Object checkForCollision(physics_object physics_object,ArrayList<physics_object> objects) { //generic checkForCollisions method that is overriden by all tangible pObjects
		return null;
		
	}

	public void Update(ArrayList<physics_object> objects) {	//updates all of the objects values 	
		if (hasParentObject) {
			xSpeed = parent_object.xSpeed;
			ySpeed = parent_object.ySpeed;
			zSpeed = parent_object.zSpeed;
			xAccel = parent_object.xAccel;
			yAccel = parent_object.yAccel;
			zAccel = parent_object.zAccel;
			angularVelocityX = parent_object.angularVelocityX;
			angularVelocityY = parent_object.angularVelocityY;
			angularVelocityZ = parent_object.angularVelocityZ;
			axisThetaXY = parent_object.axisThetaXY;
			axisThetaZX = parent_object.axisThetaZX;
			axisThetaZY = parent_object.axisThetaZY;
			mass = parent_object.mass;
			friction_coefficient = parent_object.friction_coefficient;
			
			//update real pos
			centerX += (xSpeed);
			centerY += (ySpeed);
			centerZ += (zSpeed);
			
			xRotation += angularVelocityX;
			yRotation += angularVelocityY;
			zRotation += angularVelocityZ;
			
			//updating relative values
			updateSize(); //calculate the size of the object based on how far away it is
			updatePos();//update the xReal,yReal,zReal and x,y,z values
			updatePoints();//set the points based on the x and y values and calculate rotation
			updateCenter(); //update the  "center" point
			
			
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
			
		}else {
			if (! isAnchored) { //updating the pos and speed based on the accel
				//update speed
				xSpeed += xAccel;
				ySpeed += yAccel;
				zSpeed += zAccel;
				
				checkForCollisions(objects);
				
				//update real pos
				centerX += xSpeed;
				centerY += ySpeed;
				centerZ += zSpeed;
				
				//updating angular velocity
				angularVelocityX += angularAccelX;
				angularVelocityY += angularAccelY;
				angularVelocityZ += angularAccelZ;
				
				//updating rotation
				xRotation += angularVelocityX;
				yRotation += angularVelocityY;
				zRotation += angularVelocityZ;
				
			}else { //object is anchored and shouldn't move
				xSpeed = 0;
				ySpeed = 0;
				zSpeed = 0;
			}
				
			
				
			//updating relative values
			updateSize(); //calculate the size of the object based on how far away it is
			updatePos();//update the xReal,yReal,zReal and x,y,z values
			updatePoints();//set the points based on the x and y values and calculate rotation
			updateCenter(); //update the  "center" point
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
		}
		
		updatePointXandYs();
	}
	
	
	public void Update(ArrayList<physics_object> objects,double frames) { //frames is the number of frames the object should update (can be a decimal)
		if (hasParentObject) {
			xSpeed = parent_object.xSpeed;
			ySpeed = parent_object.ySpeed;
			zSpeed = parent_object.zSpeed;
			xAccel = parent_object.xAccel;
			yAccel = parent_object.yAccel;
			zAccel = parent_object.zAccel;
			angularVelocityX = parent_object.angularVelocityX;
			angularVelocityY = parent_object.angularVelocityY;
			angularVelocityZ = parent_object.angularVelocityZ;
			axisThetaXY = parent_object.axisThetaXY;
			axisThetaZX = parent_object.axisThetaZX;
			axisThetaZY = parent_object.axisThetaZY;
			mass = parent_object.mass;
			friction_coefficient = parent_object.friction_coefficient;
			
			//update real pos
			centerX += (xSpeed * frames);
			centerY += (ySpeed * frames);
			centerZ += (zSpeed * frames);
			
			//updating angular velocity
			angularVelocityX += (angularAccelX * frames);
			angularVelocityY += (angularAccelY * frames);
			angularVelocityZ += (angularAccelZ * frames);
			
			//updating rotation
			xRotation += (angularVelocityX * frames);
			yRotation += (angularVelocityY * frames);
			zRotation += (angularVelocityZ * frames);
			 
			//updating relative values
			updateSize(); //calculate the size of the object based on how far away it is
			updatePos();//update the xReal,yReal,zReal and x,y,z values
			updatePoints();//set the points based on the x and y values and calculate rotation
			updateCenter(); //update the  "center" point
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
			
		}else {
			if (! isAnchored) { //updating the pos and speed based on the accel
				//update speed
				xSpeed += (xAccel * frames);
				ySpeed += (yAccel * frames);
				zSpeed += (zAccel * frames);
				
				checkForCollisions(objects);
				
				//update real pos
				centerX += (xSpeed * frames);
				centerY += (ySpeed * frames);
				centerZ += (zSpeed * frames);
				
				//updating angular velocity
				angularVelocityX += (angularAccelX * frames);
				angularVelocityY += (angularAccelY * frames);
				angularVelocityZ += (angularAccelZ * frames);
				
				//updating rotation
				xRotation += (angularVelocityX * frames);
				yRotation += (angularVelocityY * frames);
				zRotation += (angularVelocityZ * frames);
				
			}else { //object is anchored and shouldn't move
				xSpeed = 0;
				ySpeed = 0;
				zSpeed = 0;
			}
			
				
			//updating relative values
			updateSize(); //calculate the size of the object based on how far away it is
			updatePos();//update the xReal,yReal,zReal and x,y,z values
			updatePoints();//set the points based on the x and y values and calculate rotation
			updateCenter(); //update the  "center" point
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
		}
		
		updatePointXandYs();
	}
	
	public void UpdateWithoutCollision(ArrayList<physics_object> objects) { //updates all of the objects values without checking for collisions
		
		if (hasParentObject) {
			xSpeed = parent_object.xSpeed;
			ySpeed = parent_object.ySpeed;
			zSpeed = parent_object.zSpeed;
			xAccel = parent_object.xAccel;
			yAccel = parent_object.yAccel;
			zAccel = parent_object.zAccel;
			angularVelocityX = parent_object.angularVelocityX;
			angularVelocityY = parent_object.angularVelocityY;
			angularVelocityZ = parent_object.angularVelocityZ;
			axisThetaXY = parent_object.axisThetaXY;
			axisThetaZX = parent_object.axisThetaZX;
			axisThetaZY = parent_object.axisThetaZY;
			mass = parent_object.mass;
			friction_coefficient = parent_object.friction_coefficient;
			
			//update real pos
			centerX += (xSpeed);
			centerY += (ySpeed);
			centerZ += (zSpeed);
			
			//updating relative values
			updateSize(); //calculate the size of the object based on how far away it is
			updatePos();//update the xReal,yReal,zReal and x,y,z values
			updatePoints();//set the points based on the x and y values and calculate rotation
			updateCenter(); //update the  "center" point
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
			
		}else {
			if (! isAnchored) { //updating the pos and speed based on the accel
				//update speed
				xSpeed += xAccel;
				ySpeed += yAccel;
				zSpeed += zAccel;
				
				//update real pos
				centerX += xSpeed;
				centerY += ySpeed;
				centerZ += zSpeed;
				
				//updating angular velocity
				angularVelocityX += angularAccelX;
				angularVelocityY += angularAccelY;
				angularVelocityZ += angularAccelZ;
				
				//updating rotation
				xRotation += angularVelocityX;
				yRotation += angularVelocityY;
				zRotation += angularVelocityZ;
				
			}else { //object is anchored and shouldn't move
				xSpeed = 0;
				ySpeed = 0;
				zSpeed = 0;
			}
				
			//updating relative values
			updateSize(); //calculate the size of the object based on how far away it is
			updatePos();//update the xReal,yReal,zReal and x,y,z values
			updatePoints();//set the points based on the x and y values and calculate rotation
			updateCenter(); //update the  "center" point
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
		}
		
		updatePointXandYs();
	}
	
	protected void UpdateWithoutCollision(ArrayList<physics_object> objects, double frames) { //updates all of the objects values without checking for collisions
		//frame fraction is the number of frames we should update by (including by a fraction of a frame) for instance if you only want to update the object for half a frame of time, frames would be 0.5
		
		if (hasParentObject) {
			xSpeed = parent_object.xSpeed;
			ySpeed = parent_object.ySpeed;
			zSpeed = parent_object.zSpeed;
			xAccel = parent_object.xAccel;
			yAccel = parent_object.yAccel;
			zAccel = parent_object.zAccel;
			angularVelocityX = parent_object.angularVelocityX;
			angularVelocityY = parent_object.angularVelocityY;
			angularVelocityZ = parent_object.angularVelocityZ;
			axisThetaXY = parent_object.axisThetaXY;
			axisThetaZX = parent_object.axisThetaZX;
			axisThetaZY = parent_object.axisThetaZY;
			mass = parent_object.mass;
			friction_coefficient = parent_object.friction_coefficient;
			
			//update real pos
			xReal += (xSpeed * frames);
			yReal += (ySpeed * frames);
			zReal += (zSpeed * frames);
			
			//updating relative values
			updatePoints(); //does the whole rotation thing and updates the positions of the point
			updatePos();//update int values of pos
			updateCenter(); //calculates where the center of the object is
			updateSize(); //calculates the size of the object
			
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}
			
		}else {
			if (! isAnchored) { //updating the pos and speed based on the accel
				//update speed
				xSpeed += (xAccel*frames);
				ySpeed += (yAccel*frames);
				zSpeed += (zAccel*frames);
				
				//update real pos
				xReal += (xSpeed*frames);
				yReal += (ySpeed*frames);
				zReal += (zSpeed*frames);
				
				//update int values of pos
				updatePos();
				
				//updating angular velocity
				angularVelocityX += (angularAccelX * frames);
				angularVelocityY += (angularAccelY * frames);
				angularVelocityZ += (angularAccelZ * frames);
				
				//updating rotation
				xRotation += (angularVelocityX * frames);
				yRotation += (angularVelocityY * frames);
				zRotation += (angularVelocityZ * frames);
				
				
			}else { //object is anchored and shouldn't move
				xSpeed = 0;
				ySpeed = 0;
				zSpeed = 0;
			}
		

			updatePos();	//update int values of pos
			updatePoints();		 //does the whole rotation thing and updates the positions of the point
			updateCenter();//calculates where the center of the object is
			updateSize();//calculates the size of the object
			
			try {
				axis.UpdateAxis();
			}catch(NullPointerException e) {}			
		}
		
		updatePointXandYs();
	}
	
	public void applyForce(double r, double theta, double z_magn) { //theta is an angle from the eastward direction
		zAccel += z_magn/mass; //updating the zAccel with the z component of the force vector
		
		//calculating the components of the force vectors
		double xComponent = r * Math.cos(theta);
		double yComponent = r * Math.sin(theta);
		
		//updating the x and y Accel with the respective x and y components of the force vector
		xAccel += xComponent/mass; 
		yAccel += yComponent/mass;
	
	}
	
	public void applyForce(double r, double theta, double z_magn, double time) { //applies a force to the object
		zAccel += z_magn/mass; //updating the zAccel with the z component of the force vector
		
		//calculating the components of the force vectors
		double xComponent = r * Math.cos(theta);
		double yComponent = r * Math.sin(theta);
		
		//updating the x and y Accel with the respective x and y components of the force vector
		xAccel += xComponent/mass; 
		yAccel += yComponent/mass;
		object_draw.objects.add(new ForceTimer(time,xComponent,yComponent,z_magn,this));
	
	}
	
	public void applyComponentForce(double x_magn, double y_magn, double z_magn) {
		xAccel += x_magn/mass; //updating the xAccel with the x component of the force vector
		yAccel += y_magn/mass; //updating the yAccel with the y component of the force vector
		zAccel += z_magn/mass; //updating the zAccel with the z component of the force vector
	}
	
	public void applyComponentForce(double x_magn, double y_magn, double z_magn, double time) {
		xAccel += x_magn/mass; //updating the xAccel with the x component of the force vector
		yAccel += y_magn/mass; //updating the yAccel with the y component of the force vector
		zAccel += z_magn/mass; //updating the zAccel with the z component of the force vector
		object_draw.objects.add(new ForceTimer(time,x_magn,y_magn,z_magn,this));
	}
	
	public double[] getTrajectory() { //returns a polar representation of the velocity of the object
		double r = Math.sqrt(Math.pow(xSpeed,2) + Math.pow(ySpeed,2) + Math.pow(zSpeed, 2) ); //three-dimensional pythagorean theorem
		double xyTheta = Math.atan(ySpeed/xSpeed);
		double zxTheta = Math.atan(zSpeed/xSpeed);
		double[] trajPolar = {r,xyTheta,zxTheta};
		return trajPolar;
	}
	
	public double[] calculateDeflectionAngle(double angleOfApproach,double zComponent) { //this should prob get overriden
		double angleOfReflection = 180-angleOfApproach;
		return new double[] {angleOfReflection,zComponent};
		
	}
	
	protected double[] calculateDeflectionAngle(physics_object current_object) { //this shouldn't have to get overridden
		double angleOfApproach = Math.atan(current_object.ySpeed / current_object.xSpeed);
		return calculateDeflectionAngle(angleOfApproach,current_object.zSpeed);
		
	}
	
	protected void calculatePointValues() {  //this method has BIGG issues
		Vector tempVec;
		for (point cPoint : points) {
		
			try {
				cPoint.setR(distance(cPoint,pointOfRotation));
			}catch(NullPointerException n) { //this will be caught if pointOfRotation doesn't exist yet.
				pointOfRotation = new point(centerX,centerY,centerZ); //create pointOfRotation and set it to the center of the object using the default method
				cPoint.setR(distance(cPoint,pointOfRotation));
			}
		
			
			tempVec = new Vector(pointOfRotation,cPoint);
			cPoint.setAngle(tempVec.getThetaXY(), tempVec.getThetaZX(), tempVec.getThetaZY());
		}
	}
	
	public double distance(point point1, point point2) {
		return Math.sqrt( Math.pow(( point2.getXReal() - point1.getXReal() ), 2) + Math.pow(( point2.getYReal() - point1.getYReal() ), 2) + Math.pow(( point2.getZReal() - point1.getZReal() ), 2) );
	}


	public void isCollided(physics_object object, faces side) { //method that gets called when the object hits something. Useful for things like spikes or bullets in a game
		
	}
	
	public void setPos(double xReal1,double yReal1,double zReal1) {
		xReal = xReal1;
		yReal = yReal1;
		zReal = zReal1;
		x = (int) Math.round(xReal);
		y = (int) Math.round(yReal);
		z = (int) Math.round(zReal);
		updateCenter();
	}
	
	public void setPoints(point[] points1) {
		points = points1;
	}
	
	public void setSpeed(double xSpeed1, double ySpeed1, double zSpeed1) { //sets the speed of the object
		xSpeed = xSpeed1;
		ySpeed = ySpeed1;
		zSpeed = zSpeed1;
	}
	
	public void setAccel(double xAccel1, double yAccel1, double zAccel1) { //sets the acceleration of the object
		xAccel = xAccel1;
		yAccel = yAccel1;
		zAccel = zAccel1;
	}
	
	public void setAngularVelocity(double angVX, double angVY, double angVZ) {
		angularVelocityX = angVX;
		angularVelocityY = angVY;
		angularVelocityZ = angVZ;
	}
	
	public void setAngularAccel(double angAccelX, double angAccelY, double angAccelZ) {
		angularAccelX = angAccelX;
		angularAccelY = angAccelY;
		angularAccelZ = angAccelZ;
	}
	
	public void setSize(double xSize1,double ySize1,double zSize1) { //sets the size of the object
		xSize = xSize1;
		ySize = ySize1;
		zSize = zSize1;
		updateCenter();
		updateSize();
	}
	
	public void setMass(double mass1) { //update the mass of the object (kg)
		mass = mass1;
	}
	
	public void setName(String new_name, int i) { //sets the name of the object (this will be shown if Settings.displayNames is true)
		setName(new_name); //updates the name of the component
		name = new_name;
		
		try {
			axis.setName(name + "_axis", i); //updates the name of this object's coordinate axis (if it has one)
		}catch(NullPointerException e) {}	
	}
	
	public void setParentObject(physics_object newParent) { //links this object to the object passed into this method (by setting it as the parent_object)
		parent_object = newParent;
		hasParentObject = true;
	}
	
	public void setRotation(double xRotation1, double yRotation1, double zRotation1) { //this is not a wise method to use as it frequently results in impossible rotations.
		xRotation = xRotation1;
		yRotation = yRotation1;
		zRotation = zRotation1;
	}
	
	public void setPointOfRotation(point newPointOfRotation) {
		pointOfRotation = newPointOfRotation;
		
		pointOfRotationPlace = pointOfRotationPlaces.custom;
		
		calculatePointValues();
		
		
		
	}
	
	public void setPointOfRotationPlace(pointOfRotationPlaces newPlace) {
		pointOfRotationPlace = newPlace;

		updatePointOfRotation();
	}
	
	public void updatePointOfRotation() { //this doesn't need to be updated every time the pointOfRotation changes because the pointOfRotation is an alias of the original point. (unless it's a custom pointOfRotation)
		if (pointOfRotationPlace.equals(pointOfRotationPlaces.center)) {
			pointOfRotation = center;
		}else if(pointOfRotationPlace.equals(pointOfRotationPlaces.parentCenter)) {
			pointOfRotation = parent_object.center;
		}else if(pointOfRotationPlace.equals(pointOfRotationPlaces.parentsPlace)) {
			pointOfRotation = parent_object.pointOfRotation;
		}
		
		
	}
	
	public void rotateX(double rotation) {
		
	}
	
	public void rotateY(double rotation) {
		
	}

	public void rotateZ(double rotation) {
	
	}
	
	public void rotate(double xRadians, double yRadians, double zRadians, String order) {
		
	}


	public void setColor(Color color1) {
		color = color1;
	}
	
	
	protected void updatePoints() {
		
		if ((Settings.rotationAlgorithm == 2) && (isRotatable)) { //rotates the axis, then draws each point with respect to the axis
			
			double r;
			updateCenter();
			//creating a vector from the center of the object to each point
			Vector pointVectorX,pointVectorY,pointVectorZ,pointVector = new Vector(0,0,0);
			Vector[] pointVectors = new Vector[3];
			for (int i = 0; i < points.length ; i++) {
				r =Math.sqrt(Math.pow(xSize,2) + Math.pow(ySize,2) + Math.pow(zSize,2))/2;
				pointVectorX = new Vector( r , axis.xAxis.axisVector.getThetaXY() + points[i].getThetaXY() , axis.xAxis.axisVector.getThetaZX() + points[i].getThetaZX(), axis.xAxis.axisVector.getThetaZY() + points[i].getThetaZY(),"thetaZX",this);
				pointVectorY = new Vector( r , axis.yAxis.axisVector.getThetaXY() + points[i].getThetaXY() , axis.yAxis.axisVector.getThetaZX() + points[i].getThetaZX(), axis.yAxis.axisVector.getThetaZY() + points[i].getThetaZY(),"thetaZY",this);
				pointVectorZ = new Vector( r , axis.zAxis.axisVector.getThetaXY() + points[i].getThetaXY() , axis.zAxis.axisVector.getThetaZX() + points[i].getThetaZX(), axis.zAxis.axisVector.getThetaZY() + points[i].getThetaZY(),"thetaZX",this);
				pointVectors[0] = pointVectorX;
				pointVectors[1] = pointVectorY;
				pointVectors[2] = pointVectorZ;
				for (Vector C_PVector : pointVectors) {
			//		C_PVector.setPos(centerX, centerY, centerZ);
					C_PVector.setPos(xReal, yReal, zReal);
				}
				Vector pointVectorSum = pointVector.vectorAdd(pointVectors);		
				
				points[i].setPointVector(pointVectorSum); //sets the vector AND updates the point's pos automatically
			}
			
			setPoints(points);
		}else if ( ( Settings.rotationAlgorithm == 3) && (isRotatable) ){
			

		
			double r;
			
			//creating a vector from the pointOfRotation to each point in the object
			Vector pointVector;
			point cPoint;
			 
			for (int i = 0; i < points.length ; i++) {
				cPoint = points[i];
				r = points[i].getR();
				
				pointVector = new Vector(r, cPoint.getThetaXY() + zRotation, cPoint.getThetaZX() + yRotation, cPoint.getThetaZY() + xRotation,"thetaZY",this);
				
				pointVector.setPos(pointOfRotation.getXReal(), pointOfRotation.getYReal(), pointOfRotation.getZReal());			
						
				points[i].setPointVector(pointVector); //sets the vector AND updates the point's pos automatically
				
			}
		
			
		}else {
			System.out.println("updatePoints method not overidden for physObject: " + toString() + "with name of " + name);
			System.out.println("(or you're not on the correct rotation algorithm)");
		}
		
		
		
	}
	
	private void updatePointXandYs() {
		pointXs = new int[points.length];
		pointYs = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			pointXs[i] = points[i].getX();
			pointYs[i] = points[i].getY();
		}
	}
	
	protected void calculateCenter() {
		
		 //vector method
		/*
		try {
			System.out.println(name + ">>" + points[0].getR());
			
			Vector centerVector = new Vector(points[0].getR(),Math.PI + zRotation + points[0].getThetaXY(),Math.PI + yRotation + points[0].getThetaZX(),Math.PI + xRotation + points[0].getThetaZY(),"thetaZY",this);
			centerVector.setPos(points[0].getXReal(), points[0].getYReal(), points[0].getZReal());
			
			centerX = centerVector.getVectorTip().getXReal();
			centerY = centerVector.getVectorTip().getYReal();
			centerZ = centerVector.getVectorTip().getZReal();
		}catch (ArrayIndexOutOfBoundsException a) {
			centerX = xReal + xSize/2;
			centerY = yReal + ySize/2;
			centerZ = zReal + zSize/2;
		}
		*/
		
		//center of mass by point coordinate averaging method
			//this method of finding the center uses physics instead of geometry to find the center. It attempts to estimate the center of mass of the object by using the points as an estimate to where mass of the object is.
			
		try {
			double temp = points[0].getXReal(); //this will throw an error and trigger the catch statement if there are no points
			
		System.out.println(">>>");
		System.out.println(name);
			// the sums of all the x,y,and z coordinates of the points
			double totalX = 0;
			double totalY = 0;
			double totalZ = 0;
			
			for (point cPoint : points) { //loop through the points and add their coordinates to the totals
				System.out.println(cPoint.getXReal() + "," + cPoint.getYReal());
				totalX += cPoint.getXReal();
				totalY += cPoint.getYReal();
				totalZ += cPoint.getZReal();
			}
			double centerXX = totalX/points.length;
			double centerYY = totalY/points.length;
			System.out.println("cenXX: " + centerXX);
			System.out.println("cenYY: " + centerYY);
			
			System.out.println("<<<");
			//divide by the number of points to get the average
			centerX = totalX / points.length;
			centerY = totalY / points.length;
			centerZ = totalZ / points.length;
			
			System.out.println(">>");
			
			System.out.println(centerX + "," + centerY + "," + centerZ);
			System.out.println("<<");
			
			
			
		}catch (ArrayIndexOutOfBoundsException a) { //if the object doesn't have a points list, use the default method of finding the center
			System.out.println(name + " has no points");
			centerX = xReal + xSize/2;
			centerY = yReal + ySize/2;
			centerZ = zReal + zSize/2;
		}
		
		
		
		try {
			center.setPos(centerX, centerY,centerZ);
		}catch(NullPointerException n) {
			center = new point(centerX,centerY,centerZ);
		}
		
	}
	
	protected void updateCenter() {
		try {
			center.setPos(centerX, centerY,centerZ);
		}catch(NullPointerException n) {
			center = new point(centerX,centerY,centerZ);
		}
	}
	
	protected void updatePos() {
		xReal = centerX - xSize/2;
		yReal = centerY - ySize/2;
		zReal = centerZ - zSize/2;
		x = (int) Math.round(xReal);
		y = (int) Math.round(yReal);
		z = (int) Math.round(zReal);
	}
	
	public String getObjectName() { //gets the name of the object
		return name;
	}


	protected void updateSize() {
		//as z gets bigger, the object gets further away from the viewer, and the object appears to be smaller
		xSizeAppearance = (Settings.distanceFromScreen * xSize ) / (zReal + Settings.distanceFromScreen);
		ySizeAppearance = (Settings.distanceFromScreen * ySize ) / (zReal + Settings.distanceFromScreen);
		zSizeAppearance = (Settings.distanceFromScreen * zSize ) / (zReal + Settings.distanceFromScreen);		
	}
	
	
	//Getter methods
	public point[] getPoints() {
		return points;
	}
	
	public double getCenterX() { //finds the x coordinate of the object's center
		return centerX;
	}


	public double getCenterY() { //finds the y coordinate of the object's center
		return centerY;
	}


	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public double getXReal() {
		return xReal;
	}
	
	public double getYReal() {
		return yReal;
	}
	
	public double getZReal() {
		return zReal;
	}
	
	public double getXSize() {
		return xSize;
	}
	
	public double getYSize() {
		return ySize;
	}

	public double getZSize() {
		return zSize;
	}
	
	public Color getColor() {
		return color;
	}
	
	public double getAxisThetaXY() {
		return axisThetaXY;
	}
	
	public double getAxisThetaZX() {
		return axisThetaZX;
	}
	
	public double getAxisThetaZY() {
		return axisThetaZY;
	}
	
	public double getXRotation() {
		return xRotation;
	}
	
	public double getYRotation() {
		return yRotation;
	}
	
	public double getZRotation() {
		return zRotation;
	}
	
	
	public physics_object copy(physics_object new_object)  { //copies this physics_object to another
		//this method exists because you cannot do object1 = object2 because that will just tell object1 to point to object2 and not create a new object. (Aliasing)
	
		//booleans
		new_object.isVisible = isVisible;
		new_object.isTangible = isTangible;
		new_object.isAnchored = isAnchored;
		new_object.isFilled = isFilled;
		new_object.affectedByBorder = affectedByBorder;
		new_object.hasParentObject = hasParentObject;
		
		//ints
		new_object.x = x;
		new_object.y = y;
		new_object.xSize = xSize;
		new_object.ySize = ySize;
		
		
		//doubles
		new_object.xReal = xReal;
		new_object.yReal = yReal;
		new_object.zReal = zReal;
		new_object.centerX = centerX;
		new_object.centerY = centerY;
		new_object.centerZ = centerZ;
		new_object.xSpeed = xSpeed;
		new_object.ySpeed = ySpeed;
		new_object.zSpeed = zSpeed;
		new_object.xAccel = xAccel;
		new_object.yAccel = yAccel;
		new_object.zAccel = zAccel;
		new_object.xRotation = xRotation;
		new_object.yRotation = yRotation;
		new_object.zRotation = zRotation;
		new_object.axisThetaXY = axisThetaXY;
		new_object.axisThetaZX = axisThetaZX;
		new_object.axisThetaZY = axisThetaZY;
		new_object.mass = mass;
		new_object.friction_coefficient = friction_coefficient;
		new_object.xSizeAppearance = xSizeAppearance;
		new_object.ySizeAppearance = ySizeAppearance;
		new_object.zSizeAppearance = zSizeAppearance;
		
		
		//strings
		new_object.name = name;
		
		//other
		new_object.drawMethod = drawMethod;
		new_object.axis = axis;
		new_object.parent_object = parent_object;
		
		//this for loop probably doesn't work as it hasn't been tested yet
		for (int i=0; i < points.length; i++) {
			point new_point = new point(null, i);
			new_point = (point) points[i].copy(new_object);
			new_object.points[i] = new_point;
		}
		
		
		new_object.pointRenderOrder = pointRenderOrder;
		new_object.color = color;
		
		return new_object;
	}

}

