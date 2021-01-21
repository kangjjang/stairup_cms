package vo;

public class StampEventBeaconVO {

	int seqNo;
	int stampEventSeq;
    int stampBeaconMajor;
	int stampBeaconMinor;
	String crtDate;
	String updDate;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public int getStampEventSeq() {
        return stampEventSeq;
    }

    public void setStampEventSeq(int stampEventSeq) {
        this.stampEventSeq = stampEventSeq;
    }

    public int getStampBeaconMajor() {
        return stampBeaconMajor;
    }

    public void setStampBeaconMajor(int stampBeaconMajor) {
        this.stampBeaconMajor = stampBeaconMajor;
    }

    public int getStampBeaconMinor() {
        return stampBeaconMinor;
    }

    public void setStampBeaconMinor(int stampBeaconMinor) {
        this.stampBeaconMinor = stampBeaconMinor;
    }

    public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
