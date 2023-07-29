package htw.berlin.de.game_manager.controller;

import htw.berlin.de.game_manager.export.GameNotFoundException;
import htw.berlin.de.material_system.export.CardNotFoundException;
import htw.berlin.de.material_system.export.EmptyPileOfCardsException;
import htw.berlin.de.player_manager.export.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@EnableWebMvc
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> notFoundException(GameNotFoundException ex){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> notFoundException(CardNotFoundException ex){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> notFoundException(PlayerNotFoundException ex){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyPileOfCardsException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> notFoundException(EmptyPileOfCardsException ex){
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> exc(Exception ex){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
