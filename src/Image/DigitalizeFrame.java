package Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.ParseException;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

public class DigitalizeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JButton btnAceptar;
	private int bits;
	private int pps;

	public DigitalizeFrame(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 130);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblPixelsPorMuestra = new JLabel("Pixels per sample:");
		GridBagConstraints gbc_lblPixelsPorMuestra = new GridBagConstraints();
		gbc_lblPixelsPorMuestra.anchor = GridBagConstraints.SOUTH;
		gbc_lblPixelsPorMuestra.insets = new Insets(0, 0, 5, 5);
		gbc_lblPixelsPorMuestra.gridx = 0;
		gbc_lblPixelsPorMuestra.gridy = 0;
		contentPane.add(lblPixelsPorMuestra, gbc_lblPixelsPorMuestra);
		
		JLabel lblBitsTo = new JLabel("Bits to encode color:");
		GridBagConstraints gbc_lblBitsTo = new GridBagConstraints();
		gbc_lblBitsTo.anchor = GridBagConstraints.SOUTH;
		gbc_lblBitsTo.insets = new Insets(0, 0, 5, 0);
		gbc_lblBitsTo.gridx = 1;
		gbc_lblBitsTo.gridy = 0;
		contentPane.add(lblBitsTo, gbc_lblBitsTo);
		
		pps = 2;
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(2.0, 1.0, 8.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					spinner.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				pps = ((Double)spinner.getValue()).intValue();
			}
		});
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.anchor = GridBagConstraints.NORTH;
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.gridx = 0;
		gbc_spinner.gridy = 1;
		contentPane.add(spinner, gbc_spinner);
		
		bits = 7;
		JSpinner spinner_1 = new JSpinner(new SpinnerNumberModel(7.0, 1.0, 8.0, 1.0));
		spinner_1.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					spinner_1.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				bits = ((Double)spinner_1.getValue()).intValue();
			}
		});
		GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
		gbc_spinner_1.anchor = GridBagConstraints.NORTH;
		gbc_spinner_1.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_1.gridx = 1;
		gbc_spinner_1.gridy = 1;
		contentPane.add(spinner_1, gbc_spinner_1);
		
		btnAceptar = new JButton("Accept");
		GridBagConstraints gbc_btnAccept = new GridBagConstraints();
		gbc_btnAccept.anchor = GridBagConstraints.LINE_END;
		gbc_btnAccept.gridx = 1;
		gbc_btnAccept.gridy = 2;
		contentPane.add(btnAceptar, gbc_btnAccept);		
	}
	
	public int[] getData() {
		int[] data = new int[2];
		data[0] = pps; data[1] = bits;
		return data;
	}
}
