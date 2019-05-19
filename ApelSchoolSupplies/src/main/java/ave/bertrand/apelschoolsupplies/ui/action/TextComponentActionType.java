package ave.bertrand.apelschoolsupplies.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;
import ave.bertrand.apelschoolsupplies.ui.CopiablePasswordField;
import ave.bertrand.apelschoolsupplies.util.ClipboardUtils;

/**
 * Enumeration which holds text actions and related data.
 *
 * @author Gabor_Bata
 *
 */
public enum TextComponentActionType {
	CUT("jpass.text.cut_action", new TextComponentAction("Cut",
			KeyStroke.getKeyStroke(KeyEvent.VK_X, ApelManagerJFrame.MASK), KeyEvent.VK_T) {
		private static final long serialVersionUID = 6463843410774724700L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getTextComponent(e);
			if (isEnabled(component)) {
				try {
					ClipboardUtils.setClipboardContent(component.getSelectedText());
				} catch (Exception ex) {
					// ignore
				}
				component.replaceSelection("");
			}
		}

		@Override
		public boolean isEnabled(JTextComponent component) {
			boolean copyEnabled = true;
			if (component instanceof CopiablePasswordField) {
				copyEnabled = ((CopiablePasswordField) component).isCopyEnabled();
			}
			return component != null && copyEnabled && component.isEnabled() && component.isEditable()
					&& component.getSelectedText() != null;
		}
	}),

	COPY("jpass.text.copy_action", new TextComponentAction("Copy",
			KeyStroke.getKeyStroke(KeyEvent.VK_C, ApelManagerJFrame.MASK), KeyEvent.VK_C) {
		private static final long serialVersionUID = 8502265220762730908L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getTextComponent(e);
			if (isEnabled(component)) {
				try {
					ClipboardUtils.setClipboardContent(component.getSelectedText());
				} catch (Exception ex) {
					// ignore
				}
			}
		}

		@Override
		public boolean isEnabled(JTextComponent component) {
			boolean copyEnabled = true;
			if (component instanceof CopiablePasswordField) {
				copyEnabled = ((CopiablePasswordField) component).isCopyEnabled();
			}
			return component != null && copyEnabled && component.isEnabled() && component.getSelectedText() != null;
		}
	}),

	PASTE("jpass.text.paste_action", new TextComponentAction("Paste",
			KeyStroke.getKeyStroke(KeyEvent.VK_V, ApelManagerJFrame.MASK), KeyEvent.VK_P) {
		private static final long serialVersionUID = -4089879595174370487L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getTextComponent(e);
			if (isEnabled(component)) {
				component.replaceSelection(ClipboardUtils.getClipboardContent());
			}
		}

		@Override
		public boolean isEnabled(JTextComponent component) {
			return component != null && component.isEnabled() && component.isEditable()
					&& ClipboardUtils.getClipboardContent() != null;
		}
	}),

	DELETE("jpass.text.delete_action",
			new TextComponentAction("Delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), KeyEvent.VK_D) {
				private static final long serialVersionUID = 1227622869347781706L;

				@Override
				public void actionPerformed(ActionEvent e) {
					JTextComponent component = getTextComponent(e);
					if (component != null && component.isEnabled() && component.isEditable()) {
						try {
							Document doc = component.getDocument();
							Caret caret = component.getCaret();
							int dot = caret.getDot();
							int mark = caret.getMark();
							if (dot != mark) {
								doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
							} else if (dot < doc.getLength()) {
								int delChars = 1;
								if (dot < doc.getLength() - 1) {
									String dotChars = doc.getText(dot, 2);
									char c0 = dotChars.charAt(0);
									char c1 = dotChars.charAt(1);
									if (c0 >= '\uD800' && c0 <= '\uDBFF' && c1 >= '\uDC00' && c1 <= '\uDFFF') {
										delChars = 2;
									}
								}
								doc.remove(dot, delChars);
							}
						} catch (Exception bl) {
							// ignore
						}
					}
				}

				@Override
				public boolean isEnabled(JTextComponent component) {
					return component != null && component.isEnabled() && component.isEditable()
							&& component.getSelectedText() != null;
				}
			}),

	CLEAR_ALL("jpass.text.clear_all_action", new TextComponentAction("Clear All", null, KeyEvent.VK_L) {
		private static final long serialVersionUID = 5810788894068735542L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getTextComponent(e);
			if (isEnabled(component)) {
				component.selectAll();
				component.replaceSelection("");
			}
		}

		@Override
		public boolean isEnabled(JTextComponent component) {
			boolean result;
			if (component instanceof CopiablePasswordField) {
				result = component.isEnabled() && component.isEditable()
						&& ((CopiablePasswordField) component).getPassword() != null
						&& ((CopiablePasswordField) component).getPassword().length > 0;
			} else {
				result = component != null && component.isEnabled() && component.isEditable()
						&& component.getText() != null && !component.getText().isEmpty();
			}
			return result;
		}
	}),

	SELECT_ALL("jpass.text.select_all_action", new TextComponentAction("Select All",
			KeyStroke.getKeyStroke(KeyEvent.VK_A, ApelManagerJFrame.MASK), KeyEvent.VK_A) {
		private static final long serialVersionUID = 7236761124177884500L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getTextComponent(e);
			if (isEnabled(component)) {
				component.selectAll();
			}
		}

		@Override
		public boolean isEnabled(JTextComponent component) {
			boolean result;
			if (component instanceof CopiablePasswordField) {
				result = component.isEnabled() && ((CopiablePasswordField) component).getPassword() != null
						&& ((CopiablePasswordField) component).getPassword().length > 0;
			} else {
				result = component != null && component.isEnabled() && component.getText() != null
						&& !component.getText().isEmpty();
			}
			return result;
		}
	});

	private final String name;
	private final TextComponentAction action;

	private TextComponentActionType(String name, TextComponentAction action) {
		this.name = name;
		this.action = action;
	}

	public String getName() {
		return this.name;
	}

	public TextComponentAction getAction() {
		return this.action;
	}

	public KeyStroke getAccelerator() {
		return (KeyStroke) this.action.getValue(Action.ACCELERATOR_KEY);
	}

	public static final void bindAllActions(JTextComponent component) {
		ActionMap actionMap = component.getActionMap();
		InputMap inputMap = component.getInputMap();
		for (TextComponentActionType type : values()) {
			actionMap.put(type.getName(), type.getAction());
			KeyStroke acc = type.getAccelerator();
			if (acc != null) {
				inputMap.put(type.getAccelerator(), type.getName());
			}
		}
	}
}
