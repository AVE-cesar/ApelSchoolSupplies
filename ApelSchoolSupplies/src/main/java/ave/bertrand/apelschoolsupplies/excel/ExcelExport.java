package ave.bertrand.apelschoolsupplies.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ave.bertrand.apelschoolsupplies.model.Request;

public class ExcelExport {

	private static final String XLSX_FILENAME = "Fournitures";

	private static final String FOURNITURES_SHEET = "Fournitures";

	private static String[] columns = { "classe", "Elève", "Sexe", "Kit", "Montant", "Réponse à", "Date de la demande",
			"Remarque", "Actif", "Payé", "Répondu" };

	public static void exportToXLSX(List<Request> requests) {
		if (requests.size() == 0) {
			return;
		}

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(FOURNITURES_SHEET);

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Other rows and cells with requests data
		int rowNum = 1;

		for (Request request : requests) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(request.getClassNumber());
			row.createCell(1).setCellValue(request.getStudentName());
			row.createCell(2).setCellValue(request.getGender());
			row.createCell(3).setCellValue(request.getKitType());
			row.createCell(4).setCellValue(request.getAmount());
			row.createCell(5).setCellValue(request.getReplyTos());
			row.createCell(6).setCellValue(request.getReceivedDate());
			row.createCell(7).setCellValue(request.getNote());
			row.createCell(8).setCellValue(request.isEnabled());
			row.createCell(9).setCellValue(request.isPayed());
			row.createCell(10).setCellValue(request.isSent());
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// add auto filter
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:K" + requests.size()));

		FileOutputStream fileOut = null;
		try {
			String pattern = "yyyy-MM-ddHHmmss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			fileOut = new FileOutputStream(XLSX_FILENAME + "_" + simpleDateFormat.format(new Date()) + ".xlsx");
			workbook.write(fileOut);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fileOut != null)
					fileOut.close();
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}