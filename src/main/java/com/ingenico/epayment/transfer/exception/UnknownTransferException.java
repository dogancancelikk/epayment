package com.ingenico.epayment.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The transfer operation can not be done.")
public class UnknownTransferException extends RuntimeException {

}
