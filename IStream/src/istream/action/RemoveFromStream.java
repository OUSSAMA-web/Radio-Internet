package istream.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JList;

import istream.gui.StationsPanel;
import istream.station.Station;

public class RemoveFromStream implements ActionListener {

	JList<File> list;
	public RemoveFromStream(JList<File> list) {
		this.list = list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (list.getSelectedValue() != null) {
			try {
				Files.deleteIfExists(list.getSelectedValue().toPath());
				StationsPanel.updateListModel();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}

}

