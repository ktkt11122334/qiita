package com.stock.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.OrderHeader;



public interface OrderRepository extends JpaRepository<OrderHeader, Long> {

  @Query( value = " SELECT" +
                            " order_header.order_id, "        +
                            " customer.first_name, "          +
                            " customer.last_name, "           +
                            " customer.tell_number, "         +
                            " customer.postal_code, "         +
                            " customer.first_address, "       +
                            " customer.last_address, "        +
                            " order_header.order_price, "     +
                            " order_header.order_status, "    +
                            " order_header.shipping_date, "   +
                            " order_header.create_ts, "       +
                            " order_header.last_modified_ts " +
                  " FROM "       +
                            " order_header "                  +
                  " INNER JOIN " +
                            " customer "                      +
                  " ON "         +
                            "( order_header.customer_id = customer.customer_id) " +
                  " ORDER BY "   +
                            " order_header.order_id DESC ",
  nativeQuery = true)
  public List<Map<String, Object>> getListOrderHeaderWithCustomer();



  @Query( value = " SELECT Count(*) FROM order_header WHERE order_status = 10 ", nativeQuery = true)
  public Integer getUnshippedOrderCount();

  @Query( value = " SELECT * FROM order_header WHERE order_status = 10 ORDER BY order_id ", nativeQuery = true)
  public List<OrderHeader> getUnshippedOrder();

}
