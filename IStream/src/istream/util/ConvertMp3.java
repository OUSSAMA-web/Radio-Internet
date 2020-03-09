package istream.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ConvertMp3 {

	URL url;
	byte[] result;
	URLConnection conn;

	public ConvertMp3(String streamUrl, String pathToStreams) {
		try {
			url = new URL(streamUrl);
			conn = url.openConnection();
			System.out.println("1--");
			result = inputStreamToByteArray(conn.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (result != null) {

		}
		
		System.out.println("2--");
		
		try {
			byteArrayToFile(result, pathToStreams + "/" + url.getFile() + ".mp3");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("3--");
		System.out.println(print(result));
	}

	public static byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int bytesRead;
		while ((bytesRead = inStream.read(buffer)) > 0) {
			baos.write(buffer, 0, bytesRead);
		}
		return baos.toByteArray();
	}

	public static void byteArrayToFile(byte[] byteArray, String outFilePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(outFilePath);
		fos.write(byteArray);
		fos.close();
	}

	public static String print(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (byte b : bytes) {
			sb.append(String.format("0x%02X ", b));
		}
		sb.append("]");
		return sb.toString();
	}

}
