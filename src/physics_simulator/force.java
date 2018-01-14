package physics_simulator;

public class force extends Vector{
	public double frame;
	public double time;
	public boolean isApplied = false, delete = false;
	public physics_object object;
	
	
	
	public force(physics_object object1, double xComponent1, double yComponent1, double zComponent1, double time1,double frame1) {
		super( xComponent1, yComponent1,zComponent1);
		xComponent = xComponent1;
		yComponent = yComponent1;
		zComponent = zComponent1;
		time = time1;
		object = object1;
		frame = frame1;
	}
	
	public force(physics_object object1, double r, double theta, double zComponent1, double time1, double frame1, boolean isPolar) {
		super( r, theta, zComponent1, isPolar);
		assert isPolar == true;
		time = time1;
		object = object1;
		frame = frame1;
	}
	
	
	
	public void apply() {
		if (! isApplied) {
			
			///Check this line \/
			if ((Math.abs(object_draw.current_frame - (int)object_draw.current_frame ) < 0.00001) || (frame == -1)) {
				System.out.println("applied");
				object.applyComponentForce(xComponent, yComponent, zComponent,time);
				isApplied = true;
				delete = true;
			}
		}
		
	}
	
	
}
