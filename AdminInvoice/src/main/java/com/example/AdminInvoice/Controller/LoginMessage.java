package com.example.AdminInvoice.Controller;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class LoginMessage {

    String message;
    Boolean status;
    String accessToken;
    String refresh;
    String userRole;
    String userName;
    HttpStatus httpStatus;
    String statusCode;


    public LoginMessage(String string, boolean status, String accessToken, String refresh, String userRole, String userName, HttpStatus httpStatus) {
        this.message=string;
        this.status=status;
        this.accessToken=accessToken;
        this.refresh = refresh;
        this.userRole = userRole;
        this.userName = userName;
        this.httpStatus = httpStatus;
        this.statusCode = String.valueOf(httpStatus.value());
    }


    public void LoginMesage(String message, Boolean status, String token,String refresh, String userName,HttpStatus httpStatus) {
        this.message = message;
        this.status = status;
        this.accessToken = token;
        this.refresh =refresh;
        this.userName=userName;
        this.httpStatus = httpStatus;

    }

}
