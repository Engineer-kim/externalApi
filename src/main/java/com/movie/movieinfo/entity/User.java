package com.movie.movieinfo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_account")
public class User {

    @Id
    @Column(name = "id", nullable = false, length = 100 ,unique = true)
    private String id;

    @Column(name = "password", nullable = false, length = 300 ,unique = true)
    private String password;

    @Column(name = "userName", nullable = false, length = 100)
    private String userName;

    @Column(name = "userEmail", nullable = false, length = 50,unique = true)
    private String userEmaiil;

    @Column(name = "signDate", nullable = false)
    private LocalDateTime signDate = LocalDateTime.now();

    @Column(name = "dbSts", nullable = false, length = 1, columnDefinition = "char default 'A'")
    private char dbSts = 'A'; // 기본값 'A'

}