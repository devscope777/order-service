package com.example.order_service.web;

import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.domain.Order;
import com.example.order_service.service.OrderService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getAllOrders(@AuthenticationPrincipal Jwt jwt) {
        return orderService.getAllOrders(jwt.getSubject());
    }

    @PostMapping
    public Mono<Order> submitOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid OrderRequest orderRequest) {
        Mono<Order> order = orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
        order.subscribe(value -> {
            log.info("Order {} submiited by {} ", value, jwt.getClaims().get("given_name"));
        });
        return order;
    }

}
