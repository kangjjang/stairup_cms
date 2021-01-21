package vo;

public class EventVO {
	int eventSeqNo;
	String eventTitle;
	String eventContent;
	String eventPic;
	String crtDate;
	private String affiliationName;
	int affiliationSeq;
	
	
	public String getAffiliationName() {
		return affiliationName;
	}
	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}
	public int getEventSeqNo() {
		return eventSeqNo;
	}
	public void setEventSeqNo(int eventSeqNo) {
		this.eventSeqNo = eventSeqNo;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	public String getEventPic() {
		return eventPic;
	}
	public void setEventPic(String eventPic) {
		this.eventPic = eventPic;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public int getAffiliationSeq() {
		return affiliationSeq;
	}
	public void setAffiliationSeq(int affiliationSeq) {
		this.affiliationSeq = affiliationSeq;
	}
}
