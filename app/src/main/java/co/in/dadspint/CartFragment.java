package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.google.android.material.badge.BadgeDrawable;

import co.in.dadspint.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    RecyclerView recyclerCartPage;
    RelativeLayout rel_gotoCheckout,rel_totalprice;
    public static TextView text_subTotalPrice,text_deliveryPrice,text_totalPrice;
    ArrayList<ViewCartModel> viewCartModelArray = new ArrayList<>();
    ViewCartAdapter viewCartAdapter;
    LinearLayoutManager linearLayoutManager;
    double totalprice, sales_Price, quanTity, totalAmount = 0.0, shipCharge,taxCharge;
    LinearLayout cartempty;
    SessionManager sessionManager;
    String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment,container,false);

        recyclerCartPage = view.findViewById(R.id.recyclerCartPage);
        rel_gotoCheckout = view.findViewById(R.id.rel_gotoCheckout);
        text_subTotalPrice = view.findViewById(R.id.text_subTotalPrice);
        cartempty = view.findViewById(R.id.cartempty);
        rel_totalprice = view.findViewById(R.id.rel_totalprice);
        text_totalPrice = view.findViewById(R.id.text_totalPrice);
        //text_deliveryPrice = view.findViewById(R.id.text_deliveryPrice);

        sessionManager = new SessionManager(getActivity());
        userId = sessionManager.getUSERID();

        showProduct(userId);

        rel_gotoCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CheckOut_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                DeshBoardActivity.menu.setVisibility(View.GONE);
                DeshBoardActivity.backimage.setVisibility(View.VISIBLE);
                DeshBoardActivity.image_search.setVisibility(View.GONE);
            }
        });

        return view;

    }

    public void showProduct(String userId){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Show Product Details Wait ....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.View_cart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")){

                        viewCartModelArray.clear();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");
                        JSONObject jsonstatues = new JSONObject(statusArray);

                        String cart_items = jsonstatues.getString("cart_items");
                        JSONArray jsonArray_item = new JSONArray(cart_items);
                        for (int i=0;i<jsonArray_item.length();i++){

                            JSONObject jsonObject_items = jsonArray_item.getJSONObject(i);

                            String cart_id = jsonObject_items.getString("cart_id");
                            String user_id = jsonObject_items.getString("user_id");
                            String product_id = jsonObject_items.getString("product_id");
                            String product_name = jsonObject_items.getString("product_name");
                            String quantity = jsonObject_items.getString("quantity");
                            String Product_price = jsonObject_items.getString("Product_price");
                            String variation_id = jsonObject_items.getString("variation_id");
                            String product_type = jsonObject_items.getString("product_type");
                            String primary_image = jsonObject_items.getString("primary_image");
                            String variation_names = jsonObject_items.getString("variation_names");

                            ViewCartModel viewCartModel = new ViewCartModel(
                                    cart_id,user_id,product_id,product_name,quantity,Product_price,variation_id,
                                    product_type,primary_image,variation_names
                            );
                            viewCartModelArray.add(viewCartModel);

                            sales_Price = Double.valueOf(Product_price);

                            quanTity = Double.valueOf(quantity);

                            totalprice = sales_Price * quanTity;

                            totalAmount = totalAmount + totalprice;

                        }

                        if(viewCartModelArray.size() == 0){

                            cartempty.setVisibility(View.VISIBLE);
                            rel_gotoCheckout.setVisibility(View.GONE);
                            recyclerCartPage.setVisibility(View.GONE);
                            rel_totalprice.setVisibility(View.GONE);

                        }else {

                            cartempty.setVisibility(View.GONE);
                            rel_gotoCheckout.setVisibility(View.VISIBLE);
                            recyclerCartPage.setVisibility(View.VISIBLE);
                            rel_totalprice.setVisibility(View.VISIBLE);

                            int size =  viewCartModelArray.size();

                            String str_size = String.valueOf(size);

                            //text_ItemCount.setText(str_size);

                            String total_price = String.valueOf(totalAmount);

                            text_subTotalPrice.setText(total_price);
                            text_totalPrice.setText(total_price);

                            viewCartAdapter = new ViewCartAdapter(viewCartModelArray,getActivity());
                            linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            recyclerCartPage.setHasFixedSize(true);
                            recyclerCartPage.setLayoutManager(linearLayoutManager);
                            recyclerCartPage.setAdapter(viewCartAdapter);
                        }

                    }

                }catch (JSONException e){
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", userId);
                    return params;
                }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
