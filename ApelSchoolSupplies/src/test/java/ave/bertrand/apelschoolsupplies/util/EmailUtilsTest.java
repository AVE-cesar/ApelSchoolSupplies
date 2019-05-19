package ave.bertrand.apelschoolsupplies.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ave.bertrand.apelschoolsupplies.Consts;

public class EmailUtilsTest {

	private static final String BERTRAND_AVE_GMAIL_COM = "bertrand.ave@gmail.com";

	private static final String LYDIA_SBARAGLI_SIMAC_LU = "lydia.sbaragli@simac.lu";

	private EmailUtils client = null;

	@Before
	public void beforeTest() {
		System.getProperty("file.encoding", Consts.UTF_8);
		client = new EmailUtils();
	}

	@After
	public void afterTest() {
		if (client != null) {
			client.closeSmtpConnectionPool();
		}
	}

	@Test
	public void testHmlTemplateWithImage6() throws Exception {

		client.sendEmail(BERTRAND_AVE_GMAIL_COM, "TEST Opération kit Apel - Accusé de réception", "123456789", "6",
				false);
	}

	@Test
	public void testHmlTemplateWithImage5() throws Exception {
		client.sendEmail(BERTRAND_AVE_GMAIL_COM, "TEST Opération kit Apel - Accusé de réception", "123456789", "5",
				false);
	}

	@Test
	public void testHmlTemplateWithImage4() throws Exception {
		client.sendEmail(BERTRAND_AVE_GMAIL_COM, "TEST Opération kit Apel - Accusé de réception", "123456789", "4",
				false);
	}

	@Test
	public void testHmlTemplateWithImage3() throws Exception {
		client.sendEmail(BERTRAND_AVE_GMAIL_COM, "TEST Opération kit Apel - Accusé de réception", "123456789", "3",
				false);
	}

	@Test(expected = Exception.class)
	public void testHmlTemplateWithImageThrowsException() throws Exception {
		client.sendEmail("", "", null, "66", false);
	}
}
