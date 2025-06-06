package com.mnogo.transactions.exception;

import com.mnogo.transactions.dto.response.ErrorResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(UserNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadRequestException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException exception) {
        Optional<FieldError> fieldError = exception.getBindingResult().getFieldErrors().stream().findFirst();

        if (fieldError.isPresent()) {
            return new ErrorResponse(fieldError.get().getDefaultMessage());
        }

        // если ошибка возникла не при валидации полей
        Optional<ObjectError> globalError = exception.getBindingResult().getGlobalErrors().stream().findFirst();

        return globalError
                .map(objectError -> new ErrorResponse(objectError.getDefaultMessage()))
                .orElseGet(() -> new ErrorResponse("Произошла неизвестная ошибка валидации"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleException(DataIntegrityViolationException exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        String rootMessage = rootCause.getMessage();
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)=\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(rootMessage);

        String errorMessage = rootMessage;

        // хз будет ли работать в других БД, но ничего другого я не нашел
        if (matcher.find()) {
            String field = matcher.group(1);
            String value = matcher.group(2);
            errorMessage = String.format("Пользователь с '%s' = '%s' уже зарегистрирован'", field, value);
        }

        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleException(BadCredentialsException exception) {
        return new ErrorResponse(exception.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
