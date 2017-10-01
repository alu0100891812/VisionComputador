package Image;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class CloseableTab {
	static public JPanel createTab(String name, JButton button) {
		JPanel tab = new JPanel();
		JLabel label = new JLabel(name);
		
		tab.setBackground(new Color(1,1,1,0));        
		button.setText("x");
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.RED);
        button.setBorder(new EmptyBorder(0, 3, 0, 3));
        button.setMargin(new Insets(2, 15, 5, 15));
        
        tab.add(label);
        tab.add(button);
        
        return tab;
	}
}
