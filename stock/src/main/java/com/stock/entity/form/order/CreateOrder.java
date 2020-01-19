package com.stock.entity.form.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.stock.common_process.web.SpringContext;
import com.stock.common_process.web.WebUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
public class CreateOrder {

  @Valid
  private Order orderHeader;

  @Valid
  private List<OrderDetail> orderDetails;

  @Valid
  private Customer customer;

  public CreateOrder() {
    this.orderHeader = new Order();
    this.orderDetails = new ArrayList<OrderDetail>();
    this.customer = new Customer();

    // 登録フォームで送信されてくる購入商品情報分だけorderDetailsの要素を追加する
    WebUtil webUtil = SpringContext.getBean(WebUtil.class);
    Map<String, String[]> requestParamMap = WebUtil.getRequestBodyMap( webUtil.getRequestServlet() );
    for ( int i = 0; ; i++ ) {

      String paramProductId = "orderDetails[" + i + "].createProductId";
      if ( !requestParamMap.containsKey(paramProductId) )
        break;

      this.orderDetails.add( new OrderDetail() );
    }

  }



  @Getter
  @Setter
  public class Order {

    @Range( min = 1, max = 1000000, message = "商品の購入合計金額がゼロです。購入商品を設定してください。")
    private Integer createOrderPrice = 0;

  }

  @Getter
  @Setter
  public class OrderDetail {

    private Long createProductId;

    private String createProductName;

    private String createProductCode;

    private Integer createTotalPrice;

    @Range( min = 1, max = 1000, message = "購入数を設定してください。一度に{max}個まで購入可能です。")
    private Integer createPurchaseNumber;
    // createPurchaseNumber:エラーメッセージ表示用の変数
    private Boolean createPurchaseNumberErr = Boolean.FALSE;

    // 可変幅を扱う場合のフォーマットパターン
    //@DateTimeFormat( pattern = "yyyy-MM-ddTHH:mm:ss[.SSS][.SS][.S]" )
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime createProductLastModifiedTs;


  }

  @Getter
  @Setter
  public class Customer {

    @NotBlank( message = "顧客名(姓)を入力してください。" )
    private String createFirstName;

    @NotBlank( message = "顧客名(名)を入力してください。" )
    private String createLastName;

    @NotBlank( message = "電話番号を入力してください。" )
    @Pattern( regexp = "^[0-9]+$", message="電話番号はハイフン・半角数字のみで入力してください。" )
    private String createTellNumber;

    @NotBlank( message = "郵便番号を入力してください。" )
    @Pattern( regexp = "^[0-9]+$", message="郵便番号はハイフン・半角数字のみで入力してください。" )
    private String createPostalCode;

    @NotBlank( message = "住所(都道府県-区/市-丁/番/号)を入力してください。" )
    private String createFirstAddress;

    @NotBlank( message = "住所(アパート/マンション)を入力してください。" )
    private String createLastAddress;

    @NotBlank( message = "メールアドレスを入力してください。" )
    @Email( message = "メールアドレスの形式で入力してください。")
    private String createMailAddress;

  }

}
