package vo;

public class MemberVO {
	String memName;						//이름
	String memNumber;						//사번
	String memResult;					//승인유무
	String memPic;						//사진
	String memDepart;					//부서seq
	String crtDate;						//날짜
	String memAffiliationName;			//소속 이름
	
	private String memId;
	private String memPw;
	
	private int affiliationSeq;			//소속
	private int departSeq;				//부서
	int rank;							//랭킹
	
	int cnt;							//회원 유무파악
	int memSeqNo;						//회원 seq
	int FloorCnt;						//총 걸음수
	int upCnt;							//오늘 오른 수
	int downCnt;						//오늘 내린 수
	
	int fightCnt;						//힘내요 수
	int today;							//일일 운동수
	int week;							//주간 운동수
	int mon;							//월간 운동수
	int year;							//년간 운동수
	int frcnt;							//친그 유무
	int worldCnt; 						//세계일주 횟수
	int likeSelect;						//힘내요 클릭 유무
	
	int lifeUp;							//건강수명 올라간층
	int lifeDown;						//건강수명 내려간층
	CityVO cityVo;
	
	ReviewVO reviewVo;
	AffiliationVO affiliationVo;

	
	
	
	public int getDepartSeq() {
		return departSeq;
	}

	public void setDepartSeq(int departSeq) {
		this.departSeq = departSeq;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemPw() {
		return memPw;
	}

	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}

	public AffiliationVO getAffiliationVo() {
		return affiliationVo;
	}

	public void setAffiliationVo(AffiliationVO affiliationVo) {
		this.affiliationVo = affiliationVo;
	}

	public String getMemAffiliationName() {
		return memAffiliationName;
	}

	public void setMemAffiliationName(String memAffiliationName) {
		this.memAffiliationName = memAffiliationName;
	}

	public int getAffiliationSeq() {
		return affiliationSeq;
	}

	public void setAffiliationSeq(int affiliationSeq) {
		this.affiliationSeq = affiliationSeq;
	}

	public int getLifeUp() {
		return lifeUp;
	}

	public void setLifeUp(int lifeUp) {
		this.lifeUp = lifeUp;
	}

	public int getLifeDown() {
		return lifeDown;
	}

	public void setLifeDown(int lifeDown) {
		this.lifeDown = lifeDown;
	}

	public int getLikeSelect() {
		return likeSelect;
	}

	public void setLikeSelect(int likeSelect) {
		this.likeSelect = likeSelect;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getWorldCnt() {
		return worldCnt;
	}

	public void setWorldCnt(int worldCnt) {
		this.worldCnt = worldCnt;
	}

	public int getFrcnt() {
		return frcnt;
	}

	public void setFrcnt(int frcnt) {
		this.frcnt = frcnt;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(String crtDate) {
		this.crtDate = crtDate;
	}

	public ReviewVO getReviewVo() {
		return reviewVo;
	}

	public void setReviewVo(ReviewVO reviewVo) {
		this.reviewVo = reviewVo;
	}

	public MemberVO(){
		super();
		cityVo = new CityVO();
		reviewVo = new ReviewVO();
		affiliationVo = new AffiliationVO();
	}
	
	
	public int getToday() {
		return today;
	}

	public void setToday(int today) {
		this.today = today;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getMon() {
		return mon;
	}

	public void setMon(int mon) {
		this.mon = mon;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getFightCnt() {
		return fightCnt;
	}

	public void setFightCnt(int fightCnt) {
		this.fightCnt = fightCnt;
	}

	public CityVO getCityVo() {
		return cityVo;
	}
	


	public void setCityVo(CityVO cityVo) {
		this.cityVo = cityVo;
	}

	public int getUpCnt() {
		return upCnt;
	}
	public void setUpCnt(int upCnt) {
		this.upCnt = upCnt;
	}
	public int getDownCnt() {
		return downCnt;
	}
	public void setDownCnt(int downCnt) {
		this.downCnt = downCnt;
	}
	public int getFloorCnt() {
		return FloorCnt;
	}
	public void setFloorCnt(int floorCnt) {
		FloorCnt = floorCnt;
	}
	public int getMemSeqNo() {
		return memSeqNo;
	}
	public void setMemSeqNo(int memSeqNo) {
		this.memSeqNo = memSeqNo;
	}
	
	
	public String getMemDepart() {
		return memDepart;
	}
	public void setMemDepart(String memDepart) {
		this.memDepart = memDepart;
	}
	public String getMemPic() {
		return memPic;
	}
	public void setMemPic(String memPic) {
		this.memPic = memPic;
	}
	
	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public String getMemNumber() {
		return memNumber;
	}

	public void setMemNumber(String memNumber) {
		this.memNumber = memNumber;
	}

	public String getMemResult() {
		return memResult;
	}
	public void setMemResult(String memResult) {
		this.memResult = memResult;
	}
	
}
