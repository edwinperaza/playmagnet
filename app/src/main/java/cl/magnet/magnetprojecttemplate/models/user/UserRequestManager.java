package cl.magnet.magnetprojecttemplate.models.user;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cl.magnet.magnetprojecttemplate.network.APIManager;
import cl.magnet.magnetprojecttemplate.network.AppResponseListener;

/**
 * Created by yaniv on 10/28/15.
 */

/**
 * API requests regarding Users
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
     * Creates a new JsonObjectRequest for creating a new user.
     *
     * @param firstName The first name of the user to create
     * @param lastName The last name of the user to create
     * @param email The email of the user to create
     * @param password The password of the user to create
     * @param responseListener The listener for on success and error callbacks
     * @return The created JsonObjectRequest for create user webservice
     */
    public static JsonObjectRequest createUserRequest(String firstName, String lastName,
                                                      String email, String password,
                                                      AppResponseListener<JSONObject> responseListener) {

        JSONObject params = new JSONObject();
        try {
            params.put(FIRST_NAME, firstName);
            params.put(LAST_NAME, lastName);
            params.put(EMAIL, email);
            params.put(PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(Request.Method.POST, USER_API_URL, params, responseListener, responseListener);
    }

    /**
     * Creates a new JsonObjectRequest for logging in a user.
     *
     * @param email The email of the user to log in
     * @param password The password of the user to log in
     * @param responseListener The listener for on success and error callbacks
     * @return The created JsonObjectRequest for logging in the user webservice
     */
    public static JsonObjectRequest userLogInRequest(String email,
                                                           String password,
                                                           AppResponseListener<JSONObject> responseListener) {

        JSONObject params = new JSONObject();

        try {
            params.put(EMAIL, email);
            params.put(PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(Request.Method.POST, LOGIN_API_URL, params, responseListener, responseListener);

    }

    /**
     * Logs out the user from the server session
     *
     * @param responseListener The listener for on success and error callbacks
     * @return The created JsonObjectRequest for logging out the user webservice
     */
    public static JsonObjectRequest userLogOutRequest(AppResponseListener<JSONObject> responseListener) {

        return new JsonObjectRequest(Request.Method.DELETE, LOGOUT_API_URL, responseListener, responseListener);

    }
}
