package com.microservice.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

//	rerouting paths of microservice so if we hit localhost:8072/microservice/accounts/create
//	it will redirect it to localhost:8080/create from that particular service
	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/microservice/accounts/**") //gateway path predicate
						.filters(f -> f
								.rewritePath("/microservice/accounts/(?<segment>.*)", "/${segment}")//rerouting path
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config
										.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS")) //telling for which service it should. lb://ACCOUNTS -> loadbalancer:service
				.route(p -> p
						.path("/microservice/cards/**")
						.filters(f -> f
								.rewritePath("/microservice/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
//								.requestRateLimiter(config -> config
//										.setRateLimiter(redisRateLimiter())
//										.setKeyResolver(keyResolver()))
						)
						.uri("lb://CARDS"))
				.route(p -> p
						.path("/microservice/loans/**")
						.filters(f -> f
								.rewritePath("/microservice/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig
										.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(100), 2, true)))
						.uri("lb://LOANS"))
				.build();
	}


	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}


//	rate limiter from redis
	@Bean
	public RedisRateLimiter redisRateLimiter(){
		return new RedisRateLimiter(1, 1, 1);
	}


	@Bean
	KeyResolver keyResolver(){
		return  exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}

}
