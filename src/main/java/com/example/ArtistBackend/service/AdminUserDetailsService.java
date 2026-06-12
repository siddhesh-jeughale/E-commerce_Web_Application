//package com.example.ArtistBackend.service;
//
//import com.example.ArtistBackend.model.Admin;
//import com.example.ArtistBackend.repository.AdminRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AdminUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private AdminRepository adminRepository;
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Admin admin =  adminRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));
//
//
//        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        return new org.springframework.security.core.userdetails.User(
//                admin.getEmail(),
//                admin.getPassword(),
//                authorities
//        );
//    }
//}
