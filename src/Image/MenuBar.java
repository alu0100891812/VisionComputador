package Image;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	public JMenu getMenu(String menu) {
		JMenu result = null;
		
		for(int i=0; i<this.getMenuCount(); i++) {
			if(this.getMenu(i).getText().equalsIgnoreCase(menu)) {
				result = this.getMenu(i);
				break;
			}
		}		
		return result;
	}
	
	public JMenuItem[] getMenuItems(String menu) {
		JMenuItem[] result = null;
		
		for(int i=0; i<this.getMenuCount(); i++) {
			if(this.getMenu(i).getText().equalsIgnoreCase(menu)) {
				int num = this.getMenu(i).getItemCount();
				result = new JMenuItem[num];
				for(int j=0; j<num; j++) {
					result[j] = this.getMenu(i).getItem(j);
				}
				break;
			}
		}		
		return result;
	}
	
	public JMenuItem getItem(String menu, String item) {
		JMenu menuObj = getMenu(menu);
		JMenuItem result = null;
		
		if(menuObj != null) {
			for(int i=0; i<menuObj.getItemCount(); i++) {
				if(menuObj.getItem(i).getText().equalsIgnoreCase(item)) {
					result = menuObj.getItem(i);
					break;
				}
			}
		}		
		return result;
	}
	
	public void removeFromSave(String item) {
		JMenu saveMenu = ((JMenu)this.getMenu("File").getItem(1));
		
		for(int i=0; i<saveMenu.getItemCount(); i++) {
			if(saveMenu.getItem(i).getText().equalsIgnoreCase(item)) {
				saveMenu.remove(i);
				break;
			}
		}
		if(saveMenu.getItemCount() == 0) {
			saveMenu.setEnabled(false);
		}
	}
}
