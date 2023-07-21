package co.in.dadspint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOut_Fragment extends Fragment {

    MaterialButton btn_AddnewAddress, btn_selectAddress, btn_ProceedCheckout,btn_ApplyCoupon;
    TextView text_ShowAddress, text_subTotalPrice, text_deliveryPrice,text_totalPrice;
    RecyclerView orderSummaryRecycler,recyclerAddressDetails;
    double totalprice, sales_Price, quanTity, totalAmount = 0.0, shipCharge, taxCharge;
    ArrayList<OrderSummary_ModelClass> viewCartModelArray = new ArrayList<>();
    LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    OrderSummaryAdapter orderSummaryAdapter;
    SessionManager sessionManager;
    Dialog dialog;
    Spinner spinner_City, spinner_Pincode, spinner_State;
    RadioGroup radioGroup;
    RadioButton radio_cashondelivery,selectedRadioButton;

    String str_FirstName, str_LastName, str_Email, str_MobileNo, str_CityId, str_state, str_Address1, str_Address2,
            str_PinCodeId, city_Id, city_Name, pincodeId, pincode, state_Id, state_Name, schoolId ="", schoolName,userid,
            str_ShowAddress = "",addreessid,str_shipping,Name, Email, MobileNo, City, Area, Address, PinCode, addressId,
            city_id, state_id, state_name, addressInsertMessage,selectPaymentOption;

    ArrayList<CityModelClass> arrayListCity = new ArrayList<>();
    ArrayList<PinCodeModel> arrayListPincode = new ArrayList<PinCodeModel>();
    ArrayList<StateModelClass> arrayListState = new ArrayList<StateModelClass>();
    ArrayList<SchoolModelClass> arrayListSchool = new ArrayList<SchoolModelClass>();
    ArrayList<ViewAddressModel> viewAddress_Model = new ArrayList<ViewAddressModel>();
    ViewAddressAdapter viewAddressAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.checckout_fragment, container, false);

        btn_AddnewAddress = view.findViewById(R.id.btn_AddnewAddress);
        btn_selectAddress = view.findViewById(R.id.btn_selectAddress);
        btn_ProceedCheckout = view.findViewById(R.id.btn_ProceedCheckout);
        text_ShowAddress = view.findViewById(R.id.text_ShowAddress);
        text_subTotalPrice = view.findViewById(R.id.text_subTotalPrice);
        text_deliveryPrice = view.findViewById(R.id.text_deliveryPrice);
        orderSummaryRecycler = view.findViewById(R.id.orderSummaryRecycler);
        text_totalPrice = view.findViewById(R.id.text_totalPrice);
        radioGroup = view.findViewById(R.id.radioGroup);
        btn_ApplyCoupon = view.findViewById(R.id.btn_ApplyCoupon);

        DeshBoardActivity.text_name.setText("CheckOut Page");

        sessionManager = new SessionManager(getContext());
        userid = sessionManager.getUSERID();
        showProduct(userid);

        btn_AddnewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAddressDialog();
            }
        });

        btn_selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectAddress();
            }
        });

        btn_ProceedCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if(text_ShowAddress.getText().toString().trim().equals("")){

                    Toast.makeText(getContext(), "Select Your address", Toast.LENGTH_SHORT).show();

                } else if (selectedRadioButtonId == -1) {

                    Toast.makeText(getContext(), "Select Payment Option", Toast.LENGTH_SHORT).show();

                }else{

                    selectedRadioButton = view.findViewById(selectedRadioButtonId);
                    selectPaymentOption = selectedRadioButton.getText().toString();

                    checkOutOrder(userid,"0","0",selectPaymentOption,addreessid,str_shipping);

                    Log.d("checkoutdetails",userid+""+"0"+""+"0"+""+selectPaymentOption+""+addreessid);

                }
            }
        });

        btn_ApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.couponapplylayout);

                ImageView backimage = dialog.findViewById(R.id.backimage);
                EditText edit_couponecode = dialog.findViewById(R.id.edit_couponecode);
                MaterialButton btn_ApplyCoupon = dialog.findViewById(R.id.btn_ApplyCoupon);

                btn_ApplyCoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (edit_couponecode.getText().toString().trim().equals("")){

                            Toast.makeText(getContext(), "Enter Your Coupon Code", Toast.LENGTH_SHORT).show();

                        }else{

                            String str_coupon = edit_couponecode.getText().toString().trim();
                            applyCouponCode(userid,str_coupon);
                        }
                    }
                });

                backimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //window.setBackgroundDrawableResource(R.drawable.dialogback);
            }
        });

        return view;
    }

    public void showProduct(String userId) {

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

                    if (status.equals("200")) {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");
                        JSONObject jsonstatues = new JSONObject(statusArray);

                        String cart_items = jsonstatues.getString("cart_items");
                        JSONArray jsonArray_item = new JSONArray(cart_items);

                        for (int i = 0; i < jsonArray_item.length(); i++) {

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

                           OrderSummary_ModelClass modelClass = new OrderSummary_ModelClass(
                                   product_id,primary_image,product_name,"",Product_price,quantity
                           );
                            viewCartModelArray.add(modelClass);

                            sales_Price = Double.valueOf(Product_price);

                            quanTity = Double.valueOf(quantity);

                            totalprice = sales_Price * quanTity;

                            totalAmount = totalAmount + totalprice;

                        }

                        //text_ItemCount.setText(str_size);

                        String total_price = String.valueOf(totalAmount);

                        text_subTotalPrice.setText(total_price);

                        orderSummaryAdapter = new OrderSummaryAdapter(getActivity(), viewCartModelArray);
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        orderSummaryRecycler.setHasFixedSize(true);
                        orderSummaryRecycler.setLayoutManager(linearLayoutManager);
                        orderSummaryRecycler.setAdapter(orderSummaryAdapter);

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
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

    public void getState() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get State Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Getstate, new Response.Listener<String>() {
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
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String state_data = jsonObject_statues.getString("state_data");
                        JSONArray jsonArray_state = new JSONArray(state_data);

                        for (int i = 0; i < jsonArray_state.length(); i++) {

                            JSONObject jsonObject_state = jsonArray_state.getJSONObject(i);

                            String state_id = jsonObject_state.getString("state_id");
                            String state_name = jsonObject_state.getString("state_name");
                            String Country_id = jsonObject_state.getString("Country_id");


                            StateModelClass stateModelClass = new StateModelClass(
                                    state_id, state_name, Country_id
                            );

                            arrayListState.add(stateModelClass);
                        }

                        StateSpinearAdapter adapter = new StateSpinearAdapter(getContext(), R.layout.spiner_text
                                , arrayListState);
                      //  spinner_State.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                      //  spinner_State.setSelection(-1, true);

                       /* spinner_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                    StateModelClass mystate = (StateModelClass) parent.getSelectedItem();

                                    state_Id = mystate.getState_id();
                                    state_Name = mystate.getState_name();
                                    Log.d("R_Pincode", city_Id);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } // to close the onItemSelected

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public void getCity() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get City Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.GetCity, new Response.Listener<String>() {
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
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String citty_data = jsonObject_statues.getString("citty_data");
                        JSONArray jsonArray_city = new JSONArray(citty_data);

                        for (int i = 0; i < jsonArray_city.length(); i++) {

                            JSONObject jsonObject_city = jsonArray_city.getJSONObject(i);

                            String city_id = jsonObject_city.getString("city_id");
                            String city_name = jsonObject_city.getString("city_name");
                            String state_id = jsonObject_city.getString("state_id");
                            String country_id = jsonObject_city.getString("country_id");

                            CityModelClass cityModelClass = new CityModelClass(
                                    city_id, city_name, state_id, country_id
                            );

                            arrayListCity.add(cityModelClass);
                        }

                        CitySpinerAdapter adapter = new CitySpinerAdapter(getContext(), R.layout.spiner_text
                                , arrayListCity);
                        spinner_City.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                        spinner_City.setSelection(-1, true);

                        spinner_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                    CityModelClass mystate = (CityModelClass) parent.getSelectedItem();

                                    city_Id = mystate.getCity_id();
                                    city_Name = mystate.getCity_name();
                                    Log.d("R_Pincode", city_Id);
                                    getPinCode(city_Id);
                                    //getSchool(city_Id);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } // to close the onItemSelected

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public void getPinCode(String cityId) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get City Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.GetPin, new Response.Listener<String>() {
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
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String Pin_data = jsonObject_statues.getString("Pin_data");
                        JSONArray jsonArray_pin = new JSONArray(Pin_data);

                        for (int i = 0; i < jsonArray_pin.length(); i++) {

                            JSONObject jsonObject_pin = jsonArray_pin.getJSONObject(i);

                            String pin_id = jsonObject_pin.getString("pin_id");
                            String pincode = jsonObject_pin.getString("pincode");

                            PinCodeModel pinCodeModel = new PinCodeModel(
                                    pin_id, pincode
                            );

                            arrayListPincode.add(pinCodeModel);
                        }

                        PincodeSpinerAdapter adapter = new PincodeSpinerAdapter(getContext(), R.layout.spiner_text
                                , arrayListPincode);
                        spinner_Pincode.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                        spinner_City.setSelection(-1, true);

                        spinner_Pincode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                    PinCodeModel mystate = (PinCodeModel) parent.getSelectedItem();

                                    pincode = mystate.getPincode();
                                    pincodeId = mystate.getPin_id();

                                    Log.d("R_Pincode", pincode);

                                    //Log.d("Pinocde_array",janamam.toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } // to close the onItemSelected

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("city_id", cityId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public void getSchool(String cityId) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get City Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Getschool, new Response.Listener<String>() {
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
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String Pin_data = jsonObject_statues.getString("Pin_data");
                        JSONArray jsonArray_pin = new JSONArray(Pin_data);

                        for (int i = 0; i < jsonArray_pin.length(); i++) {

                            JSONObject jsonObject_pin = jsonArray_pin.getJSONObject(i);

                            String school_name = jsonObject_pin.getString("school_name");
                            String school_id = jsonObject_pin.getString("school_id");

                            SchoolModelClass schoolModelClass = new SchoolModelClass(
                                    school_name, school_id
                            );

                            arrayListSchool.add(schoolModelClass);
                        }

                        SchoolSpinearAdapter adapter = new SchoolSpinearAdapter(getContext(), R.layout.spiner_text
                                , arrayListSchool);
                      //  spinner_School.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                     //   spinner_School.setSelection(-1, true);

                        /*schoolIdspinner_School.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                    SchoolModelClass mystate = (SchoolModelClass) parent.getSelectedItem();

                                    schoolId = mystate.getSchool_id();
                                    schoolName = mystate.getSchool_name();

                                    Log.d("R_Pincode", pincode);

                                    //Log.d("Pinocde_array",janamam.toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } // to close the onItemSelected

                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });*/

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("city_id", cityId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    public void addAddressDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.addressdetails);
        //dialog.setCancelable(false);
        spinner_City = dialog.findViewById(R.id.spinner_City);
        spinner_Pincode = dialog.findViewById(R.id.spinner_Pincode);
       // spinner_State = dialog.findViewById(R.id.spinner_State);
       // spinner_School = dialog.findViewById(R.id.spinner_School);

        EditText edit_firstname = dialog.findViewById(R.id.edit_firstname);
        EditText edit_LastName = dialog.findViewById(R.id.edit_LastName);
        EditText edit_MobileNo = dialog.findViewById(R.id.edit_MobileNo);
        EditText edit_EmailId = dialog.findViewById(R.id.edit_EmailId);
        //  EditText edit_state = dialog.findViewById(R.id.edit_state);
        EditText edit_Address1 = dialog.findViewById(R.id.edit_Address1);
        EditText edit_Address2 = dialog.findViewById(R.id.edit_Address2);
        //TextView homeAddress = dialog.findViewById(R.id.homeAddress);
        //TextView schoolAddress = dialog.findViewById(R.id.schoolAddress);
        Button btn_Save = dialog.findViewById(R.id.btn_Save);
        Button btn_cancle = dialog.findViewById(R.id.btn_cancle);

        //Button btn_Map = dialog.findViewById(R.id.btn_Map);

/*        btn_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),SerachAreaDetails.class));
            }
        });*/


        getCity();
       // getState();

       // homeAddress.setBackgroundResource(R.drawable.backgroundcolor);
       // schoolAddress.setBackgroundResource(R.drawable.textfieldback);

        addressInsertMessage = "homeAddress";

       /* homeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressInsertMessage = "homeAddress";

                homeAddress.setBackgroundResource(R.drawable.backgroundcolor);
                schoolAddress.setBackgroundResource(R.drawable.textfieldback);

                edit_Address1.setVisibility(View.VISIBLE);
                edit_Address2.setVisibility(View.VISIBLE);
                spinner_Pincode.setVisibility(View.VISIBLE);
            //    spinner_State.setVisibility(View.VISIBLE);

            }
        });

        schoolAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressInsertMessage = "schoolAddress";

                homeAddress.setBackgroundResource(R.drawable.textfieldback);
                schoolAddress.setBackgroundResource(R.drawable.backgroundcolor);

                edit_Address1.setVisibility(View.GONE);
                edit_Address2.setVisibility(View.GONE);
                spinner_Pincode.setVisibility(View.GONE);
               // spinner_State.setVisibility(View.GONE);
            }
        });*/

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_firstname.getText().toString().trim().equals("")) {

                    edit_firstname.setError("Please Enter Name");

                } else if (TextUtils.isEmpty(edit_LastName.getText())) {

                    edit_LastName.setError("Please Enter Email");

                } else if (TextUtils.isEmpty(edit_EmailId.getText())) {

                    edit_EmailId.setError("Please Enter Email");

                }else if (!isValidEmail(edit_EmailId.getText().toString().trim())) {

                    edit_EmailId.setError("Enter Valide Email id");

                } else if (TextUtils.isEmpty(edit_MobileNo.getText()) && edit_MobileNo.getText().toString().trim().length() != 10) {

                    edit_MobileNo.setError("Please Enter MobileNumber");

                } else if (TextUtils.isEmpty(edit_Address1.getText())) {

                    edit_Address1.setError("Please Enter Address");

                } else if (TextUtils.isEmpty(edit_Address2.getText())) {

                    edit_Address2.setError("Please Enter Address");

                } else {

                    str_FirstName = edit_firstname.getText().toString().trim();
                    str_LastName = edit_LastName.getText().toString().trim();
                    str_Email = edit_EmailId.getText().toString().trim();
                    str_MobileNo = edit_MobileNo.getText().toString().trim();
                    //str_state = edit_state.getText().toString().trim();
                    str_Address1 = edit_Address1.getText().toString().trim();
                    str_Address2 = edit_Address2.getText().toString().trim();
                    str_PinCodeId = pincodeId;
                    str_CityId = city_Id;

                    addAddress_Save(userid, str_FirstName, str_LastName, str_Address1, str_Address2, str_CityId, state_Name,
                            str_PinCodeId, str_Email, str_MobileNo, "1", schoolId);
                }

               /* if (addressInsertMessage.equals("homeAddress")) {

                } else {

                    if (edit_firstname.getText().toString().trim().equals("")) {

                        edit_firstname.setError("Please Enter Name");

                    } else if (TextUtils.isEmpty(edit_LastName.getText())) {

                        edit_LastName.setError("Please Enter Email");

                    } else if (TextUtils.isEmpty(edit_EmailId.getText())) {

                        edit_EmailId.setError("Please Enter Email");

                    } else if (TextUtils.isEmpty(edit_MobileNo.getText()) && edit_MobileNo.getText().toString().trim().length() == 10) {

                        edit_MobileNo.setError("Please Enter MobileNumber");

                    } else {

                        str_FirstName = edit_firstname.getText().toString().trim();
                        str_LastName = edit_LastName.getText().toString().trim();
                        str_Email = edit_EmailId.getText().toString().trim();
                        str_MobileNo = edit_MobileNo.getText().toString().trim();
                        //str_state = edit_state.getText().toString().trim();
                        str_Address1 = edit_Address1.getText().toString().trim();
                        str_Address2 = edit_Address2.getText().toString().trim();
                        str_PinCodeId = pincodeId;
                        str_CityId = city_Id;

                        addAddress_Save(userid, str_FirstName, str_LastName, "", "", str_CityId, "",
                                "", str_Email, str_MobileNo, "2", schoolId);

                    }

                }*/

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);


    }
    public void addAddress_Save(String user_id, String firstname, String lasttname, String address1, String address2,
                                String city, String state, String zip, String email, String phone, String address_type,
                                String school_id) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Add Address Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.add_address, new Response.Listener<String>() {
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

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("firstname", firstname);
                params.put("lasttname", lasttname);
                params.put("address1", address1);
                params.put("address2", address2);
                params.put("city", city_Id);
                params.put("state", state_Name);
                params.put("zip", pincode);
                params.put("email", email);
                params.put("phone", phone);
                params.put("address_type", address_type);
                params.put("school_id", schoolId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }
    public void selectAddress(){

        Dialog dialogSelect = new Dialog(getContext());
        dialogSelect.setContentView(R.layout.selectaddress);
        dialogSelect.setCancelable(false);

        Button btn_SelectAddress = dialogSelect.findViewById(R.id.btn_SelectAddress);

        recyclerAddressDetails = dialogSelect.findViewById(R.id.recyclerAddressDetails);

        btn_SelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_ShowAddress = viewAddressAdapter.addressvalue();
                str_shipping = viewAddressAdapter.shipping();
                addreessid = ViewAddressAdapter.addressId;

                Toast.makeText(getActivity(), str_ShowAddress, Toast.LENGTH_SHORT).show();

                if (str_ShowAddress == null){

                    Toast.makeText(getActivity(), "Select You Address", Toast.LENGTH_SHORT).show();

                }else {

                    text_deliveryPrice.setText(str_shipping);

                    String str_price = text_subTotalPrice.getText().toString().trim();
                    double d_price = Double.valueOf(str_price);
                    double d_shipping = Double.valueOf(str_shipping);
                    double d_Total = d_price + d_shipping;
                    String str_Totalprice = String.valueOf(d_Total);

                    text_ShowAddress.setText(str_ShowAddress);
                    text_totalPrice.setText(str_Totalprice);

                    dialogSelect.dismiss();
                }

            }
        });

        viewAddress(userid);

        dialogSelect.show();
        Window window = dialogSelect.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    }
    public void viewAddress(String userId) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("View Address Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.view_address, new Response.Listener<String>() {
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
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String Address_data = jsonObject_statues.getString("Address_data");
                        JSONArray jsonArray_Address_data = new JSONArray(Address_data);

                        if (jsonArray_Address_data.length() != 0){

                            for (int i = 0; i < jsonArray_Address_data.length(); i++) {

                                JSONObject jsonObject_Address_data = jsonArray_Address_data.getJSONObject(i);

                                String address_id = jsonObject_Address_data.getString("address_id");
                                String user_id = jsonObject_Address_data.getString("user_id");
                                String first_name = jsonObject_Address_data.getString("first_name");
                                String last_name = jsonObject_Address_data.getString("last_name");
                                String address_type = jsonObject_Address_data.getString("address_type");
                                String school_id = jsonObject_Address_data.getString("school_id");
                                String school_name = jsonObject_Address_data.getString("school_name");
                                String pincode = jsonObject_Address_data.getString("pincode");
                                String email = jsonObject_Address_data.getString("email");
                                String number = jsonObject_Address_data.getString("number");
                                String address1 = jsonObject_Address_data.getString("address1");
                                String adress2 = jsonObject_Address_data.getString("adress2");
                                String city_id = jsonObject_Address_data.getString("city_id");
                                String city_name = jsonObject_Address_data.getString("city_name");
                                String shipping_price = jsonObject_Address_data.getString("shipping_price");

                                ViewAddressModel viewAddressModel = new ViewAddressModel(
                                        address_id, user_id, first_name, last_name, address_type, school_id, school_name, "state", pincode,
                                        email, number, address1, adress2, city_id, city_name, shipping_price
                                );

                                viewAddress_Model.add(viewAddressModel);
                            }

                            linearLayoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            viewAddressAdapter = new ViewAddressAdapter(viewAddress_Model,getContext(),"CheckOut");
                            recyclerAddressDetails.setHasFixedSize(true);
                            recyclerAddressDetails.setLayoutManager(linearLayoutManager1);
                            recyclerAddressDetails.setAdapter(viewAddressAdapter);
                        }else{

                            Toast.makeText(getContext(), "Address Detils Not Found", Toast.LENGTH_SHORT).show();
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
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("user_id", userId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
    public void checkOutOrder(String user_id, String cupon_code, String cupon_price, String paymentmode, String address_id,String str_shipping){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Your Order Placed Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.checkout, new Response.Listener<String>() {
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

                        Toast.makeText(getContext(), status_message, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getContext(),OrderSuccessFully.class));

                    }else{

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        Toast.makeText(getContext(), status_message, Toast.LENGTH_SHORT).show();
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
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("cupon_code",cupon_code);
                params.put("cupon_price",cupon_price);
                params.put("paymentmode",paymentmode);
                params.put("address_id",address_id);
                params.put("shipping_charge",str_shipping);

                Log.d("parameters",params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void applyCouponCode(String userid, String couponcode){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Apply Coupon Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.apply_coupon, new Response.Listener<String>() {
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

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
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

                Map<String,String> params = new HashMap<>();
                params.put("user_id",userid);
                params.put("coupon_code",couponcode);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

        pattern = Patterns.EMAIL_ADDRESS;
        matcher = pattern.matcher(email);

        return matcher.matches();

        //return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
