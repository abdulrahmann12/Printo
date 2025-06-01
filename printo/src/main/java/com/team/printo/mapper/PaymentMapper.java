package com.team.printo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.team.printo.dto.PaymentDTO;
import com.team.printo.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentDTO toDTO(Payment payment);

    @Mapping(target = "order.id", source = "orderId")
    Payment toEntity(PaymentDTO paymentDTO);
}
