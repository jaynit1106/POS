package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(
		name = "inventory_pojo",
		uniqueConstraints = @UniqueConstraint(columnNames = {"id"})
)
public class InventoryPojo {
	
	@Id
	@Column(nullable = false)
	private int id;

	@Column(nullable = false)
	private int quantity;

}
