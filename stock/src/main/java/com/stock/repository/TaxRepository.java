package com.stock.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.stock.entity.Tax;


/**
 *
 * @author tadakeisuke
 * 税率マスタの設定に使用
 * 商品マスタドロップダウンリスト取得に使用
 *
 */
public interface TaxRepository extends JpaRepository<Tax, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query( "SELECT tax FROM Tax tax WHERE tax.taxId = ?1" )
  Tax findTaxByTaxIdForUpdate(Long taxId);

  @Query( "SELECT tax FROM Tax tax WHERE tax.taxRate = ?1 AND tax.disableFlg = 0" )
  public Tax findOneBySameTaxRateAndDisableFlgIsFalse(Byte taxRate);

  @Query( "SELECT tax FROM Tax tax WHERE tax.disableFlg = 0" )
  public List<Tax> findAllByDisableFlgIsFalse();

}
