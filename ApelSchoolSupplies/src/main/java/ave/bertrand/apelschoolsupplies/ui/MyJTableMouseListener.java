package ave.bertrand.apelschoolsupplies.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import ave.bertrand.apelschoolsupplies.ui.helper.EntryHelper;

public class MyJTableMouseListener implements MouseListener {

	/**
	 * Show entry on double click.
	 */
	@Override
	public void mouseClicked(MouseEvent event) {

		if (ApelManagerJFrame.getInstance().isProcessing()) {
			return;
		}

		int selectedRow = ApelManagerJFrame.getInstance().getEntryTable().getSelectedRow();
		if (selectedRow >= 0) {
			ApelManagerJFrame.getInstance().toggleEditMenu(true);

			if (SwingUtilities.isLeftMouseButton(event) && event.getClickCount() == 2) {
				EntryHelper.editEntry(ApelManagerJFrame.getInstance());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
