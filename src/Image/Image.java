package Image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Image {
	private BufferedImage image;
	
	public Image(BufferedImage buffImage) {
		image = buffImage;
	}
	
	public JPanel drawImage(int x, int y, int width, int height) {
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, x, y, width, height, null);
                g.dispose();
            }
        };
        return panel;
	}
	
	public Image RGBtoGray() {
		BufferedImage GrayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = GrayImage.getGraphics();  
		g.drawImage(image, 0, 0, null);  
		g.dispose();
		return new Image(GrayImage);
	}
	
	public int getRed(int x, int y) {
	    return (image.getRGB(x, y) >> 16) & 0xFF;
	}
	
	public int getBlue(int x, int y) {
	    return (image.getRGB(x, y) >> 8) & 0xFF;
	}
	
	public int getGreen(int x, int y) {
	    return (image.getRGB(x, y) >> 0) & 0xFF;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public void setColor(int x, int y, int rgb) {
		image.setRGB(x, y, rgb);
	}
}
