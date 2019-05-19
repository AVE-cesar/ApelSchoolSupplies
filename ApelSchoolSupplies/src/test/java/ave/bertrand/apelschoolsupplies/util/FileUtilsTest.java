package ave.bertrand.apelschoolsupplies.util;

import org.junit.Test;

public class FileUtilsTest {

	@Test
	public void test() {
		FileUtils.readConvertWriteFile("db_file.script", "db_file.script.mysql");
	}
}
