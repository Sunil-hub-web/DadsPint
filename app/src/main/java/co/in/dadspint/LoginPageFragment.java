package co.in.dadspint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.NumberUtils;

import co.in.dadspint.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageFragment extends Fragment {

    Button btn_signin,btn_sendOTP;
    SessionManager sessionManager;
    EditText edit_MobileNo,edit_Password,edit_EmailId;
    TextView text_signUp,text_ClickHere,loginOTP,emailIdPassword;
    LinearLayout lin_otp,lin_emailId;
    boolean numeric;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        btn_signin = view.findViewById(R.id.btn_signin);
        edit_MobileNo = view.findViewById(R.id.edit_MobileNo);
        btn_sendOTP = view.findViewById(R.id.btn_sendOTP);
        text_signUp = view.findViewById(R.id.text_signUp);
        text_ClickHere = view.findViewById(R.id.text_ClickHere);
        edit_EmailId = view.findViewById(R.id.edit_EmailId);
        loginOTP = view.findViewById(R.id.loginOTP);
        edit_Password = view.findViewById(R.id.edit_Password);
        emailIdPassword = view.findViewById(R.id.emailIdPassword);
        lin_otp = view.findViewById(R.id.lin_otp);
        lin_emailId = view.findViewById(R.id.lin_emailId);

        sessionManager = new SessionManager(getActivity());

        loginOTP.setBackgroundResource(R.drawable.backgroundcolor);
        emailIdPassword.setBackgroundResource(R.drawable.textfieldback);
        lin_otp.setVisibility(View.VISIBLE);

        loginOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginOTP.setBackgroundResource(R.drawable.backgroundcolor);
                emailIdPassword.setBackgroundResource(R.drawable.textfieldback);
                lin_otp.setVisibility(View.VISIBLE);
                lin_emailId.setVisibility(View.GONE);

            }
        });

        emailIdPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginOTP.setBackgroundResource(R.drawable.textfieldback);
                emailIdPassword.setBackgroundResource(R.drawable.backgroundcolor);
                lin_otp.setVisibility(View.GONE);
                lin_emailId.setVisibility(View.VISIBLE);

            }
        });

        text_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new RegisterFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        text_ClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ForgotPasswordFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Fragment fragment = new OtpVerifactioFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                if (edit_EmailId.getText().toString().trim().equals("")){

                    Toast.makeText(getActivity(), "Please enter your Emailid", Toast.LENGTH_SHORT).show();

                } else if (!isValidEmail(edit_EmailId.getText().toString().trim())) {

                    edit_EmailId.setError("Enter Valide Email id");

                } else if (edit_Password.getText().toString().trim().equals("")) {

                    Toast.makeText(getActivity(), "Please enter your password", Toast.LENGTH_SHORT).show();

                }else{

                    String emailId = edit_EmailId.getText().toString().trim();
                    String password = edit_Password.getText().toString().trim();

                    loginwithEmailPassword(emailId,password);
                }

            }
        });

        btn_sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_MobileNo.getText().toString().trim().equals("")) {

                    Toast.makeText(getActivity(), "Enter mobile number", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(edit_MobileNo.getText()) && edit_MobileNo.getText().toString().trim().length() != 10) {

                    edit_MobileNo.setError("Provide 10 digit valid mobile number");

                }  else if (edit_MobileNo.getText().toString().trim().length() == 10) {

                    String mobileNo = edit_MobileNo.getText().toString().trim();

                    userLoginPage(mobileNo);

                } else {

                    Toast.makeText(getActivity(), "Enter 10 Digit MobileNo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    public void userLoginPage(String contact) {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Login please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.userLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    progressDialog.dismiss();

                    if (status.equals("200")) {

                       // Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");
                        JSONObject jsonObject_statues = new JSONObject(statusArray);

                        String contact_otp = jsonObject_statues.getString("contact_otp");
                        String login_otp = jsonObject_statues.getString("login_otp");

                        sessionManager.setMOBILENO(contact_otp);
                        sessionManager.setLOGINOTP(login_otp);
                        sessionManager.setLogin();

                        startActivity(new Intent(getActivity(), OtpVerifactionActivity.class));                   Intent intent = new Intent(getActivity(), OtpVerifactionActivity.class);
                        intent.putExtra("message","Login");
                        startActivity(intent);

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getContext(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();

/*                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginPage.this, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }*/
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("contact", contact);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void loginwithEmailPassword(String email, String password) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("User Login Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Loginauth, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    progressDialog.dismiss();

                    if (status.equals("200")) {

                        // Toast.makeText(OtpVerifactionActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        JSONObject jsonObject_statues = new JSONObject(statusArray);

                        String user_id = jsonObject_statues.getString("cust_id");
                        String fullname = jsonObject_statues.getString("fullname");
                        String email = jsonObject_statues.getString("email");
                        String contact = jsonObject_statues.getString("contact");
                        boolean isLoggedIn = jsonObject_statues.getBoolean("isLoggedIn");

                        sessionManager.setUSERID(user_id);
                        sessionManager.setUSERNAME(fullname);
                        sessionManager.setUSEREMAIL(email);
                        sessionManager.setMOBILENO(contact);
                        sessionManager.setlOGGEDIN(isLoggedIn);

                        sessionManager.setLogin();

                        startActivity(new Intent(getActivity(), DeshBoardActivity.class));

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

/*                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginPage.this, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }*/
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

        pattern = Patterns.EMAIL_ADDRESS;
        matcher = pattern.matcher(email);

        return matcher.matches();

        //return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
