package com.example.AgenceImmobilier.security;

import com.example.AgenceImmobilier.security.jwt.AuthEntryPointJwt;
import com.example.AgenceImmobilier.security.jwt.AuthTokenFilter;
import com.example.AgenceImmobilier.security.oauth2.CustomOAuth2UserService;
import com.example.AgenceImmobilier.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.AgenceImmobilier.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.AgenceImmobilier.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.AgenceImmobilier.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;


    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

  @Bean
  public ClientRegistrationRepository clientRepository() {

      ClientRegistration githubRegistration =
              CommonOAuth2Provider.GITHUB.getBuilder("github")
                      .clientId("Iv1.61889f59c65d617e")
                      .clientSecret("b82f62361cea118c0bbde757aff4a2cd7935a3ae")
                      .redirectUri("{baseUrl}/oauth2/callback/{registrationId}")
                      .scope("user:email", "read:user")
                      .authorizationUri("https://github.com/login/oauth/authorize")
                      .tokenUri("https://github.com/login/oauth/access_token")
                      .userInfoUri("https://api.github.com/user")
                      .userNameAttributeName("id")
                      .clientName("GitHub")
                      .build();

      ClientRegistration googleRegistration =
              CommonOAuth2Provider.GOOGLE.getBuilder("google")
                      .clientId("5014057553-8gm9um6vnli3cle5rgigcdjpdrid14m9.apps.googleusercontent.com")
                      .clientSecret("tWZKVLxaD_ARWsriiiUFYoIk")
                      .redirectUri("{baseUrl}/oauth2/callback/{registrationId}")
                      .scope("email", "profile")
                      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                      .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                      .tokenUri("https://www.googleapis.com/oauth2/v3/token")
                      .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                      .userNameAttributeName(IdTokenClaimNames.SUB)
                      .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                      .clientName("Google")
                      .build();

      return new InMemoryClientRegistrationRepository(githubRegistration,
              googleRegistration);
  }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->  auth.requestMatchers("/api/auth/**","/oauth2/**").permitAll()
                        .requestMatchers("/api/test/**","/api/**").permitAll()

                        .anyRequest().authenticated())
               .oauth2Login(oauth2Login ->
                oauth2Login
                        .authorizationEndpoint(authorizationEndpoint ->
                                authorizationEndpoint
                                        .baseUri("/oauth2/authorize")
                                        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                        )
                        .redirectionEndpoint(redirectionEndpoint ->
                                redirectionEndpoint
                                        .baseUri("/oauth2/callback/*")
                        )
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint
                                        .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)

        );
        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
