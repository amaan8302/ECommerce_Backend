package com.website.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.website.model.Address;


public interface AddressRepository  extends JpaRepository<Address, Long> {

}
