package Image;

import javax.swing.JPanel;


public class RangeValue {
	
	private JPanel panel;
	
	public RangeValue() {
			panel = new JPanel();
	}
		
	public String calculateRange(Image image) {
		byte[] pixel = image.getVector();
		int[] count = new int[256];
		int maxvalue = 0;
		int minvalue = 0;
		String range = "";
				
		for(int i = 0; i < pixel.length; i++) {
			count[pixel[i] & 0xFF]++;
		}
		
		for(int j = 0; j < count.length; j++) {
			if(count[j] != 0) {
				minvalue = j;
				break;
			}
		}
		
		for(int j = count.length-1; j > 0; j--) {
			if(count[j] != 0) {
				maxvalue = j;
				break;
			}
		}
		range = "[ " + minvalue + " - " + maxvalue + " ]";
		return range;
	}
}
