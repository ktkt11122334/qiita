package com.stock.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.ProductMaster;



public interface ProductMasterSettingsRepository extends JpaRepository<ProductMaster, Long>, JpaSpecificationExecutor<ProductMaster> {


  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query( "SELECT pm FROM ProductMaster pm WHERE pm.productId = ?1" )
  ProductMaster findProductMasterByProductIdForUpdate(Long productId);

}
