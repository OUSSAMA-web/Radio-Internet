package istream.main;


import java.net.PortUnreachableException;

import istream.gui.MainFrame;
import istream.server.ServerCore;
import istream.station.Station;

public class ServerMain {

	public static void main(String[] args) {
		
		try {
			
			Station station = new Station("UPEC");
			ServerCore server = new ServerCore(65500, station);
			
			MainFrame frame = new MainFrame("Shoutcast", server, station);
			frame.setVisible(true);
			
		} catch (PortUnreachableException e1) {
			e1.printStackTrace();
		}
	}
}
