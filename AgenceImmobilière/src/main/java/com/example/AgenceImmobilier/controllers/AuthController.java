package com.example.AgenceImmobilier.controllers;

import com.example.AgenceImmobilier.DTOs.request.LoginRequest;
import com.example.AgenceImmobilier.DTOs.request.RefreshTokenDto;
import com.example.AgenceImmobilier.DTOs.request.SignupRequest;
import com.example.AgenceImmobilier.DTOs.response.JwtRefreshResponse;
import com.example.AgenceImmobilier.DTOs.response.JwtResponse;
import com.example.AgenceImmobilier.DTOs.response.MessageResponse;
import com.example.AgenceImmobilier.models.user.AuthProvider;
import com.example.AgenceImmobilier.models.user.ERole;
import com.example.AgenceImmobilier.models.user.Role;
import com.example.AgenceImmobilier.models.user.UserModel;
import com.example.AgenceImmobilier.repositories.userR.RoleRepository;
import com.example.AgenceImmobilier.security.jwt.JwtUtils;
import com.example.AgenceImmobilier.security.services.UserDetailsImpl;
import com.example.AgenceImmobilier.security.services.UserDetailsServiceImpl;
import com.example.AgenceImmobilier.services.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshJwt = jwtUtils.generateRefreshJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok( JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshJwt)
                .roles(roles)
                .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        UserModel user = new UserModel(signUpRequest.getFirstname(),
                signUpRequest.getLastname(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleService.findRoleByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleService.findRoleByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "host":
                        Role hostRole = roleService.findRoleByName(ERole.ROLE_HOST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(hostRole);

                        break;
                    default:
                        Role userRole = roleService.findRoleByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setProvider(AuthProvider.local);
        user.setRoles(roles);
        userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/refreshtoken") // Todo:RefreshToken
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDto refreshToken){
         if(jwtUtils.validateJwtToken(refreshToken.getRefreshToken())==false)
            return new ResponseEntity<>(new MessageResponse("refreshToken not valid"), HttpStatus.BAD_REQUEST);


        UserDetailsImpl userPrincipal = (UserDetailsImpl) userDetailsService.loadUserByUsername(jwtUtils.getUserNameFromJwtToken(refreshToken.getRefreshToken()));
        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("roles",userPrincipal.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();


        return new ResponseEntity<>(new JwtRefreshResponse(token),HttpStatus.OK);
    }
}
