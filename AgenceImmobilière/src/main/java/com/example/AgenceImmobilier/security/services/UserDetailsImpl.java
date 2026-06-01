package com.example.AgenceImmobilier.security.services;

import com.example.AgenceImmobilier.models.user.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements OAuth2User,UserDetails {
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private  String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    public  UserDetailsImpl(Long id, String username,String email, String password,
                            Collection<? extends  GrantedAuthority > authorities){
        this.id=id;
        this.username=username;
        this.email=email;
        this.password=password;
        this.authorities=authorities;
    }
    public static UserDetailsImpl build(UserModel user){
        List<GrantedAuthority> authorities=user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
    public static UserDetailsImpl create(UserModel user, Map<String, Object> attributes) {
        UserDetailsImpl userPrincipal = UserDetailsImpl.build(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public Long getId(){
        return id;
    }
    public  String getEmail(){
        return email;
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

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getName() {
        return String.valueOf(id);
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        UserDetailsImpl user=(UserDetailsImpl) obj;
        return Objects.equals(id ,user.id);
    }


}
