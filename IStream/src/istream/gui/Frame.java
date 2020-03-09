package istream.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class Frame extends JFrame{
	
	JTabbedPane tabbedWindow;
	ServerPanel serverPanel;
	StationsPanel stationsPanel;
	
	
	
	public Frame(String name) {
		tabbedWindow = new JTabbedPane();
		serverPanel = new ServerPanel();
		stationsPanel = new StationsPanel();
		
		tabbedWindow.addTab("Server", serverPanel);
		tabbedWindow.addTab("Stations", stationsPanel);
		
		setMaximumSize(new Dimension(500, 450));
		setPreferredSize(getMaximumSize());
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		setTitle(name);
		add(tabbedWindow);
		pack();
	}
}
