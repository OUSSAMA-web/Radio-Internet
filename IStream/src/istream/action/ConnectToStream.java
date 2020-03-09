package istream.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import istream.server.ServerCore;
import istream.station.Station;

public class ConnectToStream implements ActionListener {

	private final Executor executor;
	Station station;
	ServerCore server;

	public ConnectToStream(Station station, ServerCore server) {
		executor = Executors.newSingleThreadExecutor();
		this.station = station;
		this.server = server;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (station.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Fill up the station first");
		} else {
			try {
				if (server.isClosed() || !server.isAlive()) {
					JOptionPane.showMessageDialog(null, "Start the server first");
					System.out.println(server.isClosed());
				} else {
					System.out.println(server.isClosed());
					final File playlist = File.createTempFile("playlist", ".pls");
					PrintWriter playlistWriter = new PrintWriter(new BufferedWriter(new FileWriter(playlist)));
					playlist.deleteOnExit();

					playlistWriter.printf("[playlist]%n%n");
					playlistWriter.printf("NumberOfEntries=1%n%n");
					playlistWriter.printf("File1=http://localhost:%d/", ServerCore.getPort());
					playlistWriter.close();

					executor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								Desktop.getDesktop().open(playlist);
							} catch (IOException e) {
								/* empty */ }
						}
					});
				}

			} catch (IOException e1) {
			}

		}

	}

}
