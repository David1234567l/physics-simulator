package physics_simulator;

public class Line extends physics_object{ //UNFINISHED
	
	public Line(double xReal,double yReal,double zReal) {
		drawMethod = "listedPointsAlgorithm";
		
		point[] points = {new point(xReal,yReal,zReal),new point(xReal,yReal,zReal)};
		setPoints(points);
	}
}
