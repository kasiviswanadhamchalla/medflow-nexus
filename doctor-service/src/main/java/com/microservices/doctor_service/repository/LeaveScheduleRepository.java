package com.microservices.doctor_service.repository;

import com.microservices.doctor_service.entity.LeaveSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveScheduleRepository extends JpaRepository<LeaveSchedule, Long> {
    List<LeaveSchedule> findByDoctorId(Long doctorId);
}
