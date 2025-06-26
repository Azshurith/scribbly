package com.devitrax.scribbly.unit.security;

import com.devitrax.scribbly.model.User;
import com.devitrax.scribbly.repository.UserRepository;
import com.devitrax.scribbly.security.UserExistenceFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UserExistenceFilterTest {

    private UserRepository userRepo;
    private UserExistenceFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setup() {
        userRepo = mock(UserRepository.class);
        filter = new UserExistenceFilter(userRepo);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAllowPublicPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldProceedWhenUserExists() throws Exception {
        when(request.getRequestURI()).thenReturn("/post/list");
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(new User()));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("john", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldRedirectWhenUserDoesNotExist() throws Exception {
        when(request.getRequestURI()).thenReturn("/post/list");
        when(userRepo.findByUsername("john")).thenReturn(Optional.empty());

        HttpSession session = mock(HttpSession.class);
        when(request.getSession(false)).thenReturn(session);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            "john", "password", List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filter.doFilter(request, response, chain);

        verify(request).getSession(false);
        verify(session).invalidate();
        verify(response).sendRedirect("/login?forceLogout=true");
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void shouldSkipWhenNoAuthentication() throws Exception {
        when(request.getRequestURI()).thenReturn("/post/list");

        SecurityContextHolder.clearContext();

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipWhenAnonymousUser() throws Exception {
        when(request.getRequestURI()).thenReturn("/post/list");

        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
            "key", "anonymousUser", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}
