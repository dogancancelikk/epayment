package com.ingenico.epayment.transfer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	 @RequestMapping(value = "/", method = RequestMethod.GET)
	    public ResponseEntity rootResponse() {
	        String welcomeMessage = "Welcome to the Simple EPayment Solution";
	        return new ResponseEntity<String>(welcomeMessage, HttpStatus.OK);
	    }

}
