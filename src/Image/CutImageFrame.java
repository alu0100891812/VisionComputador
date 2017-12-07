package Image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

public class CutImageFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Image sourceImage;
	private JSpinner spinner, spinner_1, spinner_2, spinner_3;
	public JButton btnAceptar;

	public CutImageFrame(String title, Image image) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		sourceImage = new Image(image.getBufferedImage()) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor(Color.RED);
				g.drawRect(((Double)spinner.getValue()).intValue(), ((Double)spinner_1.getValue()).intValue(), ((Double)spinner_2.getValue()).intValue() - ((Double)spinner.getValue()).intValue(), ((Double)spinner_3.getValue()).intValue() - ((Double)spinner_1.getValue()).intValue());
			}			
		};
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblSourceX = new JLabel("From X:");
		panel_1.add(lblSourceX);
		
		spinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, image.getImageWidth(), 1.0));
		panel_1.add(spinner);
		
		JLabel lblFromY = new JLabel("From Y:");
		panel_1.add(lblFromY);
		
		spinner_1 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, image.getImageHeight(), 1.0));
		panel_1.add(spinner_1);
		
		JLabel lblToX = new JLabel("To X:");
		panel_1.add(lblToX);
		
		spinner_2 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, image.getImageWidth(), 1.0));
		spinner_2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					spinner_2.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				double prev = ((Double)((JSpinner)panel_1.getComponent(1)).getValue()).doubleValue();
				if(((Double)((JSpinner)panel_1.getComponent(5)).getValue()).doubleValue() < prev) {	
					spinner_2.setModel(new SpinnerNumberModel(prev, prev, image.getImageWidth(), 1.0));
				}
				panel.repaint();
			}
		});
		panel_1.add(spinner_2);
		
		JLabel lblToY = new JLabel("To Y:");
		panel_1.add(lblToY);
		
		spinner_3 = new JSpinner(new SpinnerNumberModel(0.0, 0.0, image.getImageHeight(), 1.0));
		spinner_3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					spinner_3.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				double prev = ((Double)((JSpinner)panel_1.getComponent(3)).getValue()).doubleValue();
				if(((Double)((JSpinner)panel_1.getComponent(7)).getValue()).doubleValue() < prev) {		
					spinner_3.setModel(new SpinnerNumberModel(prev, prev, image.getImageHeight(), 1.0));
				}
				panel.repaint();
			}
		});
		panel_1.add(spinner_3);
		
		btnAceptar = new JButton("Accept");
		contentPane.add(btnAceptar, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		sourceImage.marginX = 0;
		sourceImage.marginY = 0;
		sourceImage.setPreferredSize(new Dimension(sourceImage.getImageWidth()+sourceImage.marginX*2, sourceImage.getImageHeight()+sourceImage.marginY*2));
		scrollPane.setViewportView(sourceImage);
		scrollPane.setPreferredSize(new Dimension(this.getWidth()-20, 330));		
		panel.add(scrollPane, gbc_scrollPane);
		
		contentPane.add(panel, BorderLayout.CENTER);
	}
	
	public Image getCuttedImage() {
		try {
			spinner.commitEdit();
			spinner_1.commitEdit();
			spinner_2.commitEdit();
			spinner_3.commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		BufferedImage cutted = new BufferedImage(((Double)spinner_2.getValue()).intValue() - ((Double)spinner.getValue()).intValue(), ((Double)spinner_3.getValue()).intValue() - ((Double)spinner_1.getValue()).intValue(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = cutted.getGraphics();  
		g.drawImage(sourceImage.getBufferedImage(), 0, 0, cutted.getWidth(), cutted.getHeight(), ((Double)spinner.getValue()).intValue(), ((Double)spinner_1.getValue()).intValue(), ((Double)spinner_2.getValue()).intValue(), ((Double)spinner_3.getValue()).intValue(), null); 
		g.dispose();
		return new Image(cutted);
	}
}
