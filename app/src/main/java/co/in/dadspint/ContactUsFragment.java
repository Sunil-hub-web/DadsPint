package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsFragment extends Fragment {

    TextView text_address,text_phono,text_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contactus_fragment,container,false);

        text_address = view.findViewById(R.id.text_address);
        text_phono = view.findViewById(R.id.text_phono);
        text_email = view.findViewById(R.id.text_email);

        aboutAsData();

        return view;
    }

    public void aboutAsData(){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Contact Us Details");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.cms, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")){

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusdata = jsonObject_message.getString("status");
                        JSONObject jsonObject_statusdata = new JSONObject(statusdata);
                        String cms = jsonObject_statusdata.getString("cms");
                        JSONArray jsonArray_cms = new JSONArray(cms);

                        for (int i=0;i<jsonArray_cms.length();i++){

                            JSONObject jsonObject_cms = jsonArray_cms.getJSONObject(i);
                            String page_name = jsonObject_cms.getString("page_name");

                            if (page_name.equals("Contact Us")){

                                String address = jsonObject_cms.getString("address");
                                String phone = jsonObject_cms.getString("phone");
                                String email = jsonObject_cms.getString("email");

                                text_address.setText(address);
                                text_phono.setText(Html.fromHtml(phone));
                                text_email.setText(Html.fromHtml(email));

                            }
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
