package co.in.dadspint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import co.in.dadspint.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPageFragment extends Fragment {

    Button btn_signin;
    SessionManager sessionManager;
    EditText edit_MobileNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        btn_signin = view.findViewById(R.id.btn_signin);
        edit_MobileNo = view.findViewById(R.id.edit_MobileNo);

        sessionManager = new SessionManager(getActivity());

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Fragment fragment = new OtpVerifactioFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                if (edit_MobileNo.getText().toString().trim().equals("")) {

                    Toast.makeText(getActivity(), "Enter MobileNo", Toast.LENGTH_SHORT).show();

                } else if (edit_MobileNo.getText().toString().trim().length() == 10) {

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

                        startActivity(new Intent(getActivity(), OtpVerifactionActivity.class));

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

}
