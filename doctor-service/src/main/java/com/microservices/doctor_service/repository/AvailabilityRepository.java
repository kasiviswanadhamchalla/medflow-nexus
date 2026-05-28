package com.microservices.doctor_service.repository;

import com.microservices.doctor_service.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByDoctorId(Long doctorId);
}
