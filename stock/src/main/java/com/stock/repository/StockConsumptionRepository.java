package com.stock.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.StockConsumption;



public interface StockConsumptionRepository extends JpaRepository<StockConsumption, Long> {

  @Query(
    value =
        "SELECT " +
                      " sc.order_id, "                     +
                      " sc.consumption_date, "             +
                      " sc.consumption_product_number, "   +
                      " pmh.product_name, "                 +
                      " pm.product_code "                  +
        "FROM "       +
                      " stock_consumption sc "             +
        "INNER JOIN " +
                      " order_detail od "                  +
        "ON "         +
                      " (sc.order_id = od.order_id && sc.order_detail_id = od.order_detail_id) "                  +
        "INNER JOIN " +
                      " product_master_history pmh "                                                              +
        "ON "         +
                      " (od.product_id = pmh.product_id && od.product_last_modified_ts = pmh.last_modified_ts) "  +
        "INNER JOIN " +
                      " product_master pm "                                                                       +
        "ON "         +
                      " (sc.product_id = pm.product_id) " +
        "ORDER BY "   +
                      " sc.order_id "
     , nativeQuery = true
  )
  public List<Map<String, Object>> findAllWithProductInfo();
}
