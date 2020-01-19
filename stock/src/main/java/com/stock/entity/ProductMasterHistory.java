package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProductMasterHistoryId.class)
public class ProductMasterHistory implements Serializable {

//  @EmbeddedId
//  private ProductMasterHistoryId productMasterHistoryId;
//
//  @ManyToOne
//  @JoinColumn( name = "product_id", insertable = false, updatable = false )
//  @MapsId("productId")
//  private ProductMaster productMaster;

  @Id
  @Column( name = "product_id" )
  private Long productId;

  @Id
  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;

  @Column( name = "product_name" )
  private String productName;

  @Column( name = "price" )
  private Integer price;

  @ManyToOne( optional = false )
  @JoinColumn( name = "tax_id" )
  private Tax tax;

  @Column( name = "create_ts" )
  private LocalDateTime createTs;

}
