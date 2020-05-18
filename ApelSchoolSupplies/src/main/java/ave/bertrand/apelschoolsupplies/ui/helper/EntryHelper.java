package ave.bertrand.apelschoolsupplies.ui.helper;

import java.util.List;

import javax.swing.table.TableModel;

import ave.bertrand.apelschoolsupplies.data.DataModel;
import ave.bertrand.apelschoolsupplies.model.Request;
import ave.bertrand.apelschoolsupplies.sql.SampleEmbeddedClient;
import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;
import ave.bertrand.apelschoolsupplies.ui.MessageDialog;
import ave.bertrand.apelschoolsupplies.ui.action.Worker;
import ave.bertrand.apelschoolsupplies.util.ClipboardUtils;
import ave.bertrand.apelschoolsupplies.util.EmailUtils;

/**
 * Helper class for entry operations.
 *
 * @author Gabor_Bata
 *
 */
public final class EntryHelper {

	private EntryHelper() {
		// not intended to be instantiated
	}

	public static void validateRequest(final ApelManagerJFrame parent) {
		int selectedRow = ApelManagerJFrame.getInstance().getEntryTable().getSelectedRow();
		int convertedRow = ApelManagerJFrame.getInstance().getEntryTable().convertRowIndexToModel(selectedRow);

		final String messageId = getMessageId(convertedRow);

		showWarning(parent, convertedRow);

		Worker worker = new Worker(parent) {
			@Override
			protected Void doInBackground() throws Exception {
				updatePayedStatut(parent, messageId);
				return null;
			}

			@Override
			protected void done() {
				stopProcessing();
				try {
					get();
				} catch (Exception e) {
					showErrorMessage(e);
				}
			}
		};
		worker.execute();

	}

	private static void updatePayedStatut(ApelManagerJFrame parent, String messageId) {
		// update DB
		SampleEmbeddedClient db = null;
		try {
			db = new SampleEmbeddedClient();
			db.update("UPDATE sample_table SET payed = TRUE WHERE messageId = '" + messageId + "';");

			List<Request> requests = db.reloadDataFromDB();
			parent.getModel().setEntries(requests);

			parent.refreshFrameTitle();
			parent.refreshEntryTitleList(null);

		} catch (Exception ex1) {
			ex1.printStackTrace(); // could not start db
		} finally {
			db.closeConnection();
		}
	}

	public static void replyToRequest(final ApelManagerJFrame parent) throws Exception {
		int selectedRow = ApelManagerJFrame.getInstance().getEntryTable().getSelectedRow();
		final int convertedRow = ApelManagerJFrame.getInstance().getEntryTable().convertRowIndexToModel(selectedRow);

		showWarning(parent, convertedRow);

		Worker worker = new Worker(parent) {
			@Override
			protected Void doInBackground() throws Exception {
				updateSentStatus(parent, convertedRow);
				return null;
			}

			@Override
			protected void done() {
				stopProcessing();
				try {
					get();
				} catch (Exception e) {
					showErrorMessage(e);
				}
			}
		};
		worker.execute();

	}

	private static void updateSentStatus(ApelManagerJFrame parent, int convertedRow) throws Exception {
		String messageId = getMessageId(convertedRow);
		String classNumber = getClassNumber(convertedRow);
		Request request = getRequest(convertedRow);
		String replyTo = request.getReplyTos();

		// FIXME envoie du mail vers réel replyTo
		EmailUtils client = new EmailUtils();
		client.sendEmail(request.getReplyTos(), "Opération kit Apel - Accusé de réception", request.getInitialSubject(),
				classNumber.trim(), true);
		client.closeSmtpConnectionPool();
		// move email de inbox vers dans le bon répertoire

		// update DB
		SampleEmbeddedClient db = null;
		try {
			db = new SampleEmbeddedClient();
			db.update("UPDATE sample_table SET sent = TRUE WHERE messageId = '" + messageId + "';");

			List<Request> requests = db.reloadDataFromDB();
			parent.getModel().setEntries(requests);

			parent.refreshFrameTitle();
			parent.refreshEntryTitleList(null);

		} catch (Exception ex1) {
			ex1.printStackTrace(); // could not start db
		} finally {
			db.closeConnection();
		}
	}

