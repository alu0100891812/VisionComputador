package Image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HistogramTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Histogram histogram;
	private JPanel infoPanel;
	
	public HistogramTab(Histogram histogram) {
		this.histogram = histogram;
		setUpInfo();
		this.setLayout(new BorderLayout());
		this.add(this.histogram, BorderLayout.CENTER);
		//this.add(infoPanel, BorderLayout.LINE_END);
	}
	
	private void setUpInfo() {
		infoPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				//g.drawString(image.getMousePixel(), 40, 40);
				g.dispose();
			}
		};
		infoPanel.setBorder(new EmptyBorder(0,3,0,3));
		infoPanel.setPreferredSize(new Dimension(300, getHeight()));
	}
	
	public Histogram getHistogram() {
		return histogram;
	}
}
