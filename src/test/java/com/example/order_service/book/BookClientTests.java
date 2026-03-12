package com.example.order_service.book;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BookClientTests {

    @Test
    public void whenBookExistsThenReturnBook() throws IOException {
        // Arrange
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();
        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString()).build();

        BookClient bookClient = new BookClient(webClient);

        var bookIsbn = "1234567890";

        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(String.format("""
                        {
                        "isbn": %s,
                        "title": "Title",
                        "author": "Author",
                        "price": 9.90,
                        "publisher": "Polarsophia"
                        }
                        """, bookIsbn));

        mockWebServer.enqueue(mockResponse);

        // Act
        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        // Assert
        StepVerifier.create(book).expectNextMatches(b -> b.isbn().equals(bookIsbn)).verifyComplete();

        // Clean up
        mockWebServer.shutdown();
        mockWebServer.close();
    }
}
