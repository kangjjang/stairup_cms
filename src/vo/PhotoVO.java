package vo;

public class PhotoVO {

	int photoSeqNo;
	int memSeqNo;
	String boardCate;
	
	String photoName;
	String photoAddr;
	
	String photoLat;
	String photoLon;
	
	String crtDate;
	String chgDate;
	int boardSeqNo;
	int countFeel;
	public int getBoardSeqNo() {
		return boardSeqNo;
	}
	public void setBoardSeqNo(int boardSeqNo) {
		this.boardSeqNo = boardSeqNo;
	}
	int countMyFeel;
	int countComment;
	
	public int getPhotoSeqNo() {
		return photoSeqNo;
	}
	public void setPhotoSeqNo(int photoSeqNo) {
		this.photoSeqNo = photoSeqNo;
	}
	public int getMemSeqNo() {
		return memSeqNo;
	}
	public void setMemSeqNo(int memSeqNo) {
		this.memSeqNo = memSeqNo;
	}
	public String getBoardCate() {
		return boardCate;
	}
	public void setBoardCate(String boardCate) {
		this.boardCate = boardCate;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String getPhotoAddr() {
		return photoAddr;
	}
	public void setPhotoAddr(String photoAddr) {
		this.photoAddr = photoAddr;
	}
	public String getPhotoLat() {
		return photoLat;
	}
	public void setPhotoLat(String photoLat) {
		this.photoLat = photoLat;
	}
	public String getPhotoLon() {
		return photoLon;
	}
	public void setPhotoLon(String photoLon) {
		this.photoLon = photoLon;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getChgDate() {
		return chgDate;
	}
	public void setChgDate(String chgDate) {
		this.chgDate = chgDate;
	}
	public int getCountFeel() {
		return countFeel;
	}
	public void setCountFeel(int countFeel) {
		this.countFeel = countFeel;
	}
	public int getCountMyFeel() {
		return countMyFeel;
	}
	public void setCountMyFeel(int countMyFeel) {
		this.countMyFeel = countMyFeel;
	}
	public int getCountComment() {
		return countComment;
	}
	public void setCountComment(int countComment) {
		this.countComment = countComment;
	}		
	
}
