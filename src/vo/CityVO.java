package vo;

public class CityVO {
	String cityName;			//도시명
	String cityPic;				//도시사진
	String cityNPic;			//도시 밤이미지
	String cityMem;				//시장이름
	String countryCity;			//나라 명
	String crtDate;				// 입력일
	String content;				// 시장 인사말
	String countryName;			//국가 명
	String countryPic;			//국기
	String memPic;				//회원 사진
	String startDate;			//진입일
	String endDate;				//종료일
	String fstartDate;			//1번 순서 진입일
	private String cityComent;  //도시별 인사말
	
	private int cityComentSeq;  //도시별 인사말 seq
	int citySeqNo;				//도시 seqno
	int countrySeqNo;			//나라 seqno
	int memSeqNo;				//회원 seqno
	int cityDay;				//시장당선일
	int orderLy;				//도시 진행순서
	int totalCountry;			//총 국가수
	int totalCity;				//총 도시수
	int clearCountry;			//클리어한 국가수
	int clearCity;				//클리어한 도시수
	int totalWork;				//총 걸음수
	int totalStair;				//목표 계단수
	int staySeqNo;				//스테이지 테이블 serq
	int allStair;				//총목표 걸음수

	int worldCnt;				//세계일주 횟수
	DepartVO departVo;
	
	AffiliationVO affiliationVo;
	
	public CityVO(){
		super();
		departVo = new DepartVO();
		affiliationVo = new AffiliationVO();
	}
	
	
	
	public AffiliationVO getAffiliationVo() {
		return affiliationVo;
	}



	public void setAffiliationVo(AffiliationVO affiliationVo) {
		this.affiliationVo = affiliationVo;
	}



	public String getCityComent() {
		return cityComent;
	}
	

	public int getCityComentSeq() {
		return cityComentSeq;
	}


	public void setCityComentSeq(int cityComentSeq) {
		this.cityComentSeq = cityComentSeq;
	}


	public void setCityComent(String cityComent) {
		this.cityComent = cityComent;
	}


	public int getWorldCnt() {
		return worldCnt;
	}

	public void setWorldCnt(int worldCnt) {
		this.worldCnt = worldCnt;
	}

	public int getAllStair() {
		return allStair;
	}

	public void setAllStair(int allStair) {
		this.allStair = allStair;
	}

	public String getFstartDate() {
		return fstartDate;
	}

	
	public int getStaySeqNo() {
		return staySeqNo;
	}

	public void setStaySeqNo(int staySeqNo) {
		this.staySeqNo = staySeqNo;
	}

	public void setFstartDate(String fstartDate) {
		this.fstartDate = fstartDate;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getMemPic() {
		return memPic;
	}


	public void setMemPic(String memPic) {
		this.memPic = memPic;
	}


	public DepartVO getDepartVo() {
		return departVo;
	}

	public void setDepartVo(DepartVO departVo) {
		this.departVo = departVo;
	}

	public String getCityNPic() {
		return cityNPic;
	}
	public void setCityNPic(String cityNPic) {
		this.cityNPic = cityNPic;
	}
	public String getCountryPic() {
		return countryPic;
	}
	public void setCountryPic(String countryPic) {
		this.countryPic = countryPic;
	}
	public int getTotalWork() {
		return totalWork;
	}
	public void setTotalWork(int totalWork) {
		this.totalWork = totalWork;
	}
	public int getTotalStair() {
		return totalStair;
	}
	public void setTotalStair(int totalStair) {
		this.totalStair = totalStair;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public int getTotalCountry() {
		return totalCountry;
	}
	public void setTotalCountry(int totalCountry) {
		this.totalCountry = totalCountry;
	}
	public int getTotalCity() {
		return totalCity;
	}
	public void setTotalCity(int totalCity) {
		this.totalCity = totalCity;
	}
	public int getClearCountry() {
		return clearCountry;
	}
	public void setClearCountry(int clearCountry) {
		this.clearCountry = clearCountry;
	}
	public int getClearCity() {
		return clearCity;
	}
	public void setClearCity(int clearCity) {
		this.clearCity = clearCity;
	}
	public int getOrderLy() {
		return orderLy;
	}
	public void setOrderLy(int orderLy) {
		this.orderLy = orderLy;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityPic() {
		return cityPic;
	}
	public void setCityPic(String cityPic) {
		this.cityPic = cityPic;
	}
	public String getCityMem() {
		return cityMem;
	}
	public void setCityMem(String cityMem) {
		this.cityMem = cityMem;
	}
	public String getCountryCity() {
		return countryCity;
	}
	public void setCountryCity(String countryCity) {
		this.countryCity = countryCity;
	}
	public int getCitySeqNo() {
		return citySeqNo;
	}
	public void setCitySeqNo(int citySeqNo) {
		this.citySeqNo = citySeqNo;
	}
	public int getCountrySeqNo() {
		return countrySeqNo;
	}
	public void setCountrySeqNo(int countrySeqNo) {
		this.countrySeqNo = countrySeqNo;
	}
	public int getMemSeqNo() {
		return memSeqNo;
	}
	public void setMemSeqNo(int memSeqNo) {
		this.memSeqNo = memSeqNo;
	}
	public int getCityDay() {
		return cityDay;
	}
	public void setCityDay(int cityDay) {
		this.cityDay = cityDay;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	
	
}
