package Image;

import javax.swing.JPanel;


public class RangeValue {
	
	private JPanel panel;
	
	public RangeValue() {
			panel = new JPanel();
	}
		
	public void calculateRange(Image image) {
		byte[] pixel = image.getVector();
		int[] contador = new int[256];
		int max = 0;
		byte maxvalor = 0;
		int min = 256;
		byte minvalor = (byte) 256;
		
		for(int i = 0; i < pixel.length; i++) {
			contador[pixel[i]]++;
		}
		for(int j = 0; j < contador.length; j++) {
			if(contador[j] > max) {
				max = contador[j];
				maxvalor = (byte) j;
			} else if (contador[j] < min) {
				min = contador[j];
				minvalor = (byte) j;
			}			
		}
	}
}
