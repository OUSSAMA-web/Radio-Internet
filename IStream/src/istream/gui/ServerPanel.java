package istream.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.PortUnreachableException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import istream.server.Server;

public class ServerPanel extends JPanel {

	JTextArea serverLog;
	JScrollPane serverLogContainer;
	JButton startServer, stopServer, editServer;

	Server server;

	public ServerPanel() {

		serverLog = new JTextArea();
		serverLogContainer = new JScrollPane();
		startServer = new JButton("Start");
		stopServer = new JButton("Stop");
		editServer = new JButton("Edit");

		stopServer.setEnabled(false);

		startServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				server.run();
				stopServer.setEnabled(true);
				startServer.setEnabled(false);
				editServer.setEnabled(false);
			}
		});

		stopServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				server.shutdown();
				stopServer.setEnabled(false);
				startServer.setEnabled(true);
				editServer.setEnabled(true);
			}
		});

		add(startServer);
		add(stopServer);
		add(editServer);

		serverLog.setEditable(false);
		serverLog.setColumns(40);
		serverLog.setRows(25);
		serverLogContainer.setViewportView(serverLog);

		add(serverLogContainer);

		try {
			server = new Server(65500, this);
		} catch (PortUnreachableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JTextArea getLogArea() {
		return serverLog;
	}

	public void log(String log) {
		String time = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss").format(Calendar.getInstance().getTime());
		serverLog.append("[" + time + "] " + log);
		serverLog.append("\n");
	}

}
