package ave.bertrand.apelschoolsupplies.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.nlab.smtp.pool.SmtpConnectionPool;
import org.nlab.smtp.transport.connection.ClosableSmtpConnection;
import org.nlab.smtp.transport.factory.SmtpConnectionFactoryBuilder;

import com.sun.mail.imap.IMAPMessage;

import ave.bertrand.apelschoolsupplies.Consts;
import ave.bertrand.apelschoolsupplies.model.Request;
import ave.bertrand.apelschoolsupplies.ui.MessageDialog;

public class EmailUtils {

	private SmtpConnectionPool smtpConnectionPool = null;

	public EmailUtils() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

		poolConfig.setMaxTotal(Consts.SMTP_POOL_MAX);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);

		Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.timeout", Consts.SMTP_TIMEOUT);
		properties.put("mail.smtp.connectiontimeout", Consts.SMTP_CONNECTION_TIMEOUT);

		Authenticator authenticator = new SMTPAuthenticator();

		// Declare the factory and the connection pool
		smtpConnectionPool = new SmtpConnectionPool(
				SmtpConnectionFactoryBuilder.newSmtpBuilder().session(properties, authenticator).protocol("smtp")
						.host(Consts.SMTP_HOST).port(Integer.parseInt(Consts.SMTP_PORT)).username(Consts.SMTP_LOGIN)
						.password(Consts.SMTP_PASSWORD).build(),
				poolConfig);
	}

	public void closeSmtpConnectionPool() {
		// Close the pool, usually when the application shutdown
		smtpConnectionPool.close();
	}

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @throws Exception
	 */
	public void sendEmail(String toEmail, String subject, String reference, String template, boolean updateSentFolder)
			throws Exception {
		if (!"3".equals(template) && !"4".equals(template) && !"5".equals(template) & !"6".equals(template)) {
			throw new Exception("Le template email pour " + template + " n'existe pas !");
		}

		long t1 = System.currentTimeMillis();

		try (ClosableSmtpConnection transport = smtpConnectionPool.borrowObject()) {

			MimeMessage msg = new MimeMessage(transport.getSession());
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress(Consts.SMTP_LOGIN, "Apel Saint Pierre Chanel"));

			msg.setSubject(subject, Consts.UTF_8);

			// msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");

			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			String htmlTemplate = MessageDialog.getResourceAsString("templateEmail" + template + ".html");
			if (reference != null) {
				htmlTemplate = htmlTemplate.replace("__ref__", reference);
			}
			messageBodyPart.setContent(htmlTemplate, "text/html; charset=utf-8");
			// add it
			multipart.addBodyPart(messageBodyPart);

			// third part (the pdf)
			messageBodyPart = new MimeBodyPart();
			String filename = "ListeDeRepartitionMateriel" + template + "eme.pdf";
			byte[] filenameUTF8 = filename.getBytes(Consts.UTF_8);
			DataSource source = new FileDataSource(
					Consts.class.getClassLoader().getResource(new String(filenameUTF8)).getFile());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// put everything together
			msg.setContent(multipart);

			System.out.println("Message is ready to be sent");
			long t2 = System.currentTimeMillis();
			// session.setDebug(true);
			transport.sendMessage(msg);
			long t3 = System.currentTimeMillis();

			if (updateSentFolder) {
				updateSentFolder(Consts.SMTP_HOST, Consts.SMTP_LOGIN, Consts.SMTP_PASSWORD, msg);
				System.out.println("Email copied in Sent folder");
			}
			System.out.println("Email Sent Successfully to: " + toEmail);
			long t4 = System.currentTimeMillis();

			System.out.println("Ready in: " + (t2 - t1));
			System.out.println("Sent in: " + (t3 - t1));
			System.out.println("Copied in: " + (t4 - t1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public static void updateSentFolder(String host, String username, String password, MimeMessage msg)
			throws Exception {
		MimeMessage msgs[] = new MimeMessage[1];
		msgs[0] = msg;

		List<Request> requests = new ArrayList();
		Properties props = new Properties();

		String provider = "imap";
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(props, auth);
		Store store = session.getStore(provider);
		store.connect(host, username, password);

		javax.mail.Folder[] folders = store.getDefaultFolder().list("*");

		Folder sentFolder = store.getFolder("Sent");
		if (sentFolder.exists()) {
			System.out.println(sentFolder.toString());
			sentFolder.appendMessages(msgs);
		}
		store.close();
	}

	public List<Request> scan(String host, String username, String password) throws Exception {

		List<Request> requests = new ArrayList();
		Properties props = new Properties();

		String provider = "imap";

		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore(provider);
		store.connect(host, username, password);

		Folder inbox = store.getFolder("INBOX");
		if (inbox == null) {
			System.out.println("No INBOX");
			System.exit(1);
		}
		inbox.open(Folder.READ_ONLY);

		Message[] messages = inbox.getMessages();
		System.out.println("il y a " + messages.length + " message(s).");
		for (int i = 0; i < messages.length; i++) {
			Message message = messages[i];
			String subject = message.getSubject();
			Date receivedDate = message.getReceivedDate();
			Address replyTos[] = message.getReplyTo();
			if (replyTos.length != 1) {
				throw new Exception(subject + " doit avoir seulement un replyTo email !");
			}
			String messageID = ((IMAPMessage) message).getMessageID();

			System.out.println("Message " + (i + 1) + " avant parsing: " + subject);

			// on a trouvé dans un sujet, ceci: 3-POIRIER--DUCASTELLE Laure-Fille-Kit
			// en 2020, il n'y a plus fille et garçon
			int nbTirets = 3;
			// Droitier- 50 € Bloc A
			// le double - pose problème
			subject = subject.replace("--", " ");
			int nb = StringUtils.countMatches(subject, "-");

			if (nb >= nbTirets && !subject.toUpperCase().startsWith("RE:")) {
				String subjectParts[] = subject.split("-");

				if (subjectParts.length != (nbTirets + 1)) {

					// il y a un tiret dans le nom
					// on le remplace par un blanc entre la classe et le sexe
					int index = subject.indexOf('-', 2);
					// System.out.println(index);
					subject = subject.substring(0, index) + " " + subject.substring(index + 1);

					subjectParts = subject.split("-");
					if (subjectParts.length != (nbTirets + 1)) {
						throw new Exception(subject + " doit avoir seulement " + nbTirets + " tirets !");
					}
				}

				String classNumber = subjectParts[0].trim();
				int number = -1;
				if (classNumber.startsWith("SPAM> ")) {
					classNumber = classNumber.substring("SPAM> ".length());
					number = Integer.parseInt(classNumber);
				} else {
					number = Integer.parseInt(classNumber);
				}
				String studentName = subjectParts[1].trim();
				String kitType = subjectParts[2].trim();
				String amount = subjectParts[3].trim();

				String pattern = "yyyy-MM-dd HH:mm:ss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				Request request = new Request(-1, (i + 1), "" + number, studentName, kitType, amount,
						simpleDateFormat.format(receivedDate), replyTos[0].toString(), messageID, "", true);

				System.out.println(request.toString());

				requests.add(request);
			} else {
				// message ecarté car commence par RE: ou on ne trouve pas au moins les 4
				// parties
				System.err.println("ATTENTION, Message écarté: " + subject + " " + receivedDate + " nb parties: " + nb);
			}
		}
		inbox.close(false);
		store.close();

		System.out.println("Fin de l'import des emails");
		return requests;
	}
}

class SMTPAuthenticator extends javax.mail.Authenticator {
	public PasswordAuthentication getPasswordAuthentication() {
		String username = Consts.SMTP_LOGIN;
		String password = Consts.SMTP_PASSWORD;
		return new PasswordAuthentication(username, password);
	}
}
