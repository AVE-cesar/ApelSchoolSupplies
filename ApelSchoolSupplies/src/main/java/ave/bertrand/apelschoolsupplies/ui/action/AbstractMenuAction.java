package ave.bertrand.apelschoolsupplies.ui.action;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * Class for handling menu actions.
 *
 * @author Gabor_Bata
 *
 */
public abstract class AbstractMenuAction extends AbstractAction {
	private static final long serialVersionUID = 5470805628583386182L;

	/**
	 * Creates a new menu action.
	 *
	 * @param text
	 *            title of the action that appears on UI
	 * @param icon
	 *            icon of action
	 * @param accelerator
	 *            accelerator key
	 */
	public AbstractMenuAction(String text, Icon icon, KeyStroke accelerator, boolean enabled) {
		super(text, icon);
		putValue(SHORT_DESCRIPTION, text);
		if (accelerator != null) {
			putValue(ACCELERATOR_KEY, accelerator);
		}
		setEnabled(enabled);
	}
}
