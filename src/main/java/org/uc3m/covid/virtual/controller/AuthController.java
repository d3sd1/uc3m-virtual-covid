package org.uc3m.covid.virtual.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.uc3m.covid.virtual.constants.TokenIssuer;
import org.uc3m.covid.virtual.constants.TokenSubject;
import org.uc3m.covid.virtual.entity.User;
import org.uc3m.covid.virtual.model.Login;
import org.uc3m.covid.virtual.model.UserBasicData;
import org.uc3m.covid.virtual.repository.UserRepository;
import org.uc3m.covid.virtual.scraper.AuthScraper;
import org.uc3m.covid.virtual.service.TokenService;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthScraper authScraper;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthController(AuthScraper authScraper, TokenService tokenService, UserRepository userRepository) {
        this.authScraper = authScraper;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody Login login) {
        System.out.println(login);
        boolean loginSuccess;
        UserBasicData userBasicData = new UserBasicData();
        try {
            userBasicData = this.authScraper.doLogin(login);
            loginSuccess = true;
        } catch (LoginException e) {
            loginSuccess = false;
        } catch (IOException e) {
            loginSuccess = false;
            e.printStackTrace();
        }
        if (loginSuccess) {
            String jwt = this.tokenService.generate(
                    Long.toString(login.getUserUc3mId()),
                    TokenIssuer.UC3M,
                    TokenSubject.LOGIN,
                    login.isLongTermLogin() ? 4 * 30 * 24 * 60 * 60 : 30 * 24 * 60 * 60
            );
            UserBasicData finalUserBasicData = userBasicData;
            User user = this.userRepository.findByUc3mId(login.getUserUc3mId()).orElseGet(() -> {
                User newUser = new User();
                newUser.setUc3mId(login.getUserUc3mId());
                newUser.setFullName(finalUserBasicData.getFullName());
                newUser.setUc3mPassword(login.getPass());
                newUser.setEmail(finalUserBasicData.getEmail());
                newUser.setMoodleId(finalUserBasicData.getUserMoodleId());
                System.out.println("SUBJECTS -> " + Arrays.toString(finalUserBasicData.getSubjects().toArray()));
                newUser.setSubjects(finalUserBasicData.getSubjects());
                return newUser;
            });
            user.setJwt(jwt);
            user = this.userRepository.save(user);
            return ResponseEntity.status(200).body(user);
            // reeturn 200
        } else {
            return ResponseEntity.status(403).body("LOGIN_FAIL");
        }
    }
}
