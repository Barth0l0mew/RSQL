package org.itstep;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static
        org.springframework.web.reactive.function.server.RequestPredicates.POST
        ;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.core.publisher.Flux;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.web.reactive.function.BodyExtractors.toDataBuffers;
import static org.springframework.web.reactive.function.BodyExtractors.toFormData;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class FormHandler {

    Mono<ServerResponse> handleLogin(ServerRequest request) {
        return request.body(toFormData())
            .map(MultiValueMap::toSingleValueMap)
            .filter(formData -> "baeldung".equals(formData.get("user")))
            .filter(formData -> "you_know_what_to_do".equals(formData.get("token")))
            .flatMap(formData -> ok().body(Mono.just("welcome back!"), String.class))
            .switchIfEmpty(ServerResponse.badRequest()
                .build());
    }

    Mono<ServerResponse> handleUpload(ServerRequest request) {
        return request.body(toDataBuffers())
            .collectList()
            .flatMap(dataBuffers -> ok().body(fromValue(extractData(dataBuffers).toString())));
    }

    private AtomicLong extractData(List<DataBuffer> dataBuffers) {
        AtomicLong atomicLong = new AtomicLong(0);
        dataBuffers.forEach(d -> atomicLong.addAndGet(d.readableByteCount()));
        return atomicLong;
    }
} 