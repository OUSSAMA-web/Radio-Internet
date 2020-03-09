package istream.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import istream.gui.DistantServerUrl;

public class AddFromDistantServer implements ActionListener {
	
	DistantServerUrl popUp;

	public AddFromDistantServer(DistantServerUrl popUp) {
		this.popUp = popUp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		popUp.setVisible(true);

	}

}
