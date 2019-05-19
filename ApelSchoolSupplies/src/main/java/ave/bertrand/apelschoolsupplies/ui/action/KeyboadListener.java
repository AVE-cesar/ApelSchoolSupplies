package ave.bertrand.apelschoolsupplies.ui.action;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;
import ave.bertrand.apelschoolsupplies.ui.helper.EntryHelper;

public class KeyboadListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (ApelManagerJFrame.getInstance().isProcessing()) {
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			EntryHelper.editEntry(ApelManagerJFrame.getInstance());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
