package ave.bertrand.apelschoolsupplies.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.lang3.StringEscapeUtils;

public class FileUtils {

	public static String convertString(String str) {
		return StringEscapeUtils.unescapeJava(str);
	}

	public static void readConvertWriteFile(String filename1, String filename2) {
		BufferedReader reader;

		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename2), "UTF-8"));
			reader = new BufferedReader(new FileReader(filename1));
			String line = reader.readLine();
			while (line != null) {

				out.write(convertString(line) + ";");
				out.write(System.getProperty("line.separator"));
				// read next line
				line = reader.readLine();
			}
			out.flush();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
