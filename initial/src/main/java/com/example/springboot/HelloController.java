package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
//import javax.validation.Valid;

import java.util.*;
@RestController
public class HelloController {

	@Autowired
	Matcher matcher;

	@GetMapping("/")
	public String index() {
		return "Greetings from the trading app!";
	}

	@GetMapping("/buy")
	public ArrayList<Order> buy() {
		return matcher.getBuy();
	}

	@GetMapping("/sell")
	public ArrayList<Order> sell() {
		return matcher.getSell();
	}

	@GetMapping("/order")
	public ArrayList[] order() {
		return matcher.getOrders();
	}

	@PostMapping("/order")
	public ArrayList[] order(@RequestBody String account, int price, int quantity, String action) {
		matcher.newOrder(account, price, quantity, action);
		return matcher.getOrders();
	}
}
