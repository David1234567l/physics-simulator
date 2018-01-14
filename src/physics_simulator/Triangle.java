package physics_simulator;

public class Triangle extends physics_object {

	public Triangle(int x, int y, int z, double width,double height, double mass) {
		
		setPos(x,y,z);
		setSize(width,height,0);
		setMass(mass);
		setRotation(0,0,0);
		
		pointRenderOrder = new int[] {0,1,2,0};
		point[] points = new point[3];
		
		points[0] = new point(xReal,yReal,zReal); 
		points[0].setAngle(Math.PI/2, 0,Math.PI/2);
		
		points[1] = new point(xReal+xSize/2,yReal + ySize,zReal);
		points[1].setAngle(0, 0,-Math.PI/2);
		
		points[2] = new point(xReal-xSize/2,yReal+ySize,zReal );
		points[2].setAngle(Math.PI, Math.PI,-Math.PI/2);
		
		setPoints(points);
		
		double r = Math.sqrt(Math.pow(xSize,2) + Math.pow(ySize,2) + Math.pow(zSize,2))/2;
		for (point cP : points) {
			cP.setR(r);
		}
		
		calculateCenter();
		
	//	calculatePointValues();
		
		updatePointOfRotation();
		
		setPos(x,y,z);
		if ( (Settings.rotationAlgorithm == 2) && (isRotatable) ) {
			for (point p : points) {
				p.setR(Math.sqrt(xSize + ySize));
			}
			axis = new Coordinate_Axis(this);
			axis.setName(name + "_axis");
			object_draw.objects.add(axis);
		}
		
	}
}
