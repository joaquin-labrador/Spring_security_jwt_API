package com.api.security.Config;

import com.api.security.Entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class UserDetailsService {
    public abstract User loadUserByUsername(String username) throws UsernameNotFoundException;
}
