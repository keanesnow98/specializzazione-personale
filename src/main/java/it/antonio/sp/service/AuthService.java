package it.antonio.sp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.AuthEntity;
import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.repository.AuthRepository;
import it.antonio.sp.repository.UserRepository;
import it.antonio.sp.util.Constants;
//import it.antonio.sp.request.LoginRequest;
//import it.antonio.sp.request.SignUpRequest;
//import it.antonio.sp.response.Response;
import it.antonio.sp.util.Generator;
import reactor.core.publisher.Flux;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthRepository authRepository;

    public Iterable<UserEntity> findAll() {
    	List<String> roles = new ArrayList<>();
    	roles.add("ADMIN");
    	roles.add("CLIENT");
    	return userRepository.findAllByRole(roles).toIterable();
    }
    
    public Flux<Boolean> login(UserEntity user, HttpServletRequest req) {
    	return userRepository.findByEmail(user.getEmail()).map(result -> {
    		if (result.getPassword().equals(Generator.generatePassword(user.getPassword()))) {
    			HttpSession session = req.getSession();
    			session.setAttribute("auth-token", addAuthToken(result.getId()));
    			return true;
    		}
    		return false;
    	}).defaultIfEmpty(false).onErrorReturn(false);
    }

    public Flux<Boolean> register(UserEntity user) {
        user.setId(Generator.generateUserId());
        user.setPassword(Generator.generatePassword(user.getPassword()));
        user.setRole(Constants.ROLE_CLIENT);
        user.setCreatedAt(new Date());

        return userRepository
                .findByEmail(user.getEmail())
                .map(result -> {
                	return false;
                })
                .switchIfEmpty(userRepository
                        .save(user)
                        .map(result -> {
                        	return true;
                        }));
    }

    public void logout(HttpServletRequest req) {
    	HttpSession session = req.getSession();
        authRepository
	        .findByToken((String) getTokenFromSession(session))
	        .map(mapper -> {
	        	return authRepository.delete(mapper).block();
	        });
        session.removeAttribute("auth-token");
    }
    
    public Flux<UserEntity> verifyToken(HttpServletRequest req) {
        Date current = Date.from(LocalDate.now().atTime(LocalTime.now()).toInstant(ZoneOffset.UTC));
    	return authRepository
                .findByToken((String) getTokenFromSession(req.getSession()))
                .map(mapper -> {
                	if (mapper.getCreatedAt().compareTo(current) < 0 && mapper.getExpiredOn().compareTo(current) > 0) {
                		return userRepository.findById(mapper.getUserId()).block();
                	}
                	return null;
                });
    }
    
    public void updateById(String id, Document document) {
    	UserEntity user = userRepository.findById(id).block();
    	if (document.getString("role") != null)
    		user.setRole(document.getString("role"));
    	if (document.getString("active") != null)
    		user.setActive(document.getString("active").equals("true"));
		userRepository.save(user).block();
    }

    String addAuthToken(String userId) {
        LocalDateTime localdateTomorrow = LocalDate.now().plusDays(1).atStartOfDay();
        Date expired = Date.from(localdateTomorrow.toInstant(ZoneOffset.UTC));
        AuthEntity newAuthToken = new AuthEntity();
        newAuthToken.setCreatedAt(new Date());
        newAuthToken.setUserId(userId);
        newAuthToken.setToken(UUID.randomUUID().toString());
        newAuthToken.setExpiredOn(expired);
        return authRepository.save(newAuthToken).block().getToken();
    }
    
    String getTokenFromSession(HttpSession session) {
    	var token = session.getAttribute("auth-token");
    	if (token == null)
    		token = "";
    	return (String) token;
    }
}
