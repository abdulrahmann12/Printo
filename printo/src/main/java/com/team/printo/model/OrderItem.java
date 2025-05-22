package com.team.printo.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@ManyToOne
	@JoinColumn(name = "product_id",nullable = false)
	private Product product;
	
	private int quantity;
	
	private BigDecimal price;
	
	@ManyToOne
	@JoinColumn(name = "order_id",nullable = false)
	@JsonIgnore
	private Order order;
	
    @OneToOne
    @JoinColumn(name = "design_id")
    private Design design;
    
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItemAttributeValue> attributeValues = new ArrayList<>();
    
	public OrderItem(Order order, Product product, int quantity, BigDecimal price,Design design) {
	    this.order = order;
	    this.product = product;
	    this.quantity = quantity;
	    this.price = price;
	    this.design = design;
	}
}
