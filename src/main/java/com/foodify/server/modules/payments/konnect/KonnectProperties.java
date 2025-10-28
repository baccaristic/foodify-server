package com.foodify.server.modules.payments.konnect;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@ConfigurationProperties(prefix = "konnect")
public class KonnectProperties {

    private boolean enabled = true;
    /**
     * Either "sandbox" or "production". Defaults to sandbox.
     */
    private String environment = "sandbox";
    private EnvironmentConfiguration sandbox = new EnvironmentConfiguration();
    private EnvironmentConfiguration production = new EnvironmentConfiguration();
    private PaymentOptions payment = new PaymentOptions();

    public EnvironmentConfiguration getActiveEnvironment() {
        String selected = environment == null ? "sandbox" : environment.toLowerCase(Locale.ROOT);
        EnvironmentConfiguration resolved = "production".equals(selected) ? production : sandbox;
        if (!StringUtils.hasText(resolved.getName())) {
            resolved.setName("production".equals(selected) ? "production" : "sandbox");
        }
        return resolved;
    }

    @Getter
    @Setter
    public static class EnvironmentConfiguration {
        private String name;
        private String baseUrl;
        private String apiKey;
        private String receiverWalletId;
        private String webhook;

        public boolean isReady() {
            return StringUtils.hasText(baseUrl)
                    && StringUtils.hasText(apiKey)
                    && StringUtils.hasText(receiverWalletId);
        }
    }

    @Getter
    @Setter
    public static class PaymentOptions {
        private String token = "TND";
        private String type = "immediate";
        private List<String> acceptedMethods = new ArrayList<>();
        private Integer lifespanMinutes = 60;
        private Boolean checkoutForm = Boolean.TRUE;
        private Boolean addPaymentFeesToAmount = Boolean.FALSE;
        private String theme = "light";
        private Integer amountScale = 1000;
        private String descriptionTemplate = "Foodify order #{orderId}";

        public List<String> getAcceptedMethods() {
            if (CollectionUtils.isEmpty(acceptedMethods)) {
                return List.of();
            }
            return acceptedMethods;
        }
    }
}
