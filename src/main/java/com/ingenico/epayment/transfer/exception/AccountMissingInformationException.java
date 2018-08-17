package com.ingenico.epayment.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "You must include a name")
public class AccountMissingInformationException extends RuntimeException{

}
