package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "customer_id" )
  private Long customerId;

  @Column( name = "postal_code" )
  private String postalCode;

  @Column( name = "first_name" )
  private String firstName;

  @Column( name = "last_name" )
  private String lastName;

  @Column( name = "tell_number" )
  private String tellNumber;

  @Column( name = "first_address" )
  private String firstAddress;

  @Column( name = "last_address" )
  private String lastAddress;

  @Column( name = "mail_address" )
  private String mailAddress;

  @Column( name = "create_ts" )
  private LocalDateTime createTs;

  @Column( name = "last_modified_ts" )
  private LocalDateTime lastModifiedTs;


}
