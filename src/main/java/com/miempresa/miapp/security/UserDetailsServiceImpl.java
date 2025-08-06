package com.miempresa.miapp.security;

import com.miempresa.miapp.model.User;
import com.miempresa.miapp.repository.UserRepository;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        System.out.println("Usuario encontrado: " + user.getUsername());
        System.out.println("Contraseña en DB: " + user.getPassword());
        System.out.println("Status: " + user.isStatus());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isStatus(), true, true, true,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRol())));
    }
}
