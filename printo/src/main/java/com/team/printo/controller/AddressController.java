package com.team.printo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.printo.dto.AddressDTO;
import com.team.printo.dto.BasicResponse;
import com.team.printo.dto.Messages;
import com.team.printo.model.User;
import com.team.printo.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
	
	private final AddressService addressService;
	
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AddressDTO> addAddress(@AuthenticationPrincipal UserDetails userDetails,@Valid @RequestBody AddressDTO addressDTO){
		Long userId = ((User) userDetails).getId();
		AddressDTO savedAddress = addressService.addAddress(userId, addressDTO);
		return ResponseEntity.ok(savedAddress);
	}
	
	@PutMapping("/{addressId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AddressDTO> updateAddress(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long addressId,@Valid @RequestBody AddressDTO addressDTO){
		Long userId = ((User) userDetails).getId();
		AddressDTO updateAddress = addressService.updateAddress(userId,addressId, addressDTO);
		return ResponseEntity.ok(updateAddress);
	}
	
	@PutMapping("/default/{addressId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> setDefaultAddress(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long addressId){
		Long userId = ((User) userDetails).getId();
		addressService.defaultAddress(userId,addressId);
		return ResponseEntity.ok(new BasicResponse(Messages.UPDATE_ADDRESS));
	}
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<List<AddressDTO>> getUserAddresses(@AuthenticationPrincipal UserDetails userDetails){
		Long userId = ((User) userDetails).getId();
		List<AddressDTO> addresses = addressService.getAllUserAddress(userId);
		return ResponseEntity.ok(addresses);
	}
	
	@GetMapping("/{addressId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<AddressDTO> getAddress(
			@PathVariable Long addressId,
			@AuthenticationPrincipal UserDetails userDetails
			){
		Long userId = ((User) userDetails).getId();
		AddressDTO address = addressService.getAddressById(addressId,userId);
		return ResponseEntity.ok(address);
	}
	
	@DeleteMapping("/{addressId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<BasicResponse> deleteAddress(
			@PathVariable Long addressId,
			@AuthenticationPrincipal UserDetails userDetails
			){
	    	Long userId = ((User) userDetails).getId();
	        addressService.deleteAddressById(addressId,userId);
	        return ResponseEntity.ok(new BasicResponse(Messages.DELETE_ADDRESS));
	}

}
