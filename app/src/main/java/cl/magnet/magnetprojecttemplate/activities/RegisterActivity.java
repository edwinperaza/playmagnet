package cl.magnet.magnetprojecttemplate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.models.user.UserRequestManager;
import cl.magnet.magnetprojecttemplate.network.AppResponseListener;
import cl.magnet.magnetrestclient.VolleyManager;

/**
 * A sign up screen that offers user registration.
 */
public class RegisterActivity extends BaseActivity {

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmationView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mFirstNameView = (EditText) findViewById(R.id.first_name);
        mLastNameView = (EditText) findViewById(R.id.last_name);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordConfirmationView = (EditText) findViewById(R.id.password_confirmation);
        mPasswordConfirmationView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        // Set up views
        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);

        // Set up Click Listeners.
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

    }

    /**
     * Attempts to sign register a new user.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual sign up attempt is made.
     */
    private void attemptSignUp() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordConfirmation = mPasswordConfirmationView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password confirmation.
        if (!isPasswordConfirmationValid(password, passwordConfirmation)) {
            mPasswordConfirmationView.setError(getString(R.string.error_invalid_password_confirmation));
            focusView = mPasswordConfirmationView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(email)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!LoginActivity.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid last name.
        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        // Check for a valid first name.
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            //We show the loader and hide the form
            showHideView(mProgressView, mLoginFormView, true);

            //We set the response listener with corresponding overridden methods
            AppResponseListener<JSONObject> appResponseListener = new AppResponseListener<JSONObject>(getApplicationContext()){
                @Override
                public void onResponse(JSONObject response) {
                    showToast(R.string.registration_succeeded);

                    startActivityClosingAllOthers(DrawerActivity.class);
                }

                //TODO: Override method when user is already registered

                @Override
                public void onPostResponse(){
                    showHideView(mLoginFormView, mProgressView, true);
                }
            };

            //We add the request
            JsonObjectRequest request = UserRequestManager.createUserRequest(firstName, lastName, email, password, appResponseListener);
            VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(request);
        }

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPasswordConfirmationValid(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
}
