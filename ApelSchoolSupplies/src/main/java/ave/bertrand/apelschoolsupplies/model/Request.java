package ave.bertrand.apelschoolsupplies.model;

public class Request {

	private int mainId;

	private String note;

	private int position;
	private String classNumber;
	private String studentName;
	private String gender;
	private String kitType;
	private String amount;

	private String receivedDate;

	private String replyTos;
	private String messageID;

	private boolean payed = false;

	private boolean sent = false;

	private boolean enabled = false;

	public Request(int mainId, int position, String number, String studentName, String gender, String kitType,
			String amount, String receivedDate, String replyTos, String messageID, String note, boolean enabled) {
		this.setMainId(mainId);

		this.position = position;
		this.classNumber = number;
		this.studentName = studentName;
		this.gender = gender;
		this.kitType = kitType;
		this.amount = amount;

		this.receivedDate = receivedDate;

		this.replyTos = replyTos;
		this.messageID = messageID;

		this.setNote(note);

		this.enabled = enabled;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getReplyTos() {
		return replyTos;
	}

	public void setReplyTos(String replyTos) {
		this.replyTos = replyTos;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getKitType() {
		return kitType;
	}

	public void setKitType(String kitType) {
		this.kitType = kitType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInitialSubject() {
		return classNumber + " - " + studentName + " - " + gender + " - " + kitType + " - " + amount;
	}

	public String toString() {
		return position + ";" + classNumber + ";" + studentName + ";" + gender + ";" + kitType + ";" + amount + ";"
				+ receivedDate + ";" + this.replyTos + ";" + this.messageID;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public int getMainId() {
		return mainId;
	}

	public void setMainId(int mainId) {
		this.mainId = mainId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
