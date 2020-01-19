package com.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.stock.service.menu.MenuService;



@Controller
@RequestMapping("menu")
public class MenuController {

  @Autowired
  private MenuService menuService;



  @RequestMapping( method = RequestMethod.GET )
  public String menuList(Model model) {
    model.addAttribute("menus", menuService.getMenuList());

    return "menu";
  }

}
