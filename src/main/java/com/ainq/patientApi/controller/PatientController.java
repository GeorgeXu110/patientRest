package com.ainq.patientApi.controller;

import com.ainq.patientApi.entity.Patient;
import com.ainq.patientApi.entity.PatientMemberRecord;
import com.ainq.patientApi.log.Timed;
import com.ainq.patientApi.service.PMRServiceImp;
import com.ainq.patientApi.service.PatientServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(value="Patient Resource Management", tags = "Patient Resource Related")
public class PatientController {

    @Autowired
    PatientServiceImp patientServiceImp;

    @Autowired
    PMRServiceImp pmrServiceImp;

    @ApiOperation(value = "Get all Patient entities", response = ResponseEntity.class)
    @GetMapping(value = "/Patients")
    @Timed
    public ResponseEntity getAllPatients() {

        List<Patient> patients = patientServiceImp.findAll();
        log.info("Get All Patients");
        if(patients == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No patient found");
        } else {
            return ResponseEntity.ok(patients);
        }
    }

    @ApiOperation(value = "Get a Patient entity by Id", response = ResponseEntity.class)
    @GetMapping(value = "/Patients/{id}")
    @Timed
    public ResponseEntity getPatientById(@PathVariable Integer id) {
        Patient patient = patientServiceImp.findByEnterpriseId(id);
        log.info("Get Patients with id:" + id, patient);
        if(patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id: " + id + "not found");
        } else {
            return ResponseEntity.ok(patient);
        }
    }

    @ApiOperation(value = "Save a Patient entity to DB", response = ResponseEntity.class)
    @PostMapping( value = "/Patients", consumes = "application/json")
    @Timed
    public ResponseEntity createOrSavePatient(@RequestBody Patient patient) {

        log.info("Save Patient",patient);
        Integer id = patient.getEnterpriseId();
        if (id != null && patientServiceImp.existsById(patient.getEnterpriseId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enterprise Id:" + id + " exists");
        } else {
            Patient newPatient = patientServiceImp.savePatient(patient);
            return ResponseEntity.ok(newPatient);
        }
    }

    @ApiOperation(value = "Delete a Patient entity by Id", response = ResponseEntity.class)
    @DeleteMapping( value = "/Patients/{id}")
    @Timed
    public ResponseEntity deletePatientById(@PathVariable Integer id) {

        log.info("Delete Patient By Id: ",id);
        if(patientServiceImp.deletePatientById(id)) {
            return ResponseEntity.ok("Enterprise Id :" + id + " deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enterprise Id doesn't exist");
        }
    }

    @ApiOperation(value = "Update given Patient entity", response = ResponseEntity.class)
    @PutMapping( value = "/Patients/{id}")
    @Timed
    public ResponseEntity updatePatientById(@PathVariable Integer id, @RequestBody Patient patient) {

        log.info("Update Patient By Id：",id);
        if(patientServiceImp.existsById(id)) {
            Patient updatedPatient = patientServiceImp.updatePatientById(id, patient);
            return ResponseEntity.ok(updatedPatient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching id found: " + id);
        }
    }

    @ApiOperation(value = "Get all PatientMemberRecord entities with given Patient ID", response = ResponseEntity.class)
    @GetMapping(value = "/Patients/{PatientId}/PatientMemberRecords")
    @Timed
    public ResponseEntity getAllPatientMemberRecordsByPatientId(@PathVariable Integer PatientId) {

        List<PatientMemberRecord> pmrs = pmrServiceImp.findByPatientEnterpriseId(PatientId);
        log.info("Get PatientMemberRecords by PatientId:" + PatientId, pmrs);

        if(pmrs == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(pmrs);
        }
    }

}
