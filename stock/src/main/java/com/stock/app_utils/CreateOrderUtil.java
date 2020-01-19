package com.stock.app_utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stock.common_process.converter.ConverterUtil;
import com.stock.entity.Customer;
import com.stock.entity.OrderDetail;
import com.stock.entity.Stock;
import com.stock.entity.form.order.CreateOrder;
import com.stock.entity.form.product_master.SelectProductMasterList;
import com.stock.repository.CustomerRepository;
import com.stock.repository.ProductMasterHistorySettingsRepository;
import com.stock.service.product_master_settings.ProductMasterSettingsService;

public class CreateOrderUtil {


  /**
   * データベースから取得されたList<Map<String, Object>>型に格納される最終更新日時をLocalDateTime型に変換します。
   * @param list
   */
  public static void setLocalDateTimeFormat( List<Map<String, Object>> list, String targetColumnName, String pattern ) {

    list.stream().forEach( listDataMap -> {

      try {
        LocalDateTime productLocalDateTime = ConverterUtil.toLocalDateTime( listDataMap.get(targetColumnName).toString(), pattern );
        listDataMap.put(targetColumnName, productLocalDateTime);
      }
      catch (ParseException e) {
        e.printStackTrace();
      }
    });

  }



  /**
   * すでに存在する既存の顧客情報を取得するメソッド
   */
  public static Customer getCustomerExistence(CreateOrder.Customer customerInfo, CustomerRepository customerRepository) {

    Customer customer = customerRepository.findCustomerByTellNumberAndCustomer_name(
        customerInfo.getCreateTellNumber(), customerInfo.getCreateFirstName(), customerInfo.getCreateLastName()
    );

    return customer;
  }



  /**
   * 税率変更日付前か確認を行うメソッド
   */
  public static List<Map<String, Object>> confirmTaxChangeDate( List<CreateOrder.OrderDetail> order_details, ProductMasterHistorySettingsRepository productHistoryRepository) {

    List<Map<String, Object>> ProductBeforeTaxFromChangeDate = new ArrayList<>();
    order_details.stream().forEach( orderDetail -> {

      Map<String, Object> productHistoryInfo = productHistoryRepository.findOneTaxChangeDateByProductMasterHistory(
          orderDetail.getCreateProductId(),
          orderDetail.getCreateProductLastModifiedTs()
      );

      LocalDate fromChangeDate = LocalDate.parse( productHistoryInfo.get("from_change_date").toString() );
      if ( fromChangeDate.isAfter( LocalDate.now() ) ) {
        ProductBeforeTaxFromChangeDate.add(productHistoryInfo);
      }
    });

    return ProductBeforeTaxFromChangeDate;
  }



  /**
   * redirect時のAttributeの追加設定を行う
   */
  public static void setRedirectAttributeToCreate(CreateOrder createOrder, RedirectAttributes redirectAttribute) {

    redirectAttribute.addFlashAttribute( "orderHeader", createOrder.getOrderHeader() );
    redirectAttribute.addFlashAttribute( "orderDetails", createOrder.getOrderDetails() );
    redirectAttribute.addFlashAttribute( "customer", createOrder.getCustomer() );
  }



  /**
   * 注文作成画面の商品選択一覧を取得する
   */
  public static List<Map<String, Object>> getSelectProductList( SelectProductMasterList selectProductMasterList, ProductMasterSettingsService productMasterSettingsService ) {
    selectProductMasterList.setSearchTaxDisableFlg( Boolean.FALSE );
    return productMasterSettingsService.getProductMasterList( selectProductMasterList );
  }



  /**
   * 在庫確認の処理
   * @return
   */
  public static void checkStock(Map<Long, Boolean> stockLackOrder, List<Map<String, String>> stockLackMsg, Stock stock, OrderDetail orderDetail ) {


      // 実在庫数 < 出荷数の場合に対象の注文データに在庫不足エラーと表示する処理
      if ( stock.getActualStockNumber().compareTo( orderDetail.getPurchaseNumber() ) < 0 ) {

        stockLackMsg.add(
            new HashMap<String, String>() {{
                put( "order_id", orderDetail.getOrderId().toString() );
                put( "product_name", orderDetail.getProductMasterHistory().getProductName() );
                put( "lack_stock_msg", "実在庫数不足エラー" );
            }}
        );


        stockLackOrder.put(orderDetail.getOrderId(), Boolean.TRUE);
      }


  }


}
