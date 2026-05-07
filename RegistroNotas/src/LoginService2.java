
import java.util.HashMap;
import java.util.Map;

public class LoginService2 {

    private final Map<String, String> users = new HashMap<>();

    public LoginService2() {
        users.put("profesor1", "12345");
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String expected = users.get(username.trim());
        return expected != null && expected.equals(password);
    }
}
