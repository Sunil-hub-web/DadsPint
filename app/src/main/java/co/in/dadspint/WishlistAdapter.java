package co.in.dadspint;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    Context context;
    ArrayList<WishlistModel> wishlistModels;
    String quantity,userId,productid,quenty,price;
    int count_value;
    SessionManager sessionManager;
    public WishlistAdapter(ArrayList<WishlistModel> wishlistModels, FragmentActivity activity) {

        this.wishlistModels = wishlistModels;
        this.context = activity;

    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.schooluniformpage, parent, false);
        return new WishlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {

        sessionManager = new SessionManager(context);
        userId = sessionManager.getUSERID();

        WishlistModel product = wishlistModels.get(position);

        holder.uniform_name1.setText(product.product_name);
        holder.restt_price1.setText("Rs. " + product.sales_price);
        holder.restt_price2.setText("Rs. " + product.regular_price);

        String str_sales_price = String.valueOf(product.sales_price);
        String str_regular_price = String.valueOf(product.regular_price);

        if (!str_sales_price.equals("null")){

            if (!str_regular_price.equals("null")){

                Double price1 = Double.valueOf(product.regular_price);
                Double price2 = Double.valueOf(product.sales_price);
                Double price3 = price1 - price2;
                Double price6 = price3 / price1;
                Log.d("pricedetails",price1+"-"+price2+"/"+price1+"="+price6);
                Double price4 = price6 * 100;
                DecimalFormat df = new DecimalFormat("#.00");
                String price5 = df.format(price4);

                holder.parcentage.setText(price5+" %");

            }else{

                holder.parcentage.setText("0"+" %");
            }
        }

        holder.restt_price2.setPaintFlags(holder.restt_price2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (product.getProduct_type().equals("0")) {

            holder.addtext1.setText("Add To cart");

        } else {

            holder.addtext1.setText("Select Product");
        }

        String imageUrl = "https://dadspint.com/uploads/" + product.getPrimary_image();
        Glide.with(context).load(imageUrl).into(holder.imag_uniform);

        holder.relImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   if (holder.addtext1.getText().toString().trim().equals("Add To cart")) {

                    Toast.makeText(context, "Varition Not Found", Toast.LENGTH_SHORT).show();

                } else {*/

                SingleProductFragment singleProductFragment = new SingleProductFragment();
                Bundle args = new Bundle();
                args.putString("productId", product.getProduct_id());
                args.putString("productName", product.getProduct_name());
                singleProductFragment.setArguments(args);
                FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framLayout, singleProductFragment); // Add your fragment class
                transaction.addToBackStack(null);
                transaction.commit();

                /*    Intent intent = new Intent(context, SingleProductDesc.class);
                    intent.putExtra("productId", product.getProduct_id());
                    intent.putExtra("productName", product.getProduct_name());
                    context.startActivity(intent);*/
                // }
            }
        });

        holder.imag_uniform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if (holder.addtext1.getText().toString().trim().equals("Add To cart")) {

                    Toast.makeText(context, "Varition Not Found", Toast.LENGTH_SHORT).show();

                } else {*/

                SingleProductFragment singleProductFragment = new SingleProductFragment();
                Bundle args = new Bundle();
                args.putString("productId", product.getProduct_id());
                args.putString("productName", product.getProduct_name());
                singleProductFragment.setArguments(args);
                FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framLayout, singleProductFragment); // Add your fragment class
                transaction.addToBackStack(null);
                transaction.commit();
/*
                    Intent intent = new Intent(context, SingleProductDesc.class);
                    intent.putExtra("productId", product.getProduct_id());
                    intent.putExtra("productName", product.getProduct_name());
                    context.startActivity(intent);*/
                // }
            }
        });

        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.linearLayout(false);

                quantity = holder.tv_count.getText().toString().trim();
                count_value = Integer.valueOf(holder.tv_count.getText().toString());
                price = product.getSales_price();

                addToCart(userId,productid,quantity,"",price);
            }
        });

        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.linearLayout(true);

                quantity = holder.tv_count.getText().toString().trim();
                count_value = Integer.valueOf(holder.tv_count.getText().toString());
                price = product.getSales_price();

                addToCart(userId,productid,quantity,"",price);

            }
        });

        holder.lin_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (product.getProduct_type().equals("0")) {

                    holder.lin_addCart.setVisibility(View.GONE);
                    holder.lin_add_cart.setVisibility(View.VISIBLE);

                    quenty = holder.tv_count.getText().toString().trim();
                    productid = product.getProduct_id();
                    price = product.getSales_price();

                    addToCart(userId,productid,quenty,"",price);

                    Log.d("adtocartdetails",userId+"   "+productid+"  "+quenty);

                }else{

                    SingleProductFragment singleProductFragment = new SingleProductFragment();
                    Bundle args = new Bundle();
                    args.putString("productId", product.getProduct_id());
                    args.putString("productName", product.getProduct_name());
                    singleProductFragment.setArguments(args);
                    FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framLayout, singleProductFragment); // Add your fragment class
                    transaction.addToBackStack(null);
                    transaction.commit();

                /*    Intent intent = new Intent(context, SingleProductDesc.class);
                    intent.putExtra("productId", product.getProduct_id());
                    intent.putExtra("productName", product.getProduct_name());
                    context.startActivity(intent);*/

                }

            }
        });


    }

    @Override
    public int getItemCount() {

        return wishlistModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imag_uniform;
        TextView uniform_name1, restt_price1, restt_price2, tv_minus, tv_count, tv_plus, addtext1,parcentage;
        LinearLayout lin_addCart, lin_add_cart;
        RelativeLayout relImageClick, addlay1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addtext1 = itemView.findViewById(R.id.addtext1);
            tv_minus = itemView.findViewById(R.id.tv_minus);
            tv_count = itemView.findViewById(R.id.tv_count1);
            tv_plus = itemView.findViewById(R.id.tv_plus);
            lin_addCart = itemView.findViewById(R.id.lin_addCart);
            lin_add_cart = itemView.findViewById(R.id.lin_add_cart);
            uniform_name1 = itemView.findViewById(R.id.uniform_name1);
            restt_price1 = itemView.findViewById(R.id.restt_price1);
            parcentage = itemView.findViewById(R.id.parcentage);
            restt_price2 = itemView.findViewById(R.id.restt_price2);
            imag_uniform = itemView.findViewById(R.id.imag_uniform);
            relImageClick = itemView.findViewById(R.id.relImageClick);
            addlay1 = itemView.findViewById(R.id.addlay1);
        }

        private void linearLayout(Boolean x) {
            int y = Integer.parseInt(tv_count.getText().toString());
            if (x) {
                y++;
                tv_count.setText(String.valueOf(y));
            } else {
                y--;
                if (y <= 0) {
                    tv_count.setText("1");
                } else {
                    tv_count.setText(String.valueOf(y));
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

                        cart_count(user_id);
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

                Log.d("addressparameterlist",params.toString());

                return params;


            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
