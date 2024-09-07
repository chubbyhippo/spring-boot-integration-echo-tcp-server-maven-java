package io.github.chubbyhippo.echoserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayCrLfSerializer;

@Configuration
public class TcpServerConfig {
    private static final Logger log = LoggerFactory.getLogger(TcpServerConfig.class);

    private static final int PORT = 1234;

    @Bean
    public TcpNetServerConnectionFactory serverFactory() {
        TcpNetServerConnectionFactory serverFactory = new TcpNetServerConnectionFactory(PORT);
        serverFactory.setDeserializer(new ByteArrayCrLfSerializer());
        serverFactory.setSerializer(new ByteArrayCrLfSerializer());
        return serverFactory;
    }


    @Bean
    public IntegrationFlow tcpServerFlow() {
        return IntegrationFlow
                .from(Tcp.inboundGateway(serverFactory()))
                .handle((payload, headers) -> {
                    String message = new String((byte[]) payload);
                    log.info("Received: {}", message);
                    return payload;
                })
                .get();
    }
}
