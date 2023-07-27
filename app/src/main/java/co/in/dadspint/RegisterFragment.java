package co.in.dadspint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.in.dadspint.databinding.ActivityRegisterPageBinding;

public class RegisterFragment extends Fragment {

    ActivityRegisterPageBinding binding;
    String str_UserFullName, str_MobileNumber, str_EmailId, str_UserName, str_Password;
    SessionManager sessionManager;
    TextView text_signin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();

        sessionManager = new SessionManager(getContext());

        String checkBox_html = "<font color=#FF6200EE>Read our   </font>";
        binding.termsconditions.setText(Html.fromHtml(checkBox_html));
        binding.textTeramcondition.setText("Terms & Conditions");
        binding.textTeramcondition.setPaintFlags(binding.textTeramcondition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        binding.textTeramcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new TermsConditionsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        binding.textSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new LoginPageFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.editUserFullName.getText())) {

                    binding.editUserFullName.setError("Please Enter User Name");

                } else if (TextUtils.isEmpty(binding.editMobileNumber.getText())) {

                    binding.editMobileNumber.setError("Please Enter Mobile No");

                } else if (binding.editMobileNumber.getText().toString().trim().length() != 10) {

                    binding.editMobileNumber.setError("Enter Your 10 Digit Mobile Number");

                }
                else if (TextUtils.isEmpty(binding.editEmailId.getText())) {

                    binding.editEmailId.setError("Please Enter EmailId");

                }
                else if (!isValidEmail(binding.editEmailId.getText().toString().trim())) {

                    binding.editEmailId.requestFocus();
                    binding.editEmailId.setError("Please Enter Valide Email id");

                }
                else if (TextUtils.isEmpty(binding.editPassword.getText())) {

                    binding.editPassword.setError("Please EnterYour password");

                } else if (!binding.termsconditions.isChecked()) {

                    Toast.makeText(getActivity(), "Please Click Terms & Conditions", Toast.LENGTH_SHORT).show();

                } else {

                    str_UserFullName = binding.editUserFullName.getText().toString().trim();
                    str_MobileNumber = binding.editMobileNumber.getText().toString().trim();
                    str_EmailId = binding.editEmailId.getText().toString().trim();;
                    str_Password = binding.editPassword.getText().toString().trim();


                    registerUser(str_UserFullName,str_MobileNumber,str_EmailId,str_Password);

                    //Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }

    public void registerUser(String fullname, String contact, String mail, String password) {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Register please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Insertregister, new Response.Listener<String>() {
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

                        JSONObject jsonObject_statues = new JSONObject(statusArray);

                        String user_id = jsonObject_statues.getString("full_name");
                        String fullname = jsonObject_statues.getString("email");
                        String email = jsonObject_statues.getString("contact_no");
                        String contact = jsonObject_statues.getString("password");
                        String otp = jsonObject_statues.getString("otp");

                        sessionManager.setUSEREMAIL(email);
                        sessionManager.setLOGINOTP(otp);

                        Intent intent = new Intent(getActivity(), OtpVerifactionActivity.class);
                        intent.putExtra("message","Register");
                        intent.putExtra("fullname",fullname);
                        intent.putExtra("mail",email);
                        intent.putExtra("contact",contact);
                        intent.putExtra("password",password);
                        startActivity(intent);

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

                progressDialog.hide();
                Log.d("error_response", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
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