	/**
	 * Edits the entry.
	 *
	 * @param parent parent component
	 */
	public static void editEntry(ApelManagerJFrame parent) {
		int selectedRow = ApelManagerJFrame.getInstance().getEntryTable().getSelectedRow();
		int convertedRow = ApelManagerJFrame.getInstance().getEntryTable().convertRowIndexToModel(selectedRow);

		String title = getStudentName(convertedRow);

		showWarning(parent, convertedRow);

		/*
		 * Entry oldEntry = parent.getModel().getEntryByTitle(title); EntryDialog ed =
		 * new EntryDialog(parent, "Edit Entry", oldEntry, false); if (ed.getFormData()
		 * != null) { parent.getModel().getEntries().getEntry().remove(oldEntry);
		 * parent.getModel().getEntries().getEntry().add(ed.getFormData());
		 * parent.getModel().setModified(true); parent.refreshFrameTitle();
		 * parent.refreshEntryTitleList(ed.getFormData().getTitle()); }
		 */
	}

	/**
	 * Gets the selected entry.
	 *
	 * @param parent the parent frame
	 * @return the entry or null
	 */
	public static Request getSelectedEntry(ApelManagerJFrame parent) {
		int selectedRow = ApelManagerJFrame.getInstance().getEntryTable().getSelectedRow();
		int convertedRow = ApelManagerJFrame.getInstance().getEntryTable().convertRowIndexToModel(selectedRow);

		showWarning(parent, selectedRow);
		String title = getStudentName(convertedRow);
		return parent.getModel().getEntryByTitle(title);
	}

	/**
	 * Copy entry field value to clipboard.
	 *
	 * @param parent  the parent frame
	 * @param content the content to copy
	 */
	public static void copyEntryField(ApelManagerJFrame parent, String content) {
		try {
			ClipboardUtils.setClipboardContent(content);
		} catch (Exception e) {
			MessageDialog.showErrorMessage(parent, e.getMessage());
		}
	}

	private static void showWarning(ApelManagerJFrame parent, int selectedRow) {
		if (selectedRow < 0) {
			MessageDialog.showWarningMessage(parent, "Please select an entry.");
			return;
		}
	}

	private static String getStudentName(int selectedRow) {
		TableModel model = ApelManagerJFrame.getInstance().getEntryTable().getModel();

		return model.getValueAt(selectedRow, DataModel.STUDENT_COLUMN).toString();
	}

	private static String getMessageId(int selectedRow) {
		TableModel model = ApelManagerJFrame.getInstance().getEntryTable().getModel();

		return model.getValueAt(selectedRow, DataModel.MESSAGEID_COLUMN).toString();
	}

	private static String getClassNumber(int selectedRow) {
		TableModel model = ApelManagerJFrame.getInstance().getEntryTable().getModel();

		return model.getValueAt(selectedRow, DataModel.CLASSNUMBER_COLUMN).toString();
	}

	private static Request getRequest(int selectedRow) {
		TableModel model = ApelManagerJFrame.getInstance().getEntryTable().getModel();

		int mainId = Integer.parseInt(model.getValueAt(selectedRow, DataModel.MAINID_COLUMN).toString());
		String classNumber = model.getValueAt(selectedRow, DataModel.CLASSNUMBER_COLUMN).toString();
		String studentName = model.getValueAt(selectedRow, DataModel.STUDENT_COLUMN).toString();
		String kitType = model.getValueAt(selectedRow, DataModel.KITTYPE_COLUMN).toString();
		String amount = model.getValueAt(selectedRow, DataModel.AMOUNT_COLUMN).toString();
		String replyTos = model.getValueAt(selectedRow, DataModel.REPLYTO_COLUMN).toString();
		String note = model.getValueAt(selectedRow, DataModel.NOTE_COLUMN) == null ? ""
				: model.getValueAt(selectedRow, DataModel.NOTE_COLUMN).toString();
		boolean enabled = Boolean.parseBoolean(model.getValueAt(selectedRow, DataModel.ENABLED_COLUMN).toString());

		Request request = new Request(mainId, -1, classNumber, studentName, kitType, amount, null, replyTos, null, note,
				enabled);
		return request;
	}
}
