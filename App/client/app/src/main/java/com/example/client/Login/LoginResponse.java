package com.example.client.Login;

public class LoginResponse {
    private String username;
    private boolean success;
    private  String message;
    private String token;
    private UserDTO userProfile;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public UserDTO getUserProfile() { return userProfile; }
}
