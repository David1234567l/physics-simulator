package physics_simulator;


import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ConcurrentModificationException;
import javax.swing.JFrame;

public class physics_runner {
	
	private static Physics_frame frame = new Physics_frame();
	private static boolean mouseIsPressed = false;
	private static int mouseStartX;
	private static int mouseStartY;
	
	public static void main(String[] args) {
	
		border_bounce boundries = new border_bounce();

		object_draw drawer = new object_draw(frame);
		
		Vector vec1 = new Vector(43, 1, 1, 0, "thetaZX");
		vec1.setPos(400, 400, 0);
		vec1.setName("vec1", 1);
		
		rectangle rekt = new rectangle(350,200,0,30,40,1);
		rekt.setName("rekt", 1);
		
		Square square1 = new Square(400,400,0,50,1);
		square1.setName("square1", 1);
		square1.setPos(400, 200, 0);
		square1.setSpeed(10, 2, 0);
		
		
		
		Box box1 = new Box(299,300,0,50,1);
		box1.setName("box1", 1);
		
		//mouseListener +==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
		MouseAdapter mouse =  new MouseAdapter() {

		public void mouseClicked(MouseEvent arg0) {
			drawer.inactivity_timer = 0;
			
		}

		public void mouseEntered(MouseEvent arg0) {
			drawer.inactivity_timer = 0;
			
		}

		
		public void mouseExited(MouseEvent arg0) {
			drawer.inactivity_timer = 0;
		}

		
		public void mousePressed(MouseEvent arg0) {
			drawer.inactivity_timer = 0;
			
			/*
			if (! mouseIsPressed) {
				mouseStartX = arg0.getX();
				mouseStartY = arg0.getY();
				
			}
			System.out.println(mouseStartX + "," + mouseStartY);
			System.out.println(arg0.getX() + "," + arg0.getY());
			int xRotation = mouseStartX - arg0.getX();
			int yRotation = mouseStartY - arg0.getY();
			*/
			
			int xRotation = arg0.getX();
			int yRotation = arg0.getY();
			square1.setRotation(xRotation,yRotation,square1.getZRotation());
		}


		public void mouseReleased(MouseEvent arg0) {
			drawer.inactivity_timer = 0;
			mouseIsPressed = false;
		}
		};
		
		
		//==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+==+
		
		
		drawer.addMouseListener(mouse);
		
		
		object_draw.objects.add(boundries);

	//	object_draw.objects.add(vec1);
		object_draw.objects.add(square1);
	//	object_draw.objects.add(box1);
		
		object_draw.objects.add(rekt);
		

		

			 
		/*
		//gravity
		for (physics_object current_object : drawer.objects) {
			current_object.applyComponentForce(0, 9.8, 0);
		}
		*/
				
		
		for(long i = 0; i < 10000000; i++) {
			try {
				
				drawer.doFrame(Settings.frameStep);
			
			}catch(ConcurrentModificationException e) {
				System.out.println("ConcModEx");	
			}
			
			if (drawer.inactivity_timer < Settings.timeOutTime) {
				drawer.inactivity_timer++;
			}else {
				System.out.println("Session timed out");
				break;
			}
		}
		
		frame.dispose();
	}

}
