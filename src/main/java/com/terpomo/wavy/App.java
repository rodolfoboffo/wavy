package com.terpomo.wavy;

import com.terpomo.wavy.signals.ConstantWave;
import com.terpomo.wavy.signals.operations.Multiplication;
import com.terpomo.wavy.sound.player.AudioPlayerPipe;

public class App {

	public static void main(String[] args) throws InterruptedException {
		int SAMPLE_RATE = 44100;
		ConstantWave cw = new ConstantWave(SAMPLE_RATE, 400);
		ConstantWave s = new ConstantWave(SAMPLE_RATE, 1);
		Multiplication w = new Multiplication(cw, s);
		AudioPlayerPipe player = new AudioPlayerPipe(1, SAMPLE_RATE);
		player.play();
//		Scanner scanner = new Scanner(System.in);
//		while(scanner.hasNextLine()) {
//			String line = scanner.nextLine();
//			if (line.startsWith("P")) {
//				player.play();
//			}
//			else if (line.startsWith("S")) {
//				player.stop();
//			}
//			else if (line.startsWith("X")) {
//				player.stop();
//				break;
//			}
//		}
//		scanner.close();
	}
	
}
