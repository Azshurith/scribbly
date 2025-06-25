package com.devitrax.scribbly.security;

import com.devitrax.scribbly.repository.UserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Filter that checks if the currently authenticated user still exists in the database.
 * If the user no longer exists, the session is invalidated and the user is redirected to the login page.
 */
@Component
public class UserExistenceFilter implements Filter {

    /** Repository to access user data. */
    private final UserRepository userRepo;

    /**
     * Constructor to inject the UserRepository.
     *
     * @param userRepo the repository used to check user existence
     */
    public UserExistenceFilter(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Filters each incoming request to ensure the authenticated user still exists.
     * Allows public paths without restriction. For authenticated users, if the user no longer exists
     * in the database, the session is invalidated and redirected to the login page.
     *
     * @param request  the incoming servlet request
     * @param response the servlet response
     * @param chain    the filter chain
     * @throws IOException      in case of I/O errors
     * @throws ServletException in case of servlet errors
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Allow access to public resources
        if (path.startsWith("/login") || path.startsWith("/register") ||
                path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        // If user is authenticated, check if user still exists
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();

            boolean userExists = userRepo.findByUsername(username).isPresent();
            if (!userExists) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                res.sendRedirect("/login?forceLogout=true");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
