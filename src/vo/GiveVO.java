package vo;

public class GiveVO {
	String giveTitle;
	String giveContent;
	String givePic;
	String crtDate;
	String chgDate;
	String endDate;
	int giveAim;
	
	
	int giveSeqNo;

	MemberVO membervo;
	public GiveVO() {
		// TODO Auto-generated constructor stub
		membervo = new MemberVO();
	}
	
	public MemberVO getMembervo() {
		return membervo;
	}

	public void setMembervo(MemberVO membervo) {
		this.membervo = membervo;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	public int getGiveAim() {
		return giveAim;
	}

	public void setGiveAim(int giveAim) {
		this.giveAim = giveAim;
	}

	public String getGiveTitle() {
		return giveTitle;
	}

	public void setGiveTitle(String giveTitle) {
		this.giveTitle = giveTitle;
	}

	public String getGiveContent() {
		return giveContent;
	}

	public void setGiveContent(String giveContent) {
		this.giveContent = giveContent;
	}

	public String getGivePic() {
		return givePic;
	}

	public void setGivePic(String givePic) {
		this.givePic = givePic;
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

	public int getGiveSeqNo() {
		return giveSeqNo;
	}

	public void setGiveSeqNo(int giveSeqNo) {
		this.giveSeqNo = giveSeqNo;
	}
	
	
}
