package Image;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import java.awt.Insets;
import java.text.ParseException;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class linearTransformationFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel nodePanel;
	public JButton btnAceptar;
	
	public linearTransformationFrame(String name) {
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNewLabel = new JLabel("Nodes number");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weightx = 0.5;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		gbc_scrollPane.gridwidth = 2;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		nodePanel = new JPanel();
		scrollPane.setViewportView(nodePanel);	
		
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 255.0, 1.0));
		spinner.setPreferredSize(new Dimension(60, 20));
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				nodePanel.removeAll();
				nodePanel.setLayout(new GridLayout(((Double)spinner.getValue()).intValue(), 4, 0, 0));	
				nodePanel.updateUI();
				try {
					spinner.commitEdit();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				for(int i=0; i < ((Double)spinner.getValue()).intValue(); i++) {
					JLabel lblNodeX = new JLabel("Node " + i + " x:");
					lblNodeX.setHorizontalAlignment(SwingConstants.CENTER);
					nodePanel.add(lblNodeX);
					
					JSpinner spinner_1 = new JSpinner();
					spinner_1.setPreferredSize(new Dimension(spinner_1.getPreferredSize().width, 50));
					nodePanel.add(spinner_1);
					
					JLabel lblNodeY = new JLabel("Node " + i + " y:");
					lblNodeY.setHorizontalAlignment(SwingConstants.CENTER);
					nodePanel.add(lblNodeY);
					
					JSpinner spinner_2 = new JSpinner();
					spinner_2.setPreferredSize(new Dimension(spinner_2.getPreferredSize().width, 50));
					nodePanel.add(spinner_2);
				}
				nodePanel.repaint();
			}
		});
		
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.weightx = 0.5;
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 0;
		contentPane.add(spinner, gbc_spinner);
		
		btnAceptar = new JButton("Aceptar");
		
		GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
		gbc_btnAceptar.gridx = 1;
		gbc_btnAceptar.gridy = 2;
		contentPane.add(btnAceptar, gbc_btnAceptar);		
	}
	
	public int[] getNodeData() {
		int[] nodes = new int[nodePanel.getComponentCount()/2];
		int index = 0;
		for(int i=1; i<nodePanel.getComponentCount()/2; i+=2, index++) {
			nodes[index] = ((Integer)((JSpinner)nodePanel.getComponent(i)).getValue()).intValue();
		}
		return nodes;
	}
}
