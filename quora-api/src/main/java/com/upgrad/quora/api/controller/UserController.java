package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/** comments by Archana **/
//The RestController annotation  adds both the @Controller and @ResponseBody annotations.
@RestController
@RequestMapping("/")

//This method is for user signup
//This method receives the object of SignupUserRequest type with its attributes being set.
//This method is listening for a HTTP POST request as indicated by method= RequestMethod.POST ,
// maps to a URL request of type '/user/signup' , consumes and produces Json.

public class UserController {

    @Autowired
    UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse>  userSignup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        //Since this is user sign up so the role is set as "nonadmin"
        userEntity.setRole("nonadmin");

        //calling the business logic
        // The Http Status code 201 , the response code HttpStatus.CREATED and the status message USER SUCCESSFULLY REGISTERED
        //defined here are as per the requirement provided in the user.json
        final UserEntity createdUserEntity = userBusinessService.signUp(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        //The ResponseEntity returns the userResponse Object along with the HttpStatus
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }

}
