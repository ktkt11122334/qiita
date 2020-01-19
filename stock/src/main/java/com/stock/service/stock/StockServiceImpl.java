package com.stock.service.stock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stock.cnst.exceptions.ExclusionControlException;
import com.stock.cnst.exceptions.messages.ErrorMessages;
import com.stock.entity.Stock;
import com.stock.entity.form.stock_master.SaveStock;
import com.stock.entity.form.stock_master.SelectProductStockList;
import com.stock.repository.ProductMasterSettingsRepository;
import com.stock.repository.StockRepository;



@Service
@Transactional
public class StockServiceImpl implements StockService {

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private ProductMasterSettingsRepository productMasterSettingsRepository;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;


  @Override
  public List<Stock> getStockList() {
    return stockRepository.findAll();
  }

  @Override
  public Stock findOneStockByProductId(Long product_id) {
    return stockRepository.findOneStockByProductId(product_id);
  }

  @Override
  public List<Map<String, Object>> findProductStockList(SelectProductStockList selectProductStockList) {
    MapSqlParameterSource paramMap = new MapSqlParameterSource();
    String sql = getSlectProductStockListSQL(selectProductStockList, paramMap);
    return jdbcTemplate.queryForList(sql, paramMap);
  }


  private String getSlectProductStockListSQL(SelectProductStockList searchParam, MapSqlParameterSource paramMap) {


    StringBuilder sql = new StringBuilder();

    sql.append(" SELECT "                                                                                       );
    sql.append(               " pm.product_id, "                                                                );
    sql.append(               " pm.product_code, "                                                              );
    sql.append(               " pmh.product_name, "                                                             );
    sql.append(               " stock.actual_stock_number, "                                                    );
    sql.append(               " stock.future_shipped_stock_number, "                                            );
    sql.append(               " stock.standard_stock_number, "                                                  );
    sql.append(               " stock.create_ts, "                                                              );
    sql.append(               " stock.last_modified_ts "                                                        );
    sql.append(" FROM "                                                                                         );
    sql.append(               " product_master pm "                                                             );
    sql.append(" INNER JOIN "                                                                                   );
    sql.append(               " product_master_history pmh"                                                     );
    sql.append(" ON "                                                                                           );
    sql.append(               " (pm.product_id=pmh.product_id AND pm.last_modified_ts=pmh.last_modified_ts) "   );
    sql.append(" LEFT OUTER JOIN "                                                                                   );
    sql.append(               " stock"                                                                          );
    sql.append(" ON "                                                                                           );
    sql.append(               " (pm.product_id=stock.product_id) "                                              );
    sql.append(" WHERE "                                                                                        );
    sql.append(               " pm.disable_flg = 0 "                                                            );

    StringBuilder whereSql = new StringBuilder();

    if ( searchParam.getSearchProductCode() != null && !"".equals( searchParam.getSearchProductCode() ) ) {
      whereSql.append(" AND pm.product_code LIKE :productCode ");
      paramMap.addValue( "productCode", "%" + searchParam.getSearchProductCode() + "%" );
    }

    if ( searchParam.getSearchProductName() != null && !"".equals( searchParam.getSearchProductName() ) ) {
      whereSql.append(" AND pmh.product_name LIKE :productName ");
      paramMap.addValue( "productName", "%" + searchParam.getSearchProductName() + "%" );
    }

    sql.append( whereSql.toString() );


    sql.append(" ORDER BY "                                                                                     );
    sql.append(               " stock.last_modified_ts DESC "                                                      );

    return sql.toString();
  }



  @Override
  public List<Map<String, Object>> registStock(SaveStock saveStock) throws Exception {

    // 排他制御
    Stock beforeStock = stockRepository.findStockByStockIdForUpdate(saveStock.getStockId());
    if ( beforeStock != null ) {

      if ( !saveStock.getLastModifiedTs().equals( beforeStock.getLastModifiedTs() ) )
        throw new ExclusionControlException(ErrorMessages.EXCLUSION_CONTROL_ERROR_MSG);
    }


    // 更新処理
    LocalDateTime nowTimestamp = LocalDateTime.now();

    // 在庫登録
    Stock stock = new Stock();
    stock.setStockId( saveStock.getStockId() );
    stock.setProductMaster( productMasterSettingsRepository.getOne( saveStock.getProductId() ) );
    stock.setActualStockNumber( ( saveStock.getActualStockNumber() !=  null ) ? saveStock.getActualStockNumber() : 0 );
    stock.setFutureShippedStockNumber( ( saveStock.getFutureShippedStockNumber() != null ) ? saveStock.getFutureShippedStockNumber() : 0 );
    stock.setStandardStockNumber( ( saveStock.getStandardStockNumber() != null ) ? saveStock.getStandardStockNumber() : 0 );
    stock.setCreateTs( saveStock.getCreateTs() );
    stock.setLastModifiedTs( nowTimestamp );
    stockRepository.save(stock);


    return findProductStockList( new SelectProductStockList() );
  }

}
