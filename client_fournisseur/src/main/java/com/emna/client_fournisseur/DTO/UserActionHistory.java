package com.emna.client_fournisseur.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserActionHistory {
        private String username;
        private String action;
        private String endpoint;
        private String method;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;


    public UserActionHistory(String username, String action, String endpoint, String method) {
        this.username = username;
        this.action = action;
        this.endpoint = endpoint;
        this.method = method;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getEndpoint() { return endpoint; }
    public String getMethod() { return method; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
