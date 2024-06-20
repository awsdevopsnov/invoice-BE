package com.example.AdminInvoice.Controller;

import com.example.AdminInvoice.Entity.*;
import com.example.AdminInvoice.Login.TokenValidator;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.Service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/role")
@Validated
public class HomeRegister {

    @Autowired private RegisterService registerService;
    @Autowired private TokenValidator tokenValidator;

    //Create
    @PostMapping("/register")
    public ResponseEntity<Register> registerUser(@Valid @RequestBody Register register) {
        try {
            Register responseRegister = registerService.addRegister(register);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseRegister);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    //Get findByUserName
    @PostMapping("/findByName/{userName}")
    public ResponseEntity<Register> responseEntity(@PathVariable String userName)throws Exception{
        try {
            Register register=registerService.findByUsername(userName);
            return ResponseEntity.status(HttpStatus.OK).body(register);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get/{id}")
    public ResponseEntity<Register> registerResponseEntity(@PathVariable String id) throws Exception{
        try {
            Register register=registerService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(register);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update
    @PostMapping("/update/{id}")
    public ResponseEntity<UpdateResponse> updateCustomer(@PathVariable String id, @RequestBody Register register) throws Exception{
        try {
                UpdateResponse updateResponse = registerService.updateRegister(id, register);
                return ResponseEntity.status(HttpStatus.OK).body(updateResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UpdateResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    //Delete
    @PostMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> deleteResponseResponseEntity(@PathVariable String id) throws Exception{
        try {
            DeleteResponse deleteResponse=registerService.deleteRegister(id);
            return ResponseEntity.status(HttpStatus.OK).body(deleteResponse);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse(id,"Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
    //List
    @PostMapping("/list")
    public ResponseEntity<List<Register>> registerList()throws Exception{
         try {
             List<Register> registerList = registerService.registerList();
             return ResponseEntity.status(HttpStatus.OK).body(registerList);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

}
