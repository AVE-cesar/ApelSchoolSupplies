package ave.bertrand.apelschoolsupplies.ui.helper;

import java.util.List;

import ave.bertrand.apelschoolsupplies.model.Request;
import ave.bertrand.apelschoolsupplies.ui.ApelManagerJFrame;
import ave.bertrand.apelschoolsupplies.ui.action.Worker;

/**
 * Helper utils for file operations.
 *
 * @author Gabor_Bata
 *
 */
public final class FileHelper {

	private FileHelper() {
		// not intended to be instantiated
	}

	/**
	 * Imports the given file.
	 *
	 * @param fileName
	 *            file name
	 * @param parent
	 *            parent component
	 */
	static void doImportFile(final String fileName, final ApelManagerJFrame parent) {
		Worker worker = new Worker(parent) {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					// parent.getModel().setEntries(DocumentHelper.newInstance(fileName).readDocument());
					parent.getModel().setModified(true);

				} catch (Throwable e) {
					throw new Exception("An error occured during the import operation:\n" + e.getMessage());
				}
				return null;
			}
		};
		worker.execute();
	}

	/**
	 * Loads a file and fills the data model.
	 *
	 * @param fileName
	 *            file name
	 * @param parent
	 *            parent component
	 */
	public static void doOpenFile(final List<Request> requests, final ApelManagerJFrame parent) {
		parent.getModel().clear();
		if (requests == null) {
			return;
		}

		Worker worker = new Worker(parent) {
			@Override
			protected Void doInBackground() throws Exception {
				try {
					parent.getModel().setEntries(requests);

				} catch (Throwable e) {
					throw new Exception("An error occured during the open operation:\n" + e.getMessage());
				}
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

	/**
	 * Checks if the file name has the given extension
	 *
	 * @param fileName
	 *            file name
	 * @param extension
	 *            extension
	 * @return file name ending with the given extenson
	 */
	private static String checkExtension(final String fileName, final String extension) {
		String separator = fileName.endsWith(".") ? "" : ".";
		if (!fileName.toLowerCase().endsWith(separator + extension)) {
			return fileName + separator + extension;
		}
		return fileName;
	}
}
