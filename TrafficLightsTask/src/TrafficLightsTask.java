import swiftbot.SwiftBotAPI;
import java.io.*;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import swiftbot.*;

public class TrafficLightsTask {

	static int avgRed = 0; // Red pixel counts, used for colour detection
	static int avgGreen = 0; // Green pixel counts, used for colour detection
	static int avgBlue = 0; // Blue pixel counts, used for colour detection
	static int DetectedTrafficLightCount = 0;
	static int RedLightCount = 0;
	static int GreenLightCount = 0;
	static int BlueLightCount = 0;
	static long StartTime;
	static long EndTime;
	static BufferedImage img;

	private static SwiftBotAPI API = null;// setting the swiftbot API to null so that it
	// can be used in other classes as a API derived from this main class

	/*
	 * Used static type for the API, red component count, green component count and
	 * for the blue component count, detected traffic light count, red light count,
	 * green light count, blue light count and start time and end time to get access
	 * for these variable in the entire program
	 */

	public static void main(String[] args) {

		API = new SwiftBotAPI();
		TrafficLightActions TrafficLights = new TrafficLightActions();

		/*
		 * Introduced a new class called Traffic light actions with 3 methods of actions
		 * to perform when the associated traffic light is detected. ( Used the concept
		 * of OOP )
		 */

		Scanner scanner = new Scanner(System.in);// Used to get the user response

		System.out.println();
		System.out.println();
		System.out.println("          *************************************** ");
		System.out.println("          * WELCOME TO THE TRAFFIC LIGHTS TASK! * ");
		System.out.println("          *************************************** ");
		System.out.println();
		System.out.println();
		// System.out.println("Hello");// just for testing
		System.out.println("Please enter your name : ");
		System.out.println();
		String name = scanner.next();

		System.out.println("Hello " + name);
		System.out.println();
		System.out.println("This program allows swiftbot to detect the traffic lights and perform some actions!");
		System.out.println();

		System.out.println();
		System.out.println("Press button B for the instruction of the game...");

		// Above lines of code displays the welcome message

		API.enableButton(Button.B, () -> {

			System.out.println("Button B has been pressed");
			System.out.println();

			System.out.println("***INSTRUCTIONS***");
			System.out.println();
			System.out.println("Press button A to start the program!");
			System.out.println();
			System.out.println("Press button X for NO and to Quit the game!");
			System.out.println();
			System.out.println("Press button Y for YES or for positive responses!");
			System.out.println();

		}); // Setting button B for printing the instructions.
			// Added functionality to enhance the user experience

		// API.disableButton(Button.B);

		API.enableButton(Button.A, () -> {

			StartTime = System.currentTimeMillis(); // To record the start time of the execution
			System.currentTimeMillis();

			System.out.println("Button A has been pressed");
			System.out.println("Starting...");

			StartingSequence(); // A method which is called to execute the starting procedure of the Swiftbot.
			ImageCapturing(); // A method which is called to execute the Image capturing procedure.

		});
		// API.disableButton(Button.A);

		API.enableButton(Button.X, () -> { // Setting button X for Quitting the game.
			API.disableAllButtons();

			EndTime = System.currentTimeMillis(); // To record the end time of the execution.
			System.currentTimeMillis();

			API.stopMove();

			System.out.println("Button X has been pressed");
			System.out.println();
			System.out.println("You choose to quit the game!!!");
			System.out.println();
			System.out.println("Do you want to display the Log information?");
			System.out.println();
			System.out.println("Press button Y to display...");
			System.out.println();
			System.out.println("Press button X again to quit the game...");
			// The above lines of code gives the user a choice to display the log
			// information before terminating
			// the program

			// API.disableButton(Button.X);// to remove the previous function of this
			// button.

			API.enableButton(Button.X, () -> {

				System.out.println("Quitting the game!!!");
				System.out.println("Writing the Log to the file");
				Filewriter();
				System.out.println();
				System.out.println("See you again champ...");// displays terminating message to the user
				System.exit(0);

			});

			// API.disableButton(Button.X);

			API.enableButton(Button.Y, () -> {

				/*
				 * Setting button Y to display the log information. the button Y is embedded
				 * inside the button X. so the following lines of code will execute only if the
				 * user has pressed the button X before.
				 */

				System.out.println("Displaying the Log Information...");
				System.out.println();
				// System.out.println(StartTime - EndTime);

				LogInformation(); // Method called to execute the log information process

			});

			// API.disableButton(Button.Y);

		});

	}

	public static SwiftBotAPI GetAPI() { // to create a new API GetAPI
		// to be used in the multiple class with same function as the API. BOTH ARE SAME
		return (API);
	}

	public static void StartingSequence() {

		int[] colourToLightUp = { 255, 0, 255 };

		try {
			API.fillUnderlights(colourToLightUp);
		} catch (Exception e) {
			e.printStackTrace();
		} // set all the underlights to yellow

		ImageCapturing();
	}

	public static void ImageCapturing() {

		double distanceToObject = API.useUltrasound();

		try {

			API.startMove(40, 40);// initial sequence where swiftbot starts to move
			API.useUltrasound();// initial sequence where swiftbot detects objects
			// System.out.println(distanceToObject);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Ultrasound Unsuccessful");
//whenever ultrasound is unsuccessful it will display this message
		}

		if (distanceToObject <= 20) {
// if the distance between the object is <20 swiftbot will stop moving and detects the 
			// traffic lights
			API.stopMove();

			System.out.println("Detected");

			DetectedTrafficLightCount = DetectedTrafficLightCount + 1;

			try {
				img = API.takeStill(ImageSize.SQUARE_480x480);
				ImageIO.write(img, "jpg", new File("/home/pi/Documents/testImage.jpg"));

			} catch (Exception e) {// image capturing
				e.printStackTrace();
			}

			ImageToRGB();

		} else {

			ImageCapturing();// if distance between object is >20 swiftbot will continue moving

		}
	}

