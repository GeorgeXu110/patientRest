package com.ainq.patientApi.controller;

import com.ainq.patientApi.entity.Address;
import com.ainq.patientApi.log.Timed;
import com.ainq.patientApi.service.AddressServiceImp;
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
@Api(value="Address Resource Management", tags = "Address Resource Related")
public class AddressController {

    @Autowired
    AddressServiceImp addressServiceImp;

    @ApiOperation(value = "Get all Address entities", response = ResponseEntity.class)
    @GetMapping(value = "/Addresses")
    @Timed
    public ResponseEntity getAllAddresses() {

        List<Address> addresses = addressServiceImp.findAll();
        log.info("Get All Addresses");
        if(addresses == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Address found");
        } else {
            return ResponseEntity.ok(addresses);
        }
    }


    @ApiOperation(value = "Get Address entity by given Id", response = ResponseEntity.class)
    @GetMapping(value = "/Addresses/{id}")
    @Timed
    public ResponseEntity getAddressById(@PathVariable Integer id) {
        Address address = addressServiceImp.findById(id);
        log.info("Get Address with id:" + id, address);
        if(address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address with id: " + id + "not found");
        } else {
            return ResponseEntity.ok(address);
        }
    }

    @ApiOperation(value = "Save an Address to DB", response = ResponseEntity.class)
    @PostMapping( value = "/Addresses", consumes = "application/json")
    @Timed
    public ResponseEntity createOrSaveAddress(@RequestBody Address address) {

        log.info("Save Address",address);
        Integer id = address.getAddressId();
        if (id != null && addressServiceImp.existsById(address.getAddressId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Address Id exists");
        } else {
            Address newAdd = addressServiceImp.saveAddress(address);
            return ResponseEntity.ok(newAdd);
        }
    }

    @ApiOperation(value = "Delete an Address entity by Id", response = ResponseEntity.class)
    @DeleteMapping( value = "/Addresses/{id}")
    @Timed
    public ResponseEntity deleteAddressById(@PathVariable Integer id) {

        log.info("Delete Address By Id: ",id);
        if(addressServiceImp.deleteAddressById(id)) {
            return ResponseEntity.ok("Delete Address :" + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching id found: " + id);
        }
    }

    @ApiOperation(value = "Update given Address entity", response = ResponseEntity.class)
    @PutMapping( value = "/Addresses/{id}")
    @Timed
    public ResponseEntity updateAddressById(@PathVariable Integer id, @RequestBody Address address) {

        log.info("Update Address By Id：",id);
        if(addressServiceImp.existsById(id)) {
            Address updatedAddr = addressServiceImp.updateAddressById(id, address);
            return ResponseEntity.ok(updatedAddr);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No matching id found: " + id);
        }
    }


}
