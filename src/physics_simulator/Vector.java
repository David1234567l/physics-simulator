package physics_simulator;

import java.awt.Graphics;

public class Vector extends physics_object{

	protected double r,xComponent,yComponent,zComponent;
	private double thetaXY, thetaZX, thetaZY;
	private String valueAngle;
	private point vectorTip = new point(xReal,yReal,zReal);;
	private int quadrant = -1;
	private physics_object parent;
	
	public Vector(double xComponent1, double yComponent1, double zComponent1) {
		xComponent = xComponent1;
		yComponent = yComponent1;
		zComponent = zComponent1;

		calculateThetas();
		
		updatePos();
		drawMethod = "paint";
		updatePoints();
		
	}
	
	public Vector (point point1, point point2) {
		setPos(point1.getXReal(), point1.getYReal(), point1.getZReal());
		vectorTip.setPos(point2.getXReal(), point2.getYReal(), point2.getZReal());
		
		xComponent = point2.getXReal() - point1.getXReal();
		yComponent = point2.getYReal() - point1.getYReal();
		zComponent = point2.getZReal() - point1.getZReal();
		
		calculateThetas();
	}
	
	public Vector (point point1, point point2,physics_object parent1) {
		parent = parent1;
		setPos(point1.getXReal(), point1.getYReal(), point1.getZReal());
		vectorTip.setPos(point2.getXReal(), point2.getYReal(), point2.getZReal());
		
		xComponent = point2.getXReal() - point1.getXReal();
		yComponent = point2.getYReal() - point1.getYReal();
		zComponent = point2.getZReal() - point1.getZReal();
		
		calculateThetas();
	}
	
	public Vector(double r1, double theta, double zComponent1, boolean isPolar) { //old angle system
		assert isPolar == true;
		r = r1;
		xComponent = r * Math.cos(theta);
		yComponent = r * Math.sin(theta);
		zComponent = zComponent1;
		updatePos();
		drawMethod = "paint";
		updatePoints();
		
	}
	
