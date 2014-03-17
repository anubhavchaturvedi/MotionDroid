import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.event.InputEvent;


public class MotionHandler {

	private Robot robot;
	private int x,y;
	
	private int RESOLUTION_X;
	private int RESOLUTION_Y;
	
	public MotionHandler() throws AWTException {
	
		robot = new Robot();
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		RESOLUTION_X = gd.getDisplayMode().getWidth();
		RESOLUTION_Y = gd.getDisplayMode().getHeight();
	
		x= RESOLUTION_X/2;
		y= RESOLUTION_Y/2;
		robot.mouseMove(x, y);
		
	}
	
	public void relativeMove(int x, int y)
	{
		this.x = this.x + x;
		
		this.y = this.y + y;
		
		if(this.x < 0)
			this.x = 0;
		else if( this.x > RESOLUTION_X)
			this.x = RESOLUTION_X;
		
		if(this.y < 0)
			this.y = 0;
		else if( this.y > RESOLUTION_Y)
			this.y = RESOLUTION_Y;
		
		
		robot.mouseMove(this.x, this.y);
	}
	
	public void click()
	{
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
}
