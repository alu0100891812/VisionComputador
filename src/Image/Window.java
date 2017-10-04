package Image;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Window {
	private JFrame frame;
	private MenuBar menuBar;
	private Tabs tabs;
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
		menuBar = new MenuBar();

		JMenu fileMenu = new JMenu("File");
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);
	    
	    JMenuItem OpenFileItem = new JMenuItem("Open File", KeyEvent.VK_O);
	    OpenFileItem.setIcon(new ImageIcon("openFile.png"));
	    setUpOpenItem(OpenFileItem);
	    fileMenu.add(OpenFileItem);
	    
	    JMenu SaveFileSubmenu = new JMenu("Save File");
	    SaveFileSubmenu.setMnemonic(KeyEvent.VK_S);
	    SaveFileSubmenu.setIcon(new ImageIcon("saveFile.png"));
	    SaveFileSubmenu.setEnabled(false);
	    fileMenu.add(SaveFileSubmenu);
	    
	    JMenu editMenu = new JMenu("Edit");
	    editMenu.setMnemonic(KeyEvent.VK_E);
	    menuBar.add(editMenu);
	    
	    JMenuItem ConvertToGrayItem = new JMenuItem("Convert to Gray", KeyEvent.VK_G);
	    ConvertToGrayItem.setIcon(new ImageIcon("RGBtoGray.png"));
	    ConvertToGrayItem.setEnabled(false);
	    setUpConvertToGray(ConvertToGrayItem);
	    editMenu.add(ConvertToGrayItem);
	    
	    JMenuItem HistogramItem = new JMenuItem("Histogram", KeyEvent.VK_H);
	    HistogramItem.setIcon(new ImageIcon("histogram.png"));
	    HistogramItem.setEnabled(false);
	    setUpHistogram(HistogramItem);
	    editMenu.add(HistogramItem);
	    
	    JMenuItem HistogramAccumulatedItem = new JMenuItem("Histogram Accumulated", KeyEvent.VK_H);
	    HistogramAccumulatedItem.setIcon(new ImageIcon("histogramAccumulated.png"));
	    HistogramAccumulatedItem.setEnabled(false);
	    setUpHistogramAccumulated(HistogramAccumulatedItem);
	    editMenu.add(HistogramAccumulatedItem);
	    

	    frame.setJMenuBar(menuBar);
	    
	    tabs = new Tabs();
	    tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    frame.add(tabs);	    
	}
	
	private void setUpOpenItem(JMenuItem item) {
		String tabName = "Original Image";
		item.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
	    		String username = System.getProperty("user.name");
	    		String path = "C:\\Users\\" + username + "\\Downloads";
	    		JFileChooser filePicker = new JFileChooser(path);
	    		filePicker.setDialogTitle("Select a image to open");
	    		filePicker.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("BMP", "bmp"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("JPEG, JPG", "jpg", "jpeg"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("TIFF", "tiff"));
	    		int result = filePicker.showOpenDialog(frame);
	    		
	    		if(result == JFileChooser.APPROVE_OPTION) {
					try {
						JButton button = new JButton();
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JMenuItem[] items = menuBar.getMenuItems("Edit");
								for(JMenuItem item : items)
									item.setEnabled(false);
								tabs.remove(tabName);
							}
						});
						File file = filePicker.getSelectedFile();
						tabs.addImageTab(tabName, new ImageTab(ImageIO.read(file), file.getName(), false), button);
						JMenuItem item = menuBar.getItem("Edit", "Convert to Gray");
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
		String tabName = "Gray Image";
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image original = tabs.getImage("Original Image");
				if(original != null) {					
					item.setEnabled(false);
					BufferedImage gray = original.RGBtoGray();
					JButton button = new JButton();
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JMenuItem itemBar = menuBar.getItem("Edit", "Histogram");
							if(itemBar != null) {
								itemBar.setEnabled(false);
							}
							itemBar = menuBar.getItem("Edit", "Histogram Accumulated");
							if(itemBar != null) {
								itemBar.setEnabled(false);
							}
							tabs.remove(tabName);
						}
					});
					tabs.addImageTab(tabName, new ImageTab(gray, tabs.getImageTab("Original Image").getImageName(), true), button);
					addToSaveItem(tabName, new ImageIcon("RGBtoGray.png"), KeyEvent.VK_G);
					JMenuItem itemBar = menuBar.getItem("File", "Save File");
					if(itemBar != null) {
						itemBar.setEnabled(true);
					}
					itemBar = menuBar.getItem("Edit", "Histogram");
					if(itemBar != null) {
						itemBar.setEnabled(true);
					}		
					itemBar = menuBar.getItem("Edit", "Histogram Accumulated");
					if(itemBar != null) {
						itemBar.setEnabled(true);
					}
				}else{
					JOptionPane.showMessageDialog(null, "Can't convert to gray, try again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void setUpHistogram(JMenuItem item) {
		String tabName = "Histogram";
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image gray = tabs.getImage("Gray Image");
				if(gray != null) {				
					item.setEnabled(false);
					histograma = new Histogram(gray, false);
					JButton button = new JButton();
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							tabs.remove(tabName);
						}
					});
					tabs.addHistogramTab(tabName, new HistogramTab(histograma), button);
					addToSaveItem(tabName, new ImageIcon("histogram.png"), KeyEvent.VK_H);
				}else{
					JOptionPane.showMessageDialog(null, "Can't show the histogram, try again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void setUpHistogramAccumulated(JMenuItem item) {
		String tabName = "Histogram Accumulated";
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image gray = tabs.getImage("Gray Image");
				if(gray != null) {				
					item.setEnabled(false);
					histograma = new Histogram(gray, true);
					JButton button = new JButton();
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							tabs.remove(tabName);
						}
					});
					tabs.addHistogramTab(tabName, new HistogramTab(histograma), button);
					addToSaveItem(tabName, new ImageIcon("histogramAccumulated.png"), KeyEvent.VK_H);
				}else{
					JOptionPane.showMessageDialog(null, "Can't show the histogram, try again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void addToSaveItem(String name, ImageIcon icon, int keyEvent) {
		JMenu menu = (JMenu) menuBar.getItem("File", "Save File");
		JMenuItem exampleItem = new JMenuItem(name);
		exampleItem.setMnemonic(keyEvent);
		if(icon != null)
			exampleItem.setIcon(icon);
		exampleItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem source = (JMenuItem)e.getSource();
				String tabName = source.getText();
				
				JFileChooser filePicker = new JFileChooser("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads");
	    		filePicker.setDialogTitle("Select a image to save");
	    		filePicker.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("BMP", "bmp"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("JPEG", "jpeg"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("JPG", "jpg"));
	    		filePicker.addChoosableFileFilter(new FileNameExtensionFilter("TIFF", "tiff"));
	    		int result = filePicker.showSaveDialog(frame);
	    		
	    		if(result == JFileChooser.APPROVE_OPTION) {
						try {
							boolean res = ImageIO.write(tabs.getBuffImage(tabName), filePicker.getFileFilter().getDescription(), new File(filePicker.getSelectedFile().getAbsolutePath() + "." + filePicker.getFileFilter().getDescription().toLowerCase()));
							if(!res) {
								JOptionPane.showMessageDialog(null, "An error has ocurred, try to save the file again",
				    					"Error", JOptionPane.ERROR_MESSAGE);
							}
						} catch (IOException ex) {
							ex.printStackTrace();
						}
	    		}else if(result == JFileChooser.ERROR_OPTION) {
	    			JOptionPane.showMessageDialog(null, "An error has ocurred, try to save the file again",
	    					"Error", JOptionPane.ERROR_MESSAGE);
	    		}
			}
		});;
		menu.add(exampleItem);
	}
}
