package one.digitalinnovation.parking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<List<Map<String,String>>> errorsHandler(ResponseStatusException e) {
        Map<String,String> error =  new LinkedHashMap<String,String>(){{put("message",e.getReason());}};
        return ResponseEntity.status(e.getStatus()).body(List.of(error));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Map<String,String>> errorsHandler(MethodArgumentNotValidException e) {
        return  e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> new LinkedHashMap<String,String>(){{put("message",err.getDefaultMessage());}})
                .collect(Collectors.toList());
    }
}
