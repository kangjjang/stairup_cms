package vo;

public class BeaconVO {
	int belogSeqNo;
	int memSeqNo;
	int beaconSeqNo;
	int stairsPosition;
	int startBeacon;
	int endBeacon;
	String crtDate;
	int resultBeacon;
	
	public int getEndBeacon() {
		return endBeacon;
	}
	public void setEndBeacon(int endBeacon) {
		this.endBeacon = endBeacon;
	}
	public int getStartBeacon() {
		return startBeacon;
	}
	public void setStartBeacon(int startBeacon) {
		this.startBeacon = startBeacon;
	}
	public int getBeaconSeqNo() {
		return beaconSeqNo;
	}
	public void setBeaconSeqNo(int beaconSeqNo) {
		this.beaconSeqNo = beaconSeqNo;
	}
	
	public int getStairsPosition() {
		return stairsPosition;
	}
	public void setStairsPosition(int stairsPosition) {
		this.stairsPosition = stairsPosition;
	}
	
	public int getBelogSeqNo() {
		return belogSeqNo;
	}
	public void setBelogSeqNo(int belogSeqNo) {
		this.belogSeqNo = belogSeqNo;
	}
	public int getMemSeqNo() {
		return memSeqNo;
	}
	public void setMemSeqNo(int memSeqNo) {
		this.memSeqNo = memSeqNo;
	}

    public int getResultBeacon() {
        return resultBeacon;
    }

    public void setResultBeacon(int resultBeacon) {
        this.resultBeacon = resultBeacon;
    }

    public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	
	
}
