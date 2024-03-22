package com.movie.movieinfo.service.user;

import com.movie.movieinfo.Repository.PasswordResetTokenRepository;
import com.movie.movieinfo.Repository.UserRepository;
import com.movie.movieinfo.dto.user.JoinRequestDto;
import com.movie.movieinfo.entity.PasswordResetToken;
import com.movie.movieinfo.entity.User;
import com.movie.movieinfo.response.CustomResponse;
import com.movie.movieinfo.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    //패스워드 초기화 만료 시간 (24시간)
    private static final int EXPIRATION = 60 * 24;
    @Transactional
    public ResponseEntity<?> registerNewUserAccount(JoinRequestDto joinRequestDto) {
        // 사용자 ID로 기존 사용자 존재 여부를 확인
        Optional<User> existingUser = userRepository.findById(joinRequestDto.getUserId());
        if (existingUser.isPresent()) {
            // 사용자가 이미 존재하면 400 상태 코드와 메시지를 JSON 형태로 반환
            return ResponseEntity
                    .badRequest()
                    .body(new CustomResponse(400, "해당 아이디로 가입된 사용자가 이미 존재합니다."));
        }
        User user = User.builder()
                .dbSts(joinRequestDto.getDbSts())
                .userName(joinRequestDto.getUserName())
                .userId(joinRequestDto.getUserId())
                .userEmail(joinRequestDto.getUserEmail())
                .password(passwordEncoder.encode(joinRequestDto.getUserPassword()))
                .signDate(LocalDateTime.now())
                .build();

        // 사용자 저장
        userRepository.save(user);
        // 성공 응답 반환
        return ResponseEntity.ok(new CustomResponse(200, "회원가입 성공"));
    }
    public CustomResponse checkIfUserIdExists(String userId) {
        if (userRepository.findById(userId).isPresent()) {
            return new CustomResponse(409, "중복된 아이디로 해당 아이디로 가입 불가합니다");
        } else {
            return new CustomResponse(200, "사용 가능한 아이디입니다");
        }

    }

    /**이메일로 유저 아이디 찿기(단건 혹은 다건)*/
    public List<String> findUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .stream()//[User{id='id1', email='email1@email.com'}, User{id='id2', email='email2@email.com'}]
                .map(User::getUserId)//User{id='id1', email='email1@email.com'}  , User{id='id2', email='email2@email.com'}
                .collect(Collectors.toList()); //("id1", "id2")
    }
    public CustomResponse findUserIdsByEmailResponse(String email) {
        List<String> userIds = findUserByEmail(email);
        if (userIds.isEmpty()) {
            return new CustomResponse(404, "해당 이메일로 등록된 사용자를 찾을 수 없습니다.");
        } else {
            return new CustomResponse(200, userIds.toString());
        }
    }
    
    /**패스워드 초기화 로직*/
    public void sendPasswordResetLink(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        String token = UUID.randomUUID().toString();
        savePasswordResetToken(token, user);

        String resetLink = "http://localhost:8080/v1/resetPassword?token=" + token;
        emailService.sendEmail(userEmail, resetLink);
    }

    private void savePasswordResetToken(String token, User user) {
        PasswordResetToken myToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(calculateExpiryDate(EXPIRATION)).build();
        passwordResetTokenRepository.save(myToken);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자의 이름으로 가입된 계정이 있습니다: " + userName));
        return new org.springframework.security.core.userdetails.User
                (user.getUserName(), user.getUserId(), Collections.emptyList());
    }
}
