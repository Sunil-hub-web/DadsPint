package co.in.dadspint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.ViewModel> {

    ArrayList<ViewCartModel> viewCartModelArray;
    Context context;
    double Total,price,quantity,salesPrice,priceDet,d_Totalprice,d_Totalprice1,d_Totalprice2;
    String userId,productid,quenty,quantity12,price12,price_total;
    int count_value;
    SessionManager sessionManager;

    public ViewCartAdapter(ArrayList<ViewCartModel> viewCartModelArray, FragmentActivity activity) {

        this.viewCartModelArray = viewCartModelArray;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewCartAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistdet,parent,false);
        return new ViewModel(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewCartAdapter.ViewModel holder, @SuppressLint("RecyclerView") int position) {

        sessionManager = new SessionManager(context);
        userId = sessionManager.getUSERID();

        ViewCartModel viewcart = viewCartModelArray.get(position);

        String imageUrl = "https://dadspint.com/uploads/" + viewcart.getPrimary_image();
        Glide.with(context).load(imageUrl).into(holder.productImage);

        holder.product_Name.setText(viewcart.product_name);

        if (viewcart.getVariation_names().equals("")){
            holder.unit_Name.setVisibility(View.GONE);
        }else{
            holder.unit_Name.setVisibility(View.VISIBLE);
            holder.unit_Name.setText(viewcart.getVariation_names());
        }

        holder.tv_count1.setText(viewcart.getQuantity());
        holder.quentity.setText("QTY : "+viewcart.getQuantity()+" x "+"Rs : "+viewcart.getProduct_price());

        String price1 = viewcart.getProduct_price();
        String quantity1 = viewcart.getQuantity();

        price = Double.parseDouble(price1);
        quantity = Double.parseDouble(quantity1);

        Total = price * quantity;

        price_total = String.valueOf(Total);
        holder.totalPrice.setText(price_total);

        holder.tv_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity12 = holder.tv_count1.getText().toString().trim();

                holder.quentity.setText("QTY : "+quantity12+" x "+"Rs : "+viewcart.getProduct_price());

                if (quantity12.equals("1")){

                    holder.quentity.setText("QTY : "+quantity12+" x "+"Rs : "+viewcart.getProduct_price());

                }else{

                    holder.linearLayout(false);
                    quantity12 = holder.tv_count1.getText().toString().trim();

                    holder.quentity.setText("QTY : "+quantity12+" x "+"Rs : "+viewcart.getProduct_price());

                    count_value = Integer.parseInt(quantity12);
                    price12 = viewcart.getProduct_price();
                    productid = viewcart.getProduct_id();

                    price = Double.parseDouble(price1);
                    quantity = Double.parseDouble(quantity12);

                    Total = price * quantity;

                    price_total = String.valueOf(Total);
                    holder.totalPrice.setText(price_total);

                    String carttotal = CartFragment.text_totalPrice.getText().toString();
                    salesPrice = Double.parseDouble(carttotal);
                    priceDet = salesPrice - price;
                    String strpriceDet = String.valueOf(priceDet);
                    CartFragment.text_totalPrice.setText(strpriceDet);
                    CartFragment.text_subTotalPrice.setText(strpriceDet);

                    addToCart(userId,productid,quantity12,"",price12);

                    quantity12 = "";
                    count_value = 0;
                    price12 = "";
                    productid = "";
                    price = 0.0;
                    quantity = 0.0;
                    Total = 0.0;
                    salesPrice = 0.0;
                    priceDet = 0.0;
                }
                
            }
        });

        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.linearLayout(true);

                quantity12 = holder.tv_count1.getText().toString().trim();

                holder.quentity.setText("QTY : "+quantity12+" x "+"Rs : "+viewcart.getProduct_price());

                count_value = Integer.parseInt(quantity12);
                price12 = viewcart.getProduct_price();
                productid = viewcart.getProduct_id();

                price = Double.parseDouble(price1);
                quantity = Double.parseDouble(quantity12);

                Total = price * quantity;

                price_total = String.valueOf(Total);
                holder.totalPrice.setText(price_total);

                String carttotal = CartFragment.text_totalPrice.getText().toString();
                salesPrice = Double.parseDouble(carttotal);
                priceDet = salesPrice + price;
                String strpriceDet = String.valueOf(priceDet);
                CartFragment.text_totalPrice.setText(strpriceDet);
                CartFragment.text_subTotalPrice.setText(strpriceDet);

                addToCart(userId,productid,quantity12,"",price12);

                quantity12 = "";
                count_value = 0;
                price12 = "";
                productid = "";
                price = 0.0;
                quantity = 0.0;
                Total = 0.0;
                salesPrice = 0.0;
                priceDet = 0.0;

            }
        });

        holder.img_Delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                String cartId = viewcart.getCart_id();

                String strTotalprice = holder.totalPrice.getText().toString().trim();
                String strTotalprice1 = CartFragment.text_totalPrice.getText().toString().trim();

                d_Totalprice = Double.parseDouble(strTotalprice);
                d_Totalprice1 = Double.parseDouble(strTotalprice1);

                d_Totalprice2 = d_Totalprice1 - d_Totalprice;

                String strTotalprice2 = String.valueOf(d_Totalprice2);
                CartFragment.text_totalPrice.setText(strTotalprice2);
                CartFragment.text_subTotalPrice.setText(strTotalprice2);

                viewCartModelArray.remove(position);
                notifyDataSetChanged();
                removeCart(cartId);

                int size = viewCartModelArray.size();

                BadgeDrawable badge = DeshBoardActivity.bottomNavigation.getOrCreateBadge(R.id.cart);//R.id.action_add is menu id
                badge.setNumber(size);
                badge.setBackgroundColor(ContextCompat.getColor(context,R.color.bluedrack));

                d_Totalprice = 0.0;
                d_Totalprice1 = 0.0;
                d_Totalprice2 = 0.0;

            }
        });

    }

    @Override
    public int getItemCount() {
        return viewCartModelArray.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView product_Name,unit_Name,quentity,totalPrice,tv_minus1,tv_count1,tv_plus;
        RelativeLayout img_Delete;
        public ViewModel(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            product_Name = itemView.findViewById(R.id.product_Name);
            unit_Name = itemView.findViewById(R.id.unit_Name);
            quentity = itemView.findViewById(R.id.quentity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            tv_minus1 = itemView.findViewById(R.id.tv_minus1);
            tv_count1 = itemView.findViewById(R.id.tv_count1);
            tv_plus = itemView.findViewById(R.id.tv_plus);
            img_Delete = itemView.findViewById(R.id.img_Delete);
        }
        private void linearLayout(Boolean x) {
            int y = Integer.parseInt(tv_count1.getText().toString());
            if (x) {
                y++;
                tv_count1.setText(String.valueOf(y));
            } else {
                y--;
                if (y <= 0) {
                    //tv_count1.setText("1");
                } else {
                    tv_count1.setText(String.valueOf(y));
                }
            }
        }
    }

    public void addToCart(String user_id,String product_id,String qty,String variation_id,String price){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Add To Cart Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.addto_to_cart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");

                    if (status.equals("200")){

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        Toast.makeText(context, status_message, Toast.LENGTH_SHORT).show();

                    }else{

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        Toast.makeText(context, status_message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("product_id",product_id);
                params.put("qty",qty);
                params.put("variation_id",variation_id);
                params.put("price",price);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    public void removeCart(String cart_id){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Delete Cart Item Wait....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Remove_cart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");

                    if (status.equals("200")){

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        Toast.makeText(context, status_message, Toast.LENGTH_SHORT).show();

                        cart_count(userId);

                    }else{

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        Toast.makeText(context, status_message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("cart_id",cart_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    public void cart_count(String user_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.cart_count, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")) {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String cart_count = jsonObject_message.getString("cart_count");
                        JSONObject jsonObject_cart_count = new JSONObject(cart_count);
                        String total_cart = jsonObject_cart_count.getString("total_cart");

                        int int_total_cart = Integer.parseInt(total_cart);

                        BadgeDrawable badge = DeshBoardActivity.bottomNavigation.getOrCreateBadge(R.id.cart);//R.id.action_add is menu id
                        badge.setNumber(int_total_cart);
                        badge.setBackgroundColor(ContextCompat.getColor(context,R.color.bluedrack));

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String cart_count = jsonObject_message.getString("cart_count");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText( context, "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("user_id", user_id);

                Log.d("addressparameterlist1111",params.toString());

                return params;


            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
