package it.antonio.sp.service;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.repository.UserRepository;

@Service
public class MongoAuthUserDetailService implements UserDetailsService {

	@Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(userName).blockFirst();
        
        if (user == null)
        	LogManager.getLogger().error("User not found!");

        return User.builder()
        		.username(user.getEmail())
        		.password(user.getPassword())
        		.authorities(user.getRoles().toArray(new String[0])).build();
    }

}
