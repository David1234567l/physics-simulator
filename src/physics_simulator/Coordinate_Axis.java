package physics_simulator;

import java.awt.Graphics;

public class Coordinate_Axis extends physics_object{
		public XAxis xAxis;
		public YAxis yAxis;
		public ZAxis zAxis;
		private physics_object child_object; //the object that is displayed on this axis
	
	public Coordinate_Axis(double xRotation1, double yRotation1, double zRotation1) {
		xRotation = xRotation1;
		yRotation = yRotation1;
		zRotation = zRotation1;
		
		xAxis = new XAxis();
		yAxis = new YAxis();
		zAxis = new ZAxis();
		
		drawMethod = "paint";
		
		isTangible = false;
		affectedByBorder = false;
		isVisible = Settings.showAxis;
		
		axis = null; //this is the axis variable from the physics_object class. An axis isn't painted on another axis!
	}
	
	public Coordinate_Axis(physics_object object) {
		
		child_object = object;
		
		setRotation(child_object.getXRotation(),child_object.getYRotation(),child_object.getZRotation());
		setPos(child_object.xReal,child_object.yReal,child_object.zReal);
		
		xAxis = new XAxis();
		yAxis = new YAxis();
		zAxis = new ZAxis();
		
		drawMethod = "paint";
		
		UpdateAxis();
		
		isTangible = false;
		affectedByBorder = false;
		isVisible = Settings.showAxis;
		
		axis = null; //this is the axis variable from the physics_object class. An axis isn't painted on another axis!
	}
	
	
	public void UpdateAxis() {

		setRotation(child_object.getXRotation(),child_object.getYRotation(),child_object.getZRotation());
		setPos(child_object.centerX,child_object.centerY,child_object.centerZ);
		xAxis.UpdateAxis();
		yAxis.UpdateAxis();
		zAxis.UpdateAxis();
		setName(child_object.getObjectName() + "_axis",1);
	}
	
	public void paint(Graphics page) {
		if (Settings.showAxis) {
			xAxis.paint(page);
			yAxis.paint(page);
			zAxis.paint(page);
		}
	}
	
