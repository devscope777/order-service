package com.example.order_service.service;

import org.springframework.stereotype.Service;

import com.example.order_service.book.Book;
import com.example.order_service.book.BookClient;
import com.example.order_service.domain.Order;
import com.example.order_service.domain.OrderRepository;
import com.example.order_service.domain.OrderStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    public OrderService(BookClient bookClient, OrderRepository orderRepository) {
        this.bookClient = bookClient;
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String isbn, int qty) {
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, qty))
                .defaultIfEmpty(buildRejectedOrder(isbn, qty))
                .flatMap(orderRepository::save);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.build(book.isbn(), book.title() + " - " + book.author(), book.price(), quantity,
                OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.build(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }

}
