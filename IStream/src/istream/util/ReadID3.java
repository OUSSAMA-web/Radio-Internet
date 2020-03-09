package istream.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Properties;

public class ReadID3 extends Properties {
	private String m_artist;
	private String m_title;
	private String m_album;
	private String m_genre;

	private File file;

	public String getAlbum() {
		return m_album == null ? "Unknown" : m_album;
	}

	public String getArtist() {
		return m_artist == null ? "Unknown" : m_artist;
	}

	public String getTitle() {
		return m_title == null ? "Unknown" : m_title;
	}

	public String getGenre() {
		return m_genre == null ? "Unknown" : m_genre;
	}

	public boolean parseNoThrow(File mp3file) {
		try {
			return parse(mp3file);
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean parse(File mp3file) throws IOException {
		this.file = mp3file;
		m_artist = null;
		m_title = null;
		m_album = null;
		m_genre = null;

		RandomAccessFile file = new RandomAccessFile(mp3file, "r");
		byte[] headerbuf = new byte[10];
		file.read(headerbuf);

		if (headerbuf[0] != 'I' || headerbuf[1] != 'D' || headerbuf[2] != '3') {
			file.close();
			return false;
		}

		final int TagVersion = headerbuf[3];

		if (TagVersion < 0 || TagVersion > 4) {
			file.close();
			return false;
		}

		int tagsize = (headerbuf[9] & 0xFF) | ((headerbuf[8] & 0xFF) << 7) | ((headerbuf[7] & 0xFF) << 14)
				| ((headerbuf[6] & 0xFF) << 21) + 10;
		boolean uses_synch = (headerbuf[5] & 0x80) != 0 ? true : false;
		boolean has_extended_hdr = (headerbuf[5] & 0x40) != 0 ? true : false;

		if (has_extended_hdr) {
			int headersize = file.read() << 21 | file.read() << 14 | file.read() << 7 | file.read();
			file.skipBytes(headersize - 4);
		}

		byte[] buffer = new byte[tagsize];
		file.read(buffer);
		file.close();

		int length = buffer.length;

		if (uses_synch) {
			int newpos = 0;
			byte[] newbuffer = new byte[tagsize];

			for (int i = 0; i < buffer.length; i++) {
				if (i < buffer.length - 1 && (buffer[i] & 0xFF) == 0xFF && buffer[i + 1] == 0) {
					newbuffer[newpos++] = (byte) 0xFF;
					i++;
					continue;
				}
				newbuffer[newpos++] = buffer[i];
			}
			length = newpos;
			buffer = newbuffer;
		}

		int pos = 0;
		final int ID3FrameSize = TagVersion < 3 ? 6 : 10;

		while (true) {
			int rembytes = length - pos;

			if (rembytes < ID3FrameSize)
				break;

			if (buffer[pos] < 'A' || buffer[pos] > 'Z')
				break;

			String framename;
			int framesize;

			if (TagVersion < 3) {
				framename = new String(buffer, pos, 3);
				framesize = ((buffer[pos + 5] & 0xFF) << 8) | ((buffer[pos + 4] & 0xFF) << 16)
						| ((buffer[pos + 3] & 0xFF) << 24);
			} else {
				framename = new String(buffer, pos, 4);
				framesize = (buffer[pos + 7] & 0xFF) | ((buffer[pos + 6] & 0xFF) << 8)
						| ((buffer[pos + 5] & 0xFF) << 16) | ((buffer[pos + 4] & 0xFF) << 24);
			}

			if (pos + framesize > length)
				break;

			if (framename.equals("TPE2") || framename.equals("TPE3") || framename.equals("TPE")) {
				if (m_artist == null)
					m_artist = parseTextField(buffer, pos + ID3FrameSize, framesize);
			}

			if (framename.equals("TIT2") || framename.equals("TIT")) {
				if (m_title == null)
					m_title = parseTextField(buffer, pos + ID3FrameSize, framesize);
			}

			if (framename.equals("TALB")) {
				if (m_album == null)
					m_album = parseTextField(buffer, pos + ID3FrameSize, framesize);
			}

			if (framename.equals("TCON")) {
				if (m_genre == null)
					m_genre = parseTextField(buffer, pos + ID3FrameSize, framesize);
			}

			pos += framesize + ID3FrameSize;
			continue;
		}

		return m_title != null || m_artist != null || m_album != null || m_genre != null;
	}

	private String parseTextField(final byte[] buffer, int pos, int size) {
		if (size < 2)
			return null;

		Charset charset;
		int charcode = buffer[pos];

		if (charcode == 0)
			charset = Charset.forName("ISO-8859-1");
		else if (charcode == 3)
			charset = Charset.forName("UTF-8");
		else
			charset = Charset.forName("UTF-16");

		return charset.decode(ByteBuffer.wrap(buffer, pos + 1, size - 1)).toString();
	}

	public String getFileExtension() {
		return ".mp3";
	}

	public void store() {

		String filename = file.getName();
		String filepath = file.getPath();

		Properties prop = new Properties();

		prop.put("name", filename);
		prop.put("path", filepath);
		try {

			FileOutputStream fos = new FileOutputStream("properties.xml");
			FileInputStream fis = new FileInputStream("properties.xml");

			prop.storeToXML(fos, "Properties Comment");

			while (fis.available() > 0) {
				System.out.print("" + (char) fis.read());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}