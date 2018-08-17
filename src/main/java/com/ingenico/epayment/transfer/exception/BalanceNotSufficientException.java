package com.ingenico.epayment.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The balance is not sufficient.")
public class BalanceNotSufficientException extends RuntimeException {

}
