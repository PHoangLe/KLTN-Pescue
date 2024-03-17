package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.Address;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.AddressRepository;
import com.project.pescueshop.repository.UserRepository;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDAO extends BaseDAO{
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public void saveAndFlushAddress(Address address){
        addressRepository.saveAndFlush(address);
    }

    public void saveAndFlushUser(User user){
        userRepository.saveAndFlush(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<Address> findAddressByUserId(String userId){
        return addressRepository.findAddressByUserId(userId);
    }

    public Address findAddressById(String addressId){
        return addressRepository.findById(addressId).orElse(null);
    }

    public void deleteAddress(String addressId) throws FriendlyException {
        Address address = findAddressById(addressId);
        if (address == null){
            throw new FriendlyException(EnumResponseCode.ADDRESS_NOT_FOUND);
        }

        address.setStatus(EnumStatus.DELETED.getValue());
        saveAndFlushAddress(address);
    }

    public User findUserByUserId(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
