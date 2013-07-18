package com.swooli.web.controller;

import com.swooli.ApplicationProperties;
import com.swooli.bo.user.Gender;
import com.swooli.web.bean.UserAccountRequestBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(method=RequestMethod.POST)
    public String submitAccount(
            @Valid @ModelAttribute("userAccountRequestBean") UserAccountRequestBean userAccountRequest,
            BindingResult userAccountRequestResult,
            HttpServletRequest httpRequest,
            @RequestParam("recaptcha_challenge_field") String challenge,
            @RequestParam("recaptcha_response_field") String response
            ) {

        // check passwords match
        if(userAccountRequest.getPassword() != null && !userAccountRequest.getPassword().equals(userAccountRequest.getConfirmPassword())) {
            userAccountRequestResult.rejectValue("confirmPassword", "Mismatch.passwords");
        }

        // check gender specified
        if(userAccountRequest.getGender() == Gender.DEFAULT) {
            userAccountRequestResult.rejectValue("gender", "NotEmpty.gender");
        }

        // check captcha value
        final String remoteAddr = httpRequest.getRemoteAddr();
        final ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

        reCaptcha.setPrivateKey(ApplicationProperties.get("swooli.recaptcha.private.key"));

        final ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, response);
        if(!reCaptchaResponse.isValid()) {
            userAccountRequestResult.rejectValue("captchaError", "Mismatch.captcha");
        }

        return "userAccountRequest";

    }

    @RequestMapping(method=RequestMethod.GET)
    public String getAccountRequest(Model model) {
        model.addAttribute("userAccountRequestBean", new UserAccountRequestBean());
        return "userAccountRequest";
    }

    @ModelAttribute(value="genders")
    public List<String> getGenders() {
        final List<String> genders = new ArrayList<>();
        for(final Gender gender : Gender.values()) {
            genders.add(gender.getDisplayName());
        }
        return genders;
    }

}