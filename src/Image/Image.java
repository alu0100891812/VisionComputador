package Image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

public class Image extends JPanel implements MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private Point MousePosition;
	private JPanel infoPanel;
	public int marginY, marginX;
	public final double id;
	
	public Image(BufferedImage buffImage) {
		this(buffImage, new JPanel());
	}
	
	public Image(BufferedImage buffImage, JPanel info) {
		image = buffImage;
		infoPanel = info;
		marginX = 20;
		marginY = 20;
		MousePosition = new Point();
		this.addMouseMotionListener(this);
		id = Math.random();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, marginX, marginY, image.getWidth(), image.getHeight(), null);
	}
	
	public BufferedImage RGBtoGray() {
		BufferedImage GrayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = GrayImage.getGraphics();  
		g.drawImage(image, 0, 0, null);  
		g.dispose();
		return GrayImage;
	}
	
	public Image getCopy() {
		BufferedImage CopyImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());  
		Graphics g = CopyImage.getGraphics();  
		g.drawImage(image, 0, 0, null);  
		g.dispose();
		return new Image(CopyImage);
	}
	
	public Image EcualizedImageFromImageVector(int[] vectorAcc) {
		Image EcualizedImage = new Image(new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
		int[] lookUpTable = new int[256];
		byte[] vectorImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vectorImage = new Histogram(this, false).getHistogramAccumulated();
		int[] vectorEcc = new int[vectorImg.length];		
				
		for(int i = 0; i < vectorImage.length; i++) {
			for(int j = 0; j < vectorAcc.length-1; j++) {
				if((vectorImage[i] > vectorAcc[j]) && (vectorImage[i] <= vectorAcc[j+1])) {
					if((vectorImage[i]-vectorAcc[j]) > (vectorAcc[j+1]-vectorImage[i])) {
						lookUpTable[i] = j + 1;
					} else {
						lookUpTable[i] = j;
					}
				} 
			}
		}
		for(int i = 0; i < vectorImg.length; i++) {
			vectorEcc[i] = lookUpTable[vectorImg[i] & 0xFF];
		}	
		EcualizedImage.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vectorEcc);
		return EcualizedImage;
	}
	
	public Image EcualizedImage() {
		float size = (image.getWidth())*(image.getHeight());
		int[] vectorAcc = new int[256];
		for(int i = 0; i < vectorAcc.length; i++) {
			vectorAcc[i] = (int)((float)(i + 1) * (size / (float)256));
		}
		return EcualizedImageFromImageVector(vectorAcc);
	}
	
	public Image EcualizedImageFromImage(Image image) {
		return EcualizedImageFromImageVector(new Histogram(image, false).getHistogramAccumulated());
	}
	
	public void BCImage(int brightness, int contrast) { 
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vResult = new int[vImg.length];
        float contrastPrev = (float)getContrast();
        float brightnessPrev = (float)getBrightness();
        float a = ((float)contrast) / contrastPrev;
        float b = ((float)brightness) - (a * brightnessPrev);
        for(int i = 0; i < vImg.length; i++) {
        	vResult[i] = Truncate((int)((a * (vImg[i] & 0xFF)) + b));
        }
        this.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
    }
	
	public Image DiffImage(Image newImage) {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		byte[] vImgNow = ((DataBufferByte)newImage.getBufferedImage().getRaster().getDataBuffer()).getData();
		int[] vResult = new int[vImg.length];
		
		if(vImg.length == vImgNow.length) {
			for(int i = 0; i < vImg.length; i++) {
				vResult[i] = Truncate((vImg[i] & 0xFF) - (vImgNow[i] & 0xFF));
			}
		}
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
	    result.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
		return result;
	}
	
	public Image DiffImageChangesMap(Image newImage, int threshold) {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		byte[] vImgNow = ((DataBufferByte)newImage.getBufferedImage().getRaster().getDataBuffer()).getData();
		int[] vResult = new int[3*vImg.length];
		int indexResult = 0;
		
		if(vImg.length == vImgNow.length) {
			for(int i = 0; i < vImg.length; i++) {
				if(Math.abs((vImg[i] & 0xFF) - (vImgNow[i] & 0xFF)) < threshold) { 
					vResult[indexResult] = vResult[indexResult+1] = vResult[indexResult+2] = (vImg[i] & 0xFF);
				} else {
					vResult[indexResult] = 255;
				}
				indexResult += 3;
			}
		}
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
	    result.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
	    FilterNoise(result, newImage);
		return result;
	}
	
	public Image DiffImageHSBChangesMap(Image newImage) {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		byte[] vImgNow = ((DataBufferByte)newImage.getBufferedImage().getRaster().getDataBuffer()).getData();
		int[] vResult = new int[3*vImg.length];
		int indexResult = 0;
		
		if(vImg.length == vImgNow.length) {
			for(int i = 0; i < vImg.length; i++) {
				Color col1 = new Color(image.getRGB(i%image.getWidth(),i/image.getWidth()));
				Color col2 = new Color(newImage.getBufferedImage().getRGB(i%newImage.getImageWidth(),i/newImage.getImageWidth()));
				float[] hsb1 = Color.RGBtoHSB(col1.getRed(), col1.getGreen(), col1.getBlue(), null);
				float[] hsb2 = Color.RGBtoHSB(col2.getRed(), col2.getGreen(), col2.getBlue(), null);
				if(Math.abs(hsb1[2] - hsb2[2]) < 0.45) {
					vResult[indexResult] = vResult[indexResult+1] = vResult[indexResult+2] = (vImg[i] & 0xFF);
				} else {
					vResult[indexResult] = 255;
				}
				indexResult += 3;
			}
		}
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
	    result.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
	    FilterNoise(result, newImage);
		return result;
	}
	
	public void FilterNoise(Image diffImage, Image original) {
		int[] vImg = ((DataBufferInt)diffImage.getBufferedImage().getRaster().getDataBuffer()).getData();
		byte[] vImgOriginal = ((DataBufferByte)original.getBufferedImage().getRaster().getDataBuffer()).getData();
		int[] vImgMod = new int[3*diffImage.getImageWidth()*diffImage.getImageHeight()];
		
		for(int i = 0; i < vImg.length; i++) {
			if(vImg[i] == 16711680) {
				if((i%diffImage.getImageWidth() != (diffImage.getImageWidth()-1) && vImg[i+1] != 16711680) || (i%diffImage.getImageWidth() > 0 && vImg[i-1] != 16711680) || (i/diffImage.getImageWidth() > 0 && vImg[i-diffImage.getImageWidth()] != 16711680) || (i/diffImage.getImageWidth() < (diffImage.getImageHeight()-1) && vImg[i+diffImage.getImageWidth()] != 16711680)) {
					vImgMod[i*3+1] = 255;
				}else {
					vImgMod[i*3] = 255;
				}
			}else {
				vImgMod[i*3] = vImgMod[i*3+1] = vImgMod[i*3+2] = vImg[i] % 256;
			}
		}
		diffImage.getBufferedImage().getRaster().setPixels(0, 0, diffImage.getImageWidth(), diffImage.getImageHeight(), vImgMod);

		vImg = ((DataBufferInt)diffImage.getBufferedImage().getRaster().getDataBuffer()).getData();
		int[] vImgMod2 = new int[3*diffImage.getImageWidth()*diffImage.getImageHeight()];
		List<List<Integer>> vBlocks = new ArrayList<List<Integer>>();
		int maxJumps = 20;
		
		for(int i = 0; i < vImg.length; i++) {
			if(vImg[i] == 65280 && !(vImgMod[i*3] == 0 && vImgMod[i*3+1] == 0 && vImgMod[i*3+2] == 255)) {
				List<Integer> listaInicial = new ArrayList<Integer>();
				List<Integer> listaParsed = new ArrayList<Integer>();
				listaInicial.add(i);
				vImgMod[listaInicial.get(0)*3] = vImgMod[listaInicial.get(0)*3+1] = 0; vImgMod[listaInicial.get(0)*3+2] = 255;
				int jumps = 0;
				while(!listaInicial.isEmpty()) {
					if(listaInicial.get(0)%diffImage.getImageWidth() != (diffImage.getImageWidth()-1) && (vImg[listaInicial.get(0)+1] == 16711680 || vImg[listaInicial.get(0)+1] == 65280) && (vImgMod[(listaInicial.get(0)+1)*3] != 0 || vImgMod[(listaInicial.get(0)+1)*3+1] != 0 || vImgMod[(listaInicial.get(0)+1)*3+2] != 255)) {
						listaInicial.add(listaInicial.get(0)+1);
						vImgMod[listaInicial.get(listaInicial.size()-1)*3] = vImgMod[listaInicial.get(listaInicial.size()-1)*3+1] = 0; vImgMod[listaInicial.get(listaInicial.size()-1)*3+2] = 255;
						if(vImg[listaInicial.get(listaInicial.size()-1)] == 16711680) { jumps++; }
					}
					if(listaInicial.get(0)%diffImage.getImageWidth() > 0 && (vImg[listaInicial.get(0)-1] == 16711680 || vImg[listaInicial.get(0)-1] == 65280) && (vImgMod[(listaInicial.get(0)-1)*3] != 0 || vImgMod[(listaInicial.get(0)-1)*3+1] != 0 || vImgMod[(listaInicial.get(0)-1)*3+2] != 255)) {
						listaInicial.add(listaInicial.get(0)-1);
						vImgMod[listaInicial.get(listaInicial.size()-1)*3] = vImgMod[listaInicial.get(listaInicial.size()-1)*3+1] = 0; vImgMod[listaInicial.get(listaInicial.size()-1)*3+2] = 255;
						if(vImg[listaInicial.get(listaInicial.size()-1)] == 16711680) { jumps++; }
					}
					if(listaInicial.get(0)/diffImage.getImageWidth() > 0 && (vImg[listaInicial.get(0)-diffImage.getImageWidth()] == 16711680 || vImg[listaInicial.get(0)-diffImage.getImageWidth()] == 65280) && (vImgMod[(listaInicial.get(0)-diffImage.getImageWidth())*3] != 0 || vImgMod[(listaInicial.get(0)-diffImage.getImageWidth())*3+1] != 0 || vImgMod[(listaInicial.get(0)-diffImage.getImageWidth())*3+2] != 255)) {
						listaInicial.add(listaInicial.get(0)-diffImage.getImageWidth());
						vImgMod[listaInicial.get(listaInicial.size()-1)*3] = vImgMod[listaInicial.get(listaInicial.size()-1)*3+1] = 0; vImgMod[listaInicial.get(listaInicial.size()-1)*3+2] = 255;
						if(vImg[listaInicial.get(listaInicial.size()-1)] == 16711680) { jumps++; }
					}
					if(listaInicial.get(0)/diffImage.getImageWidth() < (diffImage.getImageHeight()-1) && (vImg[listaInicial.get(0)+diffImage.getImageWidth()] == 16711680 || vImg[listaInicial.get(0)+diffImage.getImageWidth()] == 65280) && (vImgMod[(listaInicial.get(0)+diffImage.getImageWidth())*3] != 0 || vImgMod[(listaInicial.get(0)+diffImage.getImageWidth())*3+1] != 0 || vImgMod[(listaInicial.get(0)+diffImage.getImageWidth())*3+2] != 255)) {
						listaInicial.add(listaInicial.get(0)+diffImage.getImageWidth());
						vImgMod[listaInicial.get(listaInicial.size()-1)*3] = vImgMod[listaInicial.get(listaInicial.size()-1)*3+1] = 0; vImgMod[listaInicial.get(listaInicial.size()-1)*3+2] = 255;
						if(vImg[listaInicial.get(listaInicial.size()-1)] == 16711680) { jumps++; }
					}
					listaParsed.add(listaInicial.get(0));
					listaInicial.remove(0);
				}
				if(jumps < maxJumps) {
					for(Iterator<Integer> j=listaParsed.iterator(); j.hasNext(); ) {
						Integer val = j.next();
						vImgMod2[val*3] = vImgMod2[val*3+1] = vImgMod2[val*3+2] = vImgOriginal[val] % 256;
					}
				}else {
					vBlocks.add(listaParsed);
					for(Iterator<Integer> j=listaParsed.iterator(); j.hasNext(); ) {
						Integer val = j.next();
						vImgMod2[val*3] = vImgMod2[val*3+2] = 0; vImgMod2[val*3+1] = 255;
					}
				}
			}else if(vImg[i] == 16711680 && vImgMod2[i*3] != vImgOriginal[i] % 256) {
				vImgMod2[i*3] = 255; vImgMod2[i*3+1] = vImgMod2[i*3+2] = 0;
			}else if(!(vImgMod[i*3] == 0 && vImgMod[i*3+1] == 0 && vImgMod[i*3+2] == 255)){
				vImgMod2[i*3] = vImgMod2[i*3+1] = vImgMod2[i*3+2] = vImgOriginal[i] % 256;
			}
		}		
		diffImage.getBufferedImage().getRaster().setPixels(0, 0, diffImage.getImageWidth(), diffImage.getImageHeight(), vImgMod2);
	}

	public void GammaCImage(Double value) {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		double[] vImgNormalized = new double[vImg.length];
		int[] vResult = new int[vImg.length];
		double acc = 0;
		for(int i = 0; i < vImg.length; i++) {
			vImgNormalized[i] = (double)(vImg[i] & 0xFF) / (double)255;
		}		
		for(int j = 0; j < vImgNormalized.length; j++) {
			acc = Math.pow(vImgNormalized[j], value);
			vResult[j] = (int)(acc * (double)255);
		}
		this.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
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
	
	public int[] getIntVector() {
		int[] vector = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		return vector;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		if(x >= marginX && x < (image.getWidth() + marginX) && y >= marginY && y < (image.getHeight() + marginY)) {
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
	
	//Desviaci�n t�pica: Raiz cuadrada de la varianza
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
	
	public String getEntropy() {
		byte[] pixel = getVector();
		int[] count = new int[256];
		float cont = 0;
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF] += 1;
		}
		for(int i=0; i<count.length; i++) {
			if(count[i] != 0) cont++;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		double x = (double)(Math.log(cont) / Math.log(2));
		return df.format(x);	
	}
	
	public float getDinamicRange() {
		byte[] pixel = getVector();
		int[] count = new int[256];
		float cont = 0;
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF] += 1;
		}
		for(int i=0; i<count.length; i++) {
			if(count[i] != 0) cont++;
		}
		return cont;
	}
	
	public void setPixelWithValue(byte[] vector, int from, int to, int value) {
		for(int i=0; i<vector.length; i++) {
			if((vector[i] & 0xFF) >= from && (vector[i] & 0xFF) <= to) {
				image.getRaster().getDataBuffer().setElem(i, value);
			}
		}
	}
	
	public void setPixelWithValue(byte[] vector, int from, float value) {
		for(int i=0; i<vector.length; i++) {
			if((vector[i] & 0xFF) == from) {
				image.getRaster().getDataBuffer().setElem(i, (int)value);
			}
		}
	}
	
	public int Truncate(int value) {
		if(value < 0)
			value = 0;
		if(value > 255) 
			value = 255;
		return value;
	}
	
	public Image digitalization(int bloque, int bits) {
		byte[] vImg = this.getVector();
		int[] vNewImg = new int[vImg.length];
		int[] newColor = new int[256];
		int numColor = (int)256/(int)Math.pow(2, bits);
		
		for(int i=0; i<256; i++) {
			if(i%numColor == numColor-1) {
				newColor[i] = i;
			}else {
				newColor[i] = i-(i%numColor)-1;
				if(i-(i%numColor)-1 < 0) newColor[i] = 0;
			}
		}		
		for(int i=0, j=0; i<vNewImg.length/(bloque*bloque); i++, j+=bloque) {
			int avg = 0;
			for(int x=0; x<bloque; x++) {
				for(int y=0; y<bloque; y++) {
					avg += vImg[j+(image.getWidth()*y)+x];
				}
			}
			avg /= bloque*bloque;
			for(int x=0; x<bloque; x++) {
				for(int y=0; y<bloque; y++) {
					vNewImg[j+(image.getWidth()*y)+x] = avg;
				}
			}
			if(j%image.getWidth()==0&&i!=0) j+=(bloque-1)*image.getWidth();
		}
		for(int i=0; i<vNewImg.length; i++) {
			vNewImg[i] = newColor[(vNewImg[i] & 0xFF)];
		}
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
		result.getBufferedImage().getRaster().setPixels(0, 0, result.getImageWidth(), result.getImageHeight(), vNewImg);
		return result;
	}
	
	public Image flipVerticalColor() {
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
		BufferedImage buffer = result.getBufferedImage();
				
		for(int i=0; i<image.getHeight(); i++) {
			for(int j=0; j<image.getWidth(); j++) {
				buffer.setRGB(j, (image.getHeight()-i-1), image.getRGB(j, i));
			}
		}		
		return result;		
	}
	
	public Image flipVerticalGray() {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vResult = new int[vImg.length];
		
		for(int i=0; i<image.getHeight(); i++) {
			for(int j=0; j<image.getWidth(); j++) {
				vResult[(image.getHeight()-i-1)*image.getWidth()+j] = vImg[(i*image.getWidth())+j] & 0xFF;
			}
		}
		
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
	    result.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
		return result;		
	}
	
	public Image flipHorizontalColor() {
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
		BufferedImage buffer = result.getBufferedImage();
				
		for(int i=0; i<image.getWidth(); i++) {
			for(int j=0; j<image.getHeight(); j++) {
				buffer.setRGB((image.getWidth()-i-1), j, image.getRGB(i, j));
			}
		}		
		return result;		
	}
	
	public Image flipHorizontalGray() {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vResult = new int[vImg.length];
		
		for(int i=0; i<image.getWidth(); i++) {
			for(int j=0; j<image.getHeight(); j++) {
				vResult[(image.getWidth()-i-1)+(image.getWidth()*j)] = vImg[i+(image.getWidth()*j)] & 0xFF;
			}
		}
		
		Image result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
	    result.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
		return result;		
	}
	
	public Image transposeColor() {
		Image result = new Image(new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB));
		BufferedImage buffer = result.getBufferedImage();
				
		for(int i=0; i<image.getHeight(); i++) {
			for(int j=0; j<image.getWidth(); j++) {
				buffer.setRGB(i, j, image.getRGB(j, i));
			}
		}		
		return result;		
	}
	
	public Image transposeGray() {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vResult = new int[vImg.length];
		
		for(int i=0; i<image.getHeight(); i++) {
			for(int j=0; j<image.getWidth(); j++) {
				vResult[(j*image.getHeight())+i] = vImg[(i*image.getWidth())+j] & 0xFF;
			}
		}
		
		Image result = new Image(new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_BYTE_GRAY));
	    result.getBufferedImage().getRaster().setPixels(0, 0, image.getHeight(), image.getWidth(), vResult);
		return result;		
	}
	
	public Image rotateColor(int degrees) {				
		switch(degrees) {
		case 90:
			Image result = new Image(new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB));
			BufferedImage buffer = result.getBufferedImage();
			for(int i=0; i<image.getHeight(); i++) {
				for(int j=0; j<image.getWidth(); j++) {
					buffer.setRGB(i, image.getWidth()-j-1, image.getRGB(j, i));
				}
			}
			return result;
		case 180:
			result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
			buffer = result.getBufferedImage();
			for(int i=0; i<image.getHeight(); i++) {
				for(int j=0; j<image.getWidth(); j++) {
					buffer.setRGB((image.getWidth()-j-1), (image.getHeight()-i-1), image.getRGB(j, i));
				}
			}				
			return result;
		case 270:
			result = new Image(new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_INT_RGB));
			buffer = result.getBufferedImage();
			for(int i=0; i<image.getHeight(); i++) {
				for(int j=0; j<image.getWidth(); j++) {
					buffer.setRGB((image.getHeight()-i-1), j, image.getRGB(j, i));
				}
			}
			return result;
		}				
		return null;
	}
	
	public Image rotateGray(int degrees) {
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vResult = new int[vImg.length];
		switch(degrees) {
		case 90:
			Image result = new Image(new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_BYTE_GRAY));
			for(int i=0; i<image.getHeight(); i++) {
				for(int j=0; j<image.getWidth(); j++) {
					vResult[i+((image.getWidth()-j-1)*image.getHeight())] = vImg[(i*image.getWidth())+j];
				}
			}
			result.getBufferedImage().getRaster().setPixels(0, 0, image.getHeight(), image.getWidth(), vResult);
			return result;
		case 180:
			result = new Image(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY));
			for(int i=0; i<image.getHeight(); i++) {
				for(int j=0; j<image.getWidth(); j++) {
					vResult[((image.getHeight()-i)*image.getWidth())-j-1] = vImg[(i*image.getWidth())+j];
				}
			}				
			result.getBufferedImage().getRaster().setPixels(0, 0, image.getWidth(), image.getHeight(), vResult);
			return result;
		case 270:
			result = new Image(new BufferedImage(image.getHeight(), image.getWidth(), BufferedImage.TYPE_BYTE_GRAY));
			for(int i=0; i<image.getHeight(); i++) {
				for(int j=0; j<image.getWidth(); j++) {
					vResult[(image.getHeight()-i-1)+(j*image.getHeight())] = vImg[(i*image.getWidth())+j];
				}
			}
			result.getBufferedImage().getRaster().setPixels(0, 0, image.getHeight(), image.getWidth(), vResult);
			return result;
		}				
		return null;	
	}
	
	public Image scaleVMPColor(float fcX, float fcY, int percen) {
		float factorX, factorY;
		BufferedImage scaledImg;
		if(percen == 0) {
			factorX = fcX/((float)100);
			factorY = fcY/((float)100);
			scaledImg = new BufferedImage((int)(image.getWidth()*factorX),(int)(image.getHeight()*factorY), image.getType());
		}else {
			factorX = fcX/((float)image.getWidth());
			factorY = fcY/((float)image.getHeight());
			scaledImg = new BufferedImage((int)(fcX),(int)(fcY), image.getType());
		}		
		for(int i=0;i<scaledImg.getHeight();i++) {
			for(int j=0;j<scaledImg.getWidth();j++) {
				scaledImg.setRGB(j,i,image.getRGB((int)((j+0.5)/factorX), (int)((i+0.5)/factorY)));
			}
		}
		return new Image(scaledImg);
	}
	
	public Image scaleVMPGray(float fcX, float fcY, int percen) {
		float factorX, factorY;
		BufferedImage scaledImg;
		if(percen == 0) {
			factorX = fcX/((float)100);
			factorY = fcY/((float)100);
			scaledImg = new BufferedImage((int)(image.getWidth()*factorX),(int)(image.getHeight()*factorY), BufferedImage.TYPE_BYTE_GRAY);
		}else {
			factorX = fcX/((float)image.getWidth());
			factorY = fcY/((float)image.getHeight());
			scaledImg = new BufferedImage((int)(fcX),(int)(fcY), BufferedImage.TYPE_BYTE_GRAY);
		}
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vScaledImg = new int[scaledImg.getWidth()*scaledImg.getHeight()];
		for(int i=0;i<scaledImg.getHeight();i++) {
			for(int j=0;j<scaledImg.getWidth();j++) {
				vScaledImg[i*scaledImg.getWidth() + j] = vImg[(int)((i+0.5)/factorY)*image.getWidth() + (int)((j+0.5)/factorX)] & 0xFF;
			}
		}
		scaledImg.getRaster().setPixels(0, 0, scaledImg.getWidth(), scaledImg.getHeight(), vScaledImg);
		return new Image(scaledImg);
	}
	
	public Image scaleBilinearColor(float fcX, float fcY, int percen) {
		float factorX, factorY;
		BufferedImage scaledImg;
		if(percen == 0) {
			factorX = fcX/((float)100);
			factorY = fcY/((float)100);
			scaledImg = new BufferedImage((int)(image.getWidth()*factorX),(int)(image.getHeight()*factorY), image.getType());
		}else {
			factorX = fcX/((float)image.getWidth());
			factorY = fcY/((float)image.getHeight());
			scaledImg = new BufferedImage((int)(fcX),(int)(fcY), image.getType());
		}
		for(int i=0;i<scaledImg.getHeight();i++) {
			for(int j=0;j<scaledImg.getWidth();j++) {
				int x = (int)((j+0.5)/factorX);
				int y = (int)((i+0.5)/factorY);
				float p = (float)(((j+0.5)/factorX) - (int)((j+0.5)/factorX));
				float q = (float)(((i+0.5)/factorY) - (int)((i+0.5)/factorY));
				int A = image.getRGB(x==0?x:x-1, y==0?y:y-1);
				int Ar = (A>>16) & 0xFF, Ag = (A>>8) & 0xFF, Ab = A & 0xFF;
				int B = image.getRGB(x, y==0?y:y-1);
				int Br = (B>>16) & 0xFF, Bg = (B>>8) & 0xFF, Bb = B & 0xFF;
				int C = image.getRGB(x==0?x:x-1, y);
				int Cr = (C>>16) & 0xFF, Cg = (C>>8) & 0xFF, Cb = C & 0xFF;
				int D = image.getRGB(x, y);
				int Dr = (D>>16) & 0xFF, Dg = (D>>8) & 0xFF, Db = D & 0xFF;
				int resColR = (int)(Cr+((Dr-Cr)*p)+((Ar-Cr)*q)+((Br+Cr-Ar-Dr)*p*q));
				int resColG = (int)(Cg+((Dg-Cg)*p)+((Ag-Cg)*q)+((Bg+Cg-Ag-Dg)*p*q));
				int resColB = (int)(Cb+((Db-Cb)*p)+((Ab-Cb)*q)+((Bb+Cb-Ab-Db)*p*q));
				int rgb = resColR; rgb = (rgb << 8) + resColG;	rgb = (rgb << 8) + resColB;
				scaledImg.setRGB(j,i,rgb);
			}
		}
		return new Image(scaledImg);
	}
	
	public Image scaleBilinearGray(float fcX, float fcY, int percen) {
		float factorX, factorY;
		BufferedImage scaledImg;
		if(percen == 0) {
			factorX = fcX/((float)100);
			factorY = fcY/((float)100);
			scaledImg = new BufferedImage((int)(image.getWidth()*factorX),(int)(image.getHeight()*factorY), BufferedImage.TYPE_BYTE_GRAY);
		}else {
			factorX = fcX/((float)image.getWidth());
			factorY = fcY/((float)image.getHeight());
			scaledImg = new BufferedImage((int)(fcX),(int)(fcY), BufferedImage.TYPE_BYTE_GRAY);
		}
		byte[] vImg = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		int[] vScaledImg = new int[scaledImg.getWidth()*scaledImg.getHeight()];
		for(int i=0;i<scaledImg.getHeight();i++) {
			for(int j=0;j<scaledImg.getWidth();j++) {
				int x = (int)((j+0.5)/factorX);
				int y = (int)((i+0.5)/factorY);
				float p = (float)(((j+0.5)/factorX) - (int)((j+0.5)/factorX));
				float q = (float)(((i+0.5)/factorY) - (int)((i+0.5)/factorY));
				int A = vImg[(y==0?y:y-1)*image.getWidth() + (x==0?x:x-1)] & 0xFF;
				int B = vImg[(y==0?y:y-1)*image.getWidth() + x] & 0xFF;
				int C = vImg[y*image.getWidth() + (x==0?x:x-1)] & 0xFF;
				int D = vImg[y*image.getWidth() + x] & 0xFF;
				int resCol = (int)(C+((D-C)*p)+((A-C)*q)+((B+C-A-D)*p*q));
				vScaledImg[i*scaledImg.getWidth() + j] = resCol;
			}
		}
		scaledImg.getRaster().setPixels(0, 0, scaledImg.getWidth(), scaledImg.getHeight(), vScaledImg);
		return new Image(scaledImg);
	}
}
