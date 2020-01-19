package com.stock.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.Stock;



public interface StockRepository extends JpaRepository<Stock, Long> {

  // 名前付きクエリーの書き方に注意
  @Query("SELECT stock FROM Stock stock WHERE stock.productMaster.productId = :productId")
  public Stock findOneStockByProductId(@Param("productId")Long product_id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query( "SELECT stock FROM Stock stock WHERE stock.stockId = ?1" )
  Stock findStockByStockIdForUpdate(Long stockId);

}
