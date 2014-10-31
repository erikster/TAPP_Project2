/*
 *
 *
 */
 
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
// import static org.lwjgl.opengl.GL11.*; // lets us use methods from GL11 class without GL11. prefix

/*
 * Our display class needs to invoke three methods: create, update, destroy.
 * In create, we actually open up a window to do stuff with
 * In update, we update the display on screen
 * In destroy, we close the window and release resources
 */
public class TestDisplay {
	public final int disp_width  = 800;
	public final int disp_height = 600;
	
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(disp_width, disp_height));
			Display.create();
		} catch (LWJGLException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		
		// crack open OpenGL stuff
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, disp_width, 0, disp_height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		while (!Display.isCloseRequested()) { // close button of native window
			// Clear screen & depth buffers
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// set color of square (quadrilateral)
			GL11.glColor3f(0.0f, 0.0f, 1.0f);
			// draw square
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(300      , 300      );
				GL11.glVertex2f(300 + 100, 300      );
				GL11.glVertex2f(300 + 100, 300 + 100);
				GL11.glVertex2f(300      , 300 + 100);
			GL11.glEnd();
			
			// set color of lines
			GL11.glColor3f(0.0f, 0.0f, 0.0f);
			
			
			Display.update();
		}
		
		Display.destroy(); // destroys display and any resources it uses
	}
}