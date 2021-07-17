package org.uc3m.covid.virtual.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Login {
    @JsonProperty("uc3m_user_id")
    private long userUc3mId;

    @JsonProperty("uc3m_user_pass")
    private String pass;

    @JsonProperty("uc3m_login_long_term")
    private boolean longTermLogin;

    public long getUserUc3mId() {
        return userUc3mId;
    }

    public void setUserUc3mId(long userUc3mId) {
        this.userUc3mId = userUc3mId;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isLongTermLogin() {
        return longTermLogin;
    }

    public void setLongTermLogin(boolean longTermLogin) {
        this.longTermLogin = longTermLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Login)) return false;
        Login login = (Login) o;
        return Objects.equals(getUserUc3mId(), login.getUserUc3mId()) &&
                Objects.equals(getPass(), login.getPass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserUc3mId(), getPass());
    }

    @Override
    public String toString() {
        return "Login{" +
                "user='" + userUc3mId + '\'' +
                ", pass='" + pass + '\'' +
                ", longTermLogin=" + longTermLogin +
                '}';
    }
}
