package wavy;

import java.util.Scanner;

import wavy.signals.ConstantWave;
import wavy.sound.player.AudioPlayer;

public class App {

	public static void main(String[] args) throws InterruptedException {
		ConstantWave w = new ConstantWave(44100, 400);
		AudioPlayer player = new AudioPlayer(w);
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("P")) {
				player.play();
			}
			else if (line.startsWith("S")) {
				player.stop();
			}
			else if (line.startsWith("X")) {
				player.stop();
				break;
			}
		}
		
	}
	
}
