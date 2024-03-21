package com.movie.movieinfo.dto.movie.movieRank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoxOfficeResult {
    private String boxofficeType;
    private String showRange;
    @JsonProperty("dailyBoxOfficeList")
    private List<MovieRank> movieRankList;
}
