package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;


@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    QuestionBusinessService questionBusinessService;


    /**Commets by Archana **/
   //This endpoint is used to create Question in the QuoraApplication. Any user can go and access this endpoint and create a question
    //This endpoint requests for the attributes in QuestionRequest and accessToken in the authorization header
    @RequestMapping(method = RequestMethod.POST, path ="/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
      public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
       final  QuestionEntity questionEntity = new QuestionEntity();
        //the attribute content is set from QuestionRequest , generation of UUID is done using randomUUID() and date is set as current date-time
        // of the creation of Question
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());
      //The accesstoken can be of any form, Bearer<accessToken> or <accessToken>

        QuestionEntity  createdQuestionEntity;
        try{
            String[] bearerAccessToken = authorization.split("Bearer ");
            createdQuestionEntity = questionBusinessService.postQuestion(questionEntity, bearerAccessToken[1]);
        } catch(ArrayIndexOutOfBoundsException are) {
            createdQuestionEntity = questionBusinessService.postQuestion(questionEntity, authorization);
        }
        //The id(questionUUID) and status message(QUESTION CREATED) are returned on successfull creation of the questionEntity
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");
        //This method returns QuestionResponse Object along with httpStatus
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    /**Commets by Archana **/
    @RequestMapping(method = RequestMethod.GET, path ="/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{
        List<QuestionEntity> listOfQuestions = new ArrayList<>();
        try{
            String[] bearerAccessToken = authorization.split("Bearer ");
            listOfQuestions = questionBusinessService.getAllQuestions(bearerAccessToken[1]);
        }catch(ArrayIndexOutOfBoundsException are) {
            listOfQuestions = questionBusinessService.getAllQuestions(authorization);
        }

       // QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
       // ListIterator<QuestionEntity> questions = listOfQuestions.listIterator();
        List<QuestionDetailsResponse> displayQuestionIdAndContent = new ArrayList<>();
        for(QuestionEntity question:  listOfQuestions){
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(question.getUuid()).
                    content(question.getContent());

            displayQuestionIdAndContent.add(questionDetailsResponse);
         }
        return new ResponseEntity<List<QuestionDetailsResponse>>(displayQuestionIdAndContent, HttpStatus.OK);
    }


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
