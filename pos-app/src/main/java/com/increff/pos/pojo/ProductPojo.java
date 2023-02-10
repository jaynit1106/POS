package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(
		name = "product_pojo",
		uniqueConstraints = @UniqueConstraint(columnNames = {"brandId","name"})
)
public class ProductPojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false,unique = true)
	private String barcode;

	@Column(nullable = false)
	private int brandId;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private double mrp;

}
