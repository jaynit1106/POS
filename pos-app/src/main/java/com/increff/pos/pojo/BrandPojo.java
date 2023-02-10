package com.increff.pos.pojo;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
		name = "brand_pojo",
		uniqueConstraints = @UniqueConstraint(columnNames = {"brand","category"})
)
@Getter @Setter
public class BrandPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  int id;

	@Column(nullable = false)
	private String brand;

	@Column(nullable = false)
	private String category;

}
