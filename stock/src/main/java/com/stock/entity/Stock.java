package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class Stock implements Serializable {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "stock_id" )
  private Long stockId;

  @OneToOne
  @JoinColumn( name = "product_id", referencedColumnName = "product_id" )
  private ProductMaster productMaster;

  @Column( name = "actual_stock_number" )
  private Integer actualStockNumber;

  @Column( name = "future_shipped_stock_number" )
  private Integer futureShippedStockNumber;

  @Column( name = "standard_stock_number" )
  private Integer standardStockNumber;

  @Column( name = "create_ts" )
  private LocalDateTime createTs;

  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;


}
