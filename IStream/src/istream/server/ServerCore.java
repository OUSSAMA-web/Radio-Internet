package istream.server;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import istream.gui.ServerPanel;
import istream.station.Station;

public class ServerCore extends Thread {

	private final List<ClientHandler> clients;
	public static final int MAXIMUM_PORTNUMBER = 65535;
	protected static int port;
	protected ServerSocket serverSocket;
	ServerPanel panel;
	Station station;
	Boolean isRunning = true;

	public ServerCore(int port, Station station) throws PortUnreachableException {
		this.station = station;
		clients = new LinkedList<ClientHandler>();
		ServerSocket serverSocket = null;

		if (port < 0 || port > MAXIMUM_PORTNUMBER)
			throw new PortUnreachableException("Illegal port argument");

		while (serverSocket == null && port != -1) {
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				port = port == 0 ? -1 : 0;
			}
		}

		if (serverSocket == null)
			throw new PortUnreachableException("The connection of the server to a port failed");

		this.port = serverSocket.getLocalPort();
		this.serverSocket = serverSocket;
		this.setPriority(3);
	}

	@Override
	public void run() {
		System.out.println("Started from run " + port);
		panel.log("SHOUTcast server listening on port " + port);

		while (this.isInterrupted() == false) {

			try {
				handleRequest(serverSocket.accept());
			} catch (IOException e) {

			}
		}

		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	protected void handleRequest(Socket clientSocket) {

		try {

			ClientHandler requestHandler = new ClientHandler(clientSocket, station);

			if (requestHandler != null) {

				clients.add(requestHandler);
				requestHandler.start();
				panel.log("Started streaming to " + clientSocket.getInetAddress().getHostName() + ", connected from "
						+ clientSocket.getInetAddress().getHostAddress());
				panel.updateCount(clients.size());

				if (clients.size() == 1 && clients.get(0) != null && !clients.get(0).isInCharge())
					clients.get(0).setInCharge(true);
				System.out.println(requestHandler.isInCharge);
			}

		} catch (IOException e) {

		}

	}

	public static int getPort() {
		return port;
	}

	public boolean shutdown() {

		if (serverSocket != null) {
			try {
				serverSocket.close();
				panel.log("Server shutdown");
				System.out.println("Server shutdown");
			} catch (IOException e) {
			}
		}

		if (this.isAlive()) {
			this.interrupt();
			System.out.println("Server kill");
			return true;
		} else
			return false;

	}

	public void close() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
				System.out.println("Server shutdown from close");
				panel.log("Server closed");
			} catch (IOException e) {
			}

			for (ClientHandler r : clients) {
				r.interrupt();
			}

			clients.clear();

			if (clients.isEmpty())
				panel.log("Clients disconnected");

			panel.updateCount(clients.size());

		}
	}

	public void open() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Opened");
			panel.log("Server opened");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() {
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {
			}

		try {
			super.finalize();
		} catch (Throwable e) {
		}

	}

	public Station getStation() {
		return station;
	}

	public boolean isClosed() {
		return serverSocket.isClosed();
	}

	public void setPanel(ServerPanel panel) {
		this.panel = panel;
	}

}
