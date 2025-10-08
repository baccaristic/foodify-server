package com.foodify.server.modules.identity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "identity.service")
public class IdentityServiceProperties {

    public enum Mode {
        MONOLITH,
        REMOTE
    }

    private Mode mode = Mode.MONOLITH;
    private final Remote remote = new Remote();
    private final Schema schema = new Schema();

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Remote getRemote() {
        return remote;
    }

    public Schema getSchema() {
        return schema;
    }

    public static class Remote {
        private String baseUrl = "http://localhost:8085";
        private Duration connectTimeout = Duration.ofSeconds(2);
        private Duration readTimeout = Duration.ofSeconds(5);

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public Duration getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Duration getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }
    }

    public static class Schema {
        private boolean managed = false;
        private String name = "identity";
        private String migrationLocation = "classpath:db/identity/migration";
        private String historyTable = "flyway_identity_history";
        private boolean baselineOnMigrate = true;

        public boolean isManaged() {
            return managed;
        }

        public void setManaged(boolean managed) {
            this.managed = managed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMigrationLocation() {
            return migrationLocation;
        }

        public void setMigrationLocation(String migrationLocation) {
            this.migrationLocation = migrationLocation;
        }

        public String getHistoryTable() {
            return historyTable;
        }

        public void setHistoryTable(String historyTable) {
            this.historyTable = historyTable;
        }

        public boolean isBaselineOnMigrate() {
            return baselineOnMigrate;
        }

        public void setBaselineOnMigrate(boolean baselineOnMigrate) {
            this.baselineOnMigrate = baselineOnMigrate;
        }
    }
}
