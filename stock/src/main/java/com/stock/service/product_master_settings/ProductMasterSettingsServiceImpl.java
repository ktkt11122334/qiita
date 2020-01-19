package com.stock.service.product_master_settings;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.cnst.exceptions.ExclusionControlException;
import com.stock.cnst.exceptions.messages.ErrorMessages;
import com.stock.entity.ProductMaster;
import com.stock.entity.ProductMasterHistory;
import com.stock.entity.Stock;
import com.stock.entity.form.product_master.SaveProductMaster;
import com.stock.entity.form.product_master.SelectProductMasterList;
import com.stock.repository.ProductMasterHistorySettingsRepository;
import com.stock.repository.ProductMasterSettingsRepository;
import com.stock.repository.StockRepository;
import com.stock.repository.TaxRepository;



@Service
@Transactional
public class ProductMasterSettingsServiceImpl implements ProductMasterSettingsService {

  @Autowired
  private ProductMasterSettingsRepository productMasterSettingsRepository;

  @Autowired
  private ProductMasterHistorySettingsRepository productMasterHistorySettingsHistory;

  @Autowired
  private TaxRepository taxRepository;

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;


  @Override
  public ProductMaster getOneProductMaster(Long productId) {
    Optional<ProductMaster> productOne = productMasterSettingsRepository.findById(productId);

    return ( productOne.isPresent() ) ? productOne.get() : null;
  }



  @Override
  public List<Map<String, Object>> getProductMasterList(SelectProductMasterList selectProductMasterList) {
    MapSqlParameterSource paramMap = new MapSqlParameterSource();
    String sql = getSelectProductListSQL(selectProductMasterList, paramMap);
    return jdbcTemplate.queryForList(sql, paramMap);
  }

  private static String getSelectProductListSQL(SelectProductMasterList searchParam, MapSqlParameterSource paramMap) {


    StringBuilder sql = new StringBuilder();

    sql.append(" SELECT "                                                                                       );
    sql.append(               " pm.product_id, "                                                                );
    sql.append(               " pm.product_code, "                                                              );
    sql.append(               " pm.jancode, "                                                                   );
    sql.append(               " pmh.product_name, "                                                             );
    sql.append(               " pmh.price, "                                                                    );
    sql.append(               " tax.tax_rate, "                                                                 );
    sql.append(               " tax.from_change_date, "                                                         );
    sql.append(               " tax.disable_flg AS tax_disable_flg, "                                           );
    sql.append(               " pm.create_ts, "                                                                 );
    sql.append(               " pm.last_modified_ts, "                                                          );
    sql.append(               " pm.disable_flg "                                                                );
    sql.append(" FROM "                                                                                         );
    sql.append(               " product_master pm "                                                             );
    sql.append(" INNER JOIN "                                                                                   );
    sql.append(               " product_master_history pmh"                                                     );
    sql.append(" ON "                                                                                           );
    sql.append(               " (pm.product_id=pmh.product_id AND pm.last_modified_ts=pmh.last_modified_ts) "   );
    sql.append(" INNER JOIN "                                                                                   );
    sql.append(               " tax "                                                                           );
    sql.append(" ON "                                                                                           );
    sql.append(               " (pmh.tax_id=tax.tax_id) "                                                       );
    sql.append(" WHERE "                                                                                        );
    sql.append(               " pm.disable_flg = :disableFlg "                                                  );



    StringBuilder whereSql = new StringBuilder();

    if ( searchParam.getSearchDisableFlg() ) {
      paramMap.addValue( "disableFlg", 1 );
    }
    else {
      paramMap.addValue( "disableFlg", 0 );
    }

    if ( searchParam.getSearchProductCode() != null && !"".equals( searchParam.getSearchProductCode() ) ) {
      whereSql.append(" AND pm.product_code LIKE :productCode ");
      paramMap.addValue( "productCode", "%" + searchParam.getSearchProductCode() + "%" );
    }

    if ( searchParam.getSearchJancode() != null && !"".equals( searchParam.getSearchJancode() ) ) {
      whereSql.append(" AND pm.product_code LIKE :jancode ");
      paramMap.addValue( "jancode", "%" + searchParam.getSearchJancode() + "%" );
    }

    if ( searchParam.getSearchProductName() != null && !"".equals( searchParam.getSearchProductName() ) ) {
      whereSql.append(" AND pmh.product_name LIKE :productName ");
      paramMap.addValue( "productName", "%" + searchParam.getSearchProductName() + "%" );
    }

    if ( searchParam.getSearchTaxRate() != null ) {
      whereSql.append(" AND tax.tax_rate = :taxRate ");
      paramMap.addValue( "taxRate", searchParam.getSearchTaxRate() );
    }

    if ( searchParam.getSearchTaxDisableFlg() != null ) {

      whereSql.append(" AND tax.disable_flg = :taxDisableFlg ");
      if ( searchParam.getSearchTaxDisableFlg() ) {
        paramMap.addValue( "taxDisableFlg", 1 );
      }
      else {
        paramMap.addValue( "taxDisableFlg", 0 );
      }
    }


    sql.append( whereSql.toString() );


    sql.append(" ORDER BY "                                                                                     );
    sql.append(               " pmh.last_modified_ts DESC "                                                     );

    return sql.toString();

  }



