package istream.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.PortUnreachableException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import istream.server.ServerCore;

public class ServerPanel extends JPanel {

	JTextArea serverLog;
	JScrollPane serverLogContainer;
	JButton startServer, stopServer;// , editServer;
	JLabel clientsCount;

	public ServerPanel(ServerCore server) {
		serverLog = new JTextArea();
		serverLogContainer = new JScrollPane();
		clientsCount = new JLabel("Listeners: 0");
		startServer = new JButton("Start");
		stopServer = new JButton("Stop");
		// editServer = new JButton("Edit");
		
		startServer.setBackground(Color.WHITE);
		stopServer.setBackground(Color.WHITE);

		stopServer.setEnabled(false);

		startServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(server.getStation().isEmpty()) {
					JOptionPane.showMessageDialog(ServerPanel.this,
						    "Fill up the station first");
				} else {
					
					if (!server.isAlive()) {
					server.start();
					stopServer.setEnabled(true);
					startServer.setEnabled(false);
				}

				if (server.isClosed()) {
					server.open();
					stopServer.setEnabled(true);
					startServer.setEnabled(false);
				
				}
			}
			}
		});

		stopServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (server.isAlive()) {
					// server.stop();
					server.close();
					stopServer.setEnabled(false);
					startServer.setEnabled(true);
				}
			}
		});

		serverLog.setEditable(false);
		serverLog.setColumns(50);
		serverLog.setRows(20);
		serverLogContainer.setViewportView(serverLog);
		
		add(serverLogContainer);
		
		add(startServer);
		add(stopServer);
		add(clientsCount);

	}

	public void log(String log) {
		String time = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());
		serverLog.append("[" + time + "] " + log);
		serverLog.append("\n");
	}
	
	public void updateCount(int count) {
		clientsCount.setText("Listeners: "+count);
	}
}
