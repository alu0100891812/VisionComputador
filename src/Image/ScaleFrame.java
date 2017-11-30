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
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;

public class ScaleFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public JButton btnAceptar;
	private int sX = 100, sY = 100;

	public ScaleFrame(String name) {
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 180);
		
		((JPanel)getContentPane()).setBorder(new EmptyBorder(20, 10, 10, 10));
				
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
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

		JLabel lblNewLabel = new JLabel(sX + "%");
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 700, 100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				lblNewLabel.setText(slider.getValue() + "%");
				sX = slider.getValue();
			}			
		});
		panel.add(slider);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		gbc_panel_1.gridheight = 2;
		getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel label = new JLabel(sY + "%");
		
		JSlider slider_1 = new JSlider(JSlider.HORIZONTAL, 0, 700, 100);
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				label.setText(slider_1.getValue() + "%");
				sY = slider_1.getValue();
			}			
		});
		panel_1.add(slider_1);		
		panel_1.add(label);
				
		btnAceptar = new JButton("Accept");
		getRootPane().setDefaultButton(btnAceptar);
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 3;
		gbc_btnNewButton.anchor = GridBagConstraints.LINE_END;
		getContentPane().add(btnAceptar, gbc_btnNewButton);
	}
	
	public int[] getData() {
		int[] data = new int[2];
		data[0] = sX;
		data[1] = sY;
		return data;
	}
}
