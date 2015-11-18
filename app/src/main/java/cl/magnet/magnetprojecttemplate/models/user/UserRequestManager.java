package cl.magnet.magnetprojecttemplate.models.user;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cl.magnet.magnetprojecttemplate.network.APIManager;
import cl.magnet.magnetprojecttemplate.network.AppErrorListener;

/**
 * Created by yaniv on 10/28/15.
 */
public class UserRequestManager extends APIManager {

    private static final String USER_API_URL = BASE_URL + API_URL + "user/";
    private static final String LOGIN_API_URL = USER_API_URL + "login/";
    private static final String LOGOUT_API_URL = USER_API_URL + "logout/";

    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String EMAIL = "email";
    public static String PASSWORD = "password";

    /**
     * Creates a new JsonObjectRequest for creating a new
     * user.
     *
     * @param firstName The first name of the user to create
     * @param lastName The last name of the user to create
     * @param email The email of the user to create
     * @param password The password of the user to create
     * @param listener The listener for on success callbacks
     * @param errorListener The listener for error callbacks
     * @return The created JsonObjectRequest for create user webservice
     */
    public static JsonObjectRequest createUserRequest(String firstName, String lastName,
                                                      String email, String password,
                                                      Response.Listener<JSONObject> listener,
                                                      AppErrorListener errorListener) {

        JSONObject params = new JSONObject();
        try {
            params.put(FIRST_NAME, firstName);
            params.put(LAST_NAME, lastName);
            params.put(EMAIL, email);
            params.put(PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(Request.Method.POST, USER_API_URL, params, listener, errorListener);
    }


    public static JsonObjectRequest userLoginRequest(String email, String password, Response.Listener listener, AppErrorListener errorListener) {

        JSONObject params = new JSONObject();

        try {
            params.put(EMAIL, email);
            params.put(PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(Request.Method.POST, LOGIN_API_URL, params, listener, errorListener);

    }
}
