package kh.edu.istad.platform.order.config;

import kh.edu.istad.platform.order.httpclient.CustomerHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    public final WebClient.Builder webClient;
    @Bean
    public CustomerHttpClient customerHttpClient(){
        WebClient customer = webClient
                .baseUrl("http://customer-service")
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(customer))
                .build();

        return factory.createClient(CustomerHttpClient.class);
    }

}
