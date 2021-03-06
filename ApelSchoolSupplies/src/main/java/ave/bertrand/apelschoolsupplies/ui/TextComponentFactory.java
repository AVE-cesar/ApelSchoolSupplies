package ave.bertrand.apelschoolsupplies.ui;

import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ave.bertrand.apelschoolsupplies.ui.action.TextComponentActionType;
import ave.bertrand.apelschoolsupplies.ui.action.TextComponentPopupListener;

/**
 * Factory for creating text components with context menus.
 *
 * @author Gabor_Bata
 *
 */
public final class TextComponentFactory {

	private TextComponentFactory() {
		// not intended to be instantiated
	}

	/**
	 * Creates a new {@link JTextField} instance with a context popup menu by
	 * default.
	 *
	 * @return the new instance
	 */
	public static JTextField newTextField() {
		return newTextField(null);
	}

	/**
	 * Creates a new {@link JTextField} instance with a context popup menu by
	 * default.
	 *
	 * @param text
	 *            the initial text
	 * @return the new instance
	 */
	public static JTextField newTextField(String text) {
		JTextField textField = text == null ? new JTextField() : new JTextField(text);
		textField.addMouseListener(new TextComponentPopupListener());
		TextComponentActionType.bindAllActions(textField);
		return textField;
	}

	/**
	 * Creates a new {@link JPasswordField} instance with a context popup menu by
	 * default.
	 *
	 * @return the new instance
	 */
	public static JPasswordField newPasswordField() {
		return newPasswordField(false);
	}

	/**
	 * Creates a new {@link JPasswordField} instance with a context popup menu by
	 * default.
	 *
	 * @param copyEnabled
	 *            forces the copy of password field content to clipboard
	 * @return the new instance
	 */
	public static JPasswordField newPasswordField(boolean copyEnabled) {
		JPasswordField passwordField = new CopiablePasswordField(copyEnabled);
		passwordField.addMouseListener(new TextComponentPopupListener());
		TextComponentActionType.bindAllActions(passwordField);
		return passwordField;
	}

	/**
	 * Creates a new {@link JTextArea} instance with a context popup menu by
	 * default.
	 *
	 * @return the new instance
	 */
	public static JTextArea newTextArea() {
		return newTextArea(null);
	}

	/**
	 * Creates a new {@link JTextArea} instance with a context popup menu by
	 * default.
	 *
	 * @param text
	 *            the initial text
	 * @return the new instance
	 */
	public static JTextArea newTextArea(String text) {
		JTextArea textArea = text == null ? new JTextArea() : new JTextArea(text);
		textArea.addMouseListener(new TextComponentPopupListener());
		TextComponentActionType.bindAllActions(textArea);
		return textArea;
	}
}
