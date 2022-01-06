package com.example.spring101.controller;

import com.example.spring101.model.User;
import com.example.spring101.model.UserActions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/users")
public class UserController {

    ArrayList <User> users=new ArrayList<>();
    int bankBalance=100000;

    @GetMapping
    public ArrayList<User> getUsers(){return users;}

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user){

        if(!checkInput(user)){
            return ResponseEntity.status(400).body("Please send all the fields");
        }
        users.add(user);
        return ResponseEntity.status(200).body("User created");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositFunds(@RequestBody UserActions action){
        if(action.getId()==null || action.getPassword()==null || action.getAmount() == 0){
            return ResponseEntity.status(400).body("Please send all the fields");
        }

        for (int i = 0; i < users.size(); i++) {
            User u=users.get(i);
            if(u.getId().equals(action.getId())){
                if(!(u.getPassword().equals(action.getPassword()))){
                    return ResponseEntity.status(400).body("Password doesn't match the user id");
                }
                int oldBalance=u.getBalance();
                u.setBalance(oldBalance+action.getAmount());
                return ResponseEntity.status(200).body("Deposit confirmed");
            }
        }
        return ResponseEntity.status(400).body("Invalid ID");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFunds(@RequestBody UserActions user){
        if(user.getId()==null || user.getPassword()==null || user.getAmount() == 0){
            return ResponseEntity.status(400).body("Please send all the fields");
        }

        for (int i = 0; i < users.size(); i++) {
            User u=users.get(i);
            if(u.getId().equals(user.getId())){
                if(!(u.getPassword().equals(user.getPassword()))){
                    return ResponseEntity.status(400).body("Password doesn't match the user id");
                }

                if(u.getBalance()-user.getAmount()<0){
                    return ResponseEntity.status(200).body("You don't have enough balance ");
                }

                int oldBalance=u.getBalance();
                u.setBalance(oldBalance-user.getAmount());
                return ResponseEntity.status(200).body("Withdraw confirmed");
            }
        }
        return ResponseEntity.status(400).body("Invalid ID");
    }

    @PostMapping("/take-loan")
    public ResponseEntity<String> takeLoan(@RequestBody UserActions action){
        if(action.getId()==null || action.getPassword()==null || action.getAmount() == 0){
            return ResponseEntity.status(400).body("Please send all the fields");
        }

        for (int i = 0; i < users.size(); i++) {
            User u=users.get(i);
            if(u.getId().equals(action.getId())){
                if(!(u.getPassword().equals(action.getPassword()))){
                    return ResponseEntity.status(400).body("Password doesn't match the user id");
                }

                if(bankBalance-action.getAmount()<0){
                    return ResponseEntity.status(400).body("Bank doesn't have enough money for the transaction");
                }
                bankBalance=bankBalance-action.getAmount();
                int oldLoan=u.getLoanAmount();
                int oldBalance=u.getBalance();
                u.setLoanAmount(action.getAmount()+oldLoan);
                u.setBalance(action.getAmount()+oldBalance);
                return ResponseEntity.status(200).body("Taking loan confirmed");
            }
        }
        return ResponseEntity.status(400).body("Invalid ID");
    }

    @PostMapping("/repay-loan")
    public ResponseEntity<String> repayLoan(@RequestBody UserActions action){
        if(action.getId()==null || action.getPassword()==null || action.getAmount() == 0){
            return ResponseEntity.status(400).body("Please send all the fields");
        }

        for (int i = 0; i < users.size(); i++) {
            User u=users.get(i);
            if(u.getId().equals(action.getId())){
                if(!(u.getPassword().equals(action.getPassword()))){
                    return ResponseEntity.status(400).body("Password doesn't match the user id");
                }
                if(u.getLoanAmount()-action.getAmount()<0){
                    return ResponseEntity.status(400).body("You only have "+u.getLoanAmount()+" as loan");
                }
                if(u.getBalance()-action.getAmount()<0){
                    return ResponseEntity.status(400).body("You don't have enough balance to repay this loan");
                }
                int oldLoan=u.getLoanAmount();
                int oldBalance=u.getBalance();
                u.setLoanAmount(oldLoan-action.getAmount());
                u.setBalance(oldBalance-action.getAmount());
                bankBalance=bankBalance+action.getAmount();
                return ResponseEntity.status(200).body("Repaying loan confirmed");
            }
        }
        return ResponseEntity.status(400).body("Invalid ID");
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable("id") String id){

        boolean isFound=false;
        for (int i = 0; i < users.size() ; i++) {
            if(users.get(i).getId().equals(id)){
                if(users.get(i).getLoanAmount()>0){
                    return ResponseEntity.status(400).body("Please repay the loan before removing the user");
                }
                users.remove(i);
                break;
            }
            if(!isFound){
                return ResponseEntity.status(400).body("Please send valid id");
            }
        }
        return ResponseEntity.status(200).body("User Deleted");
    }

    public boolean checkInput(User user){
        if(user.getId()==null||
                user.getUsername()==null
                || user.getPassword() ==null
                || user.getEmail()==null){
            return false;
        }
        return  true;
    }



}