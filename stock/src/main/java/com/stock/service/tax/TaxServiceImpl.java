package com.stock.service.tax;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.cnst.exceptions.ExclusionControlException;
import com.stock.cnst.exceptions.messages.ErrorMessages;
import com.stock.entity.Tax;
import com.stock.repository.TaxRepository;



@Service
@Transactional
public class TaxServiceImpl implements TaxService {

  @Autowired
  private TaxRepository taxRepository;



  @Override
  public List<Tax> getTaxList() {
    return taxRepository.findAllByDisableFlgIsFalse();
  }



  @Override
  public Tax findOneTax(Long taxId) {
    Optional<Tax> taxOne = taxRepository.findById(taxId);

    return ( taxOne.isPresent() ) ? taxOne.get() : null;
  }



  @Override
  public Tax registTax(Tax tax) throws Exception {

    // 排他制御
    Tax beforeTax = taxRepository.findTaxByTaxIdForUpdate( tax.getTaxId() );
    if ( beforeTax != null ) {

      if ( !tax.getLastModifiedTs().equals( beforeTax.getLastModifiedTs() ) )
        throw new ExclusionControlException(ErrorMessages.EXCLUSION_CONTROL_ERROR_MSG);
    }

    Tax existingTax = taxRepository.findOneBySameTaxRateAndDisableFlgIsFalse( tax.getTaxRate() );
    if ( existingTax != null ) {
      throw new Exception("既に登録済みの税率です。税率一覧から既存の税率を削除してください。");
    }

    // 更新日時変更
    LocalDateTime now = LocalDateTime.now();
    tax.setCreateTs(now);
    tax.setLastModifiedTs(now);
    tax.setDisableFlg(Boolean.FALSE);

    return taxRepository.save(tax);
  }



  @Override
  public void deleteTax(Long taxId) {
    Tax tax = taxRepository.getOne( taxId ) ;
    tax.setDisableFlg( Boolean.TRUE );
    tax.setLastModifiedTs( LocalDateTime.now() );

    taxRepository.save(tax);
  }

}
