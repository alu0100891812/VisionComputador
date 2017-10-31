package Image;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HistogramTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Histogram histogram;
	private Image original;
	private JPanel infoPanel;
	
	public HistogramTab(Image original, Image image, boolean accumulated) {
		this.original = original;
		setUpInfo();
		this.histogram = new Histogram(image, accumulated, infoPanel);
		this.setLayout(new BorderLayout());
		this.add(this.histogram, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.PAGE_END);
	}
	
	private void setUpInfo() {
		infoPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				int[] values = histogram.getValues();
				Font font = g.getFont();
				g.setFont(new Font(font.getName(), Font.BOLD, 16));
				g.drawString("Pixel value: " + values[0], 40, 20);
				g.drawString("Pixel ocurrences: " + values[1], 200, 20);
				g.setFont(font);
			}
		};
		infoPanel.setBorder(new EmptyBorder(0,3,0,3));
		infoPanel.setPreferredSize(new Dimension(this.getWidth(), 30));
		infoPanel.setLayout(new GridBagLayout());
		JCheckBox fill = new JCheckBox("Fill");
		fill.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				histogram.repaint();
			}
		});
		
		GridBagConstraints checkCont = new GridBagConstraints();
		checkCont.gridx = 1;
		checkCont.gridy = 0;
		checkCont.insets = new Insets(0,400,0,0);
		checkCont.anchor = GridBagConstraints.LINE_END;
		infoPanel.add(fill, checkCont);
	}
	
	public Histogram getHistogram() {
		return histogram;
	}
	
	public Image getOrigin() {
		return original;
	}
}
