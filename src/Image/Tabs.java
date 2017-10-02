package Image;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

public class Tabs extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	private Vector<Pair<Image, String>> images;
	private Vector<Pair<Histogram, String>> histograms;
	
	public void remove(String name) {
		for(int i=0; i<this.getTabCount(); i++) {
			if(this.getTitleAt(i).equalsIgnoreCase(name)) {
				this.remove(i);
				break;
			}
		}
	}
	
	public Image getImage(String name) {
		if(images != null) {
			for(Pair<Image, String> image : images) {
				if(image.getRight().equalsIgnoreCase(name)) {
					return image.getLeft();
				}
			}			
		}
		return null;
	}
	
	public Histogram getHistogram(String name) {
		if(histograms != null) {
			for(Pair<Histogram, String> histogram : histograms) {
				if(histogram.getRight().equalsIgnoreCase(name)) {
					return histogram.getLeft();
				}
			}			
		}
		return null;
	}
	
	public void addImageTab(String title, Image image, JButton button) {
		this.addTab(title, image);
		int index = this.getTabCount() -1;
		this.setTabComponentAt(index, CloseableTab.createTab(title, button));
		this.setSelectedIndex(index);
		
		if(images == null) 
			images = new Vector<Pair<Image, String>>();
		images.addElement(new Pair<Image, String>(image, title));
	}
	
	public void addImageTab(String title, ImageTab image, JButton button) {
		this.addTab(title, image);
		int index = this.getTabCount() -1;
		this.setTabComponentAt(index, CloseableTab.createTab(title, button));
		this.setSelectedIndex(index);
	}
	
	public void addHistogramTab(String title, Histogram histogram, JButton button) {
		this.addTab(title, histogram);
		int index = this.getTabCount() -1;
		this.setTabComponentAt(index, CloseableTab.createTab(title, button));
		this.setSelectedIndex(index);
		
		if(histograms == null) 
			histograms = new Vector<Pair<Histogram, String>>();
		histograms.addElement(new Pair<Histogram, String>(histogram, title));
	}
}