  @Override
  public ProductMasterHistory findOneProductMasterHistory(Long productId, LocalDateTime lastModifiedTs) {
    return productMasterHistorySettingsHistory.findOneProductMasterHistory(productId, lastModifiedTs);
  }


  @Override
  public List<Map<String, Object>> registProduct(SaveProductMaster productMaster) throws Exception {

    // 排他制御
    ProductMaster beforeProduct = productMasterSettingsRepository.findProductMasterByProductIdForUpdate(productMaster.getProductId());
    if ( beforeProduct != null ) {

      if ( !productMaster.getProductMasterLastModifiedTs().equals( beforeProduct.getLastModifiedTs() ) )
        throw new ExclusionControlException(ErrorMessages.EXCLUSION_CONTROL_ERROR_MSG);
    }

    /**
     * 未更新時の入力チェックを行う 下記の処理では、pmh.lastModifiedTsが必ず最新になるため、未更新でもデータが挿入されてしまう
     */

    // 更新処理
    LocalDateTime nowTimestamp = LocalDateTime.now();

    // 商品マスタ
    ProductMaster pm = new ProductMaster();
    pm.setProductId( productMaster.getProductId() );
    pm.setProductCode( productMaster.getProductCode() );
    pm.setJancode( productMaster.getJancode() );
    pm.setDisableFlg( productMaster.getDisableFlg() );
    pm.setCreateTs(
      ( productMaster.getProductMasterCreateTs() == null ) ? nowTimestamp : productMaster.getProductMasterCreateTs()
    );
    pm.setLastModifiedTs( nowTimestamp );

    productMasterSettingsRepository.save(pm);


    // 商品明細
    ProductMasterHistory pmh = new ProductMasterHistory();
    pmh.setProductId( pm.getProductId() );
    pmh.setProductName( productMaster.getProductName() );
    pmh.setPrice( productMaster.getPrice() );
    pmh.setTax( taxRepository.getOne( productMaster.getTaxId() ) );
    pmh.setCreateTs( nowTimestamp );
    pmh.setLastModifiedTs( nowTimestamp );

    productMasterHistorySettingsHistory.save(pmh);


    // 在庫データの初期設定をおこなう
    Stock stock = stockRepository.findOneStockByProductId( productMaster.getProductId() );
    if ( stock == null ) {

      stock = new Stock();
      stock.setProductMaster( pm );
      stock.setActualStockNumber( Integer.valueOf(0) );
      stock.setFutureShippedStockNumber( Integer.valueOf(0) );
      stock.setStandardStockNumber( Integer.valueOf(0) );
      stock.setCreateTs( nowTimestamp );
      stock.setLastModifiedTs( nowTimestamp );
      stockRepository.save(stock);
    }


    return getProductMasterList( new SelectProductMasterList() );
  }


}
