package com.sky.address.service.impl;

import com.sky.address.domain.entity.Address;
import com.sky.address.mapper.AddressMapper;
import com.sky.address.service.AddressService;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public void add(Address address) {
        address.setUserId(BaseContext.getCurrentId());
        address.setIsDefault(StatusConstant.DISABLE);
        addressMapper.insert(address);
    }

    @Override
    public List<Address> list() {
        Long userId = BaseContext.getCurrentId();
        Address address = Address.builder().userId(userId).build();
        return addressMapper.list(address);
    }

    @Override
    public Address getDefault() {
        Long userId = BaseContext.getCurrentId();
        Address address = Address.builder().userId(userId).isDefault(StatusConstant.ENABLE).build();
        List<Address> list = addressMapper.list(address);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void update(Address address) {
        addressMapper.update(address);
    }

    @Override
    public void deleteById(Long id) {
        addressMapper.deleteById(id);
    }

    @Override
    public Address getById(Long id) {
        return addressMapper.getById(id);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        Long userId = BaseContext.getCurrentId();
        addressMapper.updateNotDefaultByUserId(userId);
        Address address = Address.builder().id(id).isDefault(StatusConstant.ENABLE).build();
        addressMapper.update(address);
    }
}