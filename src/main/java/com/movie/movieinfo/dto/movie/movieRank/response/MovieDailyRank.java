package com.movie.movieinfo.dto.movie.movieRank.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDailyRank {
    private String rnum;
    private String rank;
    private String rankInten;
    private String rankOldAndNew;
    private String movieCd;
    private String movieNm;
    private String openDt;
    private String salesAmt;
    private String salesShare;
    private String salesInten;
    private String  salesChange;
    private String salesAcc;
    private String audiCnt;
    private String audiInten;
    private String audiChange;
    private String audiAcc;
    private String scrnCnt;
    private String showCnt;

    public MovieDailyRank() {
    }
}
