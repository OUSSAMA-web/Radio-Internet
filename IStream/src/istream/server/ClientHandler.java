package istream.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import istream.station.Station;
import istream.util.ReadID3;
import istream.util.UtilFactory;

public class ClientHandler extends Thread {

	protected final boolean metaRequested;
	boolean isInCharge = false;
	protected int lastBytesSent;
	static final int CLIENT_REQUEST = 0;
	static final int CLIENT_PATH = 1;
	static final int CLIENT_PROTOCOL = 2;
	protected static final int OUTPUT_BUFFER_SIZE = 8192;
	protected static final int OUTPUT_TIMEOUT = 160000;
	protected static final int CONNECTION_TIMEOUT = 5000;
	protected static final int SOCKET_BUFFER_SIZE = 65536;
	protected static final int PREBUFFER_TIME = 500;
	protected final Socket client;
	protected final OutputStream clientOutputStream;
	protected final Map<String, String> clientHeader;
	protected long bytesSent = -1L;
	Station station;
	ReadID3 readMetaData;

	public ClientHandler(Socket forwardSocket, Properties properties, Station station) throws IOException {
		System.out.println(forwardSocket);
		this.client = forwardSocket;
		this.station = station;
		client.setSoTimeout(CONNECTION_TIMEOUT);
		client.setSendBufferSize(SOCKET_BUFFER_SIZE);
		this.clientOutputStream = client.getOutputStream();
		this.clientHeader = getClientHeader();

		bytesSent = station.getBytesSent();

		this.setDaemon(true);
		this.setPriority(6);
		this.setName(this.getClass().getSimpleName().concat(":").concat(client.getInetAddress().getHostAddress()));

		String metavalue;

		if (clientHeader != null && (metavalue = clientHeader.get("icy-metadata")) != null && metavalue.equals("1"))
			metaRequested = true;
		else
			metaRequested = false;

		readMetaData = new ReadID3();

	}

	public ClientHandler(Socket forwardSocket, Station station) throws IOException {
		this(forwardSocket, new Properties(), station);
	}

	public int sendStream(byte[] inputBuffer, int limit) throws IOException {

		if (lastBytesSent + limit >= OUTPUT_BUFFER_SIZE) {

			clientOutputStream.write(inputBuffer, 0, OUTPUT_BUFFER_SIZE - lastBytesSent);
			clientOutputStream.write(getMeta());
			clientOutputStream.write(inputBuffer, OUTPUT_BUFFER_SIZE - lastBytesSent,
					limit + lastBytesSent - OUTPUT_BUFFER_SIZE);

			lastBytesSent -= OUTPUT_BUFFER_SIZE;

		} else {
			clientOutputStream.write(inputBuffer, 0, limit);
		}

		lastBytesSent += limit;

		return limit;

	}

	public byte[] getValidRequestHeader() throws IOException {
		readMetaData.parse(station.getListStreams().get(station.getCurrentlyPlayingIndex()));

		ByteArrayOutputStream out;
		Writer header = new OutputStreamWriter(out = new ByteArrayOutputStream(350));

		header.write("ICY 200 OK\r\n");
		header.write("icy-notice1: Welcome to the stream and enjoy\r\n");

		header.write("icy-name: ");
		header.write(station.getName());
		header.write("\r\n");

		header.write("icy-genre: ");
		header.write("EDM");
		header.write("\r\n");

		header.write("icy-url: ");
		header.write("");
		header.write("\r\n");

		header.write("content-type: ");
		header.write("mp3/ogg");
		header.write("\r\n");

		if (metaRequested)
			header.write("icy-metaint: ".concat(String.valueOf(OUTPUT_BUFFER_SIZE)).concat("\r\n"));

		header.write("icy-br: 128\r\n");
		header.write("\r\n");

		header.close();

		return out.toByteArray();

	}

	public byte[] getBadRequestHeader() throws IOException {
		ByteArrayOutputStream out;
		Writer header = new OutputStreamWriter(out = new ByteArrayOutputStream(100));

		header.write("400 BAD REQUEST\n\n");
		header.close();

		return out.toByteArray();

	}

	public boolean isValidClientHeader(String[] firstLineHeader) {
		return firstLineHeader != null && firstLineHeader.length == 3 && firstLineHeader[CLIENT_REQUEST].equals("GET")
				&& firstLineHeader[CLIENT_PROTOCOL].startsWith("HTTP") && firstLineHeader[CLIENT_PATH].length() > 0;

	}

	private byte[] getMeta() throws IOException {
		readMetaData.parse(station.getListStreams().get(station.getCurrentlyPlayingIndex()));
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream(256);
		final Writer metaWriter = new OutputStreamWriter(byteStream, "US-ASCII");
		
		byteStream.write(0);

		metaWriter.write("StreamTitle='");
		metaWriter.write(readMetaData.getArtist() + " - " + readMetaData.getTitle());
		metaWriter.write("';");

		metaWriter.close();

		final byte[] byteArray = byteStream.toByteArray();
		final int length = Math.min(256, (byteArray.length / 16) + (byteArray.length % 16 == 0 ? 0 : 1));
		byteArray[0] = (byte) length;

		return Arrays.copyOf(byteArray, 1 + length * 16);

	}

	private Map<String, String> getClientHeader() throws IOException {

		Map<String, String> header = UtilFactory.getSocketHeader(new BufferedInputStream(client.getInputStream()));
		String[] headLine = UtilFactory.parseStreamHead(header.get(null));
		client.shutdownInput();

		if (isValidClientHeader(headLine) == false) {
			clientOutputStream.write(getBadRequestHeader());
			try {
				throw new IOException("Bad request");
			} finally {
				this.finalize();
			}
		}
		
		System.out.println("Streaming to: " + client.getInetAddress().getHostName() + " "
				+ client.getInetAddress().getHostAddress());

		return header;

	}

	public void run() {

		try {

			clientOutputStream.write(getValidRequestHeader());
			RandomAccessFile raf = new RandomAccessFile(
					station.getListStreams().get(station.getCurrentlyPlayingIndex()), "r");
			raf.seek(bytesSent);

			final byte[] inputBuffer = new byte[OUTPUT_BUFFER_SIZE];
			int limit = 1;

			while (raf.length() == 0) {
				Thread.sleep(PREBUFFER_TIME);
			}

			while (this.isInterrupted() == false) {
				limit = raf.read(inputBuffer);

				if (limit == -1) {
					if (isInCharge)
						raf = station.changeNext();
					raf.seek(0);
					bytesSent = -1L;
					limit = 1;
				} else {
					bytesSent += (long) sendStream(inputBuffer, limit);
					if (isInCharge)
						station.setBytesSent(bytesSent);
					// System.out.println(station.getBytesSent());
				}
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();

		} finally {
			System.out.println("Stopped streaming to: " + client.getInetAddress().getHostName() + " "
					+ client.getInetAddress().getHostAddress());

			this.finalize();
		}
	}
	
	public boolean isInCharge() {
		return isInCharge;
	}

	public void setInCharge(boolean isInCharge) {
		this.isInCharge = isInCharge;
	}

	public boolean shutdown() {

		if (this.isAlive()) {
			this.interrupt();
			return true;

		} else
			return false;

	}

	protected void finalize() {

		try {
			client.close();
		} catch (IOException e) {
		}

		try {
			super.finalize();
		} catch (Throwable e) {
		}

	}
}
