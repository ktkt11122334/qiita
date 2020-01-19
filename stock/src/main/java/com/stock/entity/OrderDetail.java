package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OrderDetailId.class)
public class OrderDetail implements Serializable {

  @Id
  @Column( name = "order_id" )
  private Long orderId;

  @Id
  @Column( name = "order_detail_id" )
  private Long orderDetailId;

  @Column( name = "purchase_number" )
  private Integer purchaseNumber;

  @ManyToOne( optional = false )
  @JoinColumns({
    @JoinColumn( name = "product_id", referencedColumnName = "product_id" ),
    @JoinColumn( name = "product_last_modified_ts", referencedColumnName = "last_modified_ts" )
  })
  private ProductMasterHistory productMasterHistory;

  @Column( name = "create_ts" )
  private LocalDateTime createTs;

  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;

}
