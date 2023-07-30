package co.in.dadspint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import co.in.dadspint.R;

import com.google.android.material.badge.BadgeDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetAdapter extends RecyclerView.Adapter<OrderDetAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderDetails_Model> product;
    Dialog dialog;
    String reason = "";

    public OrderDetAdapter(ArrayList<OrderDetails_Model> product, Context context) {

        this.context = context;
        this.product = product;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetailsdata, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderDetails_Model productdet = product.get(position);

        String image = productdet.getImg();

        if (image.equals("")) {
        } else {

            Picasso.with(context).load(image).into(holder.productImage);
        }


        holder.text_ProdectName.setText(productdet.getProductname());
        holder.totalunit.setText("QTY :" + productdet.getQty());

        double getQuenty = Double.valueOf(productdet.getQty());
        double getPrice = Double.valueOf(productdet.getPrice());

        double totalprice = getPrice * getQuenty;

        holder.totalPrice.setText(", " + totalprice);

        holder.text_orderId.setText("Id : " + productdet.getOrder_id());
        holder.text_orderDate.setText("Date :" + productdet.getCreated_date());
        //holder.text_orderStatus.setText("Status :" + productdet.getStatus());
        String imageUrl = "https://dadspint.com/uploads/" + productdet.getImg();
        Glide.with(context).load(imageUrl).into(holder.productImage);

        if (productdet.getStatus().equals("0")){

            holder.btn_cancelOrder.setText("Cancel");

        } else if (productdet.getStatus().equals("5")) {

            holder.btn_cancelOrder.setText("Exchange");

        }else if (productdet.getStatus().equals("3")) {

            holder.btn_cancelOrder.setText("Cancel");

        }

        if (productdet.getStatus().equals("5")){

            holder.text_orderStatus.setText("Order Delivired");
            holder.text_orderStatus.setTextColor(ContextCompat.getColor(context,R.color.payment7));

        } else if (productdet.getStatus().equals("0")) {

            holder.text_orderStatus.setText("New Order");
            holder.text_orderStatus.setTextColor(ContextCompat.getColor(context,R.color.blue600));

        }else if (productdet.getStatus().equals("3")) {

            holder.text_orderStatus.setText("Cancled Order");
            holder.text_orderStatus.setTextColor(ContextCompat.getColor(context,R.color.red));

        }else{

            holder.text_orderStatus.setText("Exchange Order");
            holder.text_orderStatus.setTextColor(ContextCompat.getColor(context,R.color.red));
        }

        String cancel_status = String.valueOf(productdet.getCancel_status());

        if (cancel_status.equals("canceled")){

           // cancel_reason(productdet.orders_id);
            holder.lin_cancleResion.setVisibility(View.VISIBLE);
            holder.btn_cancelOrder.setText("Canceled");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.cancel_reason, new Response.Listener<String>() {
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
                            String cancel_reason = jsonObject_message.getString("cancel_reason");
                            JSONArray jsonObject_cart_count = new JSONArray(cancel_reason);
                            for (int i=0;i<jsonObject_cart_count.length();i++){

                                JSONObject jsonObject1 = jsonObject_cart_count.getJSONObject(0);
                                reason = jsonObject1.getString("reason");
                                holder.text_cancleResion.setText(reason);
                            }

                        } else {

                            String error = jsonObject.getString("error");
                            String messages = jsonObject.getString("messages");
                            JSONObject jsonObject_message = new JSONObject(messages);
                            String responsecode = jsonObject_message.getString("responsecode");
                            String cancel_reason = jsonObject_message.getString("cancel_reason");

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
                    params.put("orders_id",productdet.orders_id);

                    Log.d("addressparameterlist",params.toString());

                    return params;


                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);


        }

        /*str_quantity = productdet.getProductQuantity();
        str_productPrice = productdet.getProductPrice();

        d_quantity = Double.valueOf(str_quantity);
        d_productPrice = Double.valueOf(str_productPrice);

        d_TotalPrice = d_quantity * d_productPrice;
        str_TotalPrice = String.valueOf(d_TotalPrice);

        holder.totalPrice.setText(str_TotalPrice);*/

        holder.btn_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.btn_cancelOrder.getText().toString().trim().equals("Cancel")){

                    ordercancle_Dialog(productdet.getUser_id(),productdet.getOrder_id(),productdet.getOrders_id());

                }else{

                    orderExchange_Dialog(productdet.getUser_id(),productdet.getOrder_id(),productdet.getOrders_id());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_orderId, text_orderDate, text_orderStatus, totalunit, totalPrice, text_ProdectName,
                btn_cancelOrder,text_cancleResion;
        ImageView productImage;
        LinearLayout lin_cancleResion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text_orderId = itemView.findViewById(R.id.text_orderId);
            text_orderDate = itemView.findViewById(R.id.text_orderDate);
            text_orderStatus = itemView.findViewById(R.id.text_orderStatus);
            totalunit = itemView.findViewById(R.id.totalunit);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            productImage = itemView.findViewById(R.id.productImage);
            text_ProdectName = itemView.findViewById(R.id.text_ProdectName);
            btn_cancelOrder = itemView.findViewById(R.id.btn_cancelOrder);
            lin_cancleResion = itemView.findViewById(R.id.lin_cancleResion);
            text_cancleResion = itemView.findViewById(R.id.text_cancleResion);

        }
    }

    public void productCancle(String cust_id, String order_id, String orders_id, String message) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cancelling your order please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.cancelorder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    progressDialog.dismiss();

                    if (status.equals("200")) {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(context, statusArray, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(context, statusArray, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("cust_id", cust_id);
                params.put("order_id", order_id);
                params.put("orders_id", orders_id);
                params.put("message", message);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
    public void ordercancle_Dialog(String cust_id, String order_id, String orders_id) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.productcancel_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText description = dialog.findViewById(R.id.description);
        Button btn_Submit = dialog.findViewById(R.id.btn_Submit);
        Button btn_Close = dialog.findViewById(R.id.btn_Close);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (description.getText().toString().trim().equals("")){

                    String strmessage = "";
                    productCancle(cust_id,order_id,orders_id,strmessage);

                }else{

                    String strmessage = description.getText().toString().trim();
                    productCancle(cust_id,order_id,orders_id,strmessage);
                }
            }
        });

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void orderExchange_Dialog(String cust_id, String order_id, String orders_id) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.exchangepoduct);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText description = dialog.findViewById(R.id.description);
        Button btn_Submit = dialog.findViewById(R.id.btn_Submit);
        Button btn_Close = dialog.findViewById(R.id.btn_Close);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (description.getText().toString().trim().equals("")){

                    String strmessage = "";
                    productCancle(cust_id,order_id,orders_id,strmessage);

                }else{

                    String strmessage = description.getText().toString().trim();
                    productCancle(cust_id,order_id,orders_id,strmessage);
                }
            }
        });

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void cancel_reason(String orders_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.cancel_reason, new Response.Listener<String>() {
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
                        String cancel_reason = jsonObject_message.getString("cancel_reason");
                        JSONArray jsonObject_cart_count = new JSONArray(cancel_reason);
                        for (int i=0;i<jsonObject_cart_count.length();i++){

                            JSONObject jsonObject1 = jsonObject_cart_count.getJSONObject(0);
                            reason = jsonObject1.getString("reason");
                        }

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String cancel_reason = jsonObject_message.getString("cancel_reason");

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
                params.put("orders_id", orders_id);

                Log.d("addressparameterlist",params.toString());

                return params;


            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
