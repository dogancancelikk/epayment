package com.ingenico.epayment.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "Insufficient Balance.")
public class InsufficientBalanceException extends RuntimeException {

}
