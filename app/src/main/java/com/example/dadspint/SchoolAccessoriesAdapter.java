package com.example.dadspint;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SchoolAccessoriesAdapter extends RecyclerView.Adapter<SchoolAccessoriesAdapter.ViewHolder> {

    ArrayList<ProductDataModel> productDataModel;
    Context context;
    SessionManager sessionManager;
    String userId,productid,quenty,quantity,price,quantity1;
    int count_value;
    public SchoolAccessoriesAdapter(ArrayList<ProductDataModel> productDataModel2, FragmentActivity activity) {

        this.productDataModel = productDataModel2;
        this.context = activity;
    }

    @NonNull
    @Override
    public SchoolAccessoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.schooluniformpage,parent,false);
        return new SchoolAccessoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolAccessoriesAdapter.ViewHolder holder, int position) {

        sessionManager = new SessionManager(context);
        userId = sessionManager.getUSERID();

        ProductDataModel product = productDataModel.get(position);

        holder.uniform_name1.setText(product.product_name);
        holder.restt_price1.setText("Rs. "+product.sales_price);
        holder.restt_price2.setText("Rs. "+product.regular_price);

        holder.restt_price2.setPaintFlags(holder.restt_price2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String imageUrl = "https://dadspint.com/uploads/"+product.getPrimary_image();
        Glide.with(context).load(imageUrl).into(holder.imag_uniform);

        if (product.getProdType().equals("0")){

            holder.addtext1.setText("Add To cart");

        }else {

            holder.addtext1.setText("Select Product");
        }

        holder.relImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.addtext1.getText().toString().trim().equals("Add To cart")) {

                    Toast.makeText(context, "Varition Not Found", Toast.LENGTH_SHORT).show();

                } else {

                    SingleProductFragment singleProductFragment = new SingleProductFragment();
                    Bundle args = new Bundle();
                    args.putString("productId", product.getProduct_id());
                    args.putString("productName", product.getProduct_name());
                    singleProductFragment.setArguments(args);
                    FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framLayout, singleProductFragment); // Add your fragment class
                    transaction.addToBackStack(null);
                    transaction.commit();

                 /*   Intent intent = new Intent(context, SingleProductDesc.class);
                    intent.putExtra("productId", product.getProduct_id());
                    intent.putExtra("productName", product.getProduct_name());
                    context.startActivity(intent);*/
                }

            }
        });

        holder.imag_uniform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.addtext1.getText().toString().trim().equals("Add To cart")) {

                    Toast.makeText(context, "Varition Not Found", Toast.LENGTH_SHORT).show();

                } else {

                    SingleProductFragment singleProductFragment = new SingleProductFragment();
                    Bundle args = new Bundle();
                    args.putString("productId", product.getProduct_id());
                    args.putString("productName", product.getProduct_name());
                    singleProductFragment.setArguments(args);
                    FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.framLayout, singleProductFragment); // Add your fragment class
                    transaction.addToBackStack(null);
                    transaction.commit();

              /*      Intent intent = new Intent(context, SingleProductDesc.class);
                    intent.putExtra("productId", product.getProduct_id());
                    intent.putExtra("productName", product.getProduct_name());
                    context.startActivity(intent);*/
                }
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

                holder.tv_count.setText("1");

                if (product.getProdType().equals("0")) {

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

                 /*   Intent intent = new Intent(context, SingleProductDesc.class);
                    intent.putExtra("productId", product.getProduct_id());
                    intent.putExtra("productName", product.getProduct_name());
                    context.startActivity(intent);*/

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return productDataModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imag_uniform;
        TextView uniform_name1,restt_price1,restt_price2,tv_minus,tv_count,tv_plus,addtext1,tv_count1;
        LinearLayout lin_addCart,lin_add_cart;
        RelativeLayout relImageClick,addlay1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addlay1 = (RelativeLayout) itemView.findViewById(R.id.addlay1);
            addtext1 = (TextView) itemView.findViewById(R.id.addtext1);
            tv_minus = (TextView) itemView.findViewById(R.id.tv_minus1);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count1);
            tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
            lin_addCart = (LinearLayout) itemView.findViewById(R.id.lin_addCart);
            lin_add_cart = (LinearLayout) itemView.findViewById(R.id.lin_add_cart);
            uniform_name1 = (TextView) itemView.findViewById(R.id.uniform_name1);
            restt_price1 = (TextView) itemView.findViewById(R.id.restt_price1);
            restt_price2 = (TextView) itemView.findViewById(R.id.restt_price2);
            imag_uniform = (ImageView) itemView.findViewById(R.id.imag_uniform);
            relImageClick = (RelativeLayout) itemView.findViewById(R.id.relImageClick);
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
}
