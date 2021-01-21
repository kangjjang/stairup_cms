package vo;

public class AdminVO {
	int no;
	String memberId, password, memberName, memberPhone, memberEmail, memberRole, crtDate, udtDate, adminAffiliation;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	
	
	public String getAdminAffiliation() {
		return adminAffiliation;
	}
	public void setAdminAffiliation(String adminAffiliation) {
		this.adminAffiliation = adminAffiliation;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberPhone() {
		return memberPhone;
	}
	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}
	public String getMemberEmail() {
		return memberEmail;
	}
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}
	public String getMemberRole() {
		return memberRole;
	}
	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getUdtDate() {
		return udtDate;
	}
	public void setUdtDate(String udtDate) {
		this.udtDate = udtDate;
	}
}
