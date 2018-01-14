package physics_simulator;

public class Documentation {
/*
 *  	v4 collision algorithm
 * the v4 collision algorithm uses the object_draw class to cycle through each object, which override the checkForCollisions method. The key to this collision method and what 
 * sets it apart from the others is what I call subCalculations. These are "sub-frames" which aren't rendered but in which the objects move only a fraction of the distance of 
 * their speed and the computer still checks for collisions. These sub-calculations allow the collisions to be WAYY more accurate and precise without slowing the engine down
 * since the subCalculations aren't rendered.
 * The number of subCalculations in-between every rendered frame can be controlled by the frame_step in the settings 
 * class.
 * 
 * 
 * 
	rotation V2
The rotation is calculated in the updatePoints() method of physics_object (line 381)  and the axis subclasses of the coordinate_axis class. 
Every object is drawn on its coordinate_axis. (created in the Square class for example on line 17)   
The updatePoints() method uses both the vectors created by the coordinate_axis and new vectors that It creates to translate angles of rotation (xRotation is ABOUT the x axis, yRotation is ABOUT the y axis, etc.) 
to translate it into the distances in the x, y, and z directions that each point must move to create the rotation. 
The method that does most of the heavy lifting in this process is the calculateComponents() method in the Vector class. 
This method uses three angles: thetaXY, thetaZX, and theta ZY, which are the angles from the positive xAxis toward the yAxis, the positive xAxis toward the zAxis, and 
the positive zAxis toward the yAxis respectively. The method uses these angles to find the x,y,and z components of the vector. The problem is that the equations I derived for 
this purpose only work in the first quadrant. This is where the findQuadrant() method comes in. It takes the three angles and finds based on each angle the two possibilities for
which quadrant the vector is in. (based 8 quadrants for 3d)  The method then uses an array to cross-reference these possibilities to find which quadrant the vector is actually in. 
Once the findQuadrant() method is finished with that, the calculateComponents() method takes over and uses a switch statement to apply the correct changes to the components 
and put the vector into the correct quadrant. 
	
The reduceThetas() method just reduces the angle to a 0-2pi range (like how 4pi rad is the same as 2pi rad just it does around the circle twice)	

	
	
	
*/	
}
