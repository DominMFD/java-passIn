package domin.com.passin.services;

import domin.com.passin.domain.attende.Attendee;
import domin.com.passin.domain.attende.exceptions.AttendeeAlreadyExistException;
import domin.com.passin.domain.attende.exceptions.AttendeeNotFoundException;
import domin.com.passin.domain.checkin.CheckIn;
import domin.com.passin.dto.attendee.AttendeBadgeDTO;
import domin.com.passin.dto.attendee.AttendeeDetails;
import domin.com.passin.dto.attendee.AttendeesListResponseDTO;
import domin.com.passin.dto.attendee.BadgeDTO;
import domin.com.passin.repositories.AttendeeRepository;
import domin.com.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
  private final AttendeeRepository attendeeRepository;
  private final CheckInRepository checkInRepository;
  private final CheckInService checkInService;

  public List<Attendee> getAllAttendeesFromEvent(String eventId) {
    return this.attendeeRepository.findByEventId(eventId);
  }

  public AttendeesListResponseDTO getEventsAttendee(String eventId) {
    List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

    List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
      Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
      LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
      return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), checkedInAt);
    }).toList();

    return new AttendeesListResponseDTO(attendeeDetailsList);
  }

  public void verifyAttendeeSubscription(String email, String eventId) {
    Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
    if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered");
  }

  public Attendee registerAttendee(Attendee newAttendee) {
    this.attendeeRepository.save(newAttendee);
    return newAttendee;
  }

  public AttendeBadgeDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
    Attendee attendee = this.getAttendee(attendeeId);

    var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

    BadgeDTO badgeDTO = new BadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
    return new AttendeBadgeDTO(badgeDTO);
  }

  public void checkInAttendee(String attendeeId) {
    Attendee attendee = this.getAttendee(attendeeId);
    this.checkInService.registerCheckIn(attendee);
  }

  private Attendee getAttendee(String attendeeId) {
    return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
  }
}
