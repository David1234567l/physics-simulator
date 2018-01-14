package physics_simulator;

public class point extends physics_object{
	private int id;
	private double thetaXY, thetaZX, thetaZY; //angle relative to the center of rotation
	private double r; //distance from the center of rotation
	private Vector pointVector;
	
	public point(double[] dimensions,int id1) { //dimensions = {x,y,z}
		isRotatable = false;
		setPos(dimensions[0],dimensions[1],dimensions[2]);
		id = id1;
		setSize(0,0,0);
	}
	
	public point(double xReal1, double yReal1, double zReal1) {
		isRotatable = false;
		setPos(xReal1,yReal1,zReal1);
		xSize = 0;
		ySize = 0;
		zSize = 0;
	}
	
	public void setX(double x1) {
		xReal = x1;
		updatePos();
	}
	
	public void setY(double y1) {
		yReal = y1;
		updatePos();
	}
	
	public void setZ(double z1) {
		zReal = z1;
		updatePos();
	}
	
	public void setAngle(double thetaXY1, double thetaZX1, double thetaZY1) {
		thetaXY = thetaXY1;
		thetaZX = thetaZX1;
		thetaZY = thetaZY1;
	}
	
	public void setPos(double x1, double y1, double z1) {
		xReal = x1;
		yReal = y1;
		zReal = z1;
		
		x = (int) Math.round(xReal);
		y = (int) Math.round(yReal);
		z = (int) Math.round(zReal);
	}
	
	public void setPointVector(Vector vector) {
		pointVector = vector;
		point vectorTip = pointVector.getVectorTip();
		setPos(vectorTip.getXReal(),vectorTip.getYReal(),vectorTip.getZReal());
	}

	public void setR(double r1) {
		r = r1;
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
	
	public double getR() {
		return r;
	}
	
}
