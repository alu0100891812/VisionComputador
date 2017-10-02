package Image;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CloseableTab {
	static public JPanel createTab(String name, JButton button) {
		JPanel tab = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		tab.setBackground(new Color(1,1,1,0));
		tab.setBorder(new EmptyBorder(2,5,0,0));
		
		JLabel label = new JLabel(name);
		constraints.fill = GridBagConstraints.HORIZONTAL; 
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = 0;
		tab.add(label, constraints);
		
		button.setText("x");
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.RED);
        button.setBorder(new EmptyBorder(0, 3, 0, 3));
        button.setMargin(new Insets(2, 15, 5, 15));
        constraints.fill = GridBagConstraints.HORIZONTAL; 
		constraints.gridwidth = 1;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.ipadx = 10;
        tab.add(button, constraints);
        
        return tab;
	}
}
