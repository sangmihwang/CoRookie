package com.fourttttty.corookie.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Stomp를 사용하기 위한 어노테이션
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // "/text-channel/endpoint"는 WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로이다.
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*");
    }

    // 내장된 메세지 브로커를 사용해 Client에게 Subscriptions, Broadcasting 기능을 제공한다.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Client에서 SEND 요청을 처리
        // "/send" 경로로 시작하는 STOMP 메세지의 "destination" 헤더는
        // @Controller 객체의 @MessageMapping 메서드로 라우팅된다.
        registry.setApplicationDestinationPrefixes("/app");


        // 해당 경로로 SimpleBroker 등록한다.
        // SimpleBroker는 해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달하는 간단한 작업을 수행한다.
        // "/topic", "/queue"로 시작하는 "destination" 헤더를 가진 메세지를 브로커로 라우팅한다.
        // 내장된 Simple Message Broker는 /topic, /queue prefix에 대해 특별한 의미를 부여하지 않는다.
        registry.enableSimpleBroker("/topic");


        // SimpleBroker의 기능과
        // 외부 Message Broker(RabbitMQ, ActiveMQ 등)에
        // 메세지를 전달하는 메소드
        // registry.enableStompBrokerRelay()
    }
}
