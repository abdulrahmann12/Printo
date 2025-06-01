package com.team.printo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.printo.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByOrderId(Long orderId);
}
