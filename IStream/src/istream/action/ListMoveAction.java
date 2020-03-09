package istream.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListMoveAction extends AbstractAction implements ListSelectionListener {
	
	private static final long serialVersionUID = 1L;
	
	public final static int TOP = -2;
	public final static int UP = -1;
	public final static int DOWN = 1;
	public final static int BOTTOM = 2;

	private final JList list;
	private final int direction;
	
	public ListMoveAction(JList list, int direction) {
	
		this.list = list;
		this.direction = direction;
		
		this.valueChanged(null);
		list.addListSelectionListener(this);

	}

	/**
	 * Moves the list items
	 */
	public int getDirection() {
		DefaultListModel listModel = (DefaultListModel)list.getModel();

		int[] selected = list.getSelectedIndices() ;
		int[] newSelected = new int[selected.length];
	
		System.out.println(list.getSelectedIndex());
		int listSize, swapIndex, swapTo;
			
		listSize = listModel.getSize() ;
		//System.out.println(listModel.getSize());	
		for(int i = 0; i < selected.length; i++) {
			
			if(direction < 0) {
				swapIndex = i;
				swapTo = direction == -1 ? selected[swapIndex] - 1 : i;
			} else {
				swapIndex = selected.length - 1 - i;
				swapTo = direction == 1 ? selected[swapIndex] + 1 : listSize - 1 - i;
			}
				
			if((direction < 0 && swapTo < i) || (direction > 0 && swapTo >= listSize - i)) {
				newSelected[swapIndex] = selected[swapIndex];
				continue;
			}
				
			listModel.set(selected[swapIndex], listModel.set(swapTo, listModel.get(selected[swapIndex])));
			newSelected[swapIndex] = swapTo;
				
		}
			
		//list.setSelectedIndices(newSelected);
		return direction ; 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		DefaultListModel listModel = (DefaultListModel)list.getModel();

		int[] selected = list.getSelectedIndices() ;
		int[] newSelected = new int[selected.length];
	
		System.out.println(list.getSelectedIndex());
		int listSize, swapIndex, swapTo;
			
		if((listSize = listModel.getSize()) < 2 || selected.length == listSize)
			return;
		//Sstem.out.println(listModel.getSize());	
		for(int i = 0; i < selected.length; i++) {
			
			if(direction < 0) {
				swapIndex = i;
				swapTo = direction == -1 ? selected[swapIndex] - 1 : i;
			} else {
				swapIndex = selected.length - 1 - i;
				swapTo = direction == 1 ? selected[swapIndex] + 1 : listSize - 1 - i;
			}
				
			if((direction < 0 && swapTo < i) || (direction > 0 && swapTo >= listSize - i)) {
				newSelected[swapIndex] = selected[swapIndex];
				continue;
			}
				
			listModel.set(selected[swapIndex], listModel.set(swapTo, listModel.get(selected[swapIndex])));
			newSelected[swapIndex] = swapTo;
				
		}
			
		list.setSelectedIndices(newSelected);
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		int[] selected = list.getSelectedIndices();
		
		if(selected.length == 0 
				|| (direction < 0 && selected[selected.length-1] == selected.length - 1)
				|| (direction > 0 && selected[0] == (list.getModel().getSize() - selected.length)))
			this.setEnabled(false);
		else
			this.setEnabled(true);
	}
	
}