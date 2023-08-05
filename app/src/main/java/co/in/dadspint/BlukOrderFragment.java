package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.in.dadspint.databinding.BluckorderFragmentBinding;

public class BlukOrderFragment extends Fragment {

    BluckorderFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = BluckorderFragmentBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        binding.btnBlukOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editOrganization.getText().toString().trim().equals("")) {

                    binding.editOrganization.setError("Enter Organization Name");

                } else if (TextUtils.isEmpty(binding.editEmailId.getText())) {

                    binding.editEmailId.setError("Please Enter Email");

                } else if (!isValidEmail(binding.editEmailId.getText().toString().trim())) {

                    binding.editEmailId.setError("Enter Valide Email id");

                } else if (TextUtils.isEmpty(binding.editMobileNo.getText())) {

                    binding.editMobileNo.setError("mobile number not empty");

                } else if (binding.editMobileNo.getText().toString().trim().length() != 10) {

                    binding.editMobileNo.setError("Provide 10 digit valid mobile number");

                } else if (TextUtils.isEmpty(binding.editProductDetails.getText())) {

                    binding.editMobileNo.setError("Please enter product details");

                } else if (binding.description.getText().toString().trim().length() != 10) {

                    binding.editMobileNo.setError("Please enter message");

                }else if (binding.editUentity.getText().toString().trim().length() != 10) {

                    binding.editMobileNo.setError("Please enter Quentity");

                } else {
                    BlukOrder(
                            binding.editOrganization.getText().toString().trim(),
                            binding.editUentity.getText().toString().trim(),
                            binding.editEmailId.getText().toString().trim(),
                            binding.editMobileNo.getText().toString().trim(),
                            binding.editProductDetails.getText().toString().trim(),
                            binding.description.getText().toString().trim()
                    );
                }

            }
        });

        return view;
    }

    public void BlukOrder(String org_name, String quantity, String email, String contact_no, String product_dtls,
                          String message) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Order Submit Please Wait ....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.BulkMail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String messages = jsonObject.getString("message");
                    Toast.makeText(getActivity(), messages, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("org_name", org_name);
                params.put("quantity", quantity);
                params.put("email", email);
                params.put("contact_no", contact_no);
                params.put("product_dtls", product_dtls);
                params.put("message", message);
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
