package co.in.dadspint;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.in.dadspint.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAddressAdapter extends RecyclerView.Adapter<ViewAddressAdapter.ViewHolder> {

    ArrayList<ViewAddressModel> viewAddress_Model = new ArrayList<ViewAddressModel>();
    Context context;
    public static String addressId, all_values = "", checkOut1, shippingcharge, cityNameView,pincodeNameView,
            schoolNameView,cityidview,firstNameView,lastNameView,phoneNoView,emailidview,address1View,
            address2View,addressid;
    int index;
    Dialog dialog;
    Spinner spinner_City, spinner_Pincode, spinner_State;
    ArrayList<CityModelClass> arrayListCity = new ArrayList<>();
    ArrayList<PinCodeModel> arrayListPincode = new ArrayList<PinCodeModel>();
    ArrayList<StateModelClass> arrayListState = new ArrayList<StateModelClass>();
    ArrayList<SchoolModelClass> arrayListSchool = new ArrayList<SchoolModelClass>();

    String str_FirstName, str_LastName, str_Email, str_MobileNo, str_CityId, str_state, str_Address1, str_Address2,
            str_PinCodeId, userId, city_Id = "", city_Name, pincodeId, pincode, state_Id, state_Name, schoolId, schoolName,
            addressId1, addressInsertMessage;

    public ViewAddressAdapter(ArrayList<ViewAddressModel> viewAddress_model, Context context, String checkOut) {

        this.viewAddress_Model = viewAddress_model;
        this.context = context;
        checkOut1 = checkOut;

    }

    @NonNull
    @Override
    public ViewAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addressdetailview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAddressAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ViewAddressModel addressView = viewAddress_Model.get(position);

        holder.addressdetails.setText(addressView.getFirst_name() + ", " + addressView.getLast_name() + ", " + addressView.getNumber() + "\n" +
                addressView.getEmail()  + "\n" +
                addressView.getState() + ", " + addressView.getCity_name() +", "+ addressView.getPincode() + "\n" +
                addressView.getAddress1() + ", " + addressView.getAdress2());

        holder.shoppingCharges.setText("Shopping Charges : " + addressView.getShipping_price());

        /*if (checkOut1.equals("")){

            holder.btn_SelectAddress.setVisibility(View.GONE);

        }else{

            holder.btn_SelectAddress.setVisibility(View.VISIBLE);
        }*/

        holder.rel_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressId = addressView.getAddress_id();

                index = position;
                notifyDataSetChanged();
                all_values = holder.addressdetails.getText().toString().trim();
                shippingcharge = addressView.getShipping_price();


                // holder.rel_Click.setBackgroundResource(R.drawable.selectaddressback);
                //  holder.rel_Click.setElevation(15);

                //Toast.makeText(context, all_values, Toast.LENGTH_SHORT).show();

            }
        });

        if (index == position) {

            holder.rel_Click.setBackgroundResource(R.drawable.selectaddressback);
            holder.rel_Click.setElevation(15);

        } else {

            holder.rel_Click.setBackgroundResource(R.drawable.homecard_back1);
            holder.rel_Click.setElevation(5);
        }

        holder.btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityNameView = addressView.getCity_name();
                pincodeNameView = addressView.getPincode();
                schoolNameView = addressView.getSchool_name();
                cityidview = addressView.getCity_id();
                city_Id = cityidview;
                pincode = addressView.getPincode();
                firstNameView = addressView.getFirst_name();
                lastNameView = addressView.getLast_name();
                phoneNoView = addressView.getNumber();
                emailidview = addressView.getEmail();
                address1View = addressView.getAddress1();
                address2View = addressView.getAdress2();
                addressid = addressView.getAddress_id();

                addAddressDialog(cityNameView,pincodeNameView,schoolNameView,cityidview,
                        firstNameView,lastNameView,phoneNoView,emailidview,address1View,address2View,addressid);
            }
        });

    }

    public String addressvalue() {

        return all_values;
    }

    public String shipping() {

        return shippingcharge;
    }

    @Override
    public int getItemCount() {
        return viewAddress_Model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView addressdetails, shoppingCharges;
        ImageView btn_Delete, btn_Edit;
        // MaterialButton btn_SelectAddress;
        RelativeLayout rel_Click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //btn_Delete = itemView.findViewById(R.id.btn_Delete);
            addressdetails = itemView.findViewById(R.id.addressdetails);
            shoppingCharges = itemView.findViewById(R.id.shoppingCharges);
            // btn_SelectAddress = itemView.findViewById(R.id.btn_SelectAddress);
            rel_Click = itemView.findViewById(R.id.rel_Click);
            btn_Edit = itemView.findViewById(R.id.btn_Edit);
        }
    }

    public void addAddressDialog(String cityNameView,String pincodeNameView,String schoolNameView,
                                 String cityidview,String firstNameView,String lastNameView,String phoneNoView,
                                 String emailidview,String address1View,String address2View,String addressid) {

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.addressdetails);
        //dialog.setCancelable(false);
        spinner_City = dialog.findViewById(R.id.spinner_City);
        spinner_Pincode = dialog.findViewById(R.id.spinner_Pincode);
     //   spinner_School = dialog.findViewById(R.id.spinner_School);

        EditText edit_firstname = dialog.findViewById(R.id.edit_firstname);
        EditText edit_LastName = dialog.findViewById(R.id.edit_LastName);
        EditText edit_MobileNo = dialog.findViewById(R.id.edit_MobileNo);
        EditText edit_EmailId = dialog.findViewById(R.id.edit_EmailId);
        EditText edit_Address1 = dialog.findViewById(R.id.edit_Address1);
        EditText edit_Address2 = dialog.findViewById(R.id.edit_Address2);
        Button btn_Save = dialog.findViewById(R.id.btn_Save);
        Button btn_cancle = dialog.findViewById(R.id.btn_cancle);

        edit_firstname.setText(firstNameView);
        edit_LastName.setText(lastNameView);
        edit_MobileNo.setText(phoneNoView);
        edit_EmailId.setText(emailidview);
        edit_Address1.setText(address1View);
        edit_Address2.setText(address2View);

        getCity(cityNameView,pincodeNameView,schoolNameView,cityidview);

       // addressInsertMessage = "homeAddress";
        //btn_Save.setText("Edit");

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

                } else if (TextUtils.isEmpty(edit_MobileNo.getText()) && edit_MobileNo.getText().toString().trim().length() == 10) {

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

                    addAddress_Save(addressid, str_FirstName, str_LastName, str_Address1, str_Address2, str_CityId, state_Name,
                            str_PinCodeId, str_Email, str_MobileNo, "1", schoolId);
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);


    }

    public void getCity(String cityNameView,String pincodeNameView,String schoolNameView,String cityidview) {

        ProgressDialog progressDialog = new ProgressDialog(context);
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

                        CitySpinerAdapter adapter = new CitySpinerAdapter(context, android.R.layout.simple_spinner_item
                                , arrayListCity);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_City.setAdapter(adapter);
                        int index = selectSpinnerValue1(arrayListCity, cityNameView);
                        spinner_City.setSelection(index, true);

                        Log.d("citylist", arrayListCity.toString());

                        //  spinner_City.setSelection(-1, true);


                            getPinCode(cityidview,pincodeNameView);
                            //getSchool(cityidview,schoolNameView);

                        spinner_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                    CityModelClass mystate = (CityModelClass) parent.getSelectedItem();

                                    city_Id = mystate.getCity_id();
                                    city_Name = mystate.getCity_name();
                                    Log.d("R_Pincode", city_Id);

                                    getPinCode(city_Id,pincodeNameView);
                                  //  getSchool(city_Id,schoolNameView);


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

                        Toast.makeText(context, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(context, "City " + error, Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public void getPinCode(String cityId,String pincodeNameView) {

        ProgressDialog progressDialog = new ProgressDialog(context);
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

                        PincodeSpinerAdapter adapter = new PincodeSpinerAdapter(context, android.R.layout.simple_spinner_item
                                , arrayListPincode);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_Pincode.setAdapter(adapter);
                        int index = selectSpinnerValue2(arrayListPincode, pincodeNameView);
                        spinner_Pincode.setSelection(index, true);

                        Log.d("citylist", arrayListCity.toString());

                        //spinner_City.setSelection(-1, true);

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

                        Toast.makeText(context, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    public void getSchool(String cityId,String schoolNameView) {

        ProgressDialog progressDialog = new ProgressDialog(context);
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

                        SchoolSpinearAdapter adapter = new SchoolSpinearAdapter(context, android.R.layout.simple_spinner_item
                                , arrayListSchool);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                       // spinner_School.setAdapter(adapter);
                        //int index = selectSpinnerValue3(arrayListSchool, schoolNameView);
                      //  spinner_School.setSelection(index, true);

                        Log.d("citylist", arrayListCity.toString());

                       // spinner_School.setSelection(-1, true);

                       /* spinner_School.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                        Toast.makeText(context, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private int selectSpinnerValue1(ArrayList<CityModelClass> ListSpinner, String myString) {
        int index = 0;
        for (int i = 0; i < ListSpinner.size(); i++) {
            if (ListSpinner.get(i).getCity_name().equals(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }
    private int selectSpinnerValue2(ArrayList<PinCodeModel> ListSpinner, String myString) {
        int index = 0;
        for (int i = 0; i < ListSpinner.size(); i++) {
            if (ListSpinner.get(i).getPincode().equals(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }
    private int selectSpinnerValue3(ArrayList<SchoolModelClass> ListSpinner, String myString) {
        int index = 0;
        for (int i = 0; i < ListSpinner.size(); i++) {
            if (ListSpinner.get(i).getSchool_name().equals(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void addAddress_Save(String user_id, String firstname, String lasttname, String address1, String address2,
                                String city, String state, String zip, String email, String phone, String address_type, String school_id) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Edit Address Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Editaddress, new Response.Listener<String>() {
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
                        notifyDataSetChanged();

                        if (checkOut1.equals("CheckOut")){

                            CheckOut_Fragment.dialogSelect.dismiss();
                        }

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(context, statusArray, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

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
                params.put("address_id", user_id);
                params.put("firstname", firstname);
                params.put("lastname", lasttname);
                params.put("address1", address1);
                params.put("address2", address2);
                params.put("city", city_Id);
                params.put("state", "");
                params.put("zip", pincode);
                params.put("email", email);
                params.put("phone", phone);
               // params.put("address_type", address_type);
              //  params.put("school_id", schoolId);

                Log.d("addressparameterlist",params.toString());


                return params;


            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
