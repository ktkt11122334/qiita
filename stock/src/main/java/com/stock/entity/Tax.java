package com.stock.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Getter
@Setter
@ToString
public class Tax implements Serializable {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY )
  @Column( name = "tax_id" )
  private Long taxId;

  @Column( name = "tax_rate" )
  @NotNull( message = "税率を入力してください。" )
  @Range( min = 1, max = 100, message = "{min}から{max}までの数字を入力してください。")
//  @Pattern(regexp = "^\\d{1,3}$", message = "1~3桁の数字のみ入力できます。")  **これだとダメ
  private Byte taxRate;

  @Column( name = "from_change_date" )
  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE )
  @NotNull( message = "税率変更日を入力してください。" )
  private LocalDate fromChangeDate;

  @Column( name = "create_ts" )
  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime createTs;

  @Column( name = "last_modified_ts" )
  @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
  private LocalDateTime lastModifiedTs;

  @Column( name = "disable_flg" )
  private Boolean disableFlg;

}
