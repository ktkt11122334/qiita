package com.stock.service.order.create_order;

import java.util.List;
import java.util.Map;

import com.stock.app_utils.OrderShip;
import com.stock.entity.form.order.CreateOrder;



public interface CreateOrderService {

  public void registOrder(CreateOrder createOrder) throws Exception;

  public String shipOrder(OrderShip orderShip);

  public List<Map<String, Object>> list();

}
