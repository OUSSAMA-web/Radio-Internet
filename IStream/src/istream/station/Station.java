package istream.station;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class Station {
	static final String streamsDirectoryPath = System.getProperty("user.home") + "/Documents/Streams";
	final String name;
	private final LinkedList<File> listStreams;
	static File streamsDirectory;
	static final int initialIndex = 0;
	private int currentlyPlayingIndex = -1;
	long bytesSent = 1L;
	RandomAccessFile raf;
	File file;
	

	public Station(String name) {

		this.name = name;
		listStreams = new LinkedList<File>();
		streamsDirectory = new File(streamsDirectoryPath);
		streamsDirectory.mkdir();
		
		update();
		try {
			if(!listStreams.isEmpty())
				raf = new RandomAccessFile(playNext(), "r");
			else 
				raf = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isEmpty() {
		return listStreams.isEmpty();
	} 

	public String getName() {
		return this.name;
	}
	

	public RandomAccessFile getRaf() {
		return raf;
	}

	public void setRaf(RandomAccessFile raf) {
		this.raf = raf;
	}

	public File getStreamsDirectory() {
		return streamsDirectory;
	}

	public void setStreamsDirectory(File streamsDirectory) {
		this.streamsDirectory = streamsDirectory;
	}

	public static String getStreamsdirectorypath() {
		return streamsDirectoryPath;
	}

	public void update() {

		if (!listStreams.isEmpty()) {
			listStreams.removeAll(listStreams);
		}
		for (File f : streamsDirectory.listFiles()) {
			if (f != null) {
				listStreams.add(f);
			}
		}
	}

	public LinkedList<File> getListStreams() {
		return listStreams;
	}
	
	public File playNext() {
		if(currentlyPlayingIndex == listStreams.size()-1) currentlyPlayingIndex = initialIndex;
		
		else currentlyPlayingIndex++;
		
		return listStreams.get(currentlyPlayingIndex);
		
	}
	
	public synchronized RandomAccessFile changeNext() {
		try {
		 raf = new RandomAccessFile(playNext(), "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return raf;
		
	}

	public int getCurrentlyPlayingIndex() {
		return currentlyPlayingIndex;
	}

	public synchronized void setBytesSent(long bytesSent) {
		this.bytesSent = bytesSent;
	}

	public long getBytesSent() {
		return bytesSent;
	}

}
