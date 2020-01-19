package com.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.OrderDetail;
import com.stock.entity.OrderDetailId;



public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

  @Query("SELECT od FROM OrderDetail od WHERE od.orderId = ?1 ")
  public List<OrderDetail> getOrderDetailByOrderId(Long orderId);

}
