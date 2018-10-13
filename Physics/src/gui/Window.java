package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import engine_2_0.HexGridHelper;

public class Window extends JFrame{
	//component section
	private JMenuBar menu;
	private JMenu mBegin, mFile;
	private JMenuItem miStartOne, miStartMany, miStop, miStartUpdates, miStopUpdates, miCalculateAtoms,
						miExportTxt, miExportVMD, miBeginExport, miEndExport, miSave;
	private JLabel stepInfo, layerInfo; 
	
	
	//Action section
	private ActionListener alStartOne = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			helper.runOneRawThread();
		}
	};
	
	private ActionListener alStartMany = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			helper.runMany();
			//helper.startManyUpgraded();
		}
	};
	
	private ActionListener alStop = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			helper.pauseCalculations();
		}
	};
	
	private ActionListener alStartUpdates = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			beginUpdating();
		}
	};
	
	private ActionListener alStopUpdates = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			stopUpdating();
		}
	};
	
	private  ActionListener alCalculateAtoms = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println(helper.grid.calculateAtoms() + "  |  " + helper.grid.coordinates.size());
		}
	};
	
	private ActionListener alExportTxt = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				helper.saveAsDatFile("Dats//" + helper.step + ".txt");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			helper.saveForUser("");
		}
	};
	
	private ActionListener alExportVMD = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			saveForVMD();
		}
	};
	
	private ActionListener alStartVMDAutosaves = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			beginAutosaves();
			//beginUpdating();
		}
	};
	
	private ActionListener alSaveGrid = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			helper.saveGrid();
		}
		
	};
	
	private ActionListener alStopVMDAutosaves = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			endAutosaves();
		}
	};

	
	
	
	//service section
	private HexGridHelper helper;
	public static long steps;
	private Panel panel;
	private boolean updating, backuping, autosaving;
	public int waitTime = 10;
	public int backupPeriod = 60000 * 60;//600000;


	
	
	public Window() {
		super("Molecula diffusion in volume");
		initialize();
		this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					this.finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				//JOptionPane.showMessageDialog(null, e.getKeyCode());
				
				switch(e.getKeyCode()) {
					case(38):{
						if(helper.currentlyPaintedPlane < helper.grid.dimZ - 1) {
							helper.currentlyPaintedPlane++;
						}
						break;
					}
					case(40):{
						if(helper.currentlyPaintedPlane > 0) {
							helper.currentlyPaintedPlane--;
						}
						break;
					}
					case(107):{
						helper.setTemperature(helper.getTemperature() + 0.1);
						break;
					}
					case(109):{
						helper.setTemperature(helper.getTemperature() - 0.1);
						break;
					}
				}
				setTitle( Integer.toString( helper.currentlyPaintedPlane)  + "   " + helper.getTemperature()); 
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
		});
	}

	public void initialize() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700, 500);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		setLayout(null);
		
		panel = new Panel();
		panel.setBounds(0, 25, this.getBounds().width, this.getBounds().height - 25);
		add(panel);
		
		menu = new JMenuBar();
		menu.setVisible(true);
		menu.setBounds(0, 0, this.getBounds().width, 25);
		//menu.setLayout(new FlowLayout());
		
		
		//mBegin section
		mBegin = new JMenu("Actions");
		//mBegin.setBounds(0, 0, 50, 20);
		menu.add(mBegin);
		
		miStartOne = new JMenuItem("Start one");
		miStartOne.addActionListener(alStartOne);
		mBegin.add(miStartOne);
		
		miStartMany = new JMenuItem("Start many");
		miStartMany.addActionListener(alStartMany);
		mBegin.add(miStartMany);
		
		miStop = new JMenuItem("Stop");
		miStop.addActionListener(alStop);
		mBegin.add(miStop);

		miStartUpdates = new JMenuItem("Start updates");
		miStartUpdates.addActionListener(alStartUpdates);
		mBegin.add(miStartUpdates);

		miStopUpdates = new JMenuItem("Stop updates");
		miStopUpdates.addActionListener(alStopUpdates);
		mBegin.add(miStopUpdates);
		
		miCalculateAtoms = new JMenuItem("Calculate atoms");
		miCalculateAtoms.addActionListener(alCalculateAtoms);
		mBegin.add(miCalculateAtoms);
		
		//end of mBegin section
		
		
		//mFile section
		
		mFile = new JMenu("File");
		//mBegin.setBounds(0, 0, 50, 20);
		menu.add(mFile);
		
		miExportTxt = new JMenuItem("Export to .txt file");
		miExportTxt.addActionListener(alExportTxt);
		mFile.add(miExportTxt);

		miExportVMD = new JMenuItem("Export for VMD");
		miExportVMD.addActionListener(alExportVMD);
		mFile.add(miExportVMD);
		
		miBeginExport =  new JMenuItem("Start Exporting");
		miBeginExport.addActionListener(alStartVMDAutosaves);
		mFile.add(miBeginExport);
		
		miEndExport =  new JMenuItem("Stop Exporting");
		miEndExport.addActionListener(alStopVMDAutosaves);
		mFile.add(miEndExport);
		
		miSave = new JMenuItem("Save as backup");
		miSave.addActionListener(alSaveGrid);
		mFile.add(miSave);
		
		menu.add(mFile);
		
		
		
		add(menu);
		menu.updateUI();
	}
	
	public boolean isUpdating() {
		return(updating);
	}
	
	public void stopUpdating() {
		updating = false;
	}
	
	public void beginUpdating() {
		updating = true;
		
		new Thread(new Runnable() {
			public void run() {
				while(updating) {
					refreshPanel();
					try {
						Thread.currentThread().join(waitTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}).start();
	}
	
	public void setHelper(HexGridHelper h) {
		helper = h;
		addPaintable(h);
	}
	
	public void addPaintable(Paintable p) {
		panel.painters.add(p);
		panel.addPainter(p);
	}
	
	public void refreshPanel() {
		panel.repaint();
	}
	
	public void begibBackups() {
		backuping = true;
		new Thread(new Runnable() {
			public void run() {
				while(backuping) {
					try {
						//helper.saveGrid();
						//helper.saveForVMD(steps + "");
						helper.saveAsDatFile("Backups//" + steps + "");
						Thread.currentThread().join(backupPeriod);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch(Exception ex) {
						
					}
				}
			}
		}).start();
	}
	
	public void endBackups() {
		backuping = false;
	}
	
	public void beginAutosaves() {
		//begibBackups();
		autosaving = true;
		new Thread(new Runnable() {
			public void run() {
				while(autosaving) {
					try {
						Thread.currentThread().join(backupPeriod);
						//saveForVMD();
						System.out.print("step:" + helper.step + "  " + helper.grid.coordinates.size() + " | ");
						
//						helper.saveGrid();
//						System.out.print(helper.grid.coordinates.size() + " | ");
						
						helper.saveForVMD(helper.step + ".txt");						
						System.out.print(helper.grid.coordinates.size() + " | ");
						
						helper.saveAsDatFile(helper.step + ".txt");
						System.out.println(helper.grid.coordinates.size());
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void endAutosaves() {
		autosaving = false;
	}
	
	public synchronized void saveForVMD() {
		try {
			helper.saveForVMD(helper.step + ".txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void finalize() {
		updating = false;
		//helper.beginFinalization();
	}
	
	
	
	
	
}
