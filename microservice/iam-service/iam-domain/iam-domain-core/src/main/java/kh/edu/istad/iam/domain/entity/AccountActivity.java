package kh.edu.istad.iam.domain.entity;

import kh.edu.istad.iam.domain.valueobject.AccountAction;

import java.time.Instant;
import java.util.UUID;

public class AccountActivity {

    private final Long id;
    private final UUID userUuid;
    private final AccountAction action;
    private final String metadata;
    private final String ipAddress;
    private final String device;
    private final Instant createdAt;

    public AccountActivity(Long id, UUID userUuid, AccountAction action, String metadata,
                           String ipAddress, String device, Instant createdAt) {
        this.id = id;
        this.userUuid = userUuid;
        this.action = action;
        this.metadata = metadata;
        this.ipAddress = ipAddress;
        this.device = device;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public UUID getUserUuid() { return userUuid; }
    public AccountAction getAction() { return action; }
    public String getMetadata() { return metadata; }
    public String getIpAddress() { return ipAddress; }
    public String getDevice() { return device; }
    public Instant getCreatedAt() { return createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private UUID userUuid;
        private AccountAction action;
        private String metadata;
        private String ipAddress;
        private String device;
        private Instant createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userUuid(UUID userUuid) { this.userUuid = userUuid; return this; }
        public Builder action(AccountAction action) { this.action = action; return this; }
        public Builder metadata(String metadata) { this.metadata = metadata; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder device(String device) { this.device = device; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public AccountActivity build() {
            return new AccountActivity(id, userUuid, action, metadata, ipAddress, device, createdAt);
        }
    }
}