package ave.bertrand.apelschoolsupplies.ui.action;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;

/**
 * Listener for widow close.
 *
 * @author Gabor_Bata
 *
 */
public class CloseListener extends WindowAdapter {

	/**
	 * Calls the {@code exitFrame} method of main frame.
	 *
	 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		if (event.getSource() instanceof ApelManagerJFrame) {
			((ApelManagerJFrame) event.getSource()).exitFrame();
		}
	}
}
