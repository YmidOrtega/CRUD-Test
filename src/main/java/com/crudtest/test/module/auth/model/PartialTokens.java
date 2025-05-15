package com.crudtest.test.module.auth.model;

import com.crudtest.test.module.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "partial_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PartialTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 768)
    private String token;
    @Column (name = "created_at")
    private LocalDateTime createdAt;
    @Column (name = "expires_at")
    private LocalDateTime expiresAt;
    private boolean used;
    @Column (name = "ip_address")
    private String ipAddress;
    @Column (name = "user_agent")
    private String userAgent;
    @Version
    private Long version;


}
