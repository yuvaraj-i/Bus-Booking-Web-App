package com.app.BookingApp.controllers;

import javax.servlet.http.HttpServletRequest;

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

  @GetMapping(path = "/details")
  public ResponseEntity<Object> getUser(HttpServletRequest request) {
    return userService.getUserById(request);
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

  @GetMapping("/roles")
  public ResponseEntity<Object> getUserRoles(HttpServletRequest request) {
    return userService.getUserRoles(request);
  }
}
