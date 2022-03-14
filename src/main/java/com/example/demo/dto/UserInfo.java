package com.example.demo.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {

    private int userNo;
    private String userId;
    private String userPw;
    private String userPhone;
    private String userName;
    private String currentlyEmployed;
    private String rank;

}
