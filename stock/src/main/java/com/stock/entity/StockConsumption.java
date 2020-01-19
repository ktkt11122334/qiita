package com.stock.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockConsumption {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "stock_consumption_id" )
  private Long stockConsumptionId;


  @ManyToOne( optional = false )
  @JoinColumn( name = "stock_id" )
  private Stock stock;


  @Column( name = "consumption_product_number" )
  private Integer consumptionProductNumber;


  @Column( name = "consumption_date" )
  private LocalDate consumptionDate;


  @OneToOne( optional = false )
  @JoinColumns({
    @JoinColumn( name = "order_id", referencedColumnName = "order_id" ),
    @JoinColumn( name = "order_detail_id", referencedColumnName = "order_detail_id" )
  })
  private OrderDetail orderDetail;


  @ManyToOne( optional = false )
  @JoinColumn( name = "customer_id" )
  private Customer customer;


  @ManyToOne( optional = false )
  @JoinColumn( name = "product_id" )
  private ProductMaster productMaster;


  @Column( name = "create_ts" )
  private LocalDateTime createTs;


  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;

}
