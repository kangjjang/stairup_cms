package vo;

public class DonationVO {
	String title;					//기부 제목
	String content;					//기부 내용
	String crtDate;					//입력 날짜
	String pic;						//사진
	String result;					//기부 상태 N : 진행중, Y : 종료
	String endDate;					//기부 종료일
	
	int fightCnt;					//힘내요 총개수
	int reviewCnt;					//댓글 개수
	int aim;						//목표 개단
	int giveSeqNo;					//기부 seq
	int cnt;						//달성한 계단수
	
	
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getFightCnt() {
		return fightCnt;
	}
	public void setFightCnt(int fightCnt) {
		this.fightCnt = fightCnt;
	}
	public int getReviewCnt() {
		return reviewCnt;
	}
	public void setReviewCnt(int reviewCnt) {
		this.reviewCnt = reviewCnt;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public int getGiveSeqNo() {
		return giveSeqNo;
	}
	public void setGiveSeqNo(int giveSeqNo) {
		this.giveSeqNo = giveSeqNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getAim() {
		return aim;
	}
	public void setAim(int aim) {
		this.aim = aim;
	}
	
	
}
