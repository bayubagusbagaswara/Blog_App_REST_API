package com.blog.rest.api.config;

import com.blog.rest.api.security.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class SpringSecurityAuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        // ambil authentication
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // cek jika authentication kosong, maka balikan data kosong
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // jika authentication ada datanya, maka ambil data dari UserPrincipal
        UserPrincipal userPrincipal = (UserPrincipal) Objects.requireNonNull(authentication).getPrincipal();// balikan data username dari userPrincipal

        return Optional.ofNullable(userPrincipal.getUsername());
    }
}
