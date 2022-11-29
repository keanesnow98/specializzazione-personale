package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.repository.UserRepository;
import it.antonio.sp.response.Response;
import it.antonio.sp.util.Constants;
import reactor.core.publisher.Flux;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserEntity> findAll() {
    	List<UserEntity> users = new ArrayList<UserEntity>();
    	userRepository.findAll().toIterable().forEach(user -> {
    		users.add(user);
    	});;
    	return users;
    }

    public Flux<Response> register(String email, String password) {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    	UserEntity user = new UserEntity(Constants.ROLE_ADMIN);
    	user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setActive(true);

        return userRepository
                .findByEmail(user.getEmail())
                .map(result -> {
                	return Response.Error("Email already registered by other user");
                })
                .switchIfEmpty(userRepository
                        .save(user)
                        .map(result -> {
                        	return Response.Success();
                        }));
    }

    public void updateUser(UserEntity user) {
		userRepository.save(user).block();
    }
    
    public void deleteUser(UserEntity user) {
    	userRepository.delete(user).block();
    }
}
