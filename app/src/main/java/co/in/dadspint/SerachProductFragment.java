package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

public class SerachProductFragment extends Fragment {

    SearchView searchView;
    RecyclerView searchProductRecycler;
    SessionManager sessionManager;
    String userId;
    ArrayList<ProductDataModel> productDataModel1 = new ArrayList<>();
    SchoolUniformAdapter1 schoolUniformAdapter;
    GridLayoutManager gridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.serach_fragment,container,false);

        searchView = view.findViewById(R.id.searchView);
        searchProductRecycler = view.findViewById(R.id.searchProductRecycler);

        DeshBoardActivity.realBack.setVisibility(View.GONE);

        sessionManager = new SessionManager(getActivity());
        userId = sessionManager.getUSERID();
        getall_product();
        //searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                schoolUniformAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

    public void getall_product() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Product Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.searchproduct, new Response.Listener<String>() {
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

                            JSONObject jsonstatues = new JSONObject(statusArray);

                            String product_data = jsonstatues.getString("product_data");

                            JSONArray jsonArray_product = new JSONArray(product_data);

                            for (int i = 0; i < jsonArray_product.length(); i++) {

                                JSONObject jsonObject_product = jsonArray_product.getJSONObject(i);

                                String product_id = jsonObject_product.getString("product_id");
                                String product_name = jsonObject_product.getString("product_name");
                                String primary_image = jsonObject_product.getString("primary_image");
                                String vendor_id = jsonObject_product.getString("vendor_id");
                                String trendingg = jsonObject_product.getString("trendingg");
                                String today_dealing_date_time = jsonObject_product.getString("today_dealing_date_time");
                                String product_type = jsonObject_product.getString("product_type");
                                String regular_price = jsonObject_product.getString("regular_price");
                                String sales_price = jsonObject_product.getString("sales_price");
                                String stock = jsonObject_product.getString("stock");
                                String city_name = jsonObject_product.getString("city_name");
                                String city_id = jsonObject_product.getString("city_id");
                                String school_id = jsonObject_product.getString("school_id");
                                String school_name = jsonObject_product.getString("school_name");
                                String class_id = jsonObject_product.getString("class_id");
                                String class_name = jsonObject_product.getString("class_name");
                                String brands_id = jsonObject_product.getString("brands_id");
                                String brands_name = jsonObject_product.getString("brands_name");
                                String prodType = jsonObject_product.getString("prodType");
                                String description = jsonObject_product.getString("description");

                                ProductDataModel product_DataModel1 = new ProductDataModel(
                                        product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regular_price,
                                        sales_price, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description, prodType);

                                productDataModel1.add(product_DataModel1);
                            }

                            schoolUniformAdapter = new SchoolUniformAdapter1(productDataModel1,getActivity());
                            //gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            searchProductRecycler.setLayoutManager(gridLayoutManager);
                            searchProductRecycler.setHasFixedSize(true);
                            searchProductRecycler.setAdapter(schoolUniformAdapter);
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
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}
