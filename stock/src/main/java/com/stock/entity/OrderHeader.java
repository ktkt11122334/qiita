package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class OrderHeader implements Serializable {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "order_id" )
  private Long orderId;

  @ManyToOne( optional = false )
  @JoinColumn( name = "customer_id" )
  private Customer customer;

  @Column( name = "order_price" )
  private Integer orderPrice;

  @Column( name = "order_status" )
  private Byte orderStatus;

  @Column( name = "shipping_date" )
  private LocalDate shippingDate;

  @Column( name = "create_ts" )
  private LocalDateTime createTs;

  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;

}
