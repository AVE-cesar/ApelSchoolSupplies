package ave.bertrand.apelschoolsupplies.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import ave.bertrand.apelschoolsupplies.Consts;
import ave.bertrand.apelschoolsupplies.excel.ExcelExport;
import ave.bertrand.apelschoolsupplies.model.Request;
import ave.bertrand.apelschoolsupplies.sql.SampleEmbeddedClient;
import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;
import ave.bertrand.apelschoolsupplies.ui.MessageDialog;
import ave.bertrand.apelschoolsupplies.ui.helper.EntryHelper;
import ave.bertrand.apelschoolsupplies.ui.helper.FileHelper;
import ave.bertrand.apelschoolsupplies.util.EmailUtils;

/**
 * Enumeration which holds menu actions and related data.
 *
 * @author Gabor_Bata
 *
 */
public enum MenuActionType {

	IMPORT_EMAILS("jpass.menu.open_file_action", new AbstractMenuAction("Importer les emails",
			MessageDialog.getIcon("import"), KeyStroke.getKeyStroke(KeyEvent.VK_O, ApelManagerJFrame.MASK), true) {
		private static final long serialVersionUID = -441032579227887886L;

		@Override
		public void actionPerformed(ActionEvent ev) {
			EmailUtils client = new EmailUtils();

			try {
				ApelManagerJFrame.emailsRequests = client.scan(Consts.SMTP_HOST, Consts.SMTP_LOGIN,
						Consts.SMTP_PASSWORD);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}),

	LOAD_DB("jpass.menu.save_file_action", new AbstractMenuAction("Charger la base de données",
			MessageDialog.getIcon("save"), KeyStroke.getKeyStroke(KeyEvent.VK_S, ApelManagerJFrame.MASK), true) {
		private static final long serialVersionUID = 8657273941022043906L;

		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				List<Request> requestsDB = SampleEmbeddedClient.saveDataToDB(ApelManagerJFrame.emailsRequests, false);

				FileHelper.doOpenFile(requestsDB, ApelManagerJFrame.getInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}),

	EXPORT_XLSX("jpass.menu.save_as_file_action",
			new AbstractMenuAction("Exporter sous Excel", MessageDialog.getIcon("export"), null, true) {
				private static final long serialVersionUID = 1768189708479045321L;

				@Override
				public void actionPerformed(ActionEvent ev) {
					SampleEmbeddedClient db = null;
					try {
						db = new SampleEmbeddedClient();
						ExcelExport.exportToXLSX(db.reloadDataFromDB());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						db.closeConnection();
					}
				}
			}),

	EXIT("jpass.menu.exit_action", new AbstractMenuAction("Exit", MessageDialog.getIcon("exit"),
			KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK), true) {
		private static final long serialVersionUID = -2741659403416846295L;

		@Override
		public void actionPerformed(ActionEvent ev) {
			ApelManagerJFrame.getInstance().exitFrame();
		}
	}),

	VALIDATE_ENTRY("jpass.menu.add_entry_action", new AbstractMenuAction("Pointer cette ligne...",
			MessageDialog.getIcon("entry_new"), KeyStroke.getKeyStroke(KeyEvent.VK_Y, ApelManagerJFrame.MASK), true) {
		private static final long serialVersionUID = 6793989246928698613L;

		@Override
		public void actionPerformed(ActionEvent ev) {

			EntryHelper.validateRequest(ApelManagerJFrame.getInstance());
		}
	}),

	REPLYTO_ENTRY("jpass.menu.edit_entry_action", new AbstractMenuAction("Répondre à l'expéditeur...",
			MessageDialog.getIcon("entry_edit"), KeyStroke.getKeyStroke(KeyEvent.VK_E, ApelManagerJFrame.MASK), false) {
		private static final long serialVersionUID = -3234220812811327191L;

		@Override
		public void actionPerformed(ActionEvent ev) {
			try {
				EntryHelper.replyToRequest(ApelManagerJFrame.getInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	private final String name;
	private final AbstractMenuAction action;

	private MenuActionType(String name, AbstractMenuAction action) {
		this.name = name;
		this.action = action;
	}

	public String getName() {
		return this.name;
	}

	public AbstractMenuAction getAction() {
		return this.action;
	}

	public KeyStroke getAccelerator() {
		return (KeyStroke) this.action.getValue(Action.ACCELERATOR_KEY);
	}

	public static final void bindAllActions(JComponent component) {
		ActionMap actionMap = component.getActionMap();
		InputMap inputMap = component.getInputMap();
		for (MenuActionType type : values()) {
			actionMap.put(type.getName(), type.getAction());
			KeyStroke acc = type.getAccelerator();
			if (acc != null) {
				inputMap.put(type.getAccelerator(), type.getName());
			}
		}
	}
}
