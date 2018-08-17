package com.ingenico.epayment.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The balance updating operation can not be done. Transfer information will be deleted.")
public class UnknownUpdateAccountBalanceException extends RuntimeException {

}
