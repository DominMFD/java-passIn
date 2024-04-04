package domin.com.passin.domain.attende.exceptions;

public class AttendeeNotFoundException extends RuntimeException {
  public AttendeeNotFoundException(String message) {
    super(message);
  }
}
