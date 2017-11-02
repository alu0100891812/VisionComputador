package Image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ImageTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JScrollPane scrollPane;
	private Image original;
	private Image image;
	private JPanel infoPanel;
	private String name;
	private boolean gray;
	
	public ImageTab(Image original, BufferedImage image, String name, boolean gray) {
		this.original = original;
		this.name = name;
		this.gray = gray;
		setUpInfo(image);
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gbl_contentPane);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridheight = 3;
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		this.image.setPreferredSize(new Dimension(this.image.getImageWidth()+this.image.marginX*2, this.image.getImageHeight()+this.image.marginY*2));
		scrollPane.setViewportView(this.image);
		scrollPane.setPreferredSize(new Dimension(this.getWidth()*3/4, this.getHeight()));
		this.add(scrollPane, gbc_scrollPane);
		
		GridBagConstraints gbc_infoPane = new GridBagConstraints();
		gbc_infoPane.insets = new Insets(0, 0, 5, 5);
		gbc_infoPane.fill = GridBagConstraints.BOTH;
		gbc_infoPane.gridx = 3;
		gbc_infoPane.gridy = 0;
		gbc_infoPane.gridheight = 3;
		this.add(infoPanel, gbc_infoPane);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		scrollPane.repaint();		
		if(getWidth() > 1200) {
			infoPanel.setPreferredSize(new Dimension(450, infoPanel.getHeight()));
		}else {
			infoPanel.setPreferredSize(new Dimension(300, infoPanel.getHeight()));
		}
		infoPanel.repaint();
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
					g.drawString("Dinamic Range Value:", 40, 190);
					g.drawString("Image entropy:", 40, 210);
					g.drawString("Image Brightness:", 40, 230);
					g.drawString("Image Contrast:", 40, 250);
				}
				g.drawString(name.substring(0, name.lastIndexOf('.')), 180, 40);
				g.drawString(name.substring(name.lastIndexOf('.') + 1), 180, 60);
				g.drawString(image.getImageWidth() + "px", 180, 80);
				g.drawString(image.getImageHeight() + "px", 180, 100);
				g.setFont(font);
				if(gray) {
					g.drawString(new RangeValue().calculateRange(image), 160, 170);
					g.drawString(image.getDinamicRange() + "", 200, 190);
					g.drawString(image.getEntropy() + "", 160, 210);
					g.drawString(image.getBrightness() + "", 180, 230);
					g.drawString(image.getContrast() + "", 180, 250);
				}
				int mX = image.getMousePixel().x;
				int mY = image.getMousePixel().y;
				Color color = image.getRGB(mX, mY);
				color = color == null ? new Color(255, 255, 255) : color;
				g.drawString("(" + mX + ", " + mY + ")", 160, 128);
				if(gray) {
					g.drawString((image.getVector()[image.getImageWidth()*mY+mX] & 0xFF) + "", 160, 150);
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
		infoPanel.setPreferredSize(new Dimension(250, getHeight()));
	}
	
	public Image getImage() {
		return image;
	}
	
	public String getImageName() {
		return name;
	}
	
	public Image getOrigin() {
		return original;
	}
}
