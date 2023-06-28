package com.example.schooluniformapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schooluniformapp.databinding.OtpverifactionFragmentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;

public class OtpVerifactionActivity extends AppCompatActivity {

    OtpverifactionFragmentBinding binding;
    SessionManager sessionManager;
    String mobileNo;

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

        binding.mobileNumber.setText("+91" + "  " + mobileNo);

        binding.btnVerifayOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.otpView.getOTP().equals("")) {

                    Toast.makeText(OtpVerifactionActivity.this, "Enter Your Otp", Toast.LENGTH_SHORT).show();

                } else {

                    String otp = binding.otpView.getOTP();

                    Toast.makeText(OtpVerifactionActivity.this, otp, Toast.LENGTH_SHORT).show();

                    otpVerifaction(mobileNo, otp);
                }
            }
        });

        binding.resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mobileNo.length() != 0){

                    userLoginPage(mobileNo);

                }else{

                    Toast.makeText(OtpVerifactionActivity.this, "Mobile No not provide", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(OtpVerifactionActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

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
        progressDialog.setMessage("Login Please Wait.....");
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
}