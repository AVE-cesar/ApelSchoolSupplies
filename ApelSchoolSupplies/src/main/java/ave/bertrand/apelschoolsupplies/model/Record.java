package ave.bertrand.apelschoolsupplies.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sample_table")
public class Record {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "messageid")
	private String messageId;

	@Column(name = "classnumber")
	private String classNumber;

	@Column(name = "studentname")
	private String studentName;

	@Column(name = "kittype")
	private String kitType;

	@Column(name = "amount")
	private String amount;

	@Column(name = "receiveddate")
	private Date receivedDate;

	@Column(name = "replytos")
	private String replyTos;

	@Column(name = "payed")
	private boolean payed;

	@Column(name = "sent")
	private boolean sent;

	@Column(name = "note")
	private String note;

	@Column(name = "enabled")
	private boolean enabled;

	@Override
	public String toString() {
		return this.id + " " + this.classNumber + " " + this.messageId + " " + this.studentName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
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

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getReplyTos() {
		return replyTos;
	}

	public void setReplyTos(String replyTos) {
		this.replyTos = replyTos;
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
