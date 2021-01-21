package vo;

public class BeaconEventVO {

	private int seqNo;
	private int beaconMajor;
	private int beaconMinor;
	private int memberSeq;
	private String beaconLocation;
	private String beaconUse;
	private String beaconPosition;
	private String beaconImage;
	private String beaconContent;
	private String beaconUrl;
	private String beaconTitle;
	private String delYn;
	private String crtDate;
	private String transferTime;
	private int transferDate;  // 팝업 전송 시간
	private String beaconUrlYN;
	private int  beaconSoundSeq;
	private String beaconSoundName;
	private String logoImage;
	private String soundUrl;
	
	private String beaconPopFromDateTime;
	private String beaconPopToDateTime;
	private int  beaconPopCnt;
	
	
	
	public String getSoundUrl() {
		return soundUrl;
	}
	public void setSoundUrl(String soundUrl) {
		this.soundUrl = soundUrl;
	}
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	public String getBeaconSoundName() {
		return beaconSoundName;
	}
	public void setBeaconSoundName(String beaconSoundName) {
		this.beaconSoundName = beaconSoundName;
	}
	public int getBeaconSoundSeq() {
		return beaconSoundSeq;
	}
	public void setBeaconSoundSeq(int beaconSoundSeq) {
		this.beaconSoundSeq = beaconSoundSeq;
	}
	public String getBeaconUrlYN() {
		return beaconUrlYN;
	}
	public void setBeaconUrlYN(String beaconUrlYN) {
		this.beaconUrlYN = beaconUrlYN;
	}
	public String getBeaconTitle() {
		return beaconTitle;
	}
	public void setBeaconTitle(String beaconTitle) {
		this.beaconTitle = beaconTitle;
	}
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	public int getMemberSeq() {
		return memberSeq;
	}
	public void setMemberSeq(int memberSeq) {
		this.memberSeq = memberSeq;
	}
	public int getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(int transferDate) {
		this.transferDate = transferDate;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getBeaconMajor() {
		return beaconMajor;
	}
	public void setBeaconMajor(int beaconMajor) {
		this.beaconMajor = beaconMajor;
	}
	public int getBeaconMinor() {
		return beaconMinor;
	}
	public void setBeaconMinor(int beaconMinor) {
		this.beaconMinor = beaconMinor;
	}
	public String getBeaconLocation() {
		return beaconLocation;
	}
	public void setBeaconLocation(String beaconLocation) {
		this.beaconLocation = beaconLocation;
	}
	public String getBeaconUse() {
		return beaconUse;
	}
	public void setBeaconUse(String beaconUse) {
		this.beaconUse = beaconUse;
	}
	public String getBeaconPosition() {
		return beaconPosition;
	}
	public void setBeaconPosition(String beaconPosition) {
		this.beaconPosition = beaconPosition;
	}
	public String getBeaconImage() {
		return beaconImage;
	}
	public void setBeaconImage(String beaconImage) {
		this.beaconImage = beaconImage;
	}
	public String getBeaconContent() {
		return beaconContent;
	}
	public void setBeaconContent(String beaconContent) {
		this.beaconContent = beaconContent;
	}
	public String getBeaconUrl() {
		return beaconUrl;
	}
	public void setBeaconUrl(String beaconUrl) {
		this.beaconUrl = beaconUrl;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getBeaconPopFromDateTime() {
		return beaconPopFromDateTime;
	}
	public void setBeaconPopFromDateTime(String beaconPopFromDateTime) {
		this.beaconPopFromDateTime = beaconPopFromDateTime;
	}
	public String getBeaconPopToDateTime() {
		return beaconPopToDateTime;
	}
	public void setBeaconPopToDateTime(String beaconPopToDateTime) {
		this.beaconPopToDateTime = beaconPopToDateTime;
	}
	public int getBeaconPopCnt() {
		return beaconPopCnt;
	}
	public void setBeaconPopCnt(int beaconPopCnt) {
		this.beaconPopCnt = beaconPopCnt;
	}
}
