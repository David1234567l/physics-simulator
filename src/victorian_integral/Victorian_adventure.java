package victorian_integral;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;
import physics_simulator.*;
import physics_simulator.physics_object.pointOfRotationPlaces;

public class Victorian_adventure {

	private static Physics_frame frame = new Physics_frame();
	public static int inactivity_timer = 0;
	
	public static void main(String[] args) {

		
		object_draw drawer = new object_draw(frame);
		
		//mouseListener +==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
		MouseAdapter mouse =  new MouseAdapter() {

		public void mouseClicked(MouseEvent arg0) {
				inactivity_timer = 0;
			
		}
			public void mouseEntered(MouseEvent arg0) {
			inactivity_timer = 0;
				
		}
			
		public void mouseExited(MouseEvent arg0) {
			inactivity_timer = 0;
		}

		
		public void mousePressed(MouseEvent arg0) {
			inactivity_timer = 0;
				
		
		}

		public void mouseReleased(MouseEvent arg0) {
			inactivity_timer = 0;
			
		}
		};

		//==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
		
		
		drawer.addMouseListener(mouse);	
		
		border_bounce boundries = new border_bounce();
		boundries.setName("boundries",1);
		
		Square house = new Square(200,250,0,90,1);
		house.setColor(Color.blue);
		house.isAnchored = false;
		house.setName("Your house", 1);
		house.isFilled = false;
		house.setAngularVelocity(0, 0,0.01);
		house.setSpeed(2, 2, 0);
      

	
		
		Triangle house_roof = new Triangle(255,209,0,90,40,1);
		house_roof.setName("Your house's roof", 1);
		house_roof.setParentObject(house);
		house_roof.setPointOfRotationPlace(pointOfRotationPlaces.parentsPlace);
		house_roof.setColor(Color.RED);
		house_roof.isFilled = false;
		
		
		rectangle rect = new rectangle(200,200,0,50,100,1);
		rect.setName("rect1", 1);
		rect.setSpeed(2,2,0);
		
		Vector vec1 = new Vector(50,0,0,0,"thetaZX");
		vec1.setParentObject(house);
		vec1.setPos(100, 200, 0);
		
	//	Box box1 = new Box(300,300,0,50,1);
	//	box1.setName("3dBox",1);
	//	box1.setParentObject(house);
		
//		Square square2 = new Square(100,100,0,40,1);
//		square2.setName("Square2",1);
//		square2.setSpeed(2, 1, 0);
		
		
		//adding objects to the object list
		object_draw.objects.add(house_roof);
		object_draw.objects.add(house);
		object_draw.objects.add(boundries);
	//	object_draw.objects.add(square2);
	//	object_draw.objects.add(box1);
		
		
		
		while (true) {
			try {

				drawer.doFrame(Settings.frameStep);
				vec1.setThetas(vec1.getThetaXY() + 0.0001, vec1.getThetaZX(), vec1.getThetaZY(), "thetaZY");
				if (inactivity_timer < Settings.timeOutTime) {
					inactivity_timer++;
				}else {
					System.out.println("Session timed out");
					break;
				}

			}catch(ConcurrentModificationException e) {
				System.out.println("ConcModEx");
				
			}
		}
		
		frame.dispose();
		
	}
}
