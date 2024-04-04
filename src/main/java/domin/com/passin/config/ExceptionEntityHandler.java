package domin.com.passin.config;

import domin.com.passin.domain.attende.exceptions.AttendeeAlreadyExistException;
import domin.com.passin.domain.attende.exceptions.AttendeeNotFoundException;
import domin.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import domin.com.passin.domain.event.exceptions.EventFullException;
import domin.com.passin.domain.event.exceptions.EventNotFoundException;
import domin.com.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {
  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity handleEventNotFound(EventNotFoundException exception) {

    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(EventFullException.class)
  public ResponseEntity<ErrorResponseDTO> handleEventFull(EventFullException exception) {

    return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
  }

  @ExceptionHandler(AttendeeNotFoundException.class)
  public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception) {

    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(AttendeeAlreadyExistException.class)
  public ResponseEntity handleAttendeeAlreadyExist(AttendeeNotFoundException exception) {

    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(CheckInAlreadyExistsException.class)
  public ResponseEntity handleCheckInAlreadyExists(CheckInAlreadyExistsException exception) {

    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
}
