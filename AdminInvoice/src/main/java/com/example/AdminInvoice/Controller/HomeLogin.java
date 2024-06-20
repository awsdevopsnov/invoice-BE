package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.ChangePassword;
import com.example.AdminInvoice.Entity.Login;
import com.example.AdminInvoice.Entity.Register;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.ChangeNewPasswordResponse;
import com.example.AdminInvoice.Payroll.ForgetResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeLogin {
    @Autowired private RegisterService registerService;
    @Autowired private TokenValidator tokenValidator;
    //Verify
    @PostMapping("/verification/{userEmail}")
    public ResponseEntity<ForgetResponse> verifyEmail(@PathVariable String userEmail) {
        try {
            ForgetResponse response = registerService.findbyEmail(userEmail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Change_Password
    @PostMapping("/forgotpassword/{userEmail}")
    public ResponseEntity<UpdateResponse> forgotPassword(@PathVariable String userEmail, @RequestBody Register register) {
        try {
            UpdateResponse response = registerService.updatePassword(userEmail, register);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Login
    @PostMapping("/login")
    public ResponseEntity<LoginMessage> loginUser(@RequestBody Login login)throws Exception {
        try {
            LoginMessage loginMessage = registerService.getUserByValidate(login);
            if (loginMessage.status) {
                return ResponseEntity.ok(loginMessage);
            } else {
                return ResponseEntity.status(loginMessage.getHttpStatus()).body(loginMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //Refresh_Token
    @GetMapping("/refresh")
    public ResponseEntity<LoginMessage> refreshAccessToken(@RequestHeader("refresh") String refresh)throws Exception{
        try {
            LoginMessage loginMessage = registerService.refreshTokenPerformRegeneratedAccessToken(refresh);
            if (loginMessage.status) {
                return ResponseEntity.ok(loginMessage);
            } else {
                return ResponseEntity.status(loginMessage.getHttpStatus()).body(loginMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //After Login Change_Password
    @PostMapping("/changePassword/{userName}")
    public ResponseEntity<ChangeNewPasswordResponse> changePassword(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable String userName,
            @RequestBody ChangePassword register) {
        try {
            if (tokenValidator.isValidToken(accessToken)) {
                ChangeNewPasswordResponse response = registerService.changePassword(userName, register);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ChangeNewPasswordResponse("Token expired.",HttpStatus.FORBIDDEN));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
