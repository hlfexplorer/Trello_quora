package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.UserAdminBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** comments by Archana **/
//** deleteUser **//
@RestController
@RequestMapping("/")
public class AdminController {
    @Autowired UserAdminBusinessService userAdminBusinessService;
    //The admin has a privilege of deleting the user record from the database
    //This endpoint requests for the userUuid to be deleted and the admin accesstoken in the authorization header
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userUuidToBeDeleted,@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
        //The input can be of any form "Bearer <accesstoken>" or "<accesstoken>" in the authorization header
        String uuid ;
        try {
            String[] adminUserAccessToken = authorization.split("Bearer ");
            uuid = userAdminBusinessService.deleteUser(userUuidToBeDeleted, adminUserAccessToken[1]);
        }catch(ArrayIndexOutOfBoundsException are) {
            uuid = userAdminBusinessService.deleteUser(userUuidToBeDeleted, authorization);
        }

        UserDeleteResponse authorizedDeletedResponse = new UserDeleteResponse().id(uuid).status("USER SUCCESSFULLY DELETED");
        //This method returns an object of UserDeleteResponse and HttpStatus
        return new ResponseEntity<UserDeleteResponse>(authorizedDeletedResponse, HttpStatus.OK);
    }
}

