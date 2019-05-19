package ave.bertrand.apelschoolsupplies.data;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ave.bertrand.apelschoolsupplies.model.Request;
import ave.bertrand.apelschoolsupplies.sql.SampleEmbeddedClient;

/**
 * Data model of the application data.
 *
 * @author Gabor_Bata
 *
 */
public class DataModel extends AbstractTableModel {
	private static volatile DataModel INSTANCE;

	public static int STUDENT_COLUMN = 0;
	public static int GENDER_COLUMN = 1;
	public static int KITTYPE_COLUMN = 2;
	public static int AMOUNT_COLUMN = 3;
	public static int CLASSNUMBER_COLUMN = 4;
	public static int RECEIVEDDATE_COLUMN = 5;
	public static int REPLYTO_COLUMN = 6;
	public static int MESSAGEID_COLUMN = 7;
	public static int VALIDATED_COLUMN = 8;
	public static int REPLIED_COLUMN = 9;
	public static int NOTE_COLUMN = 10;
	public static int ENABLED_COLUMN = 11;
	public static int MAINID_COLUMN = 12;

	private String[] columnNames = new String[] { "Nom", "Sexe", "Kit", "Montant", "Classe", "Date de réception",
			"Répondre à", "Clé unique", "Validé", "Répondu", "Note", "Actif", "Id" };

	private List<Request> requests = new ArrayList();

	private boolean modified = false;

	private DataModel() {
		// not intended to be instantiated
	}

