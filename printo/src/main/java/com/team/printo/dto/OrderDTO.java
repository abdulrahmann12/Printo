package com.team.printo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.printo.model.Order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderDTO {

	
	private long id;

    @Positive(message = "Total amount must be positive")
    private BigDecimal totalPrice;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm a")
    private LocalDateTime createdAt;

    @NotBlank(message = "Status is required")
    private Order.OrderStatus status;

    private UserDTO userDTO;

    private AddressDTO addressDTO;

    private List<OrderItemDTO> orderItems;
}
