package com.example.ArtistBackend.configuration;

import com.example.ArtistBackend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
//                .securityMatcher("/admin", "/admin/**")
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/admin/login",
                                "/css/**",
                                "/js/**",
                                "/uploads/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/admin", true)
//                                .successHandler(successHandler)
                        .failureUrl("/admin/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );
//                .exceptionHandling(ex -> ex
//                        .accessDeniedHandler((request, response, exception) -> {
//                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                            if (auth != null && auth.getAuthorities().stream()
//                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                                response.sendRedirect("/admin");
//                            } else {
//                                response.sendRedirect("/access-denied");
//                            }
//                        })
//                );


        return http.build();
    }



    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
//                .securityMatcher("/**")
//                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorizeRequests ->authorizeRequests
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/verify",
                                "/forgot-password",
                                "/reset-password",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers("/checkout/**").authenticated()
                        .anyRequest().permitAll()
//                        .anyRequest().denyAll()
                )
                .formLogin(form-> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        //login zhalya vr checkout page la redirect kiva home page la
                        .defaultSuccessUrl("/", true)
//                                .successHandler(successHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

//                .exceptionHandling(ex -> ex
//                        .accessDeniedHandler((request, response, exception) -> {
//                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//                            if (auth != null && auth.getAuthorities().stream()
//                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                                response.sendRedirect("/admin");
//                            } else {
//                                response.sendRedirect("/access-denied");
//                            }
//                        })
//                );
        return http.build();
    }



//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .authenticationProvider(authenticationProvider())
//
////                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/",
//                                "/login",
//                                "/register",
//                                "/css/**",
//                                "/js/**",
//                                "/images/**",
//                                "/uploads/**"
//                        ).permitAll()
//
//                        .requestMatchers("/admin/login").permitAll()
//
//                        .requestMatchers("/checkout/**").hasRole("USER")
//
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//
//
//                        .anyRequest().authenticated()
//                )
//
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/do-login")
//                        .usernameParameter("email")
//                        .successHandler((request, response, authentication) -> {
//
//                            boolean isAdmin = authentication.getAuthorities().stream()
//                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//
//                            if (isAdmin) {
//                                response.sendRedirect("/admin");
//                            } else {
//                                response.sendRedirect("/");
//                            }
//                        })
//                        .permitAll()
//                )
//
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessHandler((request, response, authentication) -> {
//                            if (authentication != null &&
//                                    authentication.getAuthorities().stream()
//                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                                response.sendRedirect("/admin/login");
//                            } else {
//                                response.sendRedirect("/");
//                            }
//                        })
//                );
//
//        return http.build();
//    }
}


