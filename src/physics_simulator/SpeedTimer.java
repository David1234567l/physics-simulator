package physics_simulator;

import java.util.ArrayList;

public class SpeedTimer extends FrameTimer{ //waits for a certain number of frames, then applies a speed
	double xComponent,yComponent,zComponent; 
	physics_object object;
	
	public SpeedTimer(int timeLeft1,double xComponent1,double yComponent1,double zComponent1,physics_object object1) {
		super(timeLeft1);
		xComponent = xComponent1;
		yComponent = yComponent1;
		zComponent = zComponent1;
		object = object1;
	}
	
	public void Update(ArrayList<physics_object> objects) {	
		if (! timerDone) {
			if (timeLeft <= 0) {
				assert timeLeft == 0;
				timerDone = true;
				object.setSpeed(xComponent, yComponent, zComponent);
			}else{
				timeLeft--;
			}
		}
	}
	

}