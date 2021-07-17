package org.uc3m.covid.virtual.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/subject")
public class SubjectController {
    private final AuthScraper authScraper;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SubjectController(AuthScraper authScraper, TokenService tokenService, UserRepository userRepository) {
        this.authScraper = authScraper;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllSubjects(@RequestAttribute("uc3mId") long uc3mId) {
        return ResponseEntity.status(200).body("HOLAHOLA");
    }
}
