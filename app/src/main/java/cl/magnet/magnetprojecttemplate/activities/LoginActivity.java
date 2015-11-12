package cl.magnet.magnetprojecttemplate.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.models.user.User;
import cl.magnet.magnetprojecttemplate.models.user.UserRequestManager;
import cl.magnet.magnetprojecttemplate.network.AppErrorListener;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;
import cl.magnet.magnetrestclient.VolleyManager;
import cl.magnet.usermanager.UserManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               showRegister();
            }
        });

        TextView mPasswordRecoverButton = (TextView) findViewById(R.id.activity_login_btn_password_recover);
        mPasswordRecoverButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);

            Response.Listener listener = new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    showProgress(false);

                    //TODO: Use method UserManager.logInUser loading user from database
                    PrefsManager.saveUserCredentials(getApplicationContext(), email, password);

                    startActivityClosingAllOthers(DrawerActivity.class);
                }
            };

            AppErrorListener errorListener = new AppErrorListener(getApplicationContext()){
                @Override
                public void onErrorResponse(VolleyError error) {
                    super.onErrorResponse(error);
                    showProgress(false);
                }
                @Override
                public void onUnauthorizedError(VolleyError error, Request request) {
                    showToast(R.string.wrong_credentials);
                    showProgress(false);
                }
            };

            Request request = UserRequestManager.userLoginRequest(email, password, listener, errorListener);
            VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(request);

        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showRegister() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    protected void recoverPassword() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        alertBuilder.setMessage(R.string.activity_login_recover_password_dialog_msg)
                .setView(editText)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String email= editText.getText().toString();

                                if (TextUtils.isEmpty(email)) {
                                    Toast.makeText(getApplicationContext(), R.string.error_field_required, Toast.LENGTH_SHORT).show();
                                }
                                else if (!isEmailValid(email))  {
                                    Toast.makeText(getApplicationContext(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    callRecoverPasswordWebservice(email);
                                }
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        alertBuilder.show();
    }

    private void callRecoverPasswordWebservice(String email) {
        //TODO: Perform password recovery on API
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

}

