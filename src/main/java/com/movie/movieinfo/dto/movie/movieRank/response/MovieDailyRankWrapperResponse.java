package com.movie.movieinfo.dto.movie.movieRank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDailyRankWrapperResponse {
    /**Wrapper DTO*/
    @JsonProperty("boxOfficeResult")
    private BoxDailyOfficeResult boxDailyOfficeResult;
}
