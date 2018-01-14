
//THIS INTERFACE IS CURRENTLY NOT USED

package physics_simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public interface Physics_object_interface {
		
	public boolean isVisible = true, isTangible = true, isAnchored = false, isFilled = false, affectedByBorder = true;
	
	String name = "unNamed";
	int x= 0,y= 0,z= 0; 	
	double xReal = 0,yReal= 0,zReal= 0;
	double centerX= 0,centerY= 0, centerZ= 0,xSpeed= 0,ySpeed= 0,zSpeed= 0,xAcce= 0l,yAccel= 0,zAccel= 0,xRotation= 0,yRotation= 0,zRotation= 0,xSize= 0, ySize= 0, zSize= 0,xSizeAppearance= 0, ySizeAppearance= 0, zSizeAppearance= 0, axisThetaXY = 0,axisThetaZX = 0, axisThetaZY = 0,mass= 0,friction_coefficient= 0;
	String drawMethod = "defaultPointAlgorithm";
	point[] points = {};
	int[] pointRenderOrder = {};
	Color color = Color.BLACK;
	Parametric_circle_equation rotateEq = null;
	
	Coordinate_Axis axis = null;
	
	public enum faces {left,right,top,bottom,far,near,none};
	
	public void paint(Graphics page);	
	public void checkForCollisions(ArrayList<physics_object> objects);
	
	public Object checkForCollision(physics_object physics_object,ArrayList<physics_object> objects);
	public void Update(ArrayList<physics_object> objects); 
	public void Update(ArrayList<physics_object> objects,double frames);
	
	public void UpdateWithoutCollision(ArrayList<physics_object> objects);
	
	public void UpdateWithoutCollision(ArrayList<physics_object> objects, double frames);
	
	public void applyForce(double r, double theta, double z_magn);
	
	public void applyForce(double r, double theta, double z_magn, double time);
	
	public void applyComponentForce(double x_magn, double y_magn, double z_magn);
	
	public void applyComponentForce(double x_magn, double y_magn, double z_magn, double time);
	
	public double[] getTrajectory();
	
	public double[] calculateDeflectionAngle(double angleOfApproach,double zComponent);
	double[] calculateDeflectionAngle(physics_object current_object);
	public void isCollided(physics_object object, faces side);
	public void setPos(double xReal1,double yReal1,double zReal1);
	
	public void setPoints(point[] points1);
	public void setSpeed(double xSpeed1, double ySpeed1, double zSpeed1);
	public void setAccel(double xAccel1, double yAccel1, double zAccel1);
	public void setSize(double xSize1,double ySize1,double zSize1);
	public void setMass(double mass1);
	public double getCenterX();
	public double getCenterY();
	public void setCenterY(double centerY);
	public void setCenterX(double centerX);
	public void setRotation(double xRotation1, double yRotation1, double zRotation1);
	
	public double getXRotation();
	public double getYRotation();
	public double getZRotation();
	public void setColor(Color color1);
	void updatePoints();
	void updateCenter();
	void updatePos();
	void updateSize();
	void updateRotateEq();	
	public point[] getPoints();
	public int getX();
	public int getY();
	public int getZ();
	public double getXReal();
	public double getYReal();
	public double getZReal();
	public double getXSize();
	public double getYSize();
	public double getZSize();
	public Color getColor();
	public double getAxisThetaXY();
	public double getAxisThetaZX();
	public double getAxisThetaZY();
	public physics_object copy(physics_object new_object);	
}



	