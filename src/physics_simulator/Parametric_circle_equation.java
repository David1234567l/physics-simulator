package physics_simulator;

public class Parametric_circle_equation extends Equation {
	protected double r_x;
	protected double r_y;
	protected double r_z;

	public Parametric_circle_equation(double width1, double height1,double depth1) {
		//http://mathworld.wolfram.com/Sphere.html
		r_x = Math.sqrt(Math.pow(width1, 2) + Math.pow(height1,2)) / 2;
		r_y = r_x;
		r_z = r_x;
	}

	public double getX(double thetaXY) {
		
		double x = r_x * Math.cos(thetaXY);
		return x;
	}
	
	public double getY(double thetaXY) {
			
			double y = r_y * Math.sin(thetaXY);
			return y;
		}
	
	public double getZ(double t) {
		
		double z = r_z * Math.cos(t);
		return z;
	}
		
		
	}