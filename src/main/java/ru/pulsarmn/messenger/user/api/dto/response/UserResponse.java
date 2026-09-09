package ru.pulsarmn.messenger.user.api.dto.response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


public record UserResponse(UUID id,
                           String username,
                           String passwordHash,
                           String phoneNumber,
                           String displayName,
                           LocalDate birthdate,
                           Instant createdAt,
                           Instant updatedAt) {

    public static class Builder {
        private UUID id;
        private String username;
        private String passwordHash;
        private String phoneNumber;
        private String displayName;
        private LocalDate birthdate;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder birthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(id, username, passwordHash, phoneNumber, displayName, birthdate, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
