package vo;

public class DeviceVO {
	int no;
	int memSeqNo;
	String deviceToken;
	String deviceOsGbn;
	String crtDate;
	String udtDate;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public int getMemSeqNo() {
		return memSeqNo;
	}

	public void setMemSeqNo(int memSeqNo) {
		this.memSeqNo = memSeqNo;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceOsGbn() {
		return deviceOsGbn;
	}

	public void setDeviceOsGbn(String deviceOsGbn) {
		this.deviceOsGbn = deviceOsGbn;
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
