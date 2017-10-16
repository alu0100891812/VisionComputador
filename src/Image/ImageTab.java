package Image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ImageTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	private JPanel infoPanel;
	private String name;
	private boolean gray;
	
	public ImageTab(BufferedImage image, String name, boolean gray) {
		this.name = name;
		this.gray = gray;
		setUpInfo(image);
		this.setLayout(new BorderLayout());
		this.add(this.image, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.LINE_END);
	}
	
	private void setUpInfo(BufferedImage buffImage) {
		infoPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Font font = g.getFont();
				g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
				g.drawString("Pixel Position:", 40, 130);
				g.drawString("Pixel Color:", 40, 150);
				g.drawString("Image name:", 40, 40);
				g.drawString("Image extension:", 40, 60);
				g.drawString("Image width:", 40, 80);
				g.drawString("Image height:", 40, 100);
				if(gray) {
					g.drawString("Range Value:", 40, 170);
					g.drawString("Image entropy:", 40, 190);
				}
				g.drawString(name.substring(0, name.lastIndexOf('.')), 180, 40);
				g.drawString(name.substring(name.lastIndexOf('.') + 1), 180, 60);
				g.drawString(image.getImageWidth() + "px", 180, 80);
				g.drawString(image.getImageHeight() + "px", 180, 100);
				g.setFont(font);
				if(gray) {
					g.drawString(new RangeValue().calculateRange(image), 160, 170);
					g.drawString(image.getEntropy() + "", 160, 190);
				}
				int mX = image.getMousePixel().x;
				int mY = image.getMousePixel().y;
				Color color = image.getRGB(mX-1, mY-1);
				color = color == null ? new Color(255, 255, 255) : color;
				g.drawString("(" + mX + ", " + mY + ")", 160, 128);
				if(gray) {
					g.drawString(color.getRed() + "", 160, 150);
				}else {
					g.drawString(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue(), 160, 150);
				}
				g.setColor(color);
				g.fillRect(245, 107, 50, 50);
				g.setColor(Color.BLACK);
				g.drawRect(244, 106, 51, 51);
				g.dispose();
			}
		};
		image = new Image(buffImage, infoPanel);
		image.setInfo(infoPanel);
		infoPanel.setBorder(new EmptyBorder(0,3,0,3));
		infoPanel.setPreferredSize(new Dimension(300, getHeight()));
	}
	
	public Image getImage() {
		return image;
	}
	
	public String getImageName() {
		return name;
	}
}
