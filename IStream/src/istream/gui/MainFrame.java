package istream.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import istream.server.ServerCore;
import istream.station.Station;

public class MainFrame extends JFrame{
	
	JTabbedPane tabbedWindow;
	ServerPanel serverPanel;
	StationsPanel stationsPanel;
	
	
	
	public MainFrame(String name, ServerCore server, Station station) {
		tabbedWindow = new JTabbedPane();
		serverPanel = new ServerPanel(server);
		stationsPanel = new StationsPanel(station, server);
		
		tabbedWindow.addTab("Server", serverPanel);
		tabbedWindow.addTab("Station", stationsPanel);
		
		setSize(600, 450);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);	
		setTitle(name);
		add(tabbedWindow);
		server.setPanel(serverPanel);
		//pack();
	}



	public ServerPanel getServerPanel() {
		return serverPanel;
	}



	public void setServerPanel(ServerPanel serverPanel) {
		this.serverPanel = serverPanel;
	}



	public StationsPanel getStationsPanel() {
		return stationsPanel;
	}



	public void setStationsPanel(StationsPanel stationsPanel) {
		this.stationsPanel = stationsPanel;
	}
	
	
}
