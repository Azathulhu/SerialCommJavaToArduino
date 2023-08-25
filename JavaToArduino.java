/*
 * JavaSE 17
 *This code is made by Chishiki Sakagami: https://www.facebook.com/Chigamii
 *Client language to control the servo motors from Java through JavaFX and serial communication protocol.
 *For the jSerialComm documentation, go here: https://fazecast.github.io/jSerialComm/javadoc/index.html
 *JavaFX documentation: https://openjfx.io/javadoc/20/
 *Download the external library here: https://fazecast.github.io/jSerialComm/
 *Before running the Java code, make sure that the Arduino IDE is closed.
 *Make sure that your circuit is all wired correctly, otherwise it may cause problems.
 *Make sure you know what you are doing before modifying the code.
 *Set up the libraries first.
 *This code is not designed to be overly complex, but you may add whatever you want here.
 
 Note: After the recording, I updated the code to make it slightly cleaner, 
 so it will be different from the code in the video. I'm too lazy to re-upload
 
 -Things I have changed in the code from the video:
 
1. I was supposed to use object synchronization for something, but I decided for this to be a more
    simple project on how you can use serial communication, but then I realized I forgot to remove it. LOL
    
2. Slightly improved the simple error handling, and it looks much nicer.

*/
import com.fazecast.jSerialComm.SerialPort;//Serial Port

//JavaFX library
import javafx.application.Application;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavatoArduino extends Application {
	private volatile long lastTransmissionTime = 0;
	private volatile long transmissionInterval = 100;

	public void start(Stage createStage) {
		SerialPort arduinoPort = SerialPort.getCommPort("COM6");//Change this into your own port if needed.
		arduinoPort.setComPortParameters(9600, 8, 1, 0); //Baudrate, Data bits, stop bits, and no parity.
		
		if(arduinoPort.openPort()) {
			System.out.println("Connected to the board successfully.");
			
		} else {
			System.err.println("Error opening port: " + arduinoPort.getSystemPortName() + ". "
					+ "Make sure you connected that before running this code.");
			return;
		}
		Pane root = new Pane();
		Scene scene = new Scene(root, 1000, 500);
		createStage.setTitle("Servo motor control with simple serial communication protocol");
		createStage.setScene(scene);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if(arduinoPort != null && arduinoPort.isOpen()) {
				arduinoPort.closePort();
				System.out.println("Closed the port, " + arduinoPort.getSystemPortName() +
						", successfully.");
			}
		}));
		
		scene.setOnMouseMoved((MouseEvent e) -> {
			int xAxis = (int) e.getX();
			int yAxis = (int) e.getY();
			String coordinates = xAxis + "," + yAxis + "\n";
			
			long currentTime = System.currentTimeMillis();
			
			if(currentTime - lastTransmissionTime >= transmissionInterval) {
				if(arduinoPort.isOpen()) {
					arduinoPort.writeBytes(coordinates.getBytes(), coordinates.length());
					lastTransmissionTime = currentTime;
					System.out.println("X Axis value: " + xAxis + " Y axis value: " + yAxis);
				}
			}
		});
		createStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
