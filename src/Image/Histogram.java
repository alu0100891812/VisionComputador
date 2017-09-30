package Image;

import java.awt.Color;
import java.awt.Graphics;

public class Histogram {
	
	public Histogram() {
	}
	
	public void draw(Image image) {
		byte[] pixel = image.getVector();
		int[] contador = new int[256];
		int max = 0;
		int min = pixel.length;
		int n = 0;
		for(int i = 0; i < pixel.length; i++) {
			n = pixel[i] & 0xFF;
			contador[n] += 1;
		}
		for(int j = 0; j < 256; j++) {
			if(contador[j] > max) {
				max = contador[j];
			} else if (contador[j] < min) {
				min = contador[j];
			}			
		}
		
		int height = image.getSize().height;
		int yPos = 20;
		int xPos = 20;		
		int barWidth = (image.getSize().width - 40)/256;
		int blockHeight = (height-40)/max;
		int barHeight;
		Graphics g2d = image.getGraphics();
		for(int k = 0; k < contador.length; k++) {
			barHeight = contador[k]*blockHeight;
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(xPos, yPos, barWidth, barHeight);
			xPos += barWidth;
		}			
	} 
}


