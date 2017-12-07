package Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;

public class FreeRotationFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JButton btnAceptar;
	private int degrees, method;

	public FreeRotationFrame(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 418, 145);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{82, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblDegrees = new JLabel("Degrees:");
		GridBagConstraints gbc_lblDegrees = new GridBagConstraints();
		gbc_lblDegrees.anchor = GridBagConstraints.EAST;
		gbc_lblDegrees.insets = new Insets(0, 0, 5, 5);
		gbc_lblDegrees.gridx = 0;
		gbc_lblDegrees.gridy = 0;
		contentPane.add(lblDegrees, gbc_lblDegrees);
		
		degrees = 90;
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(90.0, 0.0, 360.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					spinner.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				degrees = ((Double)spinner.getValue()).intValue();
			}
		});
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.WEST;
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 0;
		contentPane.add(spinner, gbc_spinner);
		
		JRadioButton rdbtnTd = new JRadioButton("Direct transforamtion");
		rdbtnTd.setSelected(true);
		GridBagConstraints gbc_rdbtnTd = new GridBagConstraints();
		gbc_rdbtnTd.anchor = GridBagConstraints.EAST;
		gbc_rdbtnTd.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnTd.gridx = 0;
		gbc_rdbtnTd.gridy = 1;
		contentPane.add(rdbtnTd, gbc_rdbtnTd);
		
		JRadioButton rdbtnTi = new JRadioButton("Reverse transforamtion");
		GridBagConstraints gbc_rdbtnTi = new GridBagConstraints();
		gbc_rdbtnTi.anchor = GridBagConstraints.WEST;
		gbc_rdbtnTi.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnTi.gridx = 1;
		gbc_rdbtnTi.gridy = 1;
		contentPane.add(rdbtnTi, gbc_rdbtnTi);
		
		rdbtnTd.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnTi.isSelected()) {
					rdbtnTi.setSelected(false);
					method = 0;
				}else {
					rdbtnTi.setSelected(true);
					method = 1;
				}
			}
		});
		
		rdbtnTi.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnTd.isSelected()) {
					rdbtnTd.setSelected(false);
					method = 1;
				}else {
					rdbtnTd.setSelected(true);
					method = 0;
				}
			}
		});
		
		btnAceptar = new JButton("Accept");
		GridBagConstraints gbc_btnAcept = new GridBagConstraints();
		gbc_btnAcept.anchor = GridBagConstraints.LINE_END;
		gbc_btnAcept.gridx = 1;
		gbc_btnAcept.gridy = 2;
		contentPane.add(btnAceptar, gbc_btnAcept);
	}
	
	public int[] getData() {
		int[] data = new int[2];
		data[0] = degrees;
		data[1] = method;
		return data;
	}

}
