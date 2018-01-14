package physics_simulator;

import java.util.ArrayList;

import physics_simulator.physics_object.faces;



public class border_bounce extends rectangle {
	
	public border_bounce() {
		super(-50, 0, 0, Settings.width *1.06, Settings.height * 0.975, 1);
		isAnchored = true;
		isRotatable = false;
		
	}
	
	public void updatePoints() {
		
	}
	public void checkForCollisions(ArrayList<physics_object> objects) {
			
		if (isTangible)  {
			for (physics_object current_pObject : objects) {
				
				if (current_pObject.isTangible && current_pObject.affectedByBorder)	current_pObject.checkForCollision(this,objects);
	
			}	
		}
	}
		
}