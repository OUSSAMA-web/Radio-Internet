package istream.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import istream.station.Station;
import istream.util.ConvertMp3;

public class DistantServerUrl extends JFrame{
	JPanel panel;
	JTextField url;
	JButton addUrl;
	String name = "URL for distant server";
	Station station;

	public DistantServerUrl(Station station, StationsPanel stationPanel) {
		this.station = station;
		
		panel = new JPanel();
		url = new JTextField("http://radio-pls.rtl.fr/fun_mq.pls");
		addUrl = new JButton("add and apply");
		
		url.setPreferredSize(new Dimension(400,40));
		
		addUrl.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ConvertMp3(url.getText(), station.getStreamsdirectorypath());
				setVisible(false);		
				stationPanel.updateListModel();
			}
		});
		
		setSize(500, 80);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		setTitle(name);
		
		panel.add(url);
		panel.add(addUrl);
		
		add(panel);
		pack();
		
	}
}
