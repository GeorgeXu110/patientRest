package com.ainq.patientApi.repository;

import com.ainq.patientApi.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Patient findByEnterpriseId(Integer id);
}
