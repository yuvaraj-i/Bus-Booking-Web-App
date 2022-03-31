package com.app.BookingApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Home {

    @RequestMapping("/greeting")
    public String home(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model){
        model.addAttribute("name", name);

		return "greeting.jsp";
    }
    
    @RequestMapping("/")
    public String hello(){
        return "home.jsp";
    }
}
