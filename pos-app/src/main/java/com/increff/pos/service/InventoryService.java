package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.JSONUTil;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

	@Autowired
	private  InventoryDao inventoryDao;

	public void addInventory(InventoryPojo inventoryPojo) throws ApiException {
		if(inventoryPojo.getQuantity()<0)throw new ApiException("Quantity cannot be Negative");


		InventoryPojo existingInventory = inventoryDao.select(inventoryPojo.getId(),InventoryPojo.class);
		if(!Objects.isNull(existingInventory)) {
			existingInventory.setQuantity(existingInventory.getQuantity()+inventoryPojo.getQuantity());
			return;
		}

		inventoryDao.insert(inventoryPojo);
	}

	public InventoryPojo getInventoryById(int inventoryId) throws ApiException {

		InventoryPojo inventoryPojo = inventoryDao.select(inventoryId,InventoryPojo.class);
		if(Objects.isNull(inventoryPojo))throw new ApiException("Inventory Does Not Exists");
		return inventoryPojo;
	}

	public List<InventoryPojo> getAllInventorys() {
		return inventoryDao.selectAll(InventoryPojo.class);
	}

	public void updateInventory(int id, InventoryPojo p) throws ApiException {
		if(p.getQuantity()<0)throw new ApiException("Quantity cannot be Negative");
		InventoryPojo ex = getInventoryById(id);
		ex.setQuantity(p.getQuantity());
		inventoryDao.update(ex);
	}

	@Transactional(rollbackOn = ApiException.class)
	public void checkAndCreateOrder(List<OrderItemPojo> itemPojoList) throws ApiException {
		List<JSONObject> errors = new ArrayList<>();
		for(OrderItemPojo orderItemPojo : itemPojoList) {
			if(orderItemPojo.getQuantity()<=0){
				errors.add(JSONUTil.getJSONObject(orderItemPojo.getBarcode(),"Please Enter a Valid Quantity for "+orderItemPojo.getBarcode()));
				continue;
			}
			if(orderItemPojo.getSellingPrice()<0) {
				errors.add(JSONUTil.getJSONObject(orderItemPojo.getBarcode(),"Please Enter a Valid Price for "+orderItemPojo.getBarcode()));
				continue;
			}
			try {
				InventoryPojo inventoryPojo = getInventoryById(orderItemPojo.getProductId());
				if(inventoryPojo.getQuantity()<orderItemPojo.getQuantity()){
					errors.add(JSONUTil.getJSONObject(orderItemPojo.getBarcode(),"Only "+inventoryPojo.getQuantity()+" pieces left for " + orderItemPojo.getBarcode()));
					continue;
				}
				inventoryPojo.setQuantity(inventoryPojo.getQuantity()-orderItemPojo.getQuantity());
			}catch (ApiException e){
				errors.add(JSONUTil.getJSONObject(orderItemPojo.getBarcode(),"Inventory does not exists for " + orderItemPojo.getBarcode()));
			}
		}
		if(errors.size()>0){
			throw new ApiException(JSONValue.toJSONString(errors));
		}
	}
}
