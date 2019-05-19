package ave.bertrand.apelschoolsupplies.ui.action;

import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

public abstract class TextComponentAction extends TextAction {
	private static final long serialVersionUID = -2061559337477351379L;

	public TextComponentAction(String text, KeyStroke accelerator, int mnemonic) {
		super(text);
		if (accelerator != null) {
			putValue(ACCELERATOR_KEY, accelerator);
		}
		putValue(MNEMONIC_KEY, Integer.valueOf(mnemonic));
	}

	public abstract boolean isEnabled(JTextComponent component);
}