	/**
	 * Gets the DataModel singleton instance.
	 *
	 * @return instance of the DataModel
	 */
	public static DataModel getInstance() {
		if (INSTANCE == null) {
			synchronized (DataModel.class) {
				if (INSTANCE == null) {
					INSTANCE = new DataModel();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * Gets list of entries.
	 *
	 * @return list of entries
	 */
	public final List<Request> getEntries() {
		return this.requests;
	}

	/**
	 * Sets list of entries.
	 *
	 * @param entries
	 *            entries
	 */
	public final void setEntries(final List<Request> requests) {
		System.out.println("On fixe les entrées du model");
		this.requests = requests;
		this.fireTableDataChanged();
	}

	/**
	 * Gets the modified state of the data model.
	 *
	 * @return modified state of the data model
	 */
	public final boolean isModified() {
		return this.modified;
	}

	/**
	 * Sets the modified state of the data model.
	 *
	 * @param modified
	 *            modified state
	 */
	public final void setModified(final boolean modified) {
		this.modified = modified;
	}

	/**
	 * Clears all fields of the data model.
	 */
	public final void clear() {
		this.requests.clear();

		this.modified = false;
	}

	/**
	 * Gets the list of entry titles.
	 *
	 * @return list of entry titles
	 */
	public List<String> getListOfTitles() {
		List<String> list = new ArrayList<String>(this.requests.size());
		for (Request entry : this.requests) {
			list.add(entry.getStudentName());
		}
		return list;
	}

	/**
	 * Gets entry index by title.
	 *
	 * @param title
	 *            entry title
	 * @return entry index
	 */
	public int getEntryIndexByTitle(String title) {
		System.out.println("on recherche: " + title);
		return getListOfTitles().indexOf(title);
	}

	/**
	 * Gets entry by title.
	 *
	 * @param title
	 *            entry title
	 * @return entry
	 */
	public Request getEntryByTitle(String title) {
		int index = getEntryIndexByTitle(title);
		if (index >= 0) {
			return this.requests.get(index);
		} else
			return null;
	}

	public Request findEntry(String title) {
		Request foundedEntry = null;
		for (Request request : this.requests) {
			if (request.getStudentName().contains(title)) {
				foundedEntry = request;
				break;
			}
		}
		return foundedEntry;
	}

	public int findEntryIndex(String title) {
		int index = 0;
		boolean founded = false;
		for (Request entry : this.requests) {
			index++;
			if (entry.getStudentName().equalsIgnoreCase(title)) {
				founded = true;
				break;
			}
		}
		if (founded)
			return index;
		else
			return -1;
	}

	@Override
	public int getRowCount() {
		return this.requests.size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex != MAINID_COLUMN && columnIndex != MESSAGEID_COLUMN && columnIndex != VALIDATED_COLUMN
				&& columnIndex != REPLIED_COLUMN) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// super.setValueAt(aValue, rowIndex, columnIndex);

		Request request = this.requests.get(rowIndex);
		if (columnIndex == NOTE_COLUMN) {
			if (aValue != null) {
				request.setNote(aValue.toString());
				System.out.println("fixe à : " + aValue);

				// FIXME sauvegarder en DB
				// update DB
				SampleEmbeddedClient db = null;
				try {
					db = new SampleEmbeddedClient();
					db.update("UPDATE sample_table SET note = '" + aValue.toString() + "' WHERE Id = '"
							+ request.getMainId() + "';");

					List<Request> requests = db.reloadDataFromDB();
					this.setEntries(requests);

				} catch (Exception ex1) {
					ex1.printStackTrace();
				} finally {
					db.closeConnection();
				}
			}
		} else if (columnIndex == STUDENT_COLUMN) {
			if (aValue != null) {
				request.setStudentName(aValue.toString());

				SampleEmbeddedClient db = null;
				try {
					db = new SampleEmbeddedClient();
					db.update("UPDATE sample_table SET studentName = '" + aValue.toString() + "' WHERE Id = '"
							+ request.getMainId() + "';");

					List<Request> requests = db.reloadDataFromDB();
					this.setEntries(requests);

				} catch (Exception ex1) {
					ex1.printStackTrace();
				} finally {
					db.closeConnection();
				}
			}
		} else if (columnIndex == ENABLED_COLUMN) {

			if (aValue != null) {
				boolean val = Boolean.parseBoolean(aValue.toString());
				SampleEmbeddedClient db = null;
				try {
					db = new SampleEmbeddedClient();
					db.update(
							"UPDATE sample_table SET enabled = " + val + " WHERE Id = '" + request.getMainId() + "';");

					List<Request> requests = db.reloadDataFromDB();
					this.setEntries(requests);

				} catch (Exception ex1) {
					ex1.printStackTrace();
				} finally {
					db.closeConnection();
				}
			}
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		/*
		 * if (columnIndex ==0) { System.out.println("appel de getValueAt:" + rowIndex +
		 * ", " + columnIndex);
		 * 
		 * System.out.println("Le model contient: " + this.requests.size()); }
		 */
		String value = null;

		if (rowIndex < 0 && rowIndex > getRowCount() - 1)
			return null;
		if (columnIndex < 0 && columnIndex > getColumnCount() - 1)
			return null;

		if (columnIndex == STUDENT_COLUMN) {
			value = this.requests.get(rowIndex).getStudentName();
		} else if (columnIndex == GENDER_COLUMN) {
			value = this.requests.get(rowIndex).getGender();
		} else if (columnIndex == KITTYPE_COLUMN) {
			value = this.requests.get(rowIndex).getKitType();
		} else if (columnIndex == AMOUNT_COLUMN) {
			value = this.requests.get(rowIndex).getAmount();
		} else if (columnIndex == CLASSNUMBER_COLUMN) {
			value = this.requests.get(rowIndex).getClassNumber();
		} else if (columnIndex == RECEIVEDDATE_COLUMN) {
			value = this.requests.get(rowIndex).getReceivedDate();
		} else if (columnIndex == REPLYTO_COLUMN) {
			value = this.requests.get(rowIndex).getReplyTos();
		} else if (columnIndex == MESSAGEID_COLUMN) {
			value = this.requests.get(rowIndex).getMessageID();
		} else if (columnIndex == VALIDATED_COLUMN) {
			value = Boolean.toString(this.requests.get(rowIndex).isPayed());
		} else if (columnIndex == REPLIED_COLUMN) {
			value = Boolean.toString(this.requests.get(rowIndex).isSent());
		} else if (columnIndex == NOTE_COLUMN) {
			value = this.requests.get(rowIndex).getNote();
		} else if (columnIndex == ENABLED_COLUMN) {
			value = Boolean.toString(this.requests.get(rowIndex).isEnabled());
		} else if (columnIndex == MAINID_COLUMN) {
			value = Integer.toString(this.requests.get(rowIndex).getMainId());
		} else {
		}
		// System.out.println("Value:" + value);
		return value;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.columnNames[columnIndex];
	}
}
