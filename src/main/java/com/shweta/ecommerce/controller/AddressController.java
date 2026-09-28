package com.shweta.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shweta.ecommerce.dto.AddressDTO;
import com.shweta.ecommerce.dto.AddressResponseDTO;
import com.shweta.ecommerce.dto.ResponseDTO;
import com.shweta.ecommerce.entity.Address;
import com.shweta.ecommerce.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<ResponseDTO<AddressResponseDTO>> addAddress(
            @Valid @RequestBody AddressDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        Address address = addressService.addAddress(request, email);

        AddressResponseDTO responseDTO = convertToResponseDTO(address);

        ResponseDTO<AddressResponseDTO> response = new ResponseDTO<>(
                true,
                "Address added successfully",
                responseDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AddressResponseDTO>>> getMyAddresses(
            Authentication authentication) {

        String email = authentication.getName();

        List<Address> addresses = addressService.getMyAddresses(email);

        List<AddressResponseDTO> responseDTOs = addresses.stream()
                .map(this::convertToResponseDTO)
                .toList();

        ResponseDTO<List<AddressResponseDTO>> response = new ResponseDTO<>(
                true,
                "Addresses fetched successfully",
                responseDTOs);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<AddressResponseDTO>> getAddressById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        Address address = addressService.getAddressById(id, email);

        AddressResponseDTO responseDTO = convertToResponseDTO(address);

        ResponseDTO<AddressResponseDTO> response = new ResponseDTO<>(
                true,
                "Address fetched successfully",
                responseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<AddressResponseDTO>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        Address address = addressService.updateAddress(id, request, email);

        AddressResponseDTO responseDTO = convertToResponseDTO(address);

        ResponseDTO<AddressResponseDTO> response = new ResponseDTO<>(
                true,
                "Address updated successfully",
                responseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteAddress(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        addressService.deleteAddress(id, email);

        ResponseDTO<Void> response = new ResponseDTO<>(
                true,
                "Address deleted successfully",
                null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ResponseDTO<AddressResponseDTO>> setDefaultAddress(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        Address address = addressService.setDefaultAddress(id, email);

        AddressResponseDTO responseDTO = convertToResponseDTO(address);

        ResponseDTO<AddressResponseDTO> response = new ResponseDTO<>(
                true,
                "Default address updated successfully",
                responseDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private AddressResponseDTO convertToResponseDTO(Address address) {

        return new AddressResponseDTO(
                address.getId(),
                address.getName(),
                address.getPhone(),
                address.getHouse(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPincode(),
                address.getDefaultAddress());
    }
}