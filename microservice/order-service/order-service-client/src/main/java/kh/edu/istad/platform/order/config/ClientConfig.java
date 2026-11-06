package kh.edu.istad.platform.order.config;

import kh.edu.istad.platform.customer.proto.CustomerGrpcServiceGrpc;
import kh.edu.istad.platform.order.exception.DownStreamServiceException;
import kh.edu.istad.platform.order.httpclient.CustomerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.support.WebClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.HttpServiceGroup;
import org.springframework.web.service.registry.ImportHttpServices;
import io.grpc.Channel;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration

@ImportHttpServices(
        group = "customer-service",
        types = {CustomerHttpClient.class},
        clientType = HttpServiceGroup.ClientType.WEB_CLIENT)
//@RequiredArgsConstructor
public class ClientConfig {

    @Bean
    public CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub customerGrpcStub(GrpcChannelFactory channelFactory) {
        // Creates the channel named "customer-service" from application.yaml
        Channel channel = channelFactory.createChannel("customer-service");
        return CustomerGrpcServiceGrpc.newBlockingStub(channel);
    }
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
//            ReactiveClientRegistrationRepository clientRegistrationRepository,
//            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                        .authorizationCode()
//                        .refreshToken()
//                        .clientCredentials()
//                        .build();
//
//        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultReactiveOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }


//    @Bean
//    OAuth2WebClientHttpServiceGroupConfigurer securityConfigurer(
//            ReactiveOAuth2AuthorizedClientManager manager) {
//        return OAuth2WebClientHttpServiceGroupConfigurer.from(manager);
//    }

//    public final WebClient.Builder webClient;
//    @Bean
//    public CustomerHttpClient customerHttpClient(){
//        return createClient("http://customer-service",CustomerHttpClient.class);
//    }
//    @Bean
//    public BusinessHttpClient businessHttpClient(){
//        return createClient("https://business-service",BusinessHttpClient.class);
//    }
//
//    private <T> T createClient(String baseUrl, Class<T> type){
//        WebClient wc = webClient
//                .baseUrl(baseUrl)
//                .build();
//
//        return createClient(wc,type);
//    }
//
//    private <T> T createClient(WebClient wc, Class<T> type){
//
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory
//                .builderFor(WebClientAdapter.create(wc))
//                .build();
//        return factory.createClient(type);
//    }

    @Bean
    public WebClientHttpServiceGroupConfigurer webClientHttpServiceGroupConfigurer(){
        return  groups -> {
            groups.filterByName("customer-service")
                    .forEachClient(
                            (group, clientBuilder) -> {
                                clientBuilder.defaultHeader("User-Agent", "Web Client")
                                        .defaultStatusHandler(HttpStatusCode::isError,
                                                clientResponse -> {
                                                   return clientResponse.createException().map(
                                                           ex -> new DownStreamServiceException(
                                                                   "Customer-Service" , ex.getMessage()
                                                           )
                                                   );
                                                });
                            }
                    );
        };
    }

}
