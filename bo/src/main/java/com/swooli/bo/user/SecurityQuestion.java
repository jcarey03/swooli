package com.swooli.bo.user;

import java.io.Serializable;

/**
 * A question and answer for user account verification.
 *
 * @author jmcarey
 */
public class SecurityQuestion implements Serializable {

    private String question;

    private String answer;

    public boolean matches(final String answer) {
        return this.answer.equalsIgnoreCase(answer.trim());
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

}
