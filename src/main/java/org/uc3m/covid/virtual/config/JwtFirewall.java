package org.uc3m.covid.virtual.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.GenericFilterBean;
import org.uc3m.covid.virtual.entity.User;
import org.uc3m.covid.virtual.exception.JwtMissmatchException;
import org.uc3m.covid.virtual.repository.UserRepository;
import org.uc3m.covid.virtual.service.TokenService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtFirewall extends GenericFilterBean {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    @Override
    public void destroy() {
    }

    public JwtFirewall(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String bearerTokenJwt = req.getHeader("Authorization");

        try {
            Claims claims = this.tokenService.parseJWT(bearerTokenJwt);
            long uc3mId = Long.parseLong(claims.getId());
            request.setAttribute("uc3mId", uc3mId);

            Collection<? extends GrantedAuthority> authorities
                    = Stream.of("ROLE_USER") // <- we don't need roles yet, we are ready. If you set claims, above getId() won't work.
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());


            UsernamePasswordAuthenticationToken t
                    = new UsernamePasswordAuthenticationToken(null, claims.getIssuer(), authorities);
            SecurityContextHolder.getContext().setAuthentication(t);
            Optional<User> optUser = this.userRepository.findByUc3mId(uc3mId);
            if (optUser.isPresent() && !optUser.get().getJwt().equals(bearerTokenJwt)) {
                throw new JwtMissmatchException();
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "NO_AUTHORIZATION");
        }
    }

}