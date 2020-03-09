package istream.server;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import istream.gui.ServerPanel;

public class Server extends Thread {

	public static final int MAXIMUM_PORTNUMBER = 65535;
	protected final int port;
	protected final ServerSocket serverSocket;
	ServerPanel panel;

	public Server(int port, ServerPanel panel) throws PortUnreachableException {

		this.panel = panel;

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
		System.out.println("Started from run");
		panel.log("Server of Type ICY, listening on port " + port);

		/*while (this.isInterrupted() == false) {

			try {
				handleRequest(serverSocket.accept());
			} catch (IOException e) {

			}
		}

		panel.log("Server of Type ICY, stopped listening on port " + port);

		try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	protected void handleRequest(Socket clientSocket/* , AudioStreamType audioStreamType */) {

		/*
		 * try {
		 * 
		 * AudioStreamRequestHandler requestHandler;
		 * 
		 * switch(audioStreamType) {
		 * 
		 * case ICY: requestHandler = new ICYRequestHandler(clientSocket); break;
		 * 
		 * default: requestHandler = null; break;
		 * 
		 * }
		 * 
		 * if(requestHandler != null) {
		 * 
		 * clients.add(requestHandler); requestHandler.start();
		 * 
		 * ServerClientEvent e = new ServerClientEvent(this);
		 * for(AudioStreamServerListener listener : serverListeners)
		 * listener.newConnection(e);
		 * 
		 * }
		 * 
		 * } catch(IOException e) {
		 * 
		 * }
		 */
	}

	public final int getPort() {
		return this.port;
	}
	
	public boolean shutdown() {
		
		if(serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {
				/* empty */
			}
		panel.log("Server shutdown");

		if(this.isAlive()) {
			this.interrupt();
			return true;
		} else
			return false;
		
		
	}
	
	@Override
	protected void finalize() {				
		if(serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {
				/* empty */
			}
		
		try {
			super.finalize();
		} catch (Throwable e) {
			/* empty */
		}
		
	}

}
