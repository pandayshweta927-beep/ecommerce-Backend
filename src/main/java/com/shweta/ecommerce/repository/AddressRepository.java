package com.shweta.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shweta.ecommerce.entity.Address;
import com.shweta.ecommerce.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);

    List<Address> findByUserAndDefaultAddressTrue(User user);
}
