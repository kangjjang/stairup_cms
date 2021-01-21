package vo;

public class NoticeVO {
	int notiSeqNo;

	String notiTitle;
	String notiContent;
	String noticePic;
	String crtDate;
	
	private String affiliationName; // 소속 이름
	int affiliationSeq;
	
	
	public String getAffiliationName() {
		return affiliationName;
	}
	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}
	public int getNotiSeqNo() {
		return notiSeqNo;
	}
	public void setNotiSeqNo(int notiSeqNo) {
		this.notiSeqNo = notiSeqNo;
	}
	
	public String getNotiTitle() {
		return notiTitle;
	}
	public void setNotiTitle(String notiTitle) {
		this.notiTitle = notiTitle;
	}
	public String getNotiContent() {
		return notiContent;
	}
	public void setNotiContent(String notiContent) {
		this.notiContent = notiContent;
	}
	public String getNoticePic() {
		return noticePic;
	}
	public void setNoticePic(String noticePic) {
		this.noticePic = noticePic;
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
