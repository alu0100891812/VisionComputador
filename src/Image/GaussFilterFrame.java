package Image;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

public class GaussFilterFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private float value;
	public JButton btnAceptar;

	public GaussFilterFrame(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 115);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{68, 51, 0};
		gbl_contentPane.rowHeights = new int[]{23, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblGauss = new JLabel("Sigma");
		GridBagConstraints gbc_lblGauss = new GridBagConstraints();
		gbc_lblGauss.anchor = GridBagConstraints.LINE_END;
		gbc_lblGauss.insets = new Insets(0, 0, 5, 5);
		gbc_lblGauss.gridx = 0;
		gbc_lblGauss.gridy = 0;
		contentPane.add(lblGauss, gbc_lblGauss);
		
		value = 1;
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(1.0, -6.0, 6.0, 0.1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					spinner.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				value = ((Double)spinner.getValue()).floatValue();
			}
		});
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.LINE_START;
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 0;
		contentPane.add(spinner, gbc_spinner);
		
		btnAceptar = new JButton("Accept");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.LAST_LINE_END;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 1;
		contentPane.add(btnAceptar, gbc_btnNewButton);
	}

	public float getData() {
		return value;
	}
}
