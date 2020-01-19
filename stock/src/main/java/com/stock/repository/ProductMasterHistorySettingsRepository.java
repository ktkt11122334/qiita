package com.stock.repository;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.ProductMasterHistory;
import com.stock.entity.ProductMasterHistoryId;



public interface ProductMasterHistorySettingsRepository extends JpaRepository<ProductMasterHistory, ProductMasterHistoryId> {

  @Query("SELECT pmh FROM ProductMasterHistory pmh WHERE pmh.productId = :productId AND pmh.lastModifiedTs = :lastModifiedTs ")
  public ProductMasterHistory findOneProductMasterHistory( @Param("productId")Long productId, @Param("lastModifiedTs")LocalDateTime lastModifiedTs );

  @Query( value = "SELECT pmh.product_name, tax.from_change_date FROM product_master_history pmh INNER JOIN  tax ON (pmh.tax_id = tax.tax_id) WHERE pmh.product_id = :productId AND pmh.last_modified_ts = :lastModifiedTs ", nativeQuery = true)
  public Map<String, Object> findOneTaxChangeDateByProductMasterHistory(@Param("productId")Long productId, @Param("lastModifiedTs")LocalDateTime lastModifiedTs );

}