	public Vector(double r1, double thetaXY1, double thetaZX1, double thetaZY1, String valueAngle1)  {
		valueAngle = valueAngle1;
		if (!( (valueAngle == "thetaZX") || (valueAngle == "thetaZY") ) ) {
			
			try {
				throw new NoSuchFieldException("valueAngle is which z angle you want the engine to use to calculate the components. It must be either \"thetaZX\" or \"thetaZY\". \n valueAngle is currently: " + valueAngle + ". Object: " + toString());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
	
		}
		
		thetaXY = thetaXY1;
		thetaZX = thetaZX1;
		thetaZY = thetaZY1;
		r = r1;
		setPos(0,0,0);
		calculateComponents();		
		updatePos();
		drawMethod = "paint";
		
	}

	public Vector(double r1, double thetaXY1, double thetaZX1, double thetaZY1, String valueAngle1, physics_object parent1)  {
		valueAngle = valueAngle1;
		parent = parent1;
		if (!( (valueAngle == "thetaZX") || (valueAngle == "thetaZY") ) ) {
			
			try {
				throw new NoSuchFieldException("valueAngle is which z angle you want the engine to use to calculate the components. It must be either \"thetaZX\" or \"thetaZY\". \n valueAngle is currently: " + valueAngle + ". Object: " + toString());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
	
		}
		
		thetaXY = thetaXY1;
		thetaZX = thetaZX1;
		thetaZY = thetaZY1;
		r = r1;
		setPos(0,0,0);
		calculateComponents();		
		updatePos();
		drawMethod = "paint";
		
	}

	
	public void UpdateVector(double x, double y, double z, double r1, double thetaXY1, double thetaZX1, double thetaZY1, String valueAngle1)  {
		valueAngle = valueAngle1;
		if (!( (valueAngle == "thetaZX") || (valueAngle == "thetaZY") ) ) {
			
			try {
				throw new NoSuchFieldException("valueAngle is which z angle you want the engine to use to calculate the components. It must be either \"thetaZX\" or \"thetaZY\". \n valueAngle is currently: " + valueAngle + ". Object: " + toString());
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
	
		}
		
		thetaXY = thetaXY1;
		thetaZX = thetaZX1;
		thetaZY = thetaZY1;
		r = r1;
		setPos(x,y,z);
		calculateComponents();		
		
		drawMethod = "paint";
		
	}
	
	public Vector vectorAdd(Vector cVector)  { //adds two vectors together
		return (new Vector(xComponent + cVector.getXComponent(),yComponent + cVector.getYComponent(), zComponent + cVector.getZComponent()));
	}

	public Vector vectorAdd(Vector[] vectors) { //adds together a list of vectors
		Vector resultant = new Vector(0,0,0);	
		for (Vector cVector : vectors) resultant.setComponents(resultant.xComponent + cVector.getXComponent(),resultant.yComponent + cVector.getYComponent(), resultant.zComponent + cVector.getZComponent());
		resultant.calculateThetas();
		resultant.setPos(vectors[0].xReal, vectors[0].yReal, vectors[0].zReal);
		resultant.updatePoints();
		return resultant;
	}
	
	public void setPos(double xReal1, double yReal1, double zReal1 ) {
		xReal = xReal1;
		yReal = yReal1;
		zReal = zReal1;
		x = (int) Math.round(xReal);
		y = (int) Math.round(yReal);
		z = (int) Math.round(zReal);
		updatePoints();
	}
	
	public void setComponents(double xComponent1, double yComponent1, double zComponent1) {
		xComponent = xComponent1;
		yComponent = yComponent1;
		zComponent = zComponent1;
		calculateThetas();
		updatePos();
		updatePoints();
	}
	
	private void findQuadrant() {
		
		int[] quadrantCounter = new int[8];
		
		reduceThetas();
	
	//thetaXY
		//finding quadrantXY 1 and 2
		int quadrantXY1 = (int) (thetaXY/(Math.PI/2) % 10 + 1);
		int quadrantXY2 = quadrantXY1 + 4;
		
		//adding a tally to the correct quadrant slots (they are offset by -1 so quadrant 1 would be slot 0)
		quadrantCounter[quadrantXY1-1]++;
		quadrantCounter[quadrantXY2-1]++;
		
//doing the same thing as above but it's a little more complicated because the ZX and ZY quadrant conversion is more complicated than XY and can't use an equation (at least to my knowledge)
	
	//thetaZX
		if (valueAngle == "thetaZX") {
			
			if ((0 < thetaZX) && (thetaZX < (Math.PI/2))) {
				quadrantCounter[4]++;
				quadrantCounter[7]++;
			}else if (((Math.PI/2) < thetaZX) && (thetaZX < (Math.PI))) {
				quadrantCounter[5]++;
				quadrantCounter[6]++;
			}else if (((Math.PI) < thetaZX) && (thetaZX < (3*Math.PI/2))) {
				quadrantCounter[1]++;
				quadrantCounter[2]++;
			}else if (((3*Math.PI/2) < thetaZX) && (thetaZX < (2*Math.PI))) {
				quadrantCounter[0]++;
				quadrantCounter[3]++;
			}else if ( (Math.abs(thetaZX) < 0.00001) || (Math.abs(thetaZX - (Math.PI/2)) < 0.00001) || (Math.abs(thetaZX - (Math.PI)) < 0.00001) || (Math.abs(thetaZX - (3*Math.PI/2)) < 0.00001) || (Math.abs(thetaZX - (2*Math.PI)) < 0.00001) ) {
				for (int i = 0; i < quadrantCounter.length; i++) {
					quadrantCounter[i]++;
				}
			} else {
				
				Exception e = new Exception("findQuadrant logic error (ZX)");
				e.printStackTrace();
				System.out.println("thetaZX: " + thetaZX);
			
			}
			
			
	//thetaZY
		}else if (valueAngle == "thetaZY") {
			
			if ((0 < thetaZY) && (thetaZY < (Math.PI/2))) {
				quadrantCounter[4]++;
				quadrantCounter[5]++;
			}else if (((Math.PI/2) < thetaZY) && (thetaZY < (Math.PI))) {
				quadrantCounter[0]++;
				quadrantCounter[1]++;
			}else if (((Math.PI) < thetaZY) && (thetaZY < (3*Math.PI/2))) {
				quadrantCounter[2]++;
				quadrantCounter[3]++;
			}else if (((3*Math.PI/2) < thetaZY) && (thetaZY < (2*Math.PI))) {
				quadrantCounter[6]++;
				quadrantCounter[7]++;
			}else if ( (Math.abs(thetaZY) < 0.00001) || (Math.abs(thetaZY - (Math.PI/2)) < 0.00001) || (Math.abs(thetaZY - (Math.PI)) < 0.00001) || (Math.abs(thetaZY - (3*Math.PI/2)) < 0.00001) || (Math.abs(thetaZY - (2*Math.PI)) < 0.00001) ) {
				for (int i = 0; i < quadrantCounter.length; i++) {
					quadrantCounter[i]++;
				}
			}else {
				
					Exception e = new Exception("findQuadrant logic error (ZY)");
					e.printStackTrace();
					
					System.out.println("thetaZY: " + thetaZY);
				
			}
		}
		
	
		
		//checking if the vector is on the axis
		if (Math.abs(xComponent) < 0.001) {
			quadrantCounter[0]++;
			quadrantCounter[3]++;
			quadrantCounter[4]++;
			quadrantCounter[7]++;
		}
		if (Math.abs(yComponent) < 0.001)  {
			quadrantCounter[0]++;
			quadrantCounter[1]++;
			quadrantCounter[4]++;
			quadrantCounter[5]++;
		}
		if (Math.abs(zComponent) < 0.001)  {
			quadrantCounter[0]++;
			quadrantCounter[1]++;
			quadrantCounter[2]++;
			quadrantCounter[3]++;			
		}
		
		
	
		
		for (int i = 0; i < 8; i++) {
			if (quadrantCounter[i] >= 2) {
				quadrant = i;
			}
		}
		
		if (quadrant == -1) {
			Exception e = new Exception("no quadrant found");
			e.printStackTrace();
			
			System.out.print("parent-object name (if applicable): "); 
			try {
				System.out.println(parent.name);
			}catch(NullPointerException n) {
				System.out.println("no parent object was found");
			}
			System.out.println("thetaXY: " + thetaXY);
			System.out.println("thetaZX: " + thetaZX);
			System.out.println("thetaZY: " + thetaZY);
			System.out.println("valueangle: " + valueAngle);
			
			System.out.println("quadCounter:");
			
			System.out.print("[");
			for (int i : quadrantCounter) {
				System.out.print(i + ",");
			}
			System.out.println("]");
			
			System.out.println("xComp: " + xComponent);
			System.out.println("yComp: " + yComponent);
			System.out.println("zComp: " + zComponent);
		}
		
	
	}
	
	public void findQuadComp() {
		
		if ( (xComponent > 0) && (yComponent > 0) && (zComponent > 0) ) {
			quadrant = 4;
		}else if (! ( (xComponent > 0) || (yComponent > 0) || (zComponent > 0) ) ) {
			quadrant = 2;
		}else {
			int[] quadrantCounter = new int[8];
			
			if (xComponent > 0) {
				quadrantCounter[0]++;
				quadrantCounter[3]++;
				quadrantCounter[7]++;
			}
			if (yComponent > 0) {
				quadrantCounter[0]++;
				quadrantCounter[1]++;
				quadrantCounter[5]++;
			}
			if (zComponent > 0) {
				quadrantCounter[5]++;
				quadrantCounter[6]++;
				quadrantCounter[7]++;
			}
	
			
			//checking if the vector is on the axis
			if (Math.abs(xComponent) < 0.001) {
				quadrantCounter[0]++;
				quadrantCounter[3]++;
				quadrantCounter[4]++;
				quadrantCounter[7]++;
			}
			if (Math.abs(yComponent) < 0.001)  {
				quadrantCounter[0]++;
				quadrantCounter[1]++;
				quadrantCounter[4]++;
				quadrantCounter[5]++;
			}
			if (Math.abs(zComponent) < 0.001)  {
				quadrantCounter[0]++;
				quadrantCounter[1]++;
				quadrantCounter[2]++;
				quadrantCounter[3]++;			
			}
			
			for (int i = 0; i < 8; i++) {
				if (quadrantCounter[i] >= 2) {
					quadrant = i;
				}
			}
			
			if (quadrant == -1) {	
				if (zComponent > 0) {
					quadrant = 6;
				}else {
					Exception e = new Exception("no quadrant found");
					e.printStackTrace();
					
					System.out.print("parent-object name (if applicable): "); 
					try {
						System.out.println(parent.name);
					}catch(NullPointerException n) {
						System.out.println("no parent object was found");
					}
					System.out.println("thetaXY: " + thetaXY);
					System.out.println("thetaZX: " + thetaZX);
					System.out.println("thetaZY: " + thetaZY);
					System.out.println("valueangle: " + valueAngle);
					
					System.out.println("quadCounter:");
					
					System.out.print("[");
					for (int i : quadrantCounter) {
						System.out.print(i + ",");
					}
					System.out.println("]");
					
					System.out.println("xComp: " + xComponent);
					System.out.println("yComp: " + yComponent);
					System.out.println("zComp: " + zComponent);
				}
			}
		}
		
	
	}
	
	private void calculateComponents() { //calculating the x,y,and z components of the vector using the angles
		
		reduceThetas();
		
		if (valueAngle == "thetaZX") {
			
			
			if ((thetaXY == 0) || (thetaXY == Math.PI)) {
				yComponent = 0;
			}else {
				yComponent = Math.sqrt( 
						(Math.pow(r, 2) * Math.pow(Math.sin(thetaXY),2) * Math.pow(Math.cos(thetaZX),2)) /
						(1 - Math.pow(Math.sin(thetaXY), 2) * Math.pow(Math.sin(thetaZX),2)));
			}
			
			if (thetaZX == 0 || thetaZX == Math.PI) {
				zComponent = 0;
			}else {
				zComponent = Math.sin(thetaZX) * Math.sqrt(Math.pow(r, 2) - Math.pow(yComponent,2));
			}
			
			if ( (thetaXY == (Math.PI/2)) || (thetaXY == 3*Math.PI/2) ) {
				xComponent = 0;
			}else {
				xComponent = Math.sqrt(Math.pow(r, 2) - Math.pow(yComponent, 2) - Math.pow(zComponent, 2) );
			}
			
			if (zComponent == 0) { //atan won't work if zComponent is 0 (dividing by zero!)
				thetaZY = Math.PI/2; //this is always true if zComp is 0 (think about it!)
			}else {
				thetaZY = Math.atan(yComponent/zComponent);
			}
	
			
		}else {
			assert valueAngle == "thetaZY";
			
			if ( (thetaXY == (Math.PI/2)) || (thetaXY == 3*Math.PI/2) ) {
				xComponent = 0;
			}else {
				xComponent = Math.sqrt( 
					(Math.pow(r, 2) * Math.pow(Math.cos(thetaXY),2) * Math.pow(Math.sin(thetaZY),2)) /
					(1 + Math.pow(Math.sin(thetaXY), 2) * Math.pow(Math.sin(thetaZY),2)));
			}
			
			if (thetaZY == 0) {
				zComponent = 0;
			}else {
				zComponent = Math.cos(thetaZY) * Math.sqrt(Math.pow(r, 2) - Math.pow(xComponent,2));
			}
			
			if ((thetaXY == 0) || (thetaXY == Math.PI)) {
				yComponent = 0;
			}else {
				yComponent = Math.sqrt(Math.pow(r, 2) - Math.pow(xComponent, 2) - Math.pow(zComponent, 2) );
			}
			
			if (xComponent == 0) { //atan won't work if xComponent is 0 (dividing by zero!)
				thetaZX = Math.PI/2; //this is always true if xComp is 0 (think about it!)
			}else {
				thetaZX = Math.atan(zComponent/xComponent);
			}
			
			
	
	
			
		}
		
		reduceThetas();
		
		findQuadrant();
		
//		System.out.print("quad: ");
//		System.out.println(quadrant + 1);
		
		//reversing the components based on quadrant
		switch (quadrant) {
			case(0):
				zComponent = -zComponent;
				yComponent = -yComponent;
				break;
			
			case(1):
				zComponent = -zComponent;
				xComponent = -xComponent;
				yComponent = -yComponent;
				break;
			
			case(2):
				zComponent = -zComponent;
				xComponent = -xComponent;
				//
				break;

			case(3):
				zComponent = -zComponent;
				//
				break;
			
			case(4): // (all are positive)
				yComponent = -yComponent;
				break;

			case(5):
				xComponent = -xComponent;
				yComponent = -yComponent;
				break;

			case(6):
				xComponent = -xComponent;
				//
				break;

			case(7):
				//
				break;
				
		}
		
//		System.out.println("xComp: " + xComponent);
//		System.out.println("yComp: " + yComponent);
//		System.out.println("zComp: " + zComponent);
	
		updatePoints();
	}
	
	private void reduceThetas() {  //reduces all of the thetas in the vector to a 0-2pi range and gets rid of negatives
		thetaXY = reduceTheta(thetaXY);
		thetaZX = reduceTheta(thetaZX);
		thetaZY = reduceTheta(thetaZY);
	}
	
	private double reduceTheta(double theta) { //reduces thetas greater than 2pi to a 0-2pi range and gets rid of negatives
		double reducedTheta = theta;
	
		if (theta > 0) {
			reducedTheta = (theta % (2 * Math.PI));
		}else if (theta < 0) {
			reducedTheta = (theta % (-2 * Math.PI));
			reducedTheta += (2 * Math.PI);
		}else if (Math.abs(theta - 6.283185307179585) < 0.0000001) {
			reducedTheta = 0;
		}else {
			reducedTheta = 0;
		}
		
		if (reducedTheta < 0) {
			Exception e = new Exception("reducedTheta is negative");
			e.printStackTrace();
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("theta: " + theta);
			System.out.println("reducedTheta: " + reducedTheta);
		}
		
		return reducedTheta;
	}
	
	public double[] calculateComponents(double r) {
		
		reduceThetas();
		
		double xComponent_ex;
		double yComponent_ex;
		double zComponent_ex;
		
		if (valueAngle == "thetaZX") {
			
			
			if ((thetaXY == 0) || (thetaXY == Math.PI)) {
				yComponent_ex = 0;
			}else {
				yComponent_ex = Math.sqrt( 
						(Math.pow(r, 2) * Math.pow(Math.sin(thetaXY),2) * Math.pow(Math.cos(thetaZX),2)) /
						(1 - Math.pow(Math.sin(thetaXY), 2) * Math.pow(Math.sin(thetaZX),2)));
			}
			
			if (thetaZX == 0 || thetaZX == Math.PI) {
				zComponent_ex = 0;
			}else {
				zComponent_ex = Math.sin(thetaZX) * Math.sqrt(Math.pow(r, 2) - Math.pow(yComponent_ex,2));
			}
			
			if ( (thetaXY == (Math.PI/2)) || (thetaXY == 3*Math.PI/2) ) {
				xComponent_ex = 0;
			}else {
				xComponent_ex = Math.sqrt(Math.pow(r, 2) - Math.pow(yComponent_ex, 2) - Math.pow(zComponent_ex, 2) );
			}
			
			if (zComponent_ex == 0) { //atan won't work if zComponent_ex is 0 (dividing by zero!)
				thetaZY = Math.PI/2; //this is always true if zComp is 0 (think about it!)
			}else {
				thetaZY = Math.atan(yComponent_ex/zComponent_ex);
			}
	
			
		}else {
			assert valueAngle == "thetaZY";
			
			if ( (thetaXY == (Math.PI/2)) || (thetaXY == 3*Math.PI/2) ) {
				xComponent_ex = 0;
			}else {
				xComponent_ex = Math.sqrt( 
					(Math.pow(r, 2) * Math.pow(Math.cos(thetaXY),2) * Math.pow(Math.sin(thetaZY),2)) /
					(1 + Math.pow(Math.sin(thetaXY), 2) * Math.pow(Math.sin(thetaZY),2)));
			}
			
			if (thetaZY == 0) {
				zComponent_ex = 0;
			}else {
				zComponent_ex = Math.cos(thetaZY) * Math.sqrt(Math.pow(r, 2) - Math.pow(xComponent_ex,2));
			}
			
			if ((thetaXY == 0) || (thetaXY == Math.PI)) {
				yComponent_ex = 0;
			}else {
				yComponent_ex = Math.sqrt(Math.pow(r, 2) - Math.pow(xComponent_ex, 2) - Math.pow(zComponent_ex, 2) );
			}
			
			if (xComponent_ex == 0) { //atan won't work if xComponent_ex is 0 (dividing by zero!)
				thetaZX = Math.PI/2; //this is always true if xComp is 0 (think about it!)
			}else {
				thetaZX = Math.atan(zComponent_ex/xComponent_ex);
			}		
		}
		
		findQuadrant();
		
		
		//reversing the components based on quadrant
		switch (quadrant) {
			case(0):
				zComponent_ex = -zComponent_ex;
				yComponent_ex = -yComponent_ex;
				break;
			
			case(1):
				zComponent_ex = -zComponent_ex;
				xComponent_ex = -xComponent_ex;
				yComponent_ex = -yComponent_ex;
				break;
			
			case(2):
				zComponent_ex = -zComponent_ex;
				xComponent_ex = -xComponent_ex;
				//
				break;

			case(3):
				zComponent_ex = -zComponent_ex;
				//
				break;
			
			case(4): // (all are positive)
				yComponent_ex = -yComponent_ex;
				break;

			case(5):
				xComponent_ex = -xComponent_ex;
				yComponent_ex = -yComponent_ex;
				break;

			case(6):
				xComponent_ex = -xComponent_ex;
				//
				break;

			case(7):
				//
				break;
				
		}

		return new double[] {xComponent_ex,yComponent_ex,zComponent_ex};
	}
	
	public void setThetas( double thetaXY1, double thetaZX1, double thetaZY1, String valueAngle1)  {
		valueAngle = valueAngle1;
		if (!( (valueAngle == "thetaZX") || (valueAngle == "thetaZY") ) ) {
			NoSuchFieldException e = new NoSuchFieldException("valueAngle is which z angle you want the engine to use to calculate the components. It must be either \"thetaZX\" or \"thetaZY\". \n valueAngle is currently: " + valueAngle + ". Object: " + toString());
			e.printStackTrace();
		}
		
		thetaXY = thetaXY1;
		thetaZX = thetaZX1;
		thetaZY = thetaZY1;
		calculateComponents();
	}	
	
	private void calculateThetas() { 
		findQuadComp();
		
		if (Math.round(xComponent) < 0.001) { //atan won't work if xComp is 0 (division by 0)
			thetaXY = Math.PI/2;
		}else {
			thetaXY = Math.atan(yComponent/xComponent);
		}
		
		if (Math.round(xComponent) < 0.001)  {
			thetaZX = Math.PI/2;
		}else {
			thetaZX = Math.atan(zComponent/xComponent);	
		}
		
		if (Math.round(zComponent) < 0.001) { 
			thetaZY = Math.PI/2;
		}else { //atan won't work if zComp is 0 (division by 0)
			thetaZY = Math.atan(yComponent/zComponent);
		}
		
		
		
		switch (quadrant) {
			case(0):
				thetaZX = 2*Math.PI - thetaZX;
				thetaZY = Math.PI - thetaZY;
				break;
			
			case(1):
				thetaXY = Math.PI - thetaXY;
				thetaZX = Math.PI + thetaZX;
				thetaZY = Math.PI - thetaZY;
				break;
			
			case(2):
				thetaXY = Math.PI + thetaXY;
				thetaZX = Math.PI + thetaZX;
				thetaZY = Math.PI + thetaZY;
				break;
			
			case(3):
				thetaXY = 2*Math.PI - thetaXY;
				thetaZX = 2*Math.PI - thetaZX;
				thetaZY = Math.PI + thetaZY;
				break;
			
			case(4): 
				//they're all in this quad already
				break;
			
			case(5):
				thetaXY = Math.PI - thetaXY;
				thetaZX = Math.PI - thetaZX;
				break;
			
			case(6):
				thetaXY = Math.PI + thetaXY;
				thetaZX = Math.PI - thetaZX;
				thetaZY = 2*Math.PI - thetaZY;
				break;
			
			case(7):
				thetaXY = 2*Math.PI - thetaXY;
				thetaZY = 2*Math.PI - thetaZY;
				break;
				
		}
	}
	
	public void updatePoints() {
		
		vectorTip.setPos(xReal + xComponent, yReal + yComponent, zReal + zComponent);
		
		points = new point[2];
		points[0] = new point(xReal,yReal,zReal);
		points[1] = vectorTip;
		
		setPoints(points);
	}
	
	public double getYComponent() {
		return yComponent;	
	}
	
	public double getXComponent() {
		return xComponent;	
	}

	public double getZComponent() {
		return zComponent;	
	}
	
	public double getThetaXY() {
		return thetaXY;
	}
	
	public double getThetaZX() {
		return thetaZX;
	}
	
	public double getThetaZY() {
		return thetaZY;
	}
	
	public point getVectorTip() {
		return vectorTip;
	}
	
	public void paint(Graphics page) {
	//	page.drawLine(points[0].getX(),points[0].getY(),vectorTip.getX(),vectorTip.getY());  //these two lines should do the same thing
		page.drawLine(x, y , x + (int) Math.round(xComponent),y + (int) Math.round(yComponent));
	}
}
	