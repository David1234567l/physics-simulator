package physics_simulator;

import java.io.Serializable;

public class Equation {
	protected double a, b, h, k;

	public void setConstants(double q, double i, double r, double z) {
		a = q;
		b = i;
		h = r;
		k = z;
	}

	public double XtoY(double x) {
		System.out.println("Error: XtoY not overridden in " + toString());
		return x;
	}

}
	
