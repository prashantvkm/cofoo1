package cofoo.controllers;

import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cofoo.dtos.CommonResponseDto;
import cofoo.dtos.ErrorDto;
import cofoo.enums.EntityStatus;
import cofoo.exceptions.CodeInvalidOrExpired;
import cofoo.exceptions.DuplicateAccountException;
import cofoo.exceptions.OtpVerificationPending;
import cofoo.exceptions.RecordNotFoundException;

@ControllerAdvice
@RestController
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponseDto validation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new CommonResponseDto("Validation failed for some field(s).",
                EntityStatus.failed,
                errors);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(DuplicateAccountException.class)
    public CommonResponseDto duplicateAccount(DuplicateAccountException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new CommonResponseDto(
                "Account with same Email ID already exits",
                EntityStatus.failed,
                details);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public CommonResponseDto userNotFound(UsernameNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new CommonResponseDto(
                "Account with this Email ID does not exists",
                EntityStatus.failed,
                details);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CodeInvalidOrExpired.class)
    public CommonResponseDto otpIsInvalidOrExpired(CodeInvalidOrExpired ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new CommonResponseDto(
                ex.getLocalizedMessage(),
                EntityStatus.failed,
                details);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RecordNotFoundException.class)
    public CommonResponseDto otpIsInvalidOrExpired(RecordNotFoundException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new CommonResponseDto(
                "Record not found",
                EntityStatus.failed,
                details);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OtpVerificationPending.class)
    public CommonResponseDto otpVerifyPending(OtpVerificationPending ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new CommonResponseDto(
                "OTP verificaion of this account is pending," +
                        " please verify your account with the OTP sent to your email or request a new OTP.",
                EntityStatus.failed,
                details);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public CommonResponseDto validation(RuntimeException ex) {
        return new CommonResponseDto(ex.getLocalizedMessage(),
                EntityStatus.failed,
                null);
    }
}
