package com.example.bankapp.configuration;

import com.example.bankapp.service.exception.account.AccountNotFoundException;
import com.example.bankapp.service.exception.auth.InvalidJwtTokenException;
import com.example.bankapp.service.exception.auth.TokenNotFoundException;
import com.example.bankapp.service.exception.email.EmailAlreadyInUseException;
import com.example.bankapp.service.exception.email.EmailNotFoundException;
import com.example.bankapp.service.exception.email.NoAccessToEmailException;
import com.example.bankapp.service.exception.phone.NoAccessToPhoneException;
import com.example.bankapp.service.exception.phone.PhoneAlreadyInUseException;
import com.example.bankapp.service.exception.phone.PhoneNotFoundException;
import com.example.bankapp.service.exception.transaction.InsufficientFundsException;
import com.example.bankapp.service.exception.transaction.InvalidAmountException;
import com.example.bankapp.service.exception.transaction.TransferToSameUserException;
import com.example.bankapp.service.exception.user.UserAuthenticationProcessingException;
import com.example.bankapp.service.exception.user.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /// Account
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /// Auth
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<Object> handleInvalidJwt(InvalidJwtTokenException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFound(TokenNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /// Email
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<Object> handEmailAlreadyInUse(EmailAlreadyInUseException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handleEmailNotFound(EmailNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(NoAccessToEmailException.class)
    public ResponseEntity<Object> handleNoAccessToEmail(NoAccessToEmailException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /// Phone
    @ExceptionHandler(NoAccessToPhoneException.class)
    public ResponseEntity<Object> handleNoAccessToPhone(NoAccessToPhoneException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(PhoneAlreadyInUseException.class)
    public ResponseEntity<Object> handlePhoneAlreadyInUse(PhoneAlreadyInUseException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(PhoneNotFoundException.class)
    public ResponseEntity<Object> handlePhoneNotFound(PhoneNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /// Transaction
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFunds(InsufficientFundsException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Object> handleInvalidDepositAmount(InvalidAmountException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(TransferToSameUserException.class)
    public ResponseEntity<Object> handleTransferToSameCard(TransferToSameUserException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /// User
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(UserAuthenticationProcessingException.class)
    public ResponseEntity<Object> handleAuthError(UserAuthenticationProcessingException ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /// Generic
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred: " + ex.getMessage(), request);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
