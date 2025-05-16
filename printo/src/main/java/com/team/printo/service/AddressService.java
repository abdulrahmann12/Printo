package com.team.printo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.stream.Collectors;

import com.team.printo.dto.AddressDTO;
import com.team.printo.exception.ResourceNotFoundException;
import com.team.printo.mapper.AddressMapper;
import com.team.printo.model.Address;
import com.team.printo.model.User;
import com.team.printo.repository.AddressRepository;
import com.team.printo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
	
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;
	private final AddressMapper addressMapper;
	
	@Transactional
	public AddressDTO addAddress(Long userId, AddressDTO addressDTO) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));


	    Address address = addressMapper.toEntity(addressDTO);
	    address.setUser(user);
	    Address savedAddress = addressRepository.save(address);
	    
	    long count = addressRepository.countByUserId(userId);

	    if (count == 0) {
	        address.setDefaultAddress(true);
	    }
	    else if (addressDTO.isDefaultAddress()) {
	    	defaultAddress(userId,address.getId());
	    }
	    else {
	        address.setDefaultAddress(false);
	    }

	    addressRepository.save(address);
	    return addressMapper.toDTO(savedAddress);
	}
	
	public AddressDTO updateAddress(Long userId,Long addressId, AddressDTO addressDTO) {
		Address existingAddress = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));
		
	    if (!Objects.equals(existingAddress.getUser().getId(), userId)) {
	        throw new IllegalArgumentException("Address does not belong to this user");
	    }
	    
		existingAddress.setCity(addressDTO.getCity());
		existingAddress.setCountry(addressDTO.getCountry());
		existingAddress.setStreet(addressDTO.getStreet());
		
		if (addressDTO.isDefaultAddress()) {
			defaultAddress(userId,addressId);
	    }
	    else {
	    	existingAddress.setDefaultAddress(false);
	    }
		
		Address savedAddress = addressRepository.save(existingAddress);
		return addressMapper.toDTO(savedAddress);
	}
	
	public void defaultAddress(Long userId, Long addressId) {
	    Address address = addressRepository.findById(addressId)
	        .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

	    if (!Objects.equals(address.getUser().getId(), userId)) {
	        throw new IllegalArgumentException("Address does not belong to this user");
	    }

	    List<Address> userAddresses = addressRepository.findByUserId(userId);
	    for (Address addr : userAddresses) {
	        addr.setDefaultAddress(false);
	    }
	    address.setDefaultAddress(true);

	    addressRepository.saveAll(userAddresses);
	}
	
	public List<AddressDTO> getAllUserAddress(Long userId) {
	    List<Address> addresses = addressRepository.findByUserId(userId);
	    if (addresses.isEmpty()) {
	        throw new ResourceNotFoundException("No addresses found for this user. Please add at least one.");
	    }
	    return addresses.stream().map(addressMapper::toDTO).collect(Collectors.toList());
	}
	
	public AddressDTO getAddressById(Long addressId, Long userId) {
	    Address address = addressRepository.findById(addressId)
		        .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
	    if (!Objects.equals(address.getUser().getId(), userId)) {
	        throw new IllegalArgumentException("Address does not belong to this user");
	    }
	    
	    return addressMapper.toDTO(address);
	}
	
	public void deleteAddressById(Long addressId, Long userId) {
	    Address address = addressRepository.findById(addressId)
		        .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
	    if (!Objects.equals(address.getUser().getId(), userId)) {
	        throw new IllegalArgumentException("Address does not belong to this user");
	    }
	    if (address.isDefaultAddress()) {
	        throw new IllegalArgumentException("Cannot delete default address");
	    }
	    addressRepository.delete(address);
	}
}
