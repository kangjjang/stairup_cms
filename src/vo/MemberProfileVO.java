package vo;

public class MemberProfileVO extends MemberNameVO {
	  
	int seqNo;

	String memBirth;
	String memGender;
	String memPhonenum;
	
	String memPhotoBg;
	String memIntroduce;
	int candy;

	String gugunCd;
	String sidoCd;
	String crtDate;
	String chgDate;
	int BestfriendCnt = 0;
	int friendOneCnt = 0;
	int friendTwoCnt = 0;
	
	// temp
	String sidoNm;
	String gugunNm;
	String address;
	
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getMemBirth() {
		return memBirth;
	}
	public void setMemBirth(String memBirth) {
		this.memBirth = memBirth;
	}
	public String getMemGender() {
		return memGender;
	}
	public void setMemGender(String memGender) {
		this.memGender = memGender;
	}
	public String getMemPhonenum() {
		return memPhonenum;
	}
	public void setMemPhonenum(String memPhonenum) {
		this.memPhonenum = memPhonenum;
	}
	public String getMemPhotoBg() {
		return memPhotoBg;
	}
	public void setMemPhotoBg(String memPhotoBg) {
		this.memPhotoBg = memPhotoBg;
	}
	public String getMemIntroduce() {
		return memIntroduce;
	}
	public void setMemIntroduce(String memIntroduce) {
		this.memIntroduce = memIntroduce;
	}
	public int getCandy() {
		return candy;
	}
	public void setCandy(int candy) {
		this.candy = candy;
	}
	public String getSidoCd() {
		return sidoCd;
	}
	public void setSidoCd(String sidoCd) {
		this.sidoCd = sidoCd;
	}
	public String getGugunCd() {
		return gugunCd;
	}
	public void setGugunCd(String gugunCd) {
		this.gugunCd = gugunCd;
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
	public String getSidoNm() {
		return sidoNm;
	}
	public void setSidoNm(String sidoNm) {
		this.sidoNm = sidoNm;
	}
	public String getGugunNm() {
		return gugunNm;
	}
	public void setGugunNm(String gugunNm) {
		this.gugunNm = gugunNm;
	}
	public int getBestfriendCnt() {
		return BestfriendCnt;
	}
	public void setBestfriendCnt(int bestfriendCnt) {
		BestfriendCnt = bestfriendCnt;
	}
	public int getFriendOneCnt() {
		return friendOneCnt;
	}
	public void setFriendOneCnt(int friendOneCnt) {
		this.friendOneCnt = friendOneCnt;
	}
	public int getFriendTwoCnt() {
		return friendTwoCnt;
	}
	public void setFriendTwoCnt(int frinedTwoCnt) {
		this.friendTwoCnt = frinedTwoCnt;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
