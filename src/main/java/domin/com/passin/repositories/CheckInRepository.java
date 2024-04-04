package domin.com.passin.repositories;

import java.util.Optional;

import domin.com.passin.domain.checkin.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer>{
  Optional<CheckIn> findByAttendeeId(String attendeeId);
}
