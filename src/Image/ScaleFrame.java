package Image;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridBagLayout;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JRadioButton;

public class ScaleFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public JButton btnAceptar;
	private int sX = 100, sY = 100, method = 0, percen = 0;
	private JSlider slider, slider_1;
	private String symbol;

	public ScaleFrame(String name, int sizeX, int sizeY) {
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 259);
		
		((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 10, 10, 10));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblBrgihtness = new JLabel("X Scale");
		lblBrgihtness.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		GridBagConstraints gbc_lblBrgihtness = new GridBagConstraints();
		gbc_lblBrgihtness.insets = new Insets(0, 0, 5, 5);
		gbc_lblBrgihtness.gridx = 0;
		gbc_lblBrgihtness.gridy = 0;
		getContentPane().add(lblBrgihtness, gbc_lblBrgihtness);
		
		JLabel lblContrast = new JLabel("Y Scale");
		lblContrast.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		GridBagConstraints gbc_lblContrast = new GridBagConstraints();
		gbc_lblContrast.insets = new Insets(0, 0, 5, 0);
		gbc_lblContrast.gridx = 1;
		gbc_lblContrast.gridy = 0;
		getContentPane().add(lblContrast, gbc_lblContrast);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		gbc_panel.gridheight = 2;
		getContentPane().add(panel, gbc_panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		symbol = new String("%");
		JLabel lblNewLabel = new JLabel(sX + symbol);
		
		slider = new JSlider(JSlider.HORIZONTAL, 1, 300, 100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				lblNewLabel.setText(slider.getValue() + symbol);
				sX = slider.getValue();
			}			
		});
		panel.add(slider);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		gbc_panel_1.gridheight = 2;
		getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel label = new JLabel(sY + symbol);
		
		slider_1 = new JSlider(JSlider.HORIZONTAL, 1, 300, 100);
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				label.setText(slider_1.getValue() + symbol);
				sY = slider_1.getValue();
			}			
		});
		panel_1.add(slider_1);		
		panel_1.add(label);
		
		JRadioButton rdbtnVmp = new JRadioButton("VMP");
		rdbtnVmp.setSelected(true);
		JRadioButton rdbtnBilineal = new JRadioButton("Bilineal");
		
		JRadioButton rdbtnPercentages = new JRadioButton("Percentages");
		rdbtnPercentages.setSelected(true);
		
		rdbtnPercentages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnPercentages.isSelected()) {
					symbol = new String("%");
					float sv = slider.getValue();
					float sv1 = slider_1.getValue();
					slider.setMaximum(300);
					slider.setValue((int)(((float)(sv/((float)sizeX)))*((float)100)));
					slider_1.setMaximum(300);
					slider_1.setValue((int)(((float)(sv1/((float)sizeY)))*((float)100)));
					percen = 0;
				} else {
					symbol = new String("px");
					float sv = slider.getValue();
					float sv1 = slider_1.getValue();
					slider.setMaximum(sizeX*3);
					slider.setValue((int)(((float)(sv/((float)100)))*((float)sizeX)));
					slider_1.setMaximum(sizeY*3);
					slider_1.setValue((int)(((float)(sv1/((float)100)))*((float)sizeY)));
					percen = 1;
				}
			}
		});
		
		GridBagConstraints gbc_rdbtnPercentages = new GridBagConstraints();
		gbc_rdbtnPercentages.gridwidth = 2;
		gbc_rdbtnPercentages.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPercentages.gridx = 0;
		gbc_rdbtnPercentages.gridy = 3;
		getContentPane().add(rdbtnPercentages, gbc_rdbtnPercentages);
		
		GridBagConstraints gbc_rdbtnVmp = new GridBagConstraints();
		gbc_rdbtnVmp.anchor = GridBagConstraints.EAST;
		gbc_rdbtnVmp.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnVmp.gridx = 0;
		gbc_rdbtnVmp.gridy = 4;
		getContentPane().add(rdbtnVmp, gbc_rdbtnVmp);
		
		GridBagConstraints gbc_rdbtnBilineal = new GridBagConstraints();
		gbc_rdbtnBilineal.anchor = GridBagConstraints.WEST;
		gbc_rdbtnBilineal.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnBilineal.gridx = 1;
		gbc_rdbtnBilineal.gridy = 4;
		getContentPane().add(rdbtnBilineal, gbc_rdbtnBilineal);
		
		rdbtnVmp.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnBilineal.isSelected()) {
					rdbtnBilineal.setSelected(false);
					method = 0;
				}else {
					rdbtnBilineal.setSelected(true);
					method = 1;
				}
			}
		});
		
		
		rdbtnBilineal.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnVmp.isSelected()) {
					rdbtnVmp.setSelected(false);
					method = 1;
				}else {
					rdbtnVmp.setSelected(true);
					method = 0;
				}
			}
		});
		
		btnAceptar = new JButton("Accept");
		getRootPane().setDefaultButton(btnAceptar);
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 5;
		gbc_btnNewButton.anchor = GridBagConstraints.LINE_END;
		getContentPane().add(btnAceptar, gbc_btnNewButton);
	}
	
	public int[] getData() {
		int[] data = new int[4];
		data[0] = sX;
		data[1] = sY;
		data[2] = method;
		data[3] = percen;
		return data;
	}
}