	public void updatePoints() {
		
	}
	
	
	public void setRotation(double xRotation1, double yRotation1, double zRotation1) {
		xRotation = xRotation1;
		yRotation = yRotation1;
		zRotation = zRotation1;
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
	
	
	public interface Axis {
		public double screenThetaXY = 0, screenThetaZX = 0, screenThetaZY = 0;
		public Vector X = null,Y = null,Z = null,axisVector = null;
		public double size = 50;
		public double getX(double r);
		public double getY(double r);
		public double getZ(double r);
		public Vector getRVector(double r);
		public void UpdateAxis();
		public void paint(Graphics page);
	}
	
	
	public class XAxis {
		public Vector axisVector,X,Y,Z;
		public Vector[] vectors = new Vector[3];
		public double size = 50;
		
		public XAxis() {
			
			X = new Vector(size,zRotation,0,0,"thetaZX",child_object);
			Y = new Vector(size,0,0,xRotation,"thetaZY",child_object);
			Z = new Vector (size,0,yRotation,0,"thetaZX",child_object);
			
			X.setPos(xReal, yReal, zReal);
			Y.setPos(xReal, yReal, zReal);
			Z.setPos(xReal, yReal, zReal);
			
			vectors[0] = X;
			vectors[1] = Y;
			vectors[2] = Z;
			
			axisVector = X.vectorAdd(vectors);
			axisVector.setPos(xReal, yReal, zReal);
		}
		
		public void UpdateAxis() {

			X.UpdateVector(xReal,yReal,zReal,size,zRotation,0,0,"thetaZX");
			Y.UpdateVector(xReal,yReal,zReal,size,0,0,xRotation,"thetaZY");
			Z.UpdateVector(xReal,yReal,zReal,size,0,yRotation,0,"thetaZX");
			vectors[0] = X;
			vectors[1] = Y;
			vectors[2] = Z;
			
			axisVector = X.vectorAdd(vectors);
			axisVector.setPos(xReal, yReal, zReal);
		}
		
		public Vector getRVector(double r) {
			Vector newVector = new Vector(r,axisVector.getThetaZY(), axisVector.getThetaZX(), axisVector.getThetaZY(), "thetaZX",child_object);
			return newVector;
		}
		
		public double getX(double r) {
			double[] components = axisVector.calculateComponents(r);
			double x = components[0];
			return x;
		}
		
		public double getY(double r) {
			double[] components = axisVector.calculateComponents(r);
			double y = components[1];
			return y;
		}
		
		public double getZ(double r) {
			double[] components = axisVector.calculateComponents(r);
			double z = components[2];
			return z;
		}
		
		public void paint(Graphics page) {
			axisVector.paint(page);
		}
		
	}
	
	public class YAxis {
		public Vector axisVector,X,Y,Z;
		public Vector[] vectors = new Vector[3];
		public double size = 50;
		
		public YAxis() {
			X = new Vector(size,zRotation + Math.PI/2,0,0,"thetaZX",child_object);
			Y = new Vector(size,0,0,xRotation + Math.PI/2,"thetaZY",child_object);
			Z = new Vector (size,0,yRotation,0,"thetaZX",child_object);
			
			X.setPos(xReal, yReal, zReal);
			Y.setPos(xReal, yReal, zReal);
			Z.setPos(xReal, yReal, zReal);
			
			vectors[0] = X;
			vectors[1] = Y;
			vectors[2] = Z;
			
			
			axisVector = X.vectorAdd(vectors);
			axisVector.setPos(xReal, yReal, zReal);
		}
		
		public void UpdateAxis() {
//			System.out.println("yAxis");
			X.UpdateVector(xReal,yReal,zReal,size,zRotation + Math.PI/2,0,0,"thetaZX");
			Y.UpdateVector(xReal,yReal,zReal,size,0,0,xRotation + Math.PI/2,"thetaZY");
			Z.UpdateVector(xReal,yReal,zReal,size,0,yRotation,0,"thetaZX");
			
			vectors[0] = X;
			vectors[1] = Y;
			vectors[2] = Z;
			
			axisVector = X.vectorAdd(vectors);
			axisVector.setPos(xReal, yReal, zReal);
		}
		
		public Vector getRVector(double r) {
			Vector newVector = new Vector(r,axisVector.getThetaZY(), axisVector.getThetaZX(), axisVector.getThetaZY(), "thetaZX",child_object);
			return newVector;
		}
		
		public double getX(double r) {
			double[] components = axisVector.calculateComponents(r);
			double x = components[0];
			return x;
		}
		
		public double getY(double r) {
			double[] components = axisVector.calculateComponents(r);
			double y = components[1];
			return y;
		}
		
		public double getZ(double r) {
			double[] components = axisVector.calculateComponents(r);
			double z = components[2];
			return z;
		}
		
		public void paint(Graphics page) {
			axisVector.paint(page);
		}
	}
	
	public class ZAxis {
		public Vector axisVector,X,Y,Z;
		public Vector[] vectors = new Vector[3];
		public double size = 50;
		
		public ZAxis() {
			X = new Vector(size,zRotation,0,0,"thetaZX",child_object);
			Y = new Vector(size,0,0,xRotation,"thetaZY",child_object);
			Z = new Vector (size,0,yRotation + Math.PI/2,0,"thetaZX",child_object);
			
			X.setPos(xReal, yReal, zReal);
			Y.setPos(xReal, yReal, zReal);
			Z.setPos(xReal, yReal, zReal);
			
			vectors[0] = X;
			vectors[1] = Y;
			vectors[2] = Z;
			
			axisVector = X.vectorAdd(vectors);
			axisVector.setPos(xReal, yReal, zReal);
		}
		
		public void UpdateAxis() {
//			System.out.println("zAxis");
			X.UpdateVector(xReal,yReal,zReal,size,zRotation,0,0,"thetaZX");
			Y.UpdateVector(xReal,yReal,zReal,size,0,0,xRotation,"thetaZY");
			Z.UpdateVector(xReal,yReal,zReal,size,0,yRotation + Math.PI/2,0,"thetaZX");
			
			vectors[0] = X;
			vectors[1] = Y;
			vectors[2] = Z;
			
			axisVector = X.vectorAdd(vectors);
			axisVector.setPos(xReal, yReal, zReal);
		}
		
		public Vector getRVector(double r) {
			Vector newVector = new Vector(r,axisVector.getThetaZY(), axisVector.getThetaZX(), axisVector.getThetaZY(), "thetaZX",child_object);
			return newVector;
		}
		
		public double getX(double r) {
			double[] components = axisVector.calculateComponents(r);
			double x = components[0];
			return x;
		}
		
		public double getY(double r) {
			double[] components = axisVector.calculateComponents(r);
			double y = components[1];
			return y;
		}
		
		public double getZ(double r) {
			double[] components = axisVector.calculateComponents(r);
			double z = components[2];
			return z;
		}
		
		public void paint(Graphics page) {
			axisVector.paint(page);
		}
	}
}
