package istream.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import istream.action.AddFromDistantServer;
import istream.action.ConnectToStream;
import istream.action.ListMoveAction;
import istream.action.RemoveFromStream;
import istream.server.ServerCore;
import istream.station.Station;

public class StationsPanel extends JPanel {
	
	File file;
	JButton serverDialogButton, addFromLocalDrive, addFromDistantServer, remove, connect;
	JTextField serverURL;
	JList<File> list;
	static DefaultListModel<File> listModel;
	JScrollPane listScroller;
	JFileChooser chooseFilePopUp;

	JPanel buttonsPanel1;
	JPanel buttonsPanel2;
	
	BoxLayout boxLayout;
	FileNameExtensionFilter extentionFilter;
	static Station station;
	ServerCore server;
	DistantServerUrl distantServerUrlGui;

	public StationsPanel(Station station, ServerCore server) {
		this.station = station;
		this.server = server;
		buttonsPanel1 = new JPanel();
		buttonsPanel2 = new JPanel();
		
		boxLayout = new BoxLayout(buttonsPanel1, BoxLayout.X_AXIS);
		buttonsPanel1.setLayout(boxLayout);
		boxLayout = new BoxLayout(buttonsPanel2, BoxLayout.X_AXIS);
		buttonsPanel2.setLayout(boxLayout);
		distantServerUrlGui = new DistantServerUrl(station, this);

		listModel = new DefaultListModel();
		System.out.println(System.getProperty("user.home"));

		for (File f : station.getStreamsDirectory().listFiles()) {
			listModel.addElement(f);
		}

		list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(555, 300));

		chooseFilePopUp = new JFileChooser();
		extentionFilter = new FileNameExtensionFilter("mp3/ogg filter", "mp3", "ogg");
		chooseFilePopUp.setFileFilter(extentionFilter);

		station.update();

		for (int i = 0; i < station.getListStreams().size(); i++)
			System.out.println(station.getListStreams().get(i));

		addFromLocalDrive = new JButton("Add from l. drive");
		addFromDistantServer = new JButton("Add from d. server");
		remove = new JButton("Remove");
		connect = new JButton("Connect locally");

		addFromLocalDrive.setBackground(Color.WHITE);
		addFromDistantServer.setBackground(Color.WHITE);
		remove.setBackground(Color.WHITE);
		connect.setBackground(Color.WHITE);

		connect.addActionListener(new ConnectToStream(station, server));

		addFromDistantServer.addActionListener(new AddFromDistantServer(distantServerUrlGui));

		addFromLocalDrive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (station.isEmpty()) {
					int returnVal = chooseFilePopUp.showOpenDialog(StationsPanel.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = chooseFilePopUp.getSelectedFile();
						station.getStreamsDirectory().mkdir();
						try {
							Files.copy(file.toPath(),
									new File(station.getStreamsdirectorypath() + "/" + file.getName()).toPath(),
									StandardCopyOption.REPLACE_EXISTING);
							station.update();
							updateListModel();
							station.setRaf(new RandomAccessFile(station.playNext(), "r"));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					int returnVal = chooseFilePopUp.showOpenDialog(StationsPanel.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						file = chooseFilePopUp.getSelectedFile();
						station.getStreamsDirectory().mkdir();
						try {
							Files.copy(file.toPath(),
									new File(station.getStreamsdirectorypath() + "/" + file.getName()).toPath(),
									StandardCopyOption.REPLACE_EXISTING);
							station.update();
							updateListModel();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		remove.addActionListener(new RemoveFromStream(list));
		
		JButton top = new JButton(new ListMoveAction(list, ListMoveAction.TOP));
		top.setText("TOP");
		top.setBackground(Color.WHITE);
	   // top.setForeground(Color.GRAY);
		
	    JButton up = new JButton(new ListMoveAction(list, ListMoveAction.UP));
		up.setText("UP");
		up.setBackground(Color.WHITE);
		
		JButton down = new JButton(new ListMoveAction(list, ListMoveAction.DOWN));
		down.setText("DOWN");
		down.setBackground(Color.WHITE);

		JButton bottom = new JButton(new ListMoveAction(list, ListMoveAction.BOTTOM));
		bottom.setText("BOTTOM");
		bottom.setBackground(Color.WHITE);
		
		
		buttonsPanel2.add(top) ; 
		buttonsPanel2.add(Box.createRigidArea(new Dimension(0, 5)));     
		buttonsPanel2.add(up) ; 
		buttonsPanel2.add(Box.createRigidArea(new Dimension(0, 5)));     
		buttonsPanel2.add(down) ; 
		buttonsPanel2.add(Box.createRigidArea(new Dimension(0, 5)));     
		buttonsPanel2.add(bottom) ; 
		buttonsPanel2.setSize(50,50);
		
		add(buttonsPanel1);
		add(listScroller);

		buttonsPanel1.add(addFromLocalDrive);
		buttonsPanel1.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonsPanel1.add(addFromDistantServer);
		buttonsPanel1.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonsPanel1.add(remove);
		buttonsPanel1.add(Box.createRigidArea(new Dimension(5, 0)));
		buttonsPanel1.add(connect);

		add(buttonsPanel2);

	}

	public static void updateListModel() {
		listModel.removeAllElements();
		for (File f : station.getStreamsDirectory().listFiles()) {
			listModel.addElement(f);
		}
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

}
