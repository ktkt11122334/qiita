package com.stock.app_utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.stock.cnst.ColumnConst;
import com.stock.entity.OrderDetail;
import com.stock.entity.OrderHeader;
import com.stock.entity.Stock;
import com.stock.entity.StockConsumption;
import com.stock.repository.OrderDetailRepository;
import com.stock.repository.OrderRepository;
import com.stock.repository.StockConsumptionRepository;
import com.stock.repository.StockRepository;



public class OrderShip extends Thread {

  private PlatformTransactionManager transactionManager;

  private OrderRepository orderRepository;
  private OrderDetailRepository orderDetailRepository;
  private StockRepository stockRepository;
  private StockConsumptionRepository stockConsumptionRepository;



  public void setRepository (PlatformTransactionManager transactionManager, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, StockRepository stockRepository, StockConsumptionRepository stockConsumptionRepository) {

    this.transactionManager = transactionManager;

    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.stockRepository = stockRepository;
    this.stockConsumptionRepository = stockConsumptionRepository;
  }



  @Override
  public void run() {


    // transaction開始
    TransactionStatus  transactionStatus = transactionManager.getTransaction( new DefaultTransactionDefinition() );
    try {


      // 在庫不足時のエラーメッセージを格納するマップ
      List<Map<String, String>> stockLackMsg = new ArrayList<>();


      List<OrderHeader> unshippedOrders = orderRepository.getUnshippedOrder();
      unshippedOrders.stream().forEach( orderHeader -> {



        // 在庫不足の注文情報を判断するマップ
        Map<Long, Boolean> stockLackOrder = new HashMap<Long, Boolean>() {{
          put( orderHeader.getOrderId(), Boolean.FALSE );
        }};
        // 注文明細番号に紐づく在庫情報のマップ
        Map<Long, Stock> orderDetailIdToStockMap = new HashMap<>();


        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailByOrderId( orderHeader.getOrderId() );
        orderDetails.forEach( orderDetail -> {

          // 在庫不足の確認を行う
          Stock stock = stockRepository.findOneStockByProductId( orderDetail.getProductMasterHistory().getProductId() );
          CreateOrderUtil.checkStock(stockLackOrder, stockLackMsg, stock, orderDetail);


          orderDetailIdToStockMap.put(orderDetail.getOrderDetailId(), stock);
        });



        // 在庫不足が含まれる注文情報は処理しない
        if ( stockLackOrder.get( orderHeader.getOrderId() ).booleanValue() )
          return;




        // 日付変更値を取得
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = LocalDate.now();

        orderDetails.forEach( orderDetail -> {


          Stock stock = orderDetailIdToStockMap.get( orderDetail.getOrderDetailId() );

          // 在庫テーブル更新
          stock.setActualStockNumber( Integer.valueOf(
              stock.getActualStockNumber().intValue() - orderDetail.getPurchaseNumber().intValue()
          ) );
          stock.setFutureShippedStockNumber( Integer.valueOf(
              stock.getFutureShippedStockNumber().intValue() - orderDetail.getPurchaseNumber().intValue()
          ) );
          stock.setLastModifiedTs(now);
          stockRepository.save(stock);





          // 在庫消費テーブル
          StockConsumption stockConsumption = new StockConsumption();
          stockConsumption.setStock(stock);
          stockConsumption.setOrderDetail(orderDetail);
          stockConsumption.setCustomer(orderHeader.getCustomer());
          stockConsumption.setProductMaster(stock.getProductMaster());
          stockConsumption.setConsumptionProductNumber(orderDetail.getPurchaseNumber());
          stockConsumption.setConsumptionDate(nowDate);
          stockConsumption.setCreateTs(now);
          stockConsumption.setLastModifiedTs(now);
          stockConsumptionRepository.save(stockConsumption);


        });



        // 在庫が存在すれば、関連するテーブルの値を更新する
        // 注文テーブル
        orderHeader.setOrderStatus(ColumnConst.ORDER_STATUS_20_SHIPPED);
        orderHeader.setLastModifiedTs(now);
        orderHeader.setShippingDate(nowDate);
        orderRepository.save(orderHeader);


      });





      // 在庫不足・未設定をメール本文出力
      StringBuilder sb = new StringBuilder();
      sb.append("出荷処理が完了しました。\r\n");
      stockLackMsg.forEach(stockLack -> {

          sb.append( "受注Id:" );
          sb.append( stockLack.get("order_id" ) );
          sb.append( "　" );
          sb.append( "商品名:" );
          sb.append( stockLack.get("product_name" ) );
          sb.append( "　" );
          sb.append( stockLack.get("lack_stock_msg" ) );
          sb.append( "\r\n" );
      });


      // メール処理を記述
      // System.out.println(sb);
      sendMail(sb.toString());



      transactionManager.commit(transactionStatus);
    }
    catch( Exception e ) {

      transactionManager.rollback(transactionStatus);
      sendMail("*ロールバック 出荷指示処理でエラーが発生したため、処理が中断されました。: " + e.getMessage() );
      e.printStackTrace();
    }



  }



  private void sendMail(String message) {


    MailSender.MailBuilder sendMail = new MailSender.MailBuilder();
    sendMail.setTo("");        // to mail address
    sendMail.setFrom("");      // from mail address
    sendMail.setUserName("");  // user mail address
    sendMail.setPassword("");  // smtp pass
    sendMail.setSmtpHost("");  // smtp host
    sendMail.setSmtpPort("");  // smtp port
    sendMail.build().sendMail("出荷指示報告", message);

  }


}
