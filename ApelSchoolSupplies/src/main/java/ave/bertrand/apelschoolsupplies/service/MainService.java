package ave.bertrand.apelschoolsupplies.service;

public interface MainService {

	public void importEmailsFromSmtpServer();

	public void exportAsXlsxFile();

	public void exportAsRegistrationSheet(String answer);
}
