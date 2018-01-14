package physics_simulator;

public class PolarObject extends physics_object {
	
	public PolarObject(double x, double y, double z, double size) {
		drawMethod = "ListedPointAlgorithm";
		setPos(x,y,z);
		setSize(size,size,size);
		
		points = new point[(int) (2*Math.PI/Settings.thetaStep )];
		double r = 0,theta;
	
		for (int i = 0; i < points.length; i ++) {
			theta = i * Settings.thetaStep;
			
			r = size * Math.sin(theta);
			points[i] = new point(centerX,centerY,centerZ);
			points[i].setPointVector(new Vector(r,theta,0,0,"thetaZX"));
			points[i].setPos(centerX, centerY, centerZ);
		}
		
		setPoints(points);
		
		axis = new Coordinate_Axis(this);
		
		if (Settings.rotationAlgorithm == 2) {
			axis.setName(name + "_axis");
		}
		
		object_draw.objects.add(axis);
		
		updatePoints();
		
	}
}
