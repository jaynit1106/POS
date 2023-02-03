package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.JSONUTil;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

	@Autowired
	private final InventoryDao dao = new InventoryDao();

	public void add(InventoryPojo p) throws ApiException {
		if(p.getQuantity()<0)throw new ApiException("Quantity cannot be Negative");
		InventoryPojo inventory = dao.select(p.getId(),InventoryPojo.class);
		if(!Objects.isNull(inventory)) {
			inventory.setQuantity(inventory.getQuantity()+p.getQuantity());
			update(inventory.getId(), inventory);
			return;
		}
		dao.insert(p);
	}

	public InventoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	public List<InventoryPojo> getAll() {
		return dao.selectAll(InventoryPojo.class);
	}

	public void update(int id, InventoryPojo p) throws ApiException {
		if(p.getQuantity()<0)throw new ApiException("Quantity cannot be Negative");
		InventoryPojo ex = getCheck(id);
		ex.setQuantity(p.getQuantity());
		dao.update(ex);
	}

	public InventoryPojo getCheck(int id) throws ApiException{
		InventoryPojo p = dao.select(id,InventoryPojo.class);
		if(Objects.isNull(p))throw new ApiException("Inventory Does Not Exists");
		return p;
	}
	
	public List<JSONObject> checkAndCreateOrder(List<OrderItemPojo> items) throws ApiException {
		List<JSONObject> errors = new ArrayList<>();
		for(OrderItemPojo p : items) {
			if(p.getQuantity()<=0){
				errors.add(JSONUTil.getJSONObject(p.getBarcode(),"Please Enter a Valid Quantity for "+p.getBarcode()));
				continue;
			}
			if(p.getSellingPrice()<0) {
				errors.add(JSONUTil.getJSONObject(p.getBarcode(),"Please Enter a Valid Price for "+p.getBarcode()));
				continue;
			}
			InventoryPojo inventory;

			try {
				inventory = getCheck(p.getProductId());
			}catch (ApiException e){
				errors.add(JSONUTil.getJSONObject(p.getBarcode(),"Inventory does not exists for " + p.getBarcode()));
				continue;
			}

			int quantity = inventory.getQuantity();
			if(quantity<p.getQuantity()){
				errors.add(JSONUTil.getJSONObject(p.getBarcode(),"Only "+quantity+" pieces left of " + p.getBarcode()));
				continue;
			}

			quantity=quantity-p.getQuantity();
			inventory.setQuantity(quantity);
			update(inventory.getId(), inventory);
		}
		return errors;
	}
	
	
	
	
}
