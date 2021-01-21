package vo;

public class ReviewVO {
	String reviewContent;
	String crtDate;
	
	int reviewSeqNo;
	int tagetSeqNo;
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public int getReviewSeqNo() {
		return reviewSeqNo;
	}
	public void setReviewSeqNo(int reviewSeqNo) {
		this.reviewSeqNo = reviewSeqNo;
	}
	public int getTagetSeqNo() {
		return tagetSeqNo;
	}
	public void setTagetSeqNo(int tagetSeqNo) {
		this.tagetSeqNo = tagetSeqNo;
	}
	
	
}
