package it.antonio.sp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
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
import it.antonio.sp.response.Response;
import it.antonio.sp.util.Constants;
import it.antonio.sp.util.Generator;
import reactor.core.publisher.Flux;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthRepository authRepository;

    public Iterable<UserEntity> findAll() {
    	return userRepository.findAll().filter(result -> !result.getRole().equals("MANAGER")).toIterable();
    }
    
    public Flux<Response> login(UserEntity user, HttpServletRequest req) {
    	return userRepository.findByEmail(user.getEmail()).map(result -> {
    		if (result.getPassword().equals(Generator.generatePassword(user.getPassword()))) {
    			if (result.getActive()) {
	    			HttpSession session = req.getSession();
	    			session.setAttribute("auth-token", addAuthToken(result.getId()));
	    			return Response.Success();
    			} else return Response.Error("This account is currently not allowed");
    		}
    		return Response.Error("Password is incorrect");
    	}).defaultIfEmpty(Response.Error("Email not found")).onErrorReturn(Response.Error());
    }

    public Flux<Response> register(UserEntity user) {
        user.setId(Generator.generateUserId());
        user.setPassword(Generator.generatePassword(user.getPassword()));
        user.setRole(Constants.ROLE_USER);
        user.setCreatedAt(new Date());

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
    	Object token = session.getAttribute("auth-token");
    	if (token == null)
    		token = "";
    	return (String) token;
    }
}
