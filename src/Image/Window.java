package Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	    
	    JMenuItem LinearTransformtionItem = new JMenuItem("Linear Transformation", KeyEvent.VK_L);
	    LinearTransformtionItem.setIcon(new ImageIcon("linearTransformations.png"));
	    LinearTransformtionItem.setEnabled(false);
	    setUpLinearTransformation(LinearTransformtionItem);
	    editMenu.add(LinearTransformtionItem);
	    
	    JMenuItem BrightnessContrastItem = new JMenuItem("Brightness Contrast", KeyEvent.VK_B);
	    BrightnessContrastItem.setIcon(new ImageIcon("brightnessContrast.png"));
	    BrightnessContrastItem.setEnabled(false);
	    setUpBrightnessContrast(BrightnessContrastItem);
	    editMenu.add(BrightnessContrastItem);
	    
	    JMenuItem EcualizeItem = new JMenuItem("Ecualized image", KeyEvent.VK_E);
	    EcualizeItem.setIcon(new ImageIcon("ecualizeImage.png"));
        EcualizeItem.setEnabled(false);
        setUpEcualized(EcualizeItem);
        editMenu.add(EcualizeItem);

	    frame.setJMenuBar(menuBar);
	    
	    tabs = new Tabs();
	    tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    frame.add(tabs);	    
	}
	
	private Image getSelectedImage() {
        int index = tabs.getSelectedIndex();
        ImageTab itab = tabs.getImageTab(tabs.getTabsNames()[index]);
        HistogramTab htab = tabs.getHistogramTab(tabs.getTabsNames()[index]);
        if(itab != null) {
        	return itab.getOrigin();
        }else if(htab != null) {
        	return htab.getOrigin();
        }
        return null;
	}
	
	private void setUpOpenItem(JMenuItem item) {
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
						File file = filePicker.getSelectedFile();
						String tabName = file.getName().substring(0, 4) + " - Original Image";
						button.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								JMenuItem[] items = menuBar.getMenuItems("Edit");
								tabs.removeTabs(tabName);
								if(!tabs.contains("Original Image")) {
									for(JMenuItem item : items)
										item.setEnabled(false);
								}
								tabs.remove(tabName);
							}
						});
						tabs.addImageTab(tabName, new ImageTab(new Image(ImageIO.read(file)), ImageIO.read(file), file.getName(), false), button);
						JMenuItem[] items = menuBar.getMenuItems("Edit");
						for(JMenuItem item : items)
							item.setEnabled(true);
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
				Image original = getSelectedImage();
				if(original != null) {
					BufferedImage gray = original.RGBtoGray();
					JButton button = new JButton();
					String tabName = tabs.getName(original).substring(0, 4) + " - Gray Image";
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							tabs.remove(tabName);
						}
					});
					tabs.addImageTab(tabName, new ImageTab(original, gray, tabs.getName(original), true), button);
					addToSaveItem(tabName, new ImageIcon("RGBtoGray.png"), KeyEvent.VK_G);           
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
				Image original = getSelectedImage();
				Image gray = new Image(original.RGBtoGray());
				JButton button = new JButton();
				String tabName = tabs.getName(original).substring(0, 4) + " - Histogram";
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tabs.remove(tabName);
					}
				});
				tabs.addHistogramTab(tabName, new HistogramTab(original, gray, false), button);
				addToSaveItem(tabName, new ImageIcon("histogram.png"), KeyEvent.VK_H);
			}
		});
	}
	
	private void setUpHistogramAccumulated(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Image original = getSelectedImage();
				Image gray = new Image(original.RGBtoGray());
				JButton button = new JButton();
				String tabName = tabs.getName(original).substring(0, 4) + " - Histogram Accumulated";
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tabs.remove(tabName);
					}
				});
				tabs.addHistogramTab(tabName, new HistogramTab(original, gray, true), button);
				addToSaveItem(tabName, new ImageIcon("histogramAccumulated.png"), KeyEvent.VK_H);
			}
		});
	}
	
    private void setUpEcualized(JMenuItem item) {
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
				Image original = getSelectedImage();
				Image gray = new Image(original.RGBtoGray());
                BufferedImage image = gray.EcualizedImage().getBufferedImage(); 
                JButton button = new JButton();
                String tabName = tabs.getName(original).substring(0, 4) + " - Ecualized Image";
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tabs.remove(tabName);
                    }
                });
                tabs.addImageTab(tabName, new ImageTab(original, image, tabs.getName(original), true), button);
                tabs.addHistogramTab(tabName + " Histogram", new HistogramTab(original, new Image(image), true), button);
                addToSaveItem(tabName, new ImageIcon("EcualizedImage.png"), KeyEvent.VK_E);
                JMenuItem itemBar = menuBar.getItem("File", "Save File");
                if(itemBar != null) {
                    itemBar.setEnabled(true);
                }               
            }
        });
    }
	
	private void setUpLinearTransformation(JMenuItem item) {
		item.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
				Image original = getSelectedImage();
				Image gray = new Image(original.RGBtoGray());
	    		linearTransformationFrame LTFrame = new linearTransformationFrame("Linear transformation");
	    		LTFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    		LTFrame.setLocationRelativeTo(null);
	    		LTFrame.setVisible(true);
	    		LTFrame.btnAceptar.addMouseListener(new MouseAdapter() {
	    			@Override
	    			public void mouseClicked(MouseEvent arg0) {
	    		        int[] nodes = LTFrame.getNodeData();	    		    
			    	    if(nodes.length > 0) {		
			    	    	LTFrame.setVisible(false);
			    	    	LTFrame.dispose();
			    	    	byte[] originalGray = gray.getCopy().getVector();
			    	    	for(int i=0; i<nodes.length-3; i+=2) {
			    	    		if(nodes[i+1] != nodes[i+3]) {
			    	    			if(nodes[i] != nodes[i+2]) {
				    	    			for(int j=0; j<(nodes[i+2]-nodes[i]); j++) {	
				    	    				float dif = ((float)(nodes[i+3]-nodes[i+1])/(float)(nodes[i+2]-nodes[i]));
				    	    				gray.setPixelWithValue(originalGray, nodes[i]+j, nodes[i+1] + (dif*j));
				    	    			}
				    	    		}
			    	    		}else {
				    	    		if(nodes[i] != nodes[i+2]) {
				    	    			gray.setPixelWithValue(originalGray, nodes[i], nodes[i+2], nodes[i+1]);
				    	    		}
			    	    		}
			    	    	}
							JButton button = new JButton();
				    		String tabName = tabs.getName(original).substring(0, 4) + " - Linear transformation";
							button.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									tabs.remove(tabName);
								}
							});
							tabs.addImageTab(tabName, new ImageTab(original, gray.getBufferedImage(), tabs.getName(original), true), button);
							addToSaveItem(tabName, new ImageIcon("linearTransformations.png"), KeyEvent.VK_L);
						}else{
							JOptionPane.showMessageDialog(null, "Can't show the transformed image, try again",
			    					"Error", JOptionPane.ERROR_MESSAGE);
						}
	    		    }
	    		});
	    	}
		});
	}

	private void setUpBrightnessContrast(JMenuItem item) {
		item.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
				Image original = getSelectedImage();
				Image gray = new Image(original.RGBtoGray());
	    		brightnessContrastFrame BGFrame = new brightnessContrastFrame("Change Brightness Contrast", gray.getBrightness(), gray.getContrast());
	    		BGFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    		BGFrame.setLocationRelativeTo(null);
	    		BGFrame.setVisible(true);
	    		BGFrame.btnAceptar.addMouseListener(new MouseAdapter() {
	    			@Override
	    			public void mouseClicked(MouseEvent arg0) {
	    				int[] data = BGFrame.getData();
	    				if(data.length == 2) {
	    					BGFrame.setVisible(false);
			    	    	BGFrame.dispose();
			    	    	gray.BCImage(data[0], data[1]);
							JButton button = new JButton();
							String tabName = tabs.getName(original).substring(0, 4) + " - Brightness Contrast";
							button.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									tabs.remove(tabName);
								}
							});
							tabs.addImageTab(tabName, new ImageTab(original, gray.getBufferedImage(), tabs.getName(original), true), button);
							addToSaveItem(tabName, new ImageIcon("brightnessContrast.png"), KeyEvent.VK_L);
						}else{
							JOptionPane.showMessageDialog(null, "Can't show the modified image, try again",
			    					"Error", JOptionPane.ERROR_MESSAGE);
						}
	    		    }
	    		});
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
