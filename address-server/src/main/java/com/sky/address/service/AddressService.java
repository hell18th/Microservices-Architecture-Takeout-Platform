package com.sky.address.service;

import com.sky.address.domain.entity.Address;

import java.util.List;

public interface AddressService {
    void add(Address address);

    List<Address> list();

    Address getDefault();

    void update(Address address);

    void deleteById(Long id);

    Address getById(Long id);

    void setDefault(Long id);
}