package com.example.schooluniformapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Address_fragment extends Fragment {

    RecyclerView addressRecyclerView;
    Dialog dialog;
    Spinner spinner_City, spinner_Pincode, spinner_State, spinner_School;
    MaterialButton btn_addAddress;

    String str_FirstName, str_LastName, str_Email, str_MobileNo, str_CityId, str_state, str_Address1, str_Address2,
            str_PinCodeId, userId, city_Id, city_Name, pincodeId, pincode, state_Id, state_Name, schoolId, schoolName,
            Name, Email, MobileNo, City, Area, Address, PinCode, addressId, city_id, state_id, state_name, addressInsertMessage;

    ArrayList<CityModelClass> arrayListCity = new ArrayList<>();
    ArrayList<PinCodeModel> arrayListPincode = new ArrayList<PinCodeModel>();
    ArrayList<StateModelClass> arrayListState = new ArrayList<StateModelClass>();
    ArrayList<SchoolModelClass> arrayListSchool = new ArrayList<SchoolModelClass>();
    ArrayList<ViewAddressModel> viewAddress_Model = new ArrayList<ViewAddressModel>();
    ViewAddressAdapter viewAddressAdapter;
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.address_fragment, container, false);

        btn_addAddress = view.findViewById(R.id.btn_addAddress);
        addressRecyclerView = view.findViewById(R.id.addressRecyclerView);

        sessionManager = new SessionManager(getActivity());
        userId = sessionManager.getUSERID();


        viewAddress(userId);

        btn_addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAddressDialog();
            }
        });
        return view;
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
                       // spinner_State.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                       // spinner_State.setSelection(-1, true);

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

                Toast.makeText(getActivity(), "State" + error, Toast.LENGTH_SHORT).show();

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

                        CitySpinerAdapter adapter = new CitySpinerAdapter(getContext(), android.R.layout.simple_spinner_item
                                , arrayListCity);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                                    getSchool(city_Id);

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

                Toast.makeText(getActivity(), "City " + error, Toast.LENGTH_SHORT).show();

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

                        PincodeSpinerAdapter adapter = new PincodeSpinerAdapter(getContext(), android.R.layout.simple_spinner_item
                                , arrayListPincode);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

                        SchoolSpinearAdapter adapter = new SchoolSpinearAdapter(getContext(), android.R.layout.simple_spinner_item
                                , arrayListSchool);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_School.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                        spinner_School.setSelection(-1, true);

                        spinner_School.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    public void addAddressDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.addressdetails);
        //dialog.setCancelable(false);
        spinner_City = dialog.findViewById(R.id.spinner_City);
        spinner_Pincode = dialog.findViewById(R.id.spinner_Pincode);
       // spinner_State = dialog.findViewById(R.id.spinner_State);
        spinner_School = dialog.findViewById(R.id.spinner_School);

        EditText edit_firstname = dialog.findViewById(R.id.edit_firstname);
        EditText edit_LastName = dialog.findViewById(R.id.edit_LastName);
        EditText edit_MobileNo = dialog.findViewById(R.id.edit_MobileNo);
        EditText edit_EmailId = dialog.findViewById(R.id.edit_EmailId);
        //  EditText edit_state = dialog.findViewById(R.id.edit_state);
        EditText edit_Address1 = dialog.findViewById(R.id.edit_Address1);
        EditText edit_Address2 = dialog.findViewById(R.id.edit_Address2);
        TextView homeAddress = dialog.findViewById(R.id.homeAddress);
        TextView schoolAddress = dialog.findViewById(R.id.schoolAddress);
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
     //   getState();

        homeAddress.setBackgroundResource(R.drawable.backgroundcolor);
        schoolAddress.setBackgroundResource(R.drawable.textfieldback);

        addressInsertMessage = "homeAddress";

        homeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressInsertMessage = "homeAddress";

                homeAddress.setBackgroundResource(R.drawable.backgroundcolor);
                schoolAddress.setBackgroundResource(R.drawable.textfieldback);

                edit_Address1.setVisibility(View.VISIBLE);
                edit_Address2.setVisibility(View.VISIBLE);
                spinner_Pincode.setVisibility(View.VISIBLE);
              //  spinner_State.setVisibility(View.VISIBLE);

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
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addressInsertMessage.equals("homeAddress")) {

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

                        addAddress_Save(userId, str_FirstName, str_LastName, str_Address1, str_Address2, str_CityId, state_Name,
                                str_PinCodeId, str_Email, str_MobileNo, "1", schoolId);
                    }

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

                        addAddress_Save(userId, str_FirstName, str_LastName, "", "", str_CityId, "",
                                "", str_Email, str_MobileNo, "2", schoolId);

                    }

                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);


    }
    public void addAddress_Save(String user_id, String firstname, String lasttname, String address1, String address2,
                                String city, String state, String zip, String email, String phone, String address_type, String school_id) {

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

                        dialog.dismiss();
                        viewAddress(userId);

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
                params.put("zip", pincode);
                params.put("email", email);
                params.put("phone", phone);
                params.put("address_type", address_type);
                params.put("school_id", schoolId);

                Log.d("addressparameterlist",params.toString());


                return params;


            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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

                        viewAddress_Model.clear();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String Address_data = jsonObject_statues.getString("Address_data");
                        JSONArray jsonArray_Address_data = new JSONArray(Address_data);

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

                        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        viewAddressAdapter = new ViewAddressAdapter(viewAddress_Model,getContext(), "");
                        addressRecyclerView.setHasFixedSize(true);
                        addressRecyclerView.setLayoutManager(linearLayoutManager);
                        addressRecyclerView.setAdapter(viewAddressAdapter);

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

}
