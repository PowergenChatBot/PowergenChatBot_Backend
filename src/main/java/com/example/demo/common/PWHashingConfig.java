package com.example.demo.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PWHashingConfig {

    /**
     * 비밀번호 Hash화 해서 변환하기
     */
    public String Hashing(byte[] password, String salt) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256"); // SHA-256 해시함수를 사용

        // key-stretching
        for (int i = 0; i < 20; i++) {
            String temp = Byte_to_String(password) + salt;                  // 패스워드와 salt를 합쳐 새로운 문자열 생성
            md.update(temp.getBytes());                                     // temp의 문자열을 해싱하여 md에 저장해둔다
            password = md.digest();                                         // md 객체의 다이제스트를 얻어 password 를 갱신한다
        }

        return Byte_to_String(password);
    }

    /**
     * 바이트 값을 16진수로 변경해준다
     * @return 16진수로 변경 된 값
     */
    private String Byte_to_String(byte[] temp) {
        StringBuilder sb = new StringBuilder();
        for(byte a : temp) {
            sb.append(String.format("%02x", a));
        }
        return sb.toString();
    }
}
