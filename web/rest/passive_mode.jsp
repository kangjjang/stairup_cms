<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@page import="org.json.*" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="util.StringUtil" %>
<%@page import="util.HashUtil" %>
<%@page import="util.ResultCode" %>
<%@page import="dao.BeaconDao" %>
<%@page import="vo.BeaconVO" %>
<%@page import="dao.MemberDao" %>
<%@page import="vo.MemberVO" %>
<%@page import="dao.CityDao" %>
<%@page import="vo.CityVO" %>
<%@page import="vo.MayorVO" %>

<%
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

    String resultCode = "0000";
    String resultDesc = "success";

    int major = Integer.parseInt(StringUtil.nchk(request.getParameter("major"), "0"));
    int startMinor = Integer.parseInt(StringUtil.nchk(request.getParameter("startMinor"), "0"));
    int endMinor = Integer.parseInt(StringUtil.nchk(request.getParameter("endMinor"), "0"));
    int memSeqNo = Integer.parseInt(StringUtil.nchk(request.getParameter("memSeqNo"), "0"));

    //int affiliationSeq = Integer.parseInt(StringUtil.nchk(request.getParameter("affiliationSeq"),"0"));   //해당 회원의 소속정보

    int wresult = 0;    //업데이트 결과값 저장
    int sresult = 0;    //인설트 결과값 저장
    //int dresult = 0;	//삭제 결과값 저장a
    int result;
    int todayStairCnt = 0;

    System.out.println("##2222222222222passive_mode");

    if (major >= 100 && major <= 800 && major != 999 && major != 108 && major != 211) {
        BeaconDao bDao = new BeaconDao();
        CityDao cDao = new CityDao();

        MemberDao mDao = new MemberDao();
        int memAffiliation = mDao.selectMemberAffiliation(memSeqNo);  // 전달 받은 회원의 소속 정보를 가져옴
        int memDepart = mDao.selectMemberDepartPassive(memSeqNo);  // 전달 받은 회원의 소속 정보를 가져옴

        if (startMinor > 99) {// 지하부터는 100 단위로 입력 되어 있음.
            startMinor = -(startMinor / 100);
        }

        if (endMinor > 99) {// 지하부터는 100 단위로 입력 되어 있음.
            endMinor = -(endMinor / 100);
        }

        System.out.println("##passive_mode");
        System.out.println("memSeqNo : " + memSeqNo);
        System.out.println("startMinor : " + startMinor);
        System.out.println("endMinor : " + endMinor);

        synchronized (this) {
            //skhero.kang 2019-10-02 -400 이런식으로 들어오는 이상 비콘 값이 있어서 예외 처리
            //if(memSeqNo > 0){
            if (memSeqNo > 0 && startMinor >= -5 && endMinor >= -5) {
                if (startMinor < endMinor) {

                    while (startMinor <= endMinor) {
                        if (startMinor == 0) {
                            startMinor = 1;
                        }

                        BeaconVO vo = bDao.selectBeacon(memSeqNo);    //회원에 관한 비콘 정보중 마지막에 입력된 end_beacon 필드가 널값인것의 정보를 가져옴.
                        if (vo.getBelogSeqNo() > 0) {    //회원이 입력한 비콘로그의 마지막 seqno가 0보다 클경우

                            if (vo.getStairsPosition() == major) {//건물, 계단의 위치가 같은지 파악을 한다.

                                result = vo.getStartBeacon() - (startMinor);    //마지막으로 입력된 비콘로그의 시작 비콘의 층수와 현재 입력된 비콘의 층수를 뺀다.

                                if (vo.getStartBeacon() == 1 && startMinor == -1) {                //result 값이 2인것은 시작비콘의 값이 1이고 끝비콘이 -1 일때이다.(1-(-1))
                                    result = 1;    // 내려간것으로 체크
                                } else if (result == -1 || (vo.getStartBeacon() == -1 && startMinor == 1)) {
                                    result = -1;
                                }

                                if (result == 1 || result == -1) { //차이가 1일 경우에만 끝 비콘에 update 한 후에 새로 insert를 한다.  양수는 내려간거, 음수는 올라간거
                                    synchronized (this) {

                                        bDao.updateEndBeacon(startMinor, vo.getBelogSeqNo(), result);    //끝비콘 UPDATE입력

                                        bDao.insertBeacon(major, startMinor, memSeqNo);    //입력된 끝비콘을 시작비콘으로 INSERT

                                        int masterSeq = bDao.selectMaster(memSeqNo);            //TB_MASTER 테이블에 오늘 날짜로 입력된 SEQ 가져오기

                                        if (masterSeq > 0) {                    //TB_MASTER 에 오늘 입력된 값이 있을경우

                                            if (result == -1) {
                                                wresult = bDao.updateMaster(masterSeq, result);
                                            }
                                        } else {
                                            int totalWalk = bDao.selectToTalWalk(memSeqNo);                // 이전에 쌓인 누적된 층수

                                            wresult = bDao.insertMaster(memSeqNo, result, totalWalk, memDepart, memAffiliation);
                                            if (wresult > 0) {
                                                wresult = 1;
                                            }
                                        }

                                        //TB_MEM_STAY 데이터 구성하는 부분

                                        // 1. TB_MEM_STAY에서 내 현재위치한 도시 SEQ 및 현재 목표까지의 계단수, 첫도시부터의 총 걸음수, orderly.
                                        CityVO cityVO = bDao.selectCurrentCityAndStair(memSeqNo);
                                        int citySeqCurrent = cityVO.getCitySeqNo();
                                        int cityTotalStair = cityVO.getTotalStair();
                                        int cityTotalWork = cityVO.getTotalWork() - (cityVO.getAllStair() * cityVO.getWorldCnt());
                                        int cityOrderLy = cityVO.getOrderLy();
                                        int staySeqNo = cityVO.getStaySeqNo();
                                        // 총 도시수
                                        // 2. 비교 : 현재 목표까지의 계단수 = 첫도시부터총 걸음수
                                        //////System.out.println("비콘 로그 저장 : "+cityTotalStair);
                                        //////System.out.println("로그 저장 : "+cityTotalWork);
                                        if (cityTotalStair <= cityTotalWork) {
                                            // 2-1 . TB_MEM_STAY 에 TB_CITY에서 ORDERLRY + 1 한것의 CITY_SEQ_NO 와 ORDERLRY 값을 가져온다

                                            CityVO cityVONext = cDao.selectCityInfo(cityVO.getOrderLy() + 1);
                                            // 2-1-1 row가 존재한다면 해당값을 TB_MEM_STAY INSERT한다.
                                            if (cityVONext.getCitySeqNo() > 0) {
                                                //bDao = new BeaconDao();
                                                bDao.updateMemStay(memSeqNo, citySeqCurrent, staySeqNo);
                                                //bDao = new BeaconDao();
                                                sresult = bDao.insertMemStay(memSeqNo, cityVONext.getCitySeqNo(), cityVONext.getOrderLy());
                                                if (sresult > 0) {
                                                    sresult = 1;
                                                }

                                            } else {
                                                // 2-1-2 TB_CITY에서 ORDERLRY = 1 CITY_SEQ_NO 와 ORDERLRY 값을 가져온다
                                                //cDao = new CityDao();
                                                CityVO cityVOfirst = cDao.selectCityInfo(1);
                                                // 2-1-2-1 row가 존재한다면 해당값을 INSERT한다.
                                                if (cityVOfirst.getCitySeqNo() > 0) {
                                                    //bDao = new BeaconDao();
                                                    bDao.updateMemStay(memSeqNo, citySeqCurrent, staySeqNo);

                                                    //bDao = new BeaconDao();
                                                    sresult = bDao.insertMemStay(memSeqNo, cityVOfirst.getCitySeqNo(), cityVOfirst.getOrderLy());
                                                    if (sresult > 0) {
                                                        sresult = 1;
                                                    }
                                                    // 2-1-2-2 TB_MEM_STAY 로 부터 startdate 와 enddate를 가져온다
                                                    //bDao = new BeaconDao();
                                                    int worldWalkPeriod = bDao.selectMemStayAllTime(memSeqNo, cityOrderLy);
                                                    // 2-1-2-3 TB_WORLD 에 세계일주 정보를 INSERT한다.
                                                    //bDao = new BeaconDao();
                                                    bDao.insertWorld(memSeqNo, worldWalkPeriod);
                                                }
                                            }
                                            //bDao = new BeaconDao();
                                            // 2-2 클리어 한 상황에서 TB_MEM_STAY에서 현재 고객 CITY_SEQ_NO 의 START_DATE END_DATE 의 차이를 가져오고
                                            int cityWalkPeriod = bDao.selectMemStayCityPeriod(memSeqNo, citySeqCurrent);  // 회원의 클리어타임 조회

                                            //     TB_MAYOR 의 MEM_SEQ_NO CITY_SEQ_NO 의 클리어한 시간을 가져와서 비교를 한다.
                                            //bDao = new BeaconDao();
                                            MayorVO cityWalkPeriodMayor = bDao.selectMayorPeriod(citySeqCurrent, memAffiliation);  //본인의 소속의 현재 시장의 클리어 타임 조회
                                            // 2-2-1 현재 고객이 더 짧은 시간에 통과를 했을 경우 TB_MAYOR 정보를 INSERT한다.

                                            if (cityWalkPeriodMayor.getWalkPeriod() == 0 || cityWalkPeriod <= cityWalkPeriodMayor.getWalkPeriod()) {
                                                //bDao = new BeaconDao();   현재 시장보다 클리어 타임이 짧을 경우
                                                bDao.deleteMayor(cityWalkPeriodMayor.getMayorSeq(), citySeqCurrent, memAffiliation);
                                                //bDao = new BeaconDao();
                                                bDao.insetMayor(memSeqNo, citySeqCurrent, cityWalkPeriod, memAffiliation);

                                                //////System.out.println(" 시장 인설트 = ");
                                                //////System.out.println(" memSeqNo= "+memSeqNo);
                                                //////System.out.println(" citySeqCurrent= "+citySeqCurrent);
                                                //////System.out.println(" memAffiliation= "+memAffiliation);
                                            }
                                        }
                                    }
                                } else {

                                    //bDao = new BeaconDao();
                                    //bDao.deleteBeacon(vo.getBelogSeqNo());	//마지막에 입력된 비콘로그(끝비콘은 NULL) 삭제
                                    //bDao = new BeaconDao();
                                    bDao.insertBeacon(major, startMinor, memSeqNo);    //입력된 비콘으로 새로이 시작
                                }
                            } else {    //건물, 계단의 위치가 다를경우
                                //bDao = new BeaconDao();
                                //bDao.deleteBeacon(vo.getBelogSeqNo());	//마지막에 입력된 비콘로그(끝비콘은 NULL) 삭제
                                //bDao = new BeaconDao();
                                bDao.insertBeacon(major, startMinor, memSeqNo);    //입력된 비콘으로 새로이 시작
                            }
                        } else {
                            //bDao = new BeaconDao();
                            bDao.insertBeacon(major, startMinor, memSeqNo);    //비콘 로그 입력
                        }
                        startMinor++;
                        //////System.out.println("올라간거");
                    }
                } else if (startMinor > endMinor) {

                    while (startMinor >= endMinor) {

                        if (startMinor == 0) {
                            startMinor = -1;
                        }

                        BeaconVO vo = bDao.selectBeacon(memSeqNo);    //회원에 관한 비콘 정보중 마지막에 입력된 end_beacon 필드가 널값인것의 정보를 가져옴.
                        if (vo.getBelogSeqNo() > 0) {    //회원이 입력한 비콘로그의 마지막 seqno가 0보다 클경우
                            if (vo.getStairsPosition() == major) {//건물, 계단의 위치가 같은지 파악을 한다.
                                result = vo.getStartBeacon() - (startMinor);    //마지막으로 입력된 비콘로그의 시작 비콘의 층수와 현재 입력된 비콘의 층수를 뺀다.
                                if (vo.getStartBeacon() == 1 && startMinor == -1) {                //result 값이 2인것은 시작비콘의 값이 1이고 끝비콘이 -1 일때이다.(1-(-1))
                                    result = 1;    // 내려간것으로 체크
                                } else if (result == -1 || (vo.getStartBeacon() == -1 && startMinor == 1)) {
                                    result = -1;
                                }

                                if (result == 1 || result == -1) { //차이가 1일 경우에만 끝 비콘에 update 한 후에 새로 insert를 한다.  양수는 내려간거, 음수는 올라간거
                                    synchronized (this) {
                                        //bDao = new BeaconDao();
                                        bDao.updateEndBeacon(startMinor, vo.getBelogSeqNo(), result);    //끝비콘 UPDATE입력
                                        //bDao = new BeaconDao();
                                        bDao.insertBeacon(major, startMinor, memSeqNo);    //입력된 끝비콘을 시작비콘으로 INSERT
                                        //bDao = new BeaconDao();
                                        int masterSeq = bDao.selectMaster(memSeqNo);            //TB_MASTER 테이블에 오늘 날짜로 입력된 SEQ 가져오기
                                        //bDao = new BeaconDao();
                                        if (masterSeq > 0) {                    //TB_MASTER 에 오늘 입력된 값이 있을경우
                                            if (result == 1) {
                                                wresult = bDao.updateMaster(masterSeq, result);
                                            }
                                        } else {
                                            int totalWalk = bDao.selectToTalWalk(memSeqNo);                // 이전에 쌓인 누적된 층수
                                            //bDao = new BeaconDao();
                                            wresult = bDao.insertMaster(memSeqNo, result, totalWalk, memDepart, memAffiliation);
                                            if (wresult > 0) {
                                                wresult = 1;
                                            }
                                        }

                                        //TB_MEM_STAY 데이터 구성하는 부분
                                        //bDao = new BeaconDao();
                                        // 1. TB_MEM_STAY에서 내 현재위치한 도시 SEQ 및 현재 목표까지의 계단수, 첫도시부터의 총 걸음수, orderly.
                                        CityVO cityVO = bDao.selectCurrentCityAndStair(memSeqNo);
                                        int citySeqCurrent = cityVO.getCitySeqNo();
                                        int cityTotalStair = cityVO.getTotalStair();
                                        int cityTotalWork = cityVO.getTotalWork() - (cityVO.getAllStair() * cityVO.getWorldCnt());
                                        int cityOrderLy = cityVO.getOrderLy();
                                        int staySeqNo = cityVO.getStaySeqNo();
                                        // 총 도시수
                                        // 2. 비교 : 현재 목표까지의 계단수 = 첫도시부터총 걸음수
                                        //////System.out.println("비콘 로그 저장 : "+cityTotalStair);
                                        //////System.out.println("로그 저장 : "+cityTotalWork);
                                        if (cityTotalStair <= cityTotalWork) {
                                            // 2-1 . TB_MEM_STAY 에 TB_CITY에서 ORDERLRY + 1 한것의 CITY_SEQ_NO 와 ORDERLRY 값을 가져온다

                                            CityVO cityVONext = cDao.selectCityInfo(cityVO.getOrderLy() + 1);
                                            // 2-1-1 row가 존재한다면 해당값을 TB_MEM_STAY INSERT한다.
                                            if (cityVONext.getCitySeqNo() > 0) {
                                                //bDao = new BeaconDao();
                                                bDao.updateMemStay(memSeqNo, citySeqCurrent, staySeqNo);
                                                //bDao = new BeaconDao();
                                                sresult = bDao.insertMemStay(memSeqNo, cityVONext.getCitySeqNo(), cityVONext.getOrderLy());
                                                if (sresult > 0) {
                                                    sresult = 1;
                                                }

                                            } else {
                                                // 2-1-2 TB_CITY에서 ORDERLRY = 1 CITY_SEQ_NO 와 ORDERLRY 값을 가져온다

                                                CityVO cityVOfirst = cDao.selectCityInfo(1);
                                                // 2-1-2-1 row가 존재한다면 해당값을 INSERT한다.
                                                if (cityVOfirst.getCitySeqNo() > 0) {

                                                    bDao.updateMemStay(memSeqNo, citySeqCurrent, staySeqNo);


                                                    sresult = bDao.insertMemStay(memSeqNo, cityVOfirst.getCitySeqNo(), cityVOfirst.getOrderLy());
                                                    if (sresult > 0) {
                                                        sresult = 1;
                                                    }
                                                    // 2-1-2-2 TB_MEM_STAY 로 부터 startdate 와 enddate를 가져온다

                                                    int worldWalkPeriod = bDao.selectMemStayAllTime(memSeqNo, cityOrderLy);
                                                    // 2-1-2-3 TB_WORLD 에 세계일주 정보를 INSERT한다.

                                                    bDao.insertWorld(memSeqNo, worldWalkPeriod);
                                                }
                                            }

                                            // 2-2 클리어 한 상황에서 TB_MEM_STAY에서 현재 고객 CITY_SEQ_NO 의 START_DATE END_DATE 의 차이를 가져오고
                                            int cityWalkPeriod = bDao.selectMemStayCityPeriod(memSeqNo, citySeqCurrent);
                                            //     TB_MAYOR 의 MEM_SEQ_NO CITY_SEQ_NO 의 클리어한 시간을 가져와서 비교를 한다.

                                            MayorVO cityWalkPeriodMayor = bDao.selectMayorPeriod(citySeqCurrent, memAffiliation);  // 현재 시장의 클리어 타임 조회
                                            // 2-2-1 현재 고객이 더 짧은 시간에 통과를 했을 경우 TB_MAYOR 정보를 INSERT한다.

                                            if (cityWalkPeriodMayor.getWalkPeriod() == 0 || cityWalkPeriod <= cityWalkPeriodMayor.getWalkPeriod()) {

                                                bDao.deleteMayor(cityWalkPeriodMayor.getMayorSeq(), citySeqCurrent, memAffiliation);

                                                bDao.insetMayor(memSeqNo, citySeqCurrent, cityWalkPeriod, memAffiliation);
                                            }
                                        }
                                    }
                                } else {

                                    //bDao = new BeaconDao();
                                    //bDao.deleteBeacon(vo.getBelogSeqNo());	//마지막에 입력된 비콘로그(끝비콘은 NULL) 삭제

                                    bDao.insertBeacon(major, startMinor, memSeqNo);    //입력된 비콘으로 새로이 시작
                                }
                            } else {    //건물, 계단의 위치가 다를경우
                                //bDao = new BeaconDao();
                                //bDao.deleteBeacon(vo.getBelogSeqNo());	//마지막에 입력된 비콘로그(끝비콘은 NULL) 삭제

                                bDao.insertBeacon(major, startMinor, memSeqNo);    //입력된 비콘으로 새로이 시작
                            }
                        } else {

                            bDao.insertBeacon(major, startMinor, memSeqNo);    //비콘 로그 입력
                        }
                        startMinor--;
                        //////System.out.println("내려간거");
                    }
                } else {
                    resultCode = "7777";
                    resultDesc = "시작과 끝이 같습니다.";
                }
            } else {
                resultCode = "9999";
                resultDesc = "잘못된 회원 번호 입니다.";
            }
        }

        bDao.closeConn();
        cDao.closeConn();
        mDao.closeConn();
    }

    JSONObject obj = new JSONObject();

    obj.put("resultCode", resultCode);
    obj.put("resultDesc", resultDesc);
    obj.put("wresult", wresult);
    obj.put("sresult", sresult);
    obj.put("todayStairCnt", todayStairCnt);

    out.print(obj);
    out.flush();

%>
