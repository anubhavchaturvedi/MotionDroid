import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MotionDroid_Client{

	private static String SERVER_IP = "";
	private final static int SERVER_PORT = 6000;
	
	private static Socket socket;

	static enum SourceSensor{
		linear_acceleration, accelerometer
	}
		
	public static void main(String args[]) throws IOException, JSONException, AWTException
	{
		if(args.length!=1)
		{
			System.out.println("No argument. USAGE : java MotionDroid_Client <IPv4 ADDRESS OF DEVICE>");
			System.exit(1);
		}
		
		SERVER_IP = args[0];
		Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Matcher m = p.matcher(SERVER_IP);
		if(!m.matches())
		{
			System.out.println("Invalid IPv4 address.");
			System.exit(1);
		}
		
		SourceSensor source = SourceSensor.accelerometer;
		
		MotionHandler handler = new MotionHandler();
		
		try
		{
			socket = new Socket(SERVER_IP, SERVER_PORT);
		}catch(Exception e)
		{
			System.out.println("Could not connect to device.");
			System.exit(2);
		}
		
		System.out.println("Application started");
		
		InputStream inputStream = socket.getInputStream();
		InputStreamReader inStreamReader = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(inStreamReader);
		
		String line;
		
		while(!(line = br.readLine()).equals("end"))
		{
			JSONArray array;
			try{
				array = new JSONArray(line);
			}catch(Exception e)
			{
				continue;
			}
			
			JSONObject record,values;
									
			int deltaX=0,deltaY=0;
			
			if( source == SourceSensor.accelerometer )
			{
				record = array.getJSONObject(1);
				values = record.getJSONObject("accelerometer");
				
				// transformation of received values
				deltaX = (int) ((-1*values.getDouble("x")-1)*(0.9));
				deltaY = (int) ((values.getDouble("y")-1)*0.9);
			}
			else if( source == SourceSensor.linear_acceleration )
			{
				record = array.getJSONObject(0);
				values = record.getJSONObject("linear_acceleration");
				
				// linear transformation of received values
				deltaX = (int) ((-1*values.getDouble("x")-1)*(0.9));
				deltaY = (int) ((values.getDouble("y")-1)*0.9);
			}
			
			handler.relativeMove(deltaX, deltaY);
		}
		
		System.out.println("Program terminated.");
	}
}
