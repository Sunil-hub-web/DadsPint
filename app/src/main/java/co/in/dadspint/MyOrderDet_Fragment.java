package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import co.in.dadspint.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrderDet_Fragment extends Fragment {

    RecyclerView myOrderRecyclerView;
    OrderListAdapter orderListAdapter;
    ArrayList<OrderListModel> orderListModels = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.myorderdetails_fragment,container,false);
        myOrderRecyclerView = view.findViewById(R.id.myOrderRecyclerView);
        sessionManager = new SessionManager(getActivity());
        userId = sessionManager.getUSERID();
        orderDetails(userId);
        return view;
    }

    public void orderDetails(String user_id){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Order Details Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.getAllOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")){

                        orderListModels.clear();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("message");
                        String data = jsonObject.getString("data");

                        JSONArray jsonstatues = new JSONArray(data);

                        Toast.makeText(getActivity(), messages, Toast.LENGTH_SHORT).show();

                        for (int i = 0;i<jsonstatues.length();i++){

                            JSONObject jsonObject_order = jsonstatues.getJSONObject(i);

                            String order_id = jsonObject_order.getString("order_id");
                            String order_date = jsonObject_order.getString("order_date");
                            String status1 = jsonObject_order.getString("status");
                            String total_qty = jsonObject_order.getString("total_qty");
                            String total_price = jsonObject_order.getString("total_price");

                            OrderListModel orderListModel = new OrderListModel(order_id,order_date,status1,total_qty,total_price);
                            orderListModels.add(orderListModel);
                        }

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        orderListAdapter = new OrderListAdapter(getActivity(),orderListModels);
                        myOrderRecyclerView.setHasFixedSize(true);
                        myOrderRecyclerView.setLayoutManager(linearLayoutManager);
                        myOrderRecyclerView.setAdapter(orderListAdapter);

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
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
