package physics_simulator;

import java.io.Serializable;

public class Power_equation extends Equation  {
	
	protected double p;

	public Power_equation(double q, double i, double r, double z, double power) {
		setConstants(q, i, r, z);
		a = q;
		b = i;
		h = r;
		k = z;
		p = power;

	}

	public double XtoY(int x) {
		double y = (double) (a * Math.pow((b * x - h), p) + k);
		return y;
	}
}

