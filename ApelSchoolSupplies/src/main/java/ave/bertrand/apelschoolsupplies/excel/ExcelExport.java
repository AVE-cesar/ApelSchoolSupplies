package ave.bertrand.apelschoolsupplies.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ave.bertrand.apelschoolsupplies.Consts;
import ave.bertrand.apelschoolsupplies.model.Record;

public class ExcelExport {

	private static final String XLSX_FILENAME = "Fournitures";

	private static final String REGISTRATION_XLSX_FILENAME = "Emargement";

	private static final String FOURNITURES_SHEET = "Fournitures";

	private static final String REGISTRATION_SHEET = "Emargement";

	private static String[] columns = { "classe", "Elève", "Sexe", "Kit", "Montant", "Réponse à", "Date de la demande",
			"Remarque", "Actif", "Payé", "Répondu" };

	private static String[] columnsRegistrationSheet = { "classe", "Elève", "Garçon", "Fille", "Gaucher", "Droitier",
			"Bloc A", "Bloc B", "Kit Complet", "Payé", "Emargement" };

	public static void exportToXLSX(Iterable<Record> records) {
		String pattern = "yyyy-MM-ddHHmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
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

		for (Record record : records) {

			if (record.isEnabled()) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(record.getClassNumber());
				row.createCell(1).setCellValue(record.getStudentName());
				row.createCell(2).setCellValue(record.getKitType());
				row.createCell(3).setCellValue(record.getAmount());
				row.createCell(4).setCellValue(record.getReplyTos());
				// FIXME convertir date avant affichage
				row.createCell(5).setCellValue(simpleDateFormat.format(record.getReceivedDate()));
				row.createCell(6).setCellValue(record.getNote());
				row.createCell(7).setCellValue(record.isEnabled());
				row.createCell(8).setCellValue(record.isPayed());
				row.createCell(9).setCellValue(record.isSent());
			}
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// add auto filter
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:K" + rowNum));

		FileOutputStream fileOut = null;
		try {

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

	public static void exportAsRegistrationSheet(Iterable<Record> records, String classNumber) {
		String pattern = "yyyy-MM-ddHHmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(REGISTRATION_SHEET);

		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		for (int i = 0; i < columnsRegistrationSheet.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columnsRegistrationSheet[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Other rows and cells with requests data
		int rowNum = 1;

		for (Record record : records) {

			if (record.isEnabled()) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(record.getClassNumber());
				row.createCell(1).setCellValue(record.getStudentName());

				if (Consts.LEFT_HANDED.equalsIgnoreCase(record.getKitType())) {
					row.createCell(2).setCellValue(1);
				} else if (Consts.RIGHT_HANDED.equalsIgnoreCase(record.getKitType())) {
					row.createCell(3).setCellValue(1);
				}
				if (record.getAmount() != null && record.getAmount().contains(Consts.KIT_A)) {
					row.createCell(4).setCellValue(1);
				} else if (record.getAmount() != null && record.getAmount().contains(Consts.KIT_B)) {
					row.createCell(5).setCellValue(1);
				} else if (record.getAmount() != null && record.getAmount().contains(Consts.KIT_FULL)) {
					row.createCell(6).setCellValue(1);
				}
				row.createCell(7).setCellValue(record.isPayed());
			}
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// add auto filter
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:J" + rowNum));

		FileOutputStream fileOut = null;
		try {

			fileOut = new FileOutputStream(REGISTRATION_XLSX_FILENAME + "_" + classNumber + "_"
					+ simpleDateFormat.format(new Date()) + ".xlsx");
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