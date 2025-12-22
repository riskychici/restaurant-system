package project_oop.util;

public class user_session {
    private static user_session instance;
    private int userId;
    private String userName;
    private String userRole;
    
    private user_session() {}
    
    public static user_session getInstance() {
        if (instance == null) {
            instance = new user_session();
        }
        return instance;
    }
    
    public void setUserData(int userId, String userName, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
    }
    
    public int getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserRole() { return userRole; }
    
    public void clearSession() {
        userId = 0;
        userName = null;
        userRole = null;
    }
}