package com.movie.movieinfo.controller;

import com.movie.movieinfo.dto.movieDetail.MovieRequestInfoDto;
import com.movie.movieinfo.dto.movieDetail.MovieInfoResponseDto;
import com.movie.movieinfo.service.MovieInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movieInfo/")
@RequiredArgsConstructor
public class MovieInfoController {

    private  final MovieInfoService movieInfoService;


    /**영화 상세정보 불러오는 API*/
    /**
     * 예시 응답 엔드 포인트
     * http://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key=f5eef3421c602c6cb7ea224104795888
     * */
    @GetMapping("/v1/movies")
    public ResponseEntity<List<MovieInfoResponseDto>> getMovies(@RequestParam MovieRequestInfoDto movieRequestInfoDto) {
      //List<MovieInfoResponseDto> movieListMono = movieInfoService.getAllMovieList(movieRequestInfoDto);
        //System.out.println(movieListMono);
        //return new ResponseEntity<>(movieListMono, HttpStatus.OK);
    }

    /**일별 박스오피스(오늘의 순위)*/
    @GetMapping("/v1/movies")
    public ResponseEntity<List<MovieInfoResponseDto>> getMovies(@RequestParam MovieRequestInfoDto movieRequestInfoDto) {
        //List<MovieInfoResponseDto> movieListMono = movieInfoService.getAllMovieList(movieRequestInfoDto);
        System.out.println(movieListMono);
        //return new ResponseEntity<>(movieListMono, HttpStatus.OK);
    }
    /**
     * 예시 응답 엔드 포인트
     *	http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=20120101
     * */


}
