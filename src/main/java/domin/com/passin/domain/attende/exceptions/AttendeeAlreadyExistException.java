package domin.com.passin.domain.attende.exceptions;

public class AttendeeAlreadyExistException extends RuntimeException {
  public AttendeeAlreadyExistException(String message) {
    super(message);
  }
}
