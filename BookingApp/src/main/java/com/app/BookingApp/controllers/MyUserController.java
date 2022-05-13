package com.app.BookingApp.controllers;

import com.app.BookingApp.services.MyUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class MyUserController {

  private MyUserService userService;

  @Autowired
  public MyUserController(MyUserService userService) {
    this.userService = userService;

  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Object> getUser(@PathVariable("id") Long id) {
    return userService.getUserById(id);
  }

  @PutMapping(path = "update/{userId}")
  public void updateUser(@PathVariable("userId") Long id,
      @RequestParam(required = false) String emailAddress,
      @RequestParam(required = false) String name) {

    userService.updateUser(id, name, emailAddress);

  }

  @DeleteMapping("update/{id}")
  public void deleteExtistingUser(@PathVariable("id") Long userId) {
      userService.deleteExtistingUser(userId);
  }

}
