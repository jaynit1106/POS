package com.increff.pos.pojo;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Getter @Setter
@Table(
		name = "order_pojo"
)
public class OrderPojo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private Instant timestamp;

	@PrePersist
	private void onCreate(){
		Instant it = Instant.now();
		timestamp = it;
	}
	
}