	/*
	 * The following method imagetoRGB converts the image taken by the swiftbot to
	 * RGB values by running it through a nested for loop
	 */
	public static void ImageToRGB() {

		int width = img.getWidth();
		int height = img.getHeight();
		int totalRed = 0;
		int totalGreen = 0;
		int totalBlue = 0;
		int pixelCount = 0;

		for (width = 0; width < img.getWidth(); ++width) {
			for (height = 0; height < img.getHeight(); ++height) {
				int p = img.getRGB(height, width);

				int r = (p >> 16) & 0xFF; // red pixels calculating
				int g = (p >> 8) & 0xFF; // green pixels calculating
				int b = p & 0xFF; // blue pixels calculating

				totalRed += r; // sum of all red pixels
				totalGreen += g; // sum of all green pixels
				totalBlue += b; // sum of all blue pixels

				pixelCount++; // total pixel count

				avgRed = totalRed / pixelCount;
				avgGreen = totalGreen / pixelCount;
				avgBlue = totalBlue / pixelCount;

			}

		}

		ColorDecision();

	}

	public static void ColorDecision() {

		if (avgRed > 130 && avgRed > avgGreen && avgRed > avgBlue) {
			System.out.println("Red color found...");

			TrafficLightActions.RedLightAction();
			// perform red color action

		} else if (avgGreen > 130 && avgGreen > avgBlue && avgGreen > avgRed) {
			System.out.println("Green color found...");

			TrafficLightActions.GreenLightAction();
			// perform green color action

		} else if (avgBlue > 130 && avgBlue > avgRed && avgBlue > avgGreen) {
			System.out.println("Blue color found...");

			TrafficLightActions.BlueLightAction();
			// perform blue color action

		} else {// if the colours are not valid it will print this message
			System.out.println("***ERROR***");
			System.out.println();
			System.out.println("Inavlid colour found....");
			System.out.println();
			System.out.println("Valid colors are RED, GREEN AND BLUE!!!");
// The above snippets are the error handling part to enhance the user interactions
			StartingSequence();
			ImageCapturing();
		}

	}

	public static String FrequentColor() {// this method will return the colour
		// based on the biggest colour count

		String color = "";

		if (RedLightCount > GreenLightCount && RedLightCount > BlueLightCount) {
			return color = "Red Traffic Light";
			// System.out.println("Red traffic light");
		} else if (GreenLightCount > RedLightCount && GreenLightCount > BlueLightCount) {
			return color = "Green traffic light";
			// System.out.println("Green traffic light");

		} else if (BlueLightCount > RedLightCount && BlueLightCount > GreenLightCount) {
			System.out.println("Blue traffic light");
			return color = "Blue traffic light";
		}

		return color;

	}

	public static int FrequentColorCount() {// this will return the number of times
		// frequent colour has been detected by the swiftbot

		int count = 0;

		if (RedLightCount > GreenLightCount && RedLightCount > BlueLightCount) {

			return count = RedLightCount;
		} else if (GreenLightCount > RedLightCount && GreenLightCount > BlueLightCount) {

			return count = GreenLightCount;

		} else if (BlueLightCount > RedLightCount && BlueLightCount > GreenLightCount) {
			return count = BlueLightCount;
		}
		return count;

	}

	public static String Filewriter() {
		FileWriter writehandle;
		try {

			writehandle = new FileWriter("/home/pi/Documents/file.txt");
			BufferedWriter bw = new BufferedWriter(writehandle);

			// String line = LogInformation();

			long milliseconds = EndTime - StartTime;
			long minutes = (milliseconds / 1000) / 60;// convert milliseconds to minutes
			long seconds = (milliseconds / 1000) % 60;// convert milliseconds to seconds

			bw.write("The Log Information");
			System.out.println();
			System.out.println();
			bw.newLine();

			bw.write("The number of times the Swiftbot encountered traffic lights = " + DetectedTrafficLightCount);
			System.out.println();
			bw.newLine();
			bw.write("The most frequent traffic light color the swiftbot encountered = " + FrequentColor());
			System.out.println();
			bw.newLine();
			bw.write("The number of times the most frequent traffic light was detected =" + FrequentColorCount());
			System.out.println();
			bw.newLine();
			String Duration = (minutes + " Minutes " + seconds + " Seconds");
			bw.newLine();
			bw.write("The total duration of the execution =" + Duration);

			bw.close();
			writehandle.close();// these will close the filewriter
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static void LogInformation() {

		System.out.println("The Log Information");

		System.out.println();
		System.out.println();

		System.out
				.println("The number of times the Swiftbot encountered traffic lights = " + DetectedTrafficLightCount);
		System.out.println();
		System.out.println("The most frequent traffic light color the swiftbot encountered =" + FrequentColor());
		System.out.println();
		System.out.println("The number of times the most frequent traffic light was detected =" + FrequentColorCount());
		System.out.println();
		long milliseconds = EndTime - StartTime;
		long minutes = (milliseconds / 1000) / 60;
		long seconds = (milliseconds / 1000) % 60;

		String Duration = (minutes + " Minutes " + seconds + " Seconds");

		System.out.println("The total duration of the execution =" + Duration);

	}
}
