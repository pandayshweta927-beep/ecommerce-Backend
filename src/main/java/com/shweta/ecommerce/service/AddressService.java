package com.shweta.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shweta.ecommerce.dto.AddressDTO;
import com.shweta.ecommerce.entity.Address;
import com.shweta.ecommerce.entity.User;
import com.shweta.ecommerce.repository.AddressRepository;
import com.shweta.ecommerce.repository.UserRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // Add new address
    public Address addAddress(AddressDTO dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If this address is marked as default,
        // remove default status from existing addresses
        if (Boolean.TRUE.equals(dto.getDefaultAddress())) {
            List<Address> existingAddresses = addressRepository.findByUser(user);

            for (Address address : existingAddresses) {
                address.setDefaultAddress(false);
            }

            addressRepository.saveAll(existingAddresses);
        }

        Address address = new Address();

        address.setUser(user);
        address.setName(dto.getName());
        address.setPhone(dto.getPhone());
        address.setHouse(dto.getHouse());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setDefaultAddress(
                Boolean.TRUE.equals(dto.getDefaultAddress()));

        return addressRepository.save(address);
    }

    // Get all addresses of logged-in user
    public List<Address> getMyAddresses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUser(user);
    }

    // Get address by ID
    public Address getAddressById(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "Address not found or you are not authorized to access it");
        }

        return address;
    }

    // Update address
    public Address updateAddress(Long id, AddressDTO dto, String email) {

        Address address = getAddressById(id, email);

        if (Boolean.TRUE.equals(dto.getDefaultAddress())) {
            List<Address> existingAddresses =
                    addressRepository.findByUser(address.getUser());

            for (Address existingAddress : existingAddresses) {
                existingAddress.setDefaultAddress(false);
            }

            addressRepository.saveAll(existingAddresses);
        }

        address.setName(dto.getName());
        address.setPhone(dto.getPhone());
        address.setHouse(dto.getHouse());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setDefaultAddress(
                Boolean.TRUE.equals(dto.getDefaultAddress()));

        return addressRepository.save(address);
    }

    // Delete address
    public void deleteAddress(Long id, String email) {

        Address address = getAddressById(id, email);

        addressRepository.delete(address);
    }

    // Set address as default
    public Address setDefaultAddress(Long id, String email) {

        Address address = getAddressById(id, email);

        List<Address> existingAddresses =
                addressRepository.findByUser(address.getUser());

        for (Address existingAddress : existingAddresses) {
            existingAddress.setDefaultAddress(false);
        }

        addressRepository.saveAll(existingAddresses);

        address.setDefaultAddress(true);

        return addressRepository.save(address);
    }
}