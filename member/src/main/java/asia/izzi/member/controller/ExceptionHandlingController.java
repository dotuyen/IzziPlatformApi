package asia.izzi.member.controller;

import asia.izzi.member.exception.ErrorResponse;
import asia.izzi.member.exception.FieldInvalidError;
import asia.izzi.member.exception.InternalServerErrorException;
import asia.izzi.member.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {


    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseBody
    protected ErrorResponse handleInternalServerErrorException(InternalServerErrorException ex) {
        return new ErrorResponse(INTERNAL_SERVER_ERROR, MessageUtils.getMessage(ex.getMessage()), ex);
    }

    @ResponseStatus(REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    @ResponseBody
    protected ErrorResponse handleSagaTimeoutException(TimeoutException ex) {
        return new ErrorResponse(HttpStatus.REQUEST_TIMEOUT, MessageUtils.getMessage("error.timeout"), ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST, MessageUtils.getMessage("error.argument.invalid"), ex);
        BindingResult result = ex.getBindingResult();
        result.getAllErrors()
                .stream()
                .map(FieldError.class::cast)
                .map(error -> new FieldInvalidError(error.getObjectName(), error.getField(), error.getDefaultMessage()))
                .forEach(errorResponse::addDetailError);
        return errorResponse;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    protected ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse(BAD_REQUEST, ex.getMessage(), ex);
    }

}
