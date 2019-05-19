package ave.bertrand.apelschoolsupplies.ui;

import javax.swing.JPasswordField;

/**
 * An extension of {@link JPasswordField} which holds an extra attribute which
 * flags if the password field content is allowed to copy to system clipboard.
 *
 * @author Gabor_Bata
 *
 */
public class CopiablePasswordField extends JPasswordField {
	private static final long serialVersionUID = 1205118236056025220L;
	private final boolean copyEnabled;

	public CopiablePasswordField(boolean copyEnabled) {
		super();
		this.copyEnabled = copyEnabled;
	}

	public boolean isCopyEnabled() {
		return this.copyEnabled;
	}
}
