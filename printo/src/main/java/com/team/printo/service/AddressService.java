package com.team.printo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.stream.Collectors;

import com.team.printo.dto.AddressDTO;
import com.team.printo.dto.Messages;
import com.team.printo.exception.AddressNotFoundException;
import com.team.printo.exception.InsufficientStockException;
import com.team.printo.exception.UserNotFoundException;
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
	            .orElseThrow(() -> new UserNotFoundException());


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
				.orElseThrow(() -> new AddressNotFoundException());
		
	    if (!Objects.equals(existingAddress.getUser().getId(), userId)) {
	        throw new IllegalArgumentException(Messages.ADDRESS_NOT_BELONG_TO_USER);
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
	        .orElseThrow(() -> new AddressNotFoundException());

	    if (!Objects.equals(address.getUser().getId(), userId)) {
	        throw new IllegalArgumentException(Messages.ADDRESS_NOT_BELONG_TO_USER);
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
	        throw new InsufficientStockException(Messages.EMPTY_ADDRESSES);
	    }
	    return addresses.stream().map(addressMapper::toDTO).collect(Collectors.toList());
	}
	
	public AddressDTO getAddressById(Long addressId, Long userId) {
	    Address address = addressRepository.findById(addressId)
		        .orElseThrow(() -> new AddressNotFoundException());
	    if (!Objects.equals(address.getUser().getId(), userId)) {
	        throw new IllegalArgumentException(Messages.ADDRESS_NOT_BELONG_TO_USER);
	    }
	    
	    return addressMapper.toDTO(address);
	}
	
	
	public void deleteAddressById(Long addressId, Long userId) {
	    Address address = addressRepository.findById(addressId)
		        .orElseThrow(() -> new AddressNotFoundException());
	    if (!Objects.equals(address.getUser().getId(), userId)) {
	        throw new IllegalArgumentException(Messages.ADDRESS_NOT_BELONG_TO_USER);
	    }
	    if (address.isDefaultAddress()) {
	        throw new IllegalArgumentException(Messages.DEFAULT_ADDRESS);
	    }
	    addressRepository.delete(address);
	}
}
