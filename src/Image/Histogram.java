package Image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Histogram extends JPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private boolean accumulated;
	final int margin = 20;
	private int[][] hint;
	
	public Histogram(Image image, boolean accumulated) {
		this.image = image;
		this.accumulated = accumulated;
		hint = new int[256][5];
		this.addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int[] count = accumulated ? getHistogramAccumulated() : getHistogram();
		
		final int height = this.getSize().height;
		final int width = this.getSize().width;
		final int max = getMax(count);
		int yPos = height - margin - 2;
		float xPos = margin - 2;	
		float barWidth = ((float)width-(margin * 3))/(float)256;
		double blockHeight = ((float)max)/((float)height - (margin * 2));
		double barHeight;
		
		g.setColor(Color.BLACK);
		g.drawString(new Integer(max).toString(), (int)xPos - (new Integer(max).toString().length()/2 * 5), 15);
		g.drawString("0", (int)xPos - 10, yPos + 10);
		g.drawString("255", width - (margin + 2) - 10, yPos + 15);
		g.drawString("Pixel value", (width/2) - margin - 25, yPos + 17);
		Graphics2D g2=(Graphics2D)g.create();
		g2.rotate(Math.toRadians(-90));
		g2.drawString("Repetitions", -(height/2) - 25, 13);
		
		g.setColor(Color.BLUE);
		for(int k = 0; k < count.length; k++) {
			barHeight = ((double)count[k])/blockHeight;
			g.drawRect((int)xPos, yPos - (int)barHeight, (int)barWidth, (int)barHeight);
			xPos += barWidth;
			//hint[0] = ; hint[1] = ; hint[2] = ; hint[3] = ; hint[4] = ;
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(margin - 2, height - margin - 2, width - ((margin + 2) * 2), 2);
		g.fillRect(margin - 2, (height - margin - 2) - (height - ((margin + 2) * 2)), 2, height - ((margin + 2) * 2));
		
		g.dispose();
	}
	
	private int[] getHistogram() {
		byte[] pixel = image.getVector();
		int[] count = new int[256];
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF] += 1;
		}
		return count;
	}
	
	public int[] getHistogramAccumulated() {
		byte[] pixel = image.getVector();
		int[] count = new int[256];
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF] += 1;
		}
		for(int i = 1; i < count.length; i++) {
			count[i] += count[i-1];
		}
		return count;
	}
	
	private float[] getHistogramAccNormalized() {
		int[] histogramAcc = getHistogramAccumulated();
		float[] histogramAccNorm = new float[histogramAcc.length];
		int pixelNum = image.getVector().length;
		for(int i = 0; i < histogramAcc.length; i++) {
			histogramAccNorm[i] = histogramAcc[i]/pixelNum;
		}
		return histogramAccNorm;
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
	
	public BufferedImage converToImage() {
		BufferedImage finalImage = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB);  
		Graphics g = finalImage.getGraphics();  
		this.paintComponent(g); 
		g.dispose();
		
		return finalImage;
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}
}


