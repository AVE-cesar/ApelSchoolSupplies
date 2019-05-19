package ave.bertrand.apelschoolsupplies.sql;

import org.junit.Test;

public class SampleEmbeddedClientTest {

	@Test
	public void test() throws Exception {
		SampleEmbeddedClient client = new SampleEmbeddedClient();
		client.queryAndDump("select * from sample_table");
		client.closeConnection();
	}

}
