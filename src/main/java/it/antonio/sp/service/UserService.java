package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    //MAIL VALIDATION
    private static final String EMAIL_PATTERN = "[A-Za-z]+[0-9]*\\.[A-Za-z]+[0-9]*@gmail\\.com"; //"[A-Za-z0-9]+\\.[A-Za-z0-9]+@gmail\\.com";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    public static boolean isValidMail(final String email) {
        return emailPattern.matcher(email).matches();
    }
    
    
    //PASSWORD VALIDATION digit + lowercase char + uppercase char + punctuation + symbol
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;'.,?/*~$^+=<>]).{8,20}$";
    private static final Pattern passawordPattern = Pattern.compile(PASSWORD_PATTERN);
    public static boolean isValid(final String password) {
        Matcher matcher = passawordPattern.matcher(password);
        return matcher.matches();
    }
    
    public Flux<Response> register(String email, String password) {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    	UserEntity user = userRepository.findAll().blockFirst() == null ? new UserEntity(
    			Constants.ROLE_ADMIN,
    			Constants.ROLE_ADMIN_QUALIFICATIONS_ALL,
    			Constants.ROLE_ADMIN_SPECIALTIES_ALL,
    			Constants.ROLE_ADMIN_USER_MANAGEMENT_ALL
    		) : new UserEntity();
    	
   
    	user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setActive(true);

        
        //MAIL VALIDATION
        if (!isValidMail(email)) {
        	return Flux.just(Response.Error("L'indirizzo email deve essere nel seguente formato: nome.cognome@gmail.com")); 
        }
        
        //PASSWORD VALIDATION
        if (!isValid(password)) {
        	return Flux.just(Response.Error("Digitare un carattere minisculo, un carattere maiuscolo, un numero ed un simbolo di punteggiatura. La password avere un numero > 8 caratteri")); 
        }
        
        
        return userRepository
                .findByEmail(user.getEmail())
                .map(result -> { 
                	return Response.Error("L'email risulta già registrata da un'altro utente.");
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
