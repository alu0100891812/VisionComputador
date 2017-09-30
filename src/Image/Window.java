package Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Window {
	private JFrame frame;
	private JMenuBar menuBar;
	private JTabbedPane tabs;
	private Image image;
	private Histogram histograma;
	
	public Window() {
		frame = new JFrame("Image");
		setUpMenu();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(900, 500);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}
	
	private void setUpMenu() {
		menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);
	    
	    JMenuItem OpenFileItem = new JMenuItem("Open File", KeyEvent.VK_O);
	    OpenFileItem.setIcon(new ImageIcon("openFile.png"));
	    setUpOpenItem(OpenFileItem);
	    fileMenu.add(OpenFileItem);
	    
	    JMenu editMenu = new JMenu("Edit");
	    editMenu.setMnemonic(KeyEvent.VK_E);
	    menuBar.add(editMenu);
	    
	    JMenuItem ConvertToGrayItem = new JMenuItem("Convert to Gray", KeyEvent.VK_G);
	    ConvertToGrayItem.setEnabled(false);
	    setUpConvertToGray(ConvertToGrayItem);
	    editMenu.add(ConvertToGrayItem);
	    
	    JMenuItem HistogramItem = new JMenuItem("Histogram", KeyEvent.VK_H);
	    HistogramItem.setEnabled(false);
	    setUpHistogram(HistogramItem);
	    editMenu.add(HistogramItem);
	    

	    frame.setJMenuBar(menuBar);
	    
	    tabs = new JTabbedPane();
	    tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    frame.add(tabs);	    
	}
	
	private void setUpOpenItem(JMenuItem item) {
		item.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
	    		String username = System.getProperty("user.name");
	    		String path = "C:\\Users\\" + username + "\\Downloads";
	    		JFileChooser filePicker = new JFileChooser(path);
	    		filePicker.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("BMP", "bmp"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("JPEG, JPG", "jpg", "jpeg"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("TIFF", "tiff"));
	    		int result = filePicker.showOpenDialog(frame);
	    		
	    		if(result == JFileChooser.APPROVE_OPTION) {
					try {
						image = new Image(ImageIO.read(filePicker.getSelectedFile()));
						tabs.addTab("Original", image);
						JMenuItem item = getItem("Edit", "Convert to Gray");
						if(item != null) {
							item.setEnabled(true);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
	    			frame.setVisible(true);	    			
	    		}else if(result == JFileChooser.ERROR_OPTION) {
	    			JOptionPane.showMessageDialog(null, "An error has ocurred, try to open the file again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
	    		}
	    	}
	    });
	}
	
	private void setUpConvertToGray(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(image != null) {
					Image img = image.RGBtoGray();
					tabs.addTab("Gray", img);
					tabs.setSelectedIndex(tabs.getTabCount() - 1);
					JMenuItem item = getItem("Edit", "Histogram");
					if(item != null) {
						item.setEnabled(true);
					}					
				}else{
					JOptionPane.showMessageDialog(null, "Can't convert to gray, try again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void setUpHistogram(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(image != null) {
					Image img = image.RGBtoGray();
					tabs.addTab("Histogram", img);
					histograma = new Histogram();
					histograma.draw(img);
					img.repaint();
					tabs.setSelectedIndex(tabs.getTabCount() - 1);
				}else{
					JOptionPane.showMessageDialog(null, "Can't show the histogram, try again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public JMenu getMenu(String menu) {
		JMenu result = null;
		
		for(int i=0; i<menuBar.getMenuCount(); i++) {
			if(menuBar.getMenu(i).getText().equalsIgnoreCase(menu)) {
				result = menuBar.getMenu(i);
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
}
