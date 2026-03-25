package com.lld.api_gateway.rateLimiter.entity;



import com.lld.api_gateway.rateLimiter.modal.UserRateLimitId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDateTime;

@Entity
@IdClass(UserRateLimitId.class)
public class UserRateLimit {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "request_at")
    private LocalDateTime requestAt;

    public UserRateLimit() {}

    public UserRateLimit(String userId, LocalDateTime requestAt) {
        this.userId = userId;
        this.requestAt = requestAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getRequestAt() {
        return requestAt;
    }

    public void setRequestAt(LocalDateTime requestAt) {
        this.requestAt = requestAt;
    }
}

