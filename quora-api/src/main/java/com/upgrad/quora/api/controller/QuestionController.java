package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class QuestionController {

@Autowired
QuestionBusinessService questionBusinessService;
    /**Commets by Archana **/
    //The admin or the owner of the Question has a privilege of deleting the question
    //This endpoint requests for the questionUuid to be deleted and the questionowner or admin accesstoken in the authorization header
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionUuid, @RequestHeader("authorization") final String authorization)throws AuthorizationFailedException, InvalidQuestionException {

        String uuid ;
        try {
            String[] accessToken = authorization.split("Bearer ");
            uuid = questionBusinessService.deleteQuestion(questionUuid, accessToken[1]);
        }catch(ArrayIndexOutOfBoundsException are) {
            uuid = questionBusinessService.deleteQuestion(questionUuid, authorization);
        }
        QuestionDeleteResponse authorizedDeletedResponse = new QuestionDeleteResponse().id(uuid).status("QUESTION DELETED");
        //This method returns an object of QuestionDeleteResponse and HttpStatus
        return new ResponseEntity<QuestionDeleteResponse>(authorizedDeletedResponse, HttpStatus.OK);
    }
}
