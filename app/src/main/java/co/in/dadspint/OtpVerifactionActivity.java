package co.in.dadspint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import co.in.dadspint.databinding.OtpverifactionFragmentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerifactionActivity extends AppCompatActivity {

    OtpverifactionFragmentBinding binding;
    SessionManager sessionManager;
    String mobileNo,emailId,message,fullname,mail,contact,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.otpverifaction_fragment);

        binding = OtpverifactionFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(OtpVerifactionActivity.this);

        mobileNo = sessionManager.getMOBILENO();
        emailId = sessionManager.getUSEREMAIL();

        message = getIntent().getStringExtra("message");
        fullname = getIntent().getStringExtra("fullname");
        mail = getIntent().getStringExtra("mail");
        contact = getIntent().getStringExtra("contact");
        password = getIntent().getStringExtra("password");

        Log.d("userdetails",fullname+" "+mail+" "+contact+" "+password);

        if (message.equals("Register")){

            binding.mobileNumber.setText(mail);

        }else{

            binding.mobileNumber.setText("+91" + "  " + mobileNo);
        }

        binding.btnVerifayOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.otpView.getOTP().equals("")) {

                    Toast.makeText(OtpVerifactionActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();

                } else {

                    String otp = binding.otpView.getOTP();

                    if (message.equals("Register")){

                        if (otp.equals(sessionManager.getLOGINOTP())){

                            userRegisterPage(fullname,contact,mail,password);

                            Log.d("userdetails",fullname+" "+mail+" "+contact+" "+password);

                        }else{

                            Toast.makeText(OtpVerifactionActivity.this, "Otp is invalide", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        if (otp.equals(sessionManager.getLOGINOTP())){

                            Toast.makeText(OtpVerifactionActivity.this, otp, Toast.LENGTH_SHORT).show();
                            otpVerifaction(mobileNo, otp);

                        }else{

                            Toast.makeText(OtpVerifactionActivity.this, "Otp is invalide", Toast.LENGTH_SHORT).show();
                        }


                    }


                }
            }
        });

        binding.resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (message.equals("Register")){
                }else{

                    if(mobileNo.length() != 0){

                        userLoginPage(mobileNo);

                    }else{

                        Toast.makeText(OtpVerifactionActivity.this, "Mobile No not provide", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        timer();
    }

    public void otpVerifaction(String contact, String otp) {

        ProgressDialog progressDialog = new ProgressDialog(OtpVerifactionActivity.this);
        progressDialog.setMessage("OTP Verify Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.VerifyOTP, new Response.Listener<String>() {
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

                        String user_id = jsonObject_statues.getString("user_id");
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

                        startActivity(new Intent(OtpVerifactionActivity.this, DeshBoardActivity.class));

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(OtpVerifactionActivity.this, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(OtpVerifactionActivity.this, "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("otp", otp);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerifactionActivity.this);
        requestQueue.add(stringRequest);
    }
    public void timer(){

        //Initialize time duration
        long duration = TimeUnit.MINUTES.toMillis(1);
        //Initialize countdown timer

        new CountDownTimer(duration, 5) {
            @Override
            public void onTick(long millisUntilFinished) {

                //When tick
                //Convert millisecond to minute and second

                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                binding.textTimer.setText(sDuration);

                binding.textTimer.setVisibility(View.VISIBLE);
                binding.resendtext.setVisibility(View.VISIBLE);
                binding.resendOTP.setVisibility(View.GONE);

            }

            @Override
            public void onFinish() {

                binding.textTimer.setVisibility(View.GONE);
                binding.resendtext.setVisibility(View.GONE);
                binding.resendOTP.setVisibility(View.VISIBLE);

            }
        }.start();
    }
    public void userLoginPage(String contact) {

        ProgressDialog progressDialog = new ProgressDialog(OtpVerifactionActivity.this);
        progressDialog.setMessage("ReSend OTP Please Wait.....");
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

                        Toast.makeText(OtpVerifactionActivity.this, "OTP Resend Successfully", Toast.LENGTH_SHORT).show();

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

                        timer();

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(OtpVerifactionActivity.this, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(OtpVerifactionActivity.this, "" + error, Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerifactionActivity.this);
        requestQueue.add(stringRequest);
    }
    public void userRegisterPage(String fullname, String contact, String mail, String password) {

        ProgressDialog progressDialog = new ProgressDialog(OtpVerifactionActivity.this);
        progressDialog.setMessage("Register please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.verify_emailOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.hide();

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

                        startActivity(new Intent(OtpVerifactionActivity.this,LogiRegisterPage.class));

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(OtpVerifactionActivity.this, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.hide();
                Log.d("error_response", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(OtpVerifactionActivity.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtpVerifactionActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("fullname", fullname);
                params.put("email", mail);
                params.put("contactno", contact);
                params.put("password", password);

                Log.d("paramsforhomeapi", "" + params);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OtpVerifactionActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
}