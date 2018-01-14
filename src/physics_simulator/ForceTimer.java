package physics_simulator;

import java.util.ArrayList;

public class ForceTimer extends FrameTimer{ //waits for a certain number of frames, then applies a counter force to essentially remove the force that it is timing]
	double xComponent,yComponent,zComponent; 
	physics_object object;
	
	public ForceTimer(double timeLeft1,double xComponent1,double yComponent1,double zComponent1,physics_object object1) {
		super(timeLeft1);
		xComponent = xComponent1;
		yComponent = yComponent1;
		zComponent = zComponent1;
		object = object1;
	}
	
	public void Update(ArrayList<physics_object> objects) {	
		if (! timerDone) {
			if (timeLeft <= 0) {
				System.out.println("done");
				assert timeLeft == 0;
				timerDone = true;
				object.applyComponentForce(-xComponent, -yComponent, -zComponent);
			}else{
				timeLeft--;
			}
		}
	}
	
	public void Update(ArrayList<physics_object> objects, double frames) {	
		if (! timerDone) {
			if (Math.abs(timeLeft) <= 0.001) {
				try {
				assert timeLeft == 0;
				}catch (AssertionError e) {
					System.out.println("TimeLeft != 0 (" + timeLeft + ") in "+ toString());
				}
				timerDone = true;
				object.applyComponentForce(-xComponent, -yComponent, -zComponent);
			}else{
				timeLeft-= frames;
			}
		}
	}
	

}
