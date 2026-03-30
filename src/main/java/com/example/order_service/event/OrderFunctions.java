package com.example.order_service.event;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.order_service.domain.OrderStatus;
import com.example.order_service.service.OrderService;

@Configuration
public class OrderFunctions {
    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    Consumer<OrderDispatchedMessage> dispatchOrder(OrderService orderService) {
        return (orderDispatchedMessage) -> {
            log.info("The order with id {} has been dispatched", orderDispatchedMessage.orderId());
            orderService.updateOrderStatus(orderDispatchedMessage.orderId(), OrderStatus.DISPATCHED);
        };
    }
}
