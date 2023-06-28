package co.in.dadspint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderListModel> orderListModels;
    ArrayList<OrderDetails_Model> orderDetails_models = new ArrayList<>();
    ArrayList<AddressModel> addressModels = new ArrayList<>();
    Dialog dialogMenu;
    TextView textView;
    double totalprice;
    String shipping_charge;


    public OrderListAdapter(FragmentActivity activity, ArrayList<OrderListModel> orderListModels) {

        this.context = activity;
        this.orderListModels = orderListModels;

    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetails, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {

        OrderListModel listModel = orderListModels.get(position);

        holder.text_orderId.setText(listModel.getOrder_id());
        holder.text_OrderDate.setText(listModel.getOrder_date());
        holder.totalPrice.setText(listModel.getTotal_price());

        holder.btn_ViewOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogMenu = new Dialog(context, android.R.style.Theme_Light_NoTitleBar);
                dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogMenu.setContentView(R.layout.myorderdetailspage);
                dialogMenu.setCancelable(true);
                dialogMenu.setCanceledOnTouchOutside(true);

                textView = dialogMenu.findViewById(R.id.addressdetails);
                TextView subTotalPrice = dialogMenu.findViewById(R.id.text_subTotalPrice);
                TextView shippingCharges = dialogMenu.findViewById(R.id.text_deliveryPrice);
                TextView totalPrice = dialogMenu.findViewById(R.id.totalPrice);
                TextView addressdetails = dialogMenu.findViewById(R.id.addressdetails);
                RecyclerView rv_vars = dialogMenu.findViewById(R.id.rv_vars);
                ImageView image_Arrow = dialogMenu.findViewById(R.id.image_Arrow);
                RelativeLayout btn_dismiss = dialogMenu.findViewById(R.id.btn_dismiss);

                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Order Details Details....");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.getSingleOrder,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");

                                    if (status.equals("200")) {

                                        String error = jsonObject.getString("error");
                                        String message = jsonObject.getString("message");
                                        String data = jsonObject.getString("data");
                                        JSONObject jsonObject_data = new JSONObject(data);
                                        String order_dtls = jsonObject_data.getString("order_dtls");
                                        String address_details = jsonObject_data.getString("address_details");
                                        JSONArray jsonArray = new JSONArray(order_dtls);
                                        JSONArray jsonArray_address = new JSONArray(address_details);

                                        orderDetails_models.clear();
                                        addressModels.clear();


                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject_datalist = jsonArray.getJSONObject(i);
                                            String orders_id = jsonObject_datalist.getString("orders_id");
                                            String productname = jsonObject_datalist.getString("productname");
                                            String variation_id = jsonObject_datalist.getString("variation_id");
                                            String qty = jsonObject_datalist.getString("qty");
                                            String img = jsonObject_datalist.getString("img");
                                            String price = jsonObject_datalist.getString("price");
                                            shipping_charge = jsonObject_datalist.getString("shipping_charge");
                                            String order_id = jsonObject_datalist.getString("order_id");
                                            String payment_mode = jsonObject_datalist.getString("payment_mode");
                                            String status1 = jsonObject_datalist.getString("status");
                                            String created_date = jsonObject_datalist.getString("created_date");
                                            String user_id = jsonObject_datalist.getString("user_id");

                                            OrderDetails_Model details_model = new OrderDetails_Model(
                                                    orders_id,productname,variation_id,qty,img,price,shipping_charge,order_id,payment_mode,status1,created_date,user_id
                                            );

                                            orderDetails_models.add(details_model);

                                            double price1 = Double.valueOf(price);
                                            totalprice = price1 + totalprice;
                                        }

                                        subTotalPrice.setText(String.valueOf(totalprice));
                                        shippingCharges.setText(shipping_charge);

                                        rv_vars.setLayoutManager(new LinearLayoutManager(context));
                                        rv_vars.setNestedScrollingEnabled(false);
                                        OrderDetAdapter varad = new OrderDetAdapter(orderDetails_models,context);
                                        rv_vars.setAdapter(varad);

                                        for (int j=0;j< jsonArray_address.length();j++){

                                            JSONObject jsonObject_address = jsonArray_address.getJSONObject(0);

                                            String address_id = jsonObject_address.getString("address_id");
                                            String user_id = jsonObject_address.getString("user_id");
                                            String first_name = jsonObject_address.getString("first_name");
                                            String last_name = jsonObject_address.getString("last_name");
                                            String address_type = jsonObject_address.getString("address_type");
                                            String city_name = jsonObject_address.getString("city_name");
                                            String sclname = jsonObject_address.getString("sclname");
                                            String email = jsonObject_address.getString("email");
                                            String number = jsonObject_address.getString("number");
                                            String state = jsonObject_address.getString("state");
                                            String pincode = jsonObject_address.getString("pincode");
                                            String address1 = jsonObject_address.getString("address1");
                                            String adress2 = jsonObject_address.getString("adress2");

                                            AddressModel addressModel = new AddressModel(
                                                    address_id,user_id,first_name,last_name,address_type,city_name,sclname,email,number,
                                                    state,pincode,address1,adress2
                                            );

                                            addressModels.add(addressModel);

                                            addressdetails.setText(first_name+" "+last_name+", "+email+" , "+number+", "+
                                                    city_name+", "+sclname+", "+state+" ,"+pincode+", "+address1+", "+adress2);
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
                        Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("order_id", listModel.order_id);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);



                btn_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogMenu.dismiss();

                        totalprice = 0.0;

                    }
                });

                image_Arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogMenu.dismiss();

                        totalprice = 0.0;

                    }
                });

                dialogMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_orderId, text_OrderDate, totalPrice, btn_ViewOrderDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_orderId = itemView.findViewById(R.id.text_orderId);
            text_OrderDate = itemView.findViewById(R.id.text_OrderDate);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            btn_ViewOrderDetails = itemView.findViewById(R.id.btn_ViewOrderDetails);
        }
    }

}
