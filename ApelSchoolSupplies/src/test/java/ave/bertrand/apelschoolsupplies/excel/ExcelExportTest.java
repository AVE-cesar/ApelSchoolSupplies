package ave.bertrand.apelschoolsupplies.excel;

import org.junit.Test;

import ave.bertrand.apelschoolsupplies.sql.SampleEmbeddedClient;

public class ExcelExportTest {

	@Test
	public void exportToXLSX() throws Exception {
		SampleEmbeddedClient db = new SampleEmbeddedClient();
		ExcelExport.exportToXLSX(db.reloadDataFromDB());
		db.closeConnection();
	}
}
