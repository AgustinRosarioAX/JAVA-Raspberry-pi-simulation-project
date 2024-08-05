//import swiftbot.SwiftBotAPI;

public class TrafficLightActions extends TrafficLightsTask {

	public static void GreenLightAction() {
		// SwiftBotAPI API = new SwiftBotAPI();

		GreenLightCount = GreenLightCount + 1;

		System.out.println("Green light found!!!");
		System.out.println("Performing green light manoeuvres...");

		int[] colourToLightUp = { 0, 0, 255 };
		try {
			TrafficLightsTask.GetAPI().fillUnderlights(colourToLightUp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			TrafficLightsTask.GetAPI().move(50, 50, 2000);// passes green light in 2 sec
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Passing the green light");
			TrafficLightsTask.GetAPI().stopMove();
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
		}

		StartingSequence();
		ImageCapturing();

	}

	public static void RedLightAction() {

		RedLightCount = RedLightCount + 1;

		System.out.println("Red light found!!!");
		System.out.println("Performing red light manoeuvres...");
		int[] colourToLightUp = { 255, 0, 0 };
		try {
			TrafficLightsTask.GetAPI().fillUnderlights(colourToLightUp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println("Stopping at the red light");

			// TrafficLightsTask.GetAPI().move(0, 0, 2000);
			TrafficLightsTask.GetAPI().stopMove();
			Thread.sleep(500);// stops at red light for half a sec

		} catch (Exception e) {
			e.printStackTrace();
		}

		StartingSequence();
		ImageCapturing();

	}

	public static void BlueLightAction() {

		BlueLightCount = BlueLightCount + 1;

		System.out.println("Blue light found!!!");
		System.out.println("Performing blue light manoeuvers...");
		int[] colourToLightUp = { 0, 255, 0 };

//			TrafficLightsTask.GetAPI().fillUnderlights(colourToLightUp);
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		try {

			TrafficLightsTask.GetAPI().stopMove();
			Thread.sleep(500);// stops at blue light for half a second

			System.out.println("Blinking the underlights in blue...");
			// to blink the under lights in blue

			TrafficLightsTask.GetAPI().fillUnderlights(colourToLightUp);

			Thread.sleep(200);

			TrafficLightsTask.GetAPI().disableUnderlights();

			TrafficLightsTask.GetAPI().fillUnderlights(colourToLightUp);

			Thread.sleep(200);

			TrafficLightsTask.GetAPI().disableUnderlights();

			Thread.sleep(200);

			TrafficLightsTask.GetAPI().fillUnderlights(colourToLightUp);

			Thread.sleep(200);

			TrafficLightsTask.GetAPI().disableUnderlights();

			TrafficLightsTask.GetAPI().move(0, 70, 1000);// Turning the swiftbot to 90
			// degrees.
			TrafficLightsTask.GetAPI().move(50, 50, 1000);
			TrafficLightsTask.GetAPI().stopMove();

			Thread.sleep(500);
//
			TrafficLightsTask.GetAPI().move(-50, -50, 1000);// retracing the path
			TrafficLightsTask.GetAPI().move(70, 0, 1000);

			// break;
			// }while(avgBlue > 130 && avgBlue > avgRed && avgBlue> avgGreen);

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		StartingSequence();
		ImageCapturing();

	}

}
