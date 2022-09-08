package com.kimcompany.jangbogbackendver2.Exception;

import com.kimcompany.jangbogbackendver2.Exception.Exceptions.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    /**
     * 토큰 만료 예외
     * @param exception
     * @return
     */
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> tokenException(TokenException exception) {
        log.info("tokenException");
        JSONObject response = new JSONObject();
        response.put("message", exception.getDetail());
        return ResponseEntity.status(exception.getHttpStatus().value()).body(response);
    }

    /**
     * 유효성검사 실패 예외 @vaild
     * @param exception
     * @return
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<?> tooBigFile(Exception exception) {
        log.info("tooBigFile:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", "파일은 최대 1048576 bytes입니다 ");
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    /**
     * 400error예외
     * @param exception
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentException(IllegalArgumentException exception) {
        log.info("IllegalArgumentException:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    /**
     * 런타임 예외
     * @param exception
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> RuntimeException(RuntimeException exception) {
        log.info("RuntimeException:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", "예상치 못한 오류:"+exception.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 형변환 실패
     * @param exception
     * @return
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> NumberFormatException(NumberFormatException exception) {
        log.info("NumberFormatException:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", "형변환실패 잘못된값요청: "+exception.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    /**
     * 배달 취소 요청시 해시값 생성실패
     * @param exception
     * @return
     */
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<?> NoSuchAlgorithmException(NoSuchAlgorithmException exception) {
        log.info("NoSuchAlgorithmException:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", "암호화 실패: "+exception.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * db트랜잭션 실패시
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> SQLException(SQLException exception) {
        log.info("SQLException:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 널포인터
     * @param exception
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> NullPointerException(NullPointerException exception) {
        log.info("NullPointerException:{}",exception.getMessage());
        JSONObject response = new JSONObject();
        response.put("message", "예상치 못한 오류발생");
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
    }

}
