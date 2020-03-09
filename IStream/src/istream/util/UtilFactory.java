package istream.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class UtilFactory {

	static final int CLIENT_REQUEST = 0;
	public static final int CLIENT_PATH = 1;
	static final int CLIENT_PROTOCOL = 2;
	final static int KEY_BUFFER = 30;
	final static int VALUE_BUFFER = 100;
	final static int MAXIMUM_HEADER_LENGTH = 50000;

	private UtilFactory() {
	}

	public static String[] parseStreamHead(InputStream in) throws IOException {

		int headerIndex = 0, currentChar, totalBytesRead = 0;

		in = new PushbackInputStream(in, 1);

		final StringBuilder metaBuffer = new StringBuilder(VALUE_BUFFER);
		final String[] header = { "", "", "" };

		while ((currentChar = in.read()) != -1) {

			if (totalBytesRead > MAXIMUM_HEADER_LENGTH)
				throw new IOException("The source stream does not seem to provide a valid header");
			else
				totalBytesRead++;

			if (currentChar == '\r') {
				currentChar = in.read();

				if (currentChar == '\n') {
					header[2] = metaBuffer.toString().trim();
					return header;
				}

			} else if (headerIndex < 2 && currentChar == ' ') {
				header[headerIndex] = metaBuffer.toString();
				metaBuffer.setLength(0);
				headerIndex++;

			} else {
				metaBuffer.append((char) currentChar);
			}
		}

		return null;

	}

	public static String[] parseStreamHead(String headLine) throws IOException {

		if (headLine == null)
			return null;
		else
			return parseStreamHead(new ByteArrayInputStream(headLine.concat("\r\n").getBytes()));

	}

	public static Map<String, String> getSocketHeader(InputStream in) throws IOException {

		int currentChar, totalBytesRead = 0;
		boolean readingKey = true;

		final StringBuilder[] keyValuePair = { new StringBuilder(KEY_BUFFER), new StringBuilder(VALUE_BUFFER) };
		final Map<String, String> header = new HashMap<String, String>();

		in = new PushbackInputStream(in, 1);

		while ((currentChar = in.read()) != -1) {

			if (totalBytesRead > MAXIMUM_HEADER_LENGTH)
				throw new IOException("The source stream does not seem to provide a valid header");
			else
				totalBytesRead++;

			if (currentChar == '\r') {

				currentChar = in.read();

				if (currentChar == '\n' && keyValuePair[0].length() != 0) {

					if (keyValuePair[1].length() == 0 && header.size() == 0)
						header.put(null, keyValuePair[0].toString().trim());
					else
						header.put(keyValuePair[0].toString().toLowerCase().trim(), keyValuePair[1].toString().trim());

					keyValuePair[0].setLength(0);
					keyValuePair[1].setLength(0);
					readingKey = true;

				} else if (currentChar == '\n')
					break;

				else
					((PushbackInputStream) in).unread(currentChar);

			} else if (readingKey && currentChar == ':')
				readingKey = false;

			else {

				if (readingKey)
					keyValuePair[0].append((char) currentChar);
				else
					keyValuePair[1].append((char) currentChar);

			}

		}

		return header;
	}

	public static int byteToInt(byte myByte) {
		return myByte < 0 ? (int) (Byte.MIN_VALUE * (-2) + myByte) : (int) myByte;
	}

	public static long[] getDuration(long timestamp) {

		long time = (new Date().getTime() - timestamp) / 1000;
		if (time < 0L)
			throw new IllegalArgumentException("Timestamp invalid.");

		long[] duration = { 0L, 0L, 0L, (time / 60L / 60L / 24L) };
		duration[2] = (time - duration[3]) / 60L / 60L;
		duration[1] = (time - duration[2]) / 60L;
		duration[0] = time - duration[1];

		return duration;

	}

	public static String getDurationAsString(long[] duration) {

		StringBuilder sb = new StringBuilder("Runs for ");
		if (duration[3] != 0)
			sb.append(String.valueOf(duration[3])).append(" days, ");

		if (duration[3] == 0 && duration[2] == 0 && duration[1] == 0)
			sb.append(String.valueOf(duration[0])).append(" seconds");
		else {
			sb.append(new Formatter().format("%02d", duration[2])).append(":");
			sb.append(new Formatter().format("%02d (h:s)", duration[1]));
		}

		return sb.toString();

	}

	public static String getDurationAsString(long timestamp) {
		return getDurationAsString(getDuration(timestamp));
	}

	public static String getTrafficAsString(long traffic) {
		return new Formatter().format("%.2f MB", ((double) (traffic / 1024) / 1024)).toString();
	}

}
