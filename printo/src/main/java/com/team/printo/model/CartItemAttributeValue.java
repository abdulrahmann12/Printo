package com.team.printo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "cart_item_attribute_values")
@NoArgsConstructor
@AllArgsConstructor
public class CartItemAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;

    @ManyToOne
    @JoinColumn(name = "attribute_value_id", nullable = false)
    private AttributeValue attributeValue;
}
