package com.emna.micro_service1.service.impl;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;


import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private String username;
    private String password;  // You may store the password if needed for further use
    private Collection<? extends GrantedAuthority> authorities;  // Authorities can be added later if needed

    // Constructor
    public UserDetailsImpl(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

