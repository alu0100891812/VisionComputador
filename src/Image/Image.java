package Image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

public class Image extends JPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private Point MousePosition;
	private JPanel infoPanel;
	private int marginY, marginX;
	
	public Image(BufferedImage buffImage) {
		new Image(buffImage, new JPanel());
	}
	
	public Image(BufferedImage buffImage, JPanel info) {
		image = buffImage;
		infoPanel = info;
		marginX = 20;
		marginY = 20;
		MousePosition = new Point();
		this.addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, marginX, marginY, image.getWidth(), image.getHeight(), null);
		g.dispose();
	}
	
	public BufferedImage RGBtoGray() {
		BufferedImage GrayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = GrayImage.getGraphics();  
		g.drawImage(image, 0, 0, null);  
		g.dispose();
		return GrayImage;
	}
	
	public Image EcualizedImage() {
		Image EcualizedImage = new Image(new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
		int[] vectorTrans = new Histogram(this, true).getVectorEcualized();
		int[] vectorImage = ((DataBufferInt)EcualizedImage.getBufferedImage().getRaster().getDataBuffer()).getData();
		for(int i = 0; i < vectorImage.length; i++) {
			vectorImage[i] = vectorTrans[i]; //Image = Vout
		}
		EcualizedImage.getBufferedImage().getRaster().setPixels(0,0,image.getWidth(), image.getHeight(),vectorImage);
		return EcualizedImage;
	}
	
	public Point getMousePixel() { 
		return MousePosition;
	}
	
	public void setInfo(JPanel info) {
		infoPanel = info;
	}
	
	public int getRed(int x, int y) {
	    return (image.getRGB(x, y) & 0x00FF0000) >> 16 ;
	}
	
	public int getGreen(int x, int y) {
		return (image.getRGB(x, y) & 0x0000FF00) >> 8 ;
	}
	
	public int getBlue(int x, int y) {
		return (image.getRGB(x, y) & 0x000000FF);
	}
	
	public Color getRGB(int x, int y) {
		if(x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight())
			return new Color(image.getRGB(x, y));
		return null;
	}
	
	public int getImageWidth() {
		return image.getWidth();
	}
	
	public int getImageHeight() {
		return image.getHeight();
	}
	
	public int getWidth() {
		return this.getSize().width;	
	}
	
	public int getHeight() {
		return this.getSize().height;	
	}
	
	public void setColor(int x, int y, int rgb) {
		image.setRGB(x, y, rgb);
	}
	
	public BufferedImage getBufferedImage() {
		return image;
	}
	
	public byte[] getVector() {
		byte[] vector = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		return vector;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		if(x > marginX && x <= (image.getWidth() + marginX) && y > marginY && y <= (image.getHeight() + marginY)) {
			MousePosition.setLocation(x - marginX, y - marginY);		
			infoPanel.repaint();
		}
	}
	
	//Media
	public int getBrightness() {
		byte[] pixel = getVector();
		int[] count = new int[256];
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF] += 1;
		}		
		int[] histogram = count;
		int pixelNum = pixel.length;
		double acc = 0;
		for(int i = 0; i < histogram.length; i++) {
			acc += histogram[i]*i;
		}
		acc = acc / pixelNum;
		acc = Math.round(acc);
		return (int)acc;
	}
	
	//Desviación típica: Raiz cuadrada de la varianza
	public int getContrast() {
		byte[] pixel = getVector();
		double media = getBrightness();
		int pixelNum = pixel.length;
		double varianza = 0;
		double desvTipica = 0;
		for(int i = 0; i < pixel.length; i++) {
			varianza += Math.pow(((pixel[i] & 0xFF) - media),2);
		}	
		varianza = varianza / pixelNum;
		desvTipica = Math.sqrt(varianza);
		desvTipica = Math.round(desvTipica);
		return (int)desvTipica;
	}
	
	public float getEntropy() {
		byte[] pixel = getVector();
		int[] count = new int[256];
		float cont = 0;
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF] += 1;
		}
		for(int i=0; i<count.length; i++) {
			if(count[i] != 0) cont++;
		}
		return cont/256;
	}
	
	public void setPixelWithValue(int from, int to, int value) {
		byte[] vector = getVector();
		for(int i=0; i<vector.length; i++) {
			if((vector[i] & 0xFF) >= from && (vector[i] & 0xFF) <= to) {
				image.getRaster().getDataBuffer().setElem(i, value);
			}
		}
	}
	
	public void setPixelWithValue(int from, int value) {
		byte[] vector = getVector();
		for(int i=0; i<vector.length; i++) {
			if((vector[i] & 0xFF) == from) {
				image.getRaster().getDataBuffer().setElem(i, value);
			}
		}
	}

}
