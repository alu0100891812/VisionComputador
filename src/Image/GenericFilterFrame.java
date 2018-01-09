package Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JButton;


public class GenericFilterFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JButton btnAceptar;
	private String value = "3x3";
	
	public GenericFilterFrame(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 120);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblDegres = new JLabel("Degrees:");
		GridBagConstraints gbc_lblDegres = new GridBagConstraints();
		gbc_lblDegres.anchor = GridBagConstraints.LINE_END;
		gbc_lblDegres.insets = new Insets(0, 0, 5, 5);
		gbc_lblDegres.gridx = 0;
		gbc_lblDegres.gridy = 0;
		contentPane.add(lblDegres, gbc_lblDegres);
		
		String[] size = {"3x3","5x5","7x7"};
		
		JComboBox<String> comboBox = new JComboBox<>(size);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value = ((String)comboBox.getSelectedItem());
			}
		});
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.LINE_START;
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		contentPane.add(comboBox, gbc_comboBox);
		
		btnAceptar = new JButton("Accept");
		GridBagConstraints gbc_btnAccept = new GridBagConstraints();
		gbc_btnAccept.insets = new Insets(0, 0, 0, 10);
		gbc_btnAccept.anchor = GridBagConstraints.LAST_LINE_END;
		gbc_btnAccept.gridx = 1;
		gbc_btnAccept.gridy = 2;
		contentPane.add(btnAceptar, gbc_btnAccept);
	}
	
	public String getData() {
		return value;
	}
}
