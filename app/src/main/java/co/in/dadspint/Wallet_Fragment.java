package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet_Fragment extends Fragment {

    RecyclerView showWalletRecycler;
    LinearLayoutManager linearLayoutManager;
    ArrayList<WalletModel> walletModels = new ArrayList<>();
    SessionManager sessionManager;
    WalletAdapter walletAdapter;
    Double cr_balance,dr_balance,crdr_balance,totcr_balance = 0.0,totdr_balance = 0.0;
    TextView showAmount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.walletfragment,container,false);

        showWalletRecycler = view.findViewById(R.id.showWalletRecycler);
        showAmount = view.findViewById(R.id.showAmount);

        sessionManager = new SessionManager(getContext());
        userwallet(sessionManager.getUSERID());
        return view;
    }

    public void userwallet(String user_id){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Wallet Details Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.walletdtls, new Response.Listener<String>() {
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
                        String statusdat = jsonObject_message.getString("status");

                        if (responsecode.equals("00")){

                            JSONObject jsonObject1_status = new JSONObject(statusdat);
                            String wallet = jsonObject1_status.getString("wallet");
                            JSONArray jsonArray_wallet = new JSONArray(wallet);

                            for (int i=0;i<jsonArray_wallet.length();i++){

                                JSONObject jsonObject_wallet = jsonArray_wallet.getJSONObject(i);

                                String wallet_id = jsonObject_wallet.getString("wallet_id");
                                String user_id = jsonObject_wallet.getString("user_id");
                                String amount = jsonObject_wallet.getString("wallet_amount");
                                String payment_type = jsonObject_wallet.getString("wallet_status");
                                String created_date = jsonObject_wallet.getString("created_date");

                                if (payment_type.equals("1")){

                                    cr_balance = Double.parseDouble(amount);
                                    totcr_balance = cr_balance + totcr_balance;

                                    Log.d("paymentdet",cr_balance+","+totcr_balance);


                                }else{

                                    dr_balance = Double.parseDouble(amount);
                                    totdr_balance = dr_balance + totdr_balance;

                                    Log.d("paymentdet1",dr_balance+","+totdr_balance);

                                }

                                WalletModel walletModel = new WalletModel(
                                        wallet_id,amount,payment_type,user_id,created_date
                                );

                                walletModels.add(walletModel);
                            }
                            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            walletAdapter = new WalletAdapter(walletModels,getActivity());
                            showWalletRecycler.setLayoutManager(linearLayoutManager);
                            showWalletRecycler.setHasFixedSize(true);
                            showWalletRecycler.setItemAnimator(new DefaultItemAnimator());
                            showWalletRecycler.setAdapter(walletAdapter);

                            crdr_balance = totcr_balance - totdr_balance;

                            showAmount.setText(String.valueOf(crdr_balance));

                            sessionManager.setWalletAmount(crdr_balance.floatValue());

                        }else{

                            Toast.makeText(getContext(), statusdat, Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusdat = jsonObject_message.getString("status");
                        Toast.makeText(getContext(), statusdat, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("cust_id",user_id);
                Log.d("userid",user_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
