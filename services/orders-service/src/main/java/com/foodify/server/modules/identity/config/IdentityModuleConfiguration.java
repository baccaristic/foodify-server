package com.foodify.server.modules.identity.config;

import com.foodify.server.modules.auth.application.SmsSender;
import com.foodify.server.modules.auth.security.JwtService;
import com.foodify.server.modules.identity.application.IdentityAuthService;
import com.foodify.server.modules.identity.application.LocalIdentityAuthService;
import com.foodify.server.modules.identity.application.RemoteIdentityAuthService;
import com.foodify.server.modules.identity.repository.ClientRepository;
import com.foodify.server.modules.identity.repository.PhoneSignupSessionRepository;
import com.foodify.server.modules.identity.repository.UserRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({IdentityServiceProperties.class, IdentityTokenProperties.class})
public class IdentityModuleConfiguration {

    @Bean
    @ConditionalOnProperty(name = "identity.service.mode", havingValue = "remote")
    public RestClient identityRestClient(ObjectProvider<RestClient.Builder> builderProvider,
                                         IdentityServiceProperties properties) {
        RestClient.Builder builder = builderProvider.getIfAvailable(RestClient::builder);
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) properties.getRemote().getConnectTimeout().toMillis());
        factory.setReadTimeout((int) properties.getRemote().getReadTimeout().toMillis());
        return builder
                .baseUrl(properties.getRemote().getBaseUrl())
                .requestFactory(factory)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "identity.service.mode", havingValue = "remote")
    public IdentityAuthService remoteIdentityAuthService(RestClient identityRestClient) {
        return new RemoteIdentityAuthService(identityRestClient);
    }

    @Bean
    @ConditionalOnMissingBean(IdentityAuthService.class)
    public IdentityAuthService localIdentityAuthService(UserRepository userRepository,
                                                        ClientRepository clientRepository,
                                                        PhoneSignupSessionRepository phoneSignupSessionRepository,
                                                        PasswordEncoder passwordEncoder,
                                                        JwtService jwtService,
                                                        SmsSender smsSender) {
        return new LocalIdentityAuthService(userRepository, clientRepository, phoneSignupSessionRepository,
                passwordEncoder, jwtService, smsSender);
    }

    @Bean(initMethod = "migrate")
    @ConditionalOnProperty(prefix = "identity.service.schema", name = "managed", havingValue = "true")
    public Flyway identityFlyway(DataSource dataSource, IdentityServiceProperties properties) {
        IdentityServiceProperties.Schema schema = properties.getSchema();
        return Flyway.configure()
                .dataSource(dataSource)
                .locations(schema.getMigrationLocation())
                .schemas(schema.getName())
                .baselineOnMigrate(schema.isBaselineOnMigrate())
                .table(schema.getHistoryTable())
                .load();
    }

}
