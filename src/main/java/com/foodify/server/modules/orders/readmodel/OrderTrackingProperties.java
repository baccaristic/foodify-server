package com.foodify.server.modules.orders.readmodel;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.orders.tracking")
public class OrderTrackingProperties {

    private boolean enabled = false;
    private Duration ttl = Duration.ofHours(24);
    private String keyPrefix = "orders:tracking:";
    private KafkaConsumerProperties kafka = new KafkaConsumerProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public KafkaConsumerProperties getKafka() {
        return kafka;
    }

    public void setKafka(KafkaConsumerProperties kafka) {
        this.kafka = kafka != null ? kafka : new KafkaConsumerProperties();
    }

    public static class KafkaConsumerProperties {

        private boolean enabled = false;
        private String groupId = "order-tracking-projection";
        private int concurrency = 1;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public int getConcurrency() {
            return concurrency;
        }

        public void setConcurrency(int concurrency) {
            this.concurrency = concurrency > 0 ? concurrency : 1;
        }
    }
}
