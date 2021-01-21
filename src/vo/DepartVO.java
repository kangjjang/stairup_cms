package vo;

public class DepartVO {
	String departName;				//부서명
	String homeName;				//홈부서명
	String homePic;					//홈부서 사진
	String awayName;				//어웨이 부서명
	String awayPic;					//어웨이 사진
	
	int departSeqNo;				//부서 seq
	int homeSeqNo;					//홈부서 seq
	int awaySeqNo;					//어웨이 seq
	int leagueSeqNo;				//리그 seq
	int departPeople;				//부서 총인원수, 홈팀 인원수
	int awayDepartPeople;			//부서 어웨이팀 총 인원수
	int workCnt;					//목표 걸음수
	int homeWalkCnt;				//홈팀 걸음수
	int awayWalkCnt;				//어웨이팀 걸음수
	String crtDate;					//시작일
	String endDate;					//종료일
	
	int departWin;					//부서 승리수
	int departLose;					//부서 패배수
	int departDraw;					//부서 무승부수
	int departRank;					//부서 랭킹
	String affiliationName;			//소속 이름
	
	
	
	
	

	public String getAffiliationName() {
		return affiliationName;
	}
	public void setAffiliationName(String affiliationName) {
		this.affiliationName = affiliationName;
	}
	public int getDepartRank() {
		return departRank;
	}
	public void setDepartRank(int departRank) {
		this.departRank = departRank;
	}
	public int getDepartWin() {
		return departWin;
	}
	public void setDepartWin(int departWin) {
		this.departWin = departWin;
	}
	public int getDepartLose() {
		return departLose;
	}
	public void setDepartLose(int departLose) {
		this.departLose = departLose;
	}
	public int getDepartDraw() {
		return departDraw;
	}
	public void setDepartDraw(int departDraw) {
		this.departDraw = departDraw;
	}
	public int getAwayDepartPeople() {
		return awayDepartPeople;
	}
	public void setAwayDepartPeople(int awayDepartPeople) {
		this.awayDepartPeople = awayDepartPeople;
	}
	public int getHomeWalkCnt() {
		return homeWalkCnt;
	}
	public void setHomeWalkCnt(int homeWalkCnt) {
		this.homeWalkCnt = homeWalkCnt;
	}
	public int getAwayWalkCnt() {
		return awayWalkCnt;
	}
	public void setAwayWalkCnt(int awayWalkCnt) {
		this.awayWalkCnt = awayWalkCnt;
	}
	public int getWorkCnt() {
		return workCnt;
	}
	public void setWorkCnt(int workCnt) {
		this.workCnt = workCnt;
	}

	public int getDepartPeople() {
		return departPeople;
	}
	public void setDepartPeople(int departPeople) {
		this.departPeople = departPeople;
	}
	public int getLeagueSeqNo() {
		return leagueSeqNo;
	}
	public void setLeagueSeqNo(int leagueSeqNo) {
		this.leagueSeqNo = leagueSeqNo;
	}
	public String getHomeName() {
		return homeName;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public String getHomePic() {
		return homePic;
	}
	public void setHomePic(String homePic) {
		this.homePic = homePic;
	}
	public String getAwayName() {
		return awayName;
	}
	public void setAwayName(String awayName) {
		this.awayName = awayName;
	}
	public String getAwayPic() {
		return awayPic;
	}
	public void setAwayPic(String awayPic) {
		this.awayPic = awayPic;
	}
	public int getHomeSeqNo() {
		return homeSeqNo;
	}
	public void setHomeSeqNo(int homeSeqNo) {
		this.homeSeqNo = homeSeqNo;
	}
	public int getAwaySeqNo() {
		return awaySeqNo;
	}
	public void setAwaySeqNo(int awaySeqNo) {
		this.awaySeqNo = awaySeqNo;
	}
	public String getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public int getDepartSeqNo() {
		return departSeqNo;
	}
	public void setDepartSeqNo(int departSeqNo) {
		this.departSeqNo = departSeqNo;
	}

	
	
}
