package Image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class Histogram extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private boolean accumulated;
	
	public Histogram(Image image, boolean accumulated) {
		this.image = image;
		this.accumulated = accumulated;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int[] contador = accumulated ? getHistogramAccumulated() : getHistogram();
		
		final int margin = 20;
		final int height = image.getSize().height;
		final int width = image.getSize().width;
		final int max = getMax(contador);
		int yPos = height - margin - 2;
		int xPos = margin - 2;		
		float barWidth = ((float)width - (margin * 2))/256;
		double blockHeight = ((float)max)/((float)height - (margin * 2));
		double barHeight;
		
		g.setColor(Color.RED);
		g.drawString(new Integer(max).toString(), xPos - (new Integer(max).toString().length()/2 * 5), 15);
		g.drawString("0", xPos - 10, yPos + 10);
		g.drawString("255", width - (margin + 2) - 10, yPos + 15);
		g.drawString("Pixel value", (width/2) - margin - 25, yPos + 17);
		Graphics2D g2=(Graphics2D)g.create();
		g2.rotate(Math.toRadians(-90));
		g2.drawString("Repetitions", -(height/2) - 25, 13);
		
		g.setColor(Color.DARK_GRAY);
		for(int k = 0; k < contador.length; k++) {
			barHeight = ((double)contador[k])/blockHeight;
			g.drawRect(xPos, yPos - (int)barHeight, (int)barWidth, (int)barHeight);
			xPos += barWidth;
		}
		
		g.setColor(Color.RED);
		g.fillRect(margin - 2, height - margin - 2, width - ((margin + 2) * 2), 2);
		g.fillRect(margin - 2, (height - margin - 2) - (height - ((margin + 2) * 2)), 2, height - ((margin + 2) * 2));
		
		g.dispose();
	}
	
	private int[] getHistogram() {
		byte[] pixel = image.getVector();
		int[] contador = new int[256];
		for(int i = 0; i < pixel.length; i++) {
			contador[pixel[i] & 0xFF] += 1;
		}
		return contador;
	}
	
	private int[] getHistogramAccumulated() {
		byte[] pixel = image.getVector();
		int[] contador = new int[256];
		for(int i = 0; i < pixel.length; i++) {
			contador[pixel[i] & 0xFF] += 1;
		}
		for(int i=1; i<contador.length; i++) {
			contador[i] += contador[i-1];
		}
		return contador;
	}
	
	private static int getMin(int[] vector) {
		int min = Integer.MAX_VALUE;
		for(int j = 0; j < 256; j++) {
			if (vector[j] < min) {
				min = vector[j];
			}			
		}
		return min;
	}
	
	private static int getMax(int[] vector) {
		int max = 0;
		for(int j = 0; j < 256; j++) {
			if(vector[j] > max) {
				max = vector[j];
			}			
		}
		return max;
	}
}


