package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class WishlistFragment extends Fragment {

    ArrayList<WishlistModel> wishlistModels = new ArrayList<>();
    RecyclerView wishlistRecyclerView;
    WishlistAdapter wishlistAdapter;
    SessionManager sessionManager;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wishist_fragment,container,false);

        wishlistRecyclerView = view.findViewById(R.id.wishlistRecyclerView);

        sessionManager = new SessionManager(getContext());
        userId = sessionManager.getUSERID();

        getall_product(userId);

        return view;
    }

    public void getall_product(String user_id) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Product Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.GetWishlist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")) {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        if (responsecode.equals("00")) {

                            JSONArray jsonArray_product = new JSONArray(statusArray);

                            for (int i = 0; i < jsonArray_product.length(); i++) {

                                JSONObject jsonObject_product = jsonArray_product.getJSONObject(i);

                                String wishlist_id = jsonObject_product.getString("wishlist_id");
                                String user_id = jsonObject_product.getString("user_id");
                                String product_id = jsonObject_product.getString("product_id");
                                String product_name = jsonObject_product.getString("product_name");
                                String primary_image = jsonObject_product.getString("primary_image");
                                String regular_price = jsonObject_product.getString("regular_price");
                                String sales_price = jsonObject_product.getString("sales_price");
                                String product_type = jsonObject_product.getString("product_type");

                                WishlistModel wishlistModel = new WishlistModel(
                                       wishlist_id,user_id,product_id,product_name,primary_image,regular_price,
                                        sales_price,product_type);

                                wishlistModels.add(wishlistModel);
                            }

                            wishlistAdapter = new WishlistAdapter(wishlistModels, getActivity());
                            //gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            wishlistRecyclerView.setLayoutManager(gridLayoutManager);
                            wishlistRecyclerView.setHasFixedSize(true);
                            wishlistRecyclerView.setAdapter(wishlistAdapter);
                        }
                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }


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
                params.put("cust_id", user_id);
                Log.d("cust_id", user_id);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}
