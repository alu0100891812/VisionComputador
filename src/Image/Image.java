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
	
	public int getRed(int x, int y) {
	    return (image.getRGB(x, y) >> 16) & 0xFF;
	}
	
	public int getBlue(int x, int y) {
	    return (image.getRGB(x, y) >> 8) & 0xFF;
	}
	
	public int getGreen(int x, int y) {
	    return (image.getRGB(x, y) >> 0) & 0xFF;
	}
}
