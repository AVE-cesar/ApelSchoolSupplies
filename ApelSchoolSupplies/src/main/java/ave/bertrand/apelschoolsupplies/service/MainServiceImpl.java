package ave.bertrand.apelschoolsupplies.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ave.bertrand.apelschoolsupplies.Consts;
import ave.bertrand.apelschoolsupplies.dao.RecordRepository;
import ave.bertrand.apelschoolsupplies.excel.ExcelExport;
import ave.bertrand.apelschoolsupplies.model.Request;
import ave.bertrand.apelschoolsupplies.sql.SampleEmbeddedClient;
import ave.bertrand.apelschoolsupplies.util.EmailUtils;

@Service
public class MainServiceImpl implements MainService {

	@Autowired
	private RecordRepository recordRepository;

	public void exportAsXlsxFile() {
		ExcelExport.exportToXLSX(recordRepository.findAll());
	}

	@Override
	public void importEmailsFromSmtpServer() {
		EmailUtils client = new EmailUtils();

		try {
			List<Request> requests = client.scan(Consts.SMTP_HOST, Consts.SMTP_LOGIN, Consts.SMTP_PASSWORD);

			List<Request> requestsDB = SampleEmbeddedClient.saveDataToDB(requests, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exportAsRegistrationSheet(String classNumber) {
		ExcelExport.exportAsRegistrationSheet(recordRepository.findByClassNumberOrderByStudentNameAsc(classNumber),
				classNumber);
	}
}
