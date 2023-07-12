package co.in.dadspint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import co.in.dadspint.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsFragment extends Fragment {

    RecyclerView productDetailsRecycler;
    RelativeLayout filterOption;
    ArrayList<ProductDataModel> productDataModel1 = new ArrayList<>();
    ArrayList<ProductDataModel> productDataModel2 = new ArrayList<>();
    ArrayList<ProductFilterModel> productDataModel3 = new ArrayList<>();
    SchoolUniformAdapter schoolUniformAdapter;
    SchoolAccessoriesAdapter schoolAccessoriesAdapter;
    FilterProductAdapter filterProductAdapter;
     GridLayoutManager gridLayoutManager, gridLayoutManager1;
   // LinearLayoutManager gridLayoutManager, gridLayoutManager1;
   Dialog dialog;
   Spinner spinner_City,spinner_Class,spinner_School;
    ArrayList<CityModelClass> arrayListCity = new ArrayList<>();
    ArrayList<SchoolModelClass> arrayListSchool = new ArrayList<SchoolModelClass>();
    ArrayList<ClassModelClass> arrayListClass = new ArrayList<ClassModelClass>();
    String city_Id = "",city_Name = "",schoolId = "",schoolName = "",class_Id = "",class_Name = "",userId,SchoolAccessories;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.productdetails_fragment,container,false);

        productDetailsRecycler = view.findViewById(R.id.productDetailsRecycler);
        filterOption = view.findViewById(R.id.filterOption);

        DeshBoardActivity.text_name.setText("Prodect Details");

        SchoolAccessories = getArguments().getString("YourKey");

        if (SchoolAccessories.equals("SchoolAccessories")){

            filterOption.setVisibility(View.GONE);
        }

        DeshBoardActivity.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new HomeFragment();
                Bundle args = new Bundle();
                args.putString("YourKey", "SchoolAccessories");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                DeshBoardActivity.menu.setVisibility(View.VISIBLE);
                DeshBoardActivity.backimage.setVisibility(View.GONE);
            }
        });

        sessionManager = new SessionManager(getActivity());
        userId = sessionManager.getUSERID();

        getall_product(userId);

        filterOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFilterDialog();
            }
        });

        return view;
    }

    public void getall_product(String user_id){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get Product Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.AllProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");
                    JSONObject jsonObject_message = new JSONObject(messages);
                    String responsecode = jsonObject_message.getString("responsecode");
                    String statusArray = jsonObject_message.getString("status");

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

                        if (product_type.equals("1")) {

                            ProductDataModel product_DataModel1 = new ProductDataModel(
                                    product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regular_price,
                                    sales_price, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description,prodType);

                            productDataModel1.add(product_DataModel1);

                        } else {

                            ProductDataModel product_DataModel2 = new ProductDataModel(
                                    product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regular_price,
                                    sales_price, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description,prodType);

                            productDataModel2.add(product_DataModel2);

                        }
                    }

                    String value = getArguments().getString("YourKey");

                    if (value.equals("SchoolUniform")){

                        schoolUniform();

                    }else{

                        schoolAccessories();

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                Log.d("userId",user_id);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
    public void schoolUniform(){

        if(productDataModel2.size() != 0){

            schoolUniformAdapter = new SchoolUniformAdapter(productDataModel2,getActivity());
            //gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            productDetailsRecycler.setLayoutManager(gridLayoutManager);
            productDetailsRecycler.setHasFixedSize(true);
            productDetailsRecycler.setAdapter(schoolUniformAdapter);
        }else{
            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
        }
    }
    public void schoolAccessories(){

        if (productDataModel1.size() != 0){

            schoolAccessoriesAdapter = new SchoolAccessoriesAdapter(productDataModel1,getActivity());
            gridLayoutManager1 = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
           // gridLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);;
            productDetailsRecycler.setLayoutManager(gridLayoutManager1);
            productDetailsRecycler.setHasFixedSize(true);
            productDetailsRecycler.setAdapter(schoolAccessoriesAdapter);
        }else{
            Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
        }

    }
    public void addFilterDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.showfilterproduct);
        //dialog.setCancelable(false);
        spinner_City = dialog.findViewById(R.id.spinner_City);
        spinner_Class = dialog.findViewById(R.id.spinner_Class);
        spinner_School = dialog.findViewById(R.id.spinner_School);

        Button btn_Save = dialog.findViewById(R.id.btn_Save);
        Button btn_cancle = dialog.findViewById(R.id.btn_cancle);


        getCity();
        getClassDet();

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (city_Id.equals("")){

                    Toast.makeText(getActivity(), "Select City", Toast.LENGTH_SHORT).show();

                } else if (schoolId.equals("")) {

                    Toast.makeText(getActivity(), "Select School", Toast.LENGTH_SHORT).show();

                } else if (class_Id.equals("")) {

                    Toast.makeText(getActivity(), "Select class", Toast.LENGTH_SHORT).show();

                }else{

                    GetFilterProduct(city_Id,schoolId,class_Id);
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //window.setBackgroundDrawableResource(R.drawable.dialogback);


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
                        spinner_School.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                        spinner_School.setSelection(-1, true);

                        spinner_School.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                    SchoolModelClass mystate = (SchoolModelClass) parent.getSelectedItem();

                                    schoolId = mystate.getSchool_id();
                                    schoolName = mystate.getSchool_name();

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
    public void getClassDet(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get class Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Getclass, new Response.Listener<String>() {
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

                            String class_id = jsonObject_state.getString("class_id");
                            String class_name = jsonObject_state.getString("class_name");

                            ClassModelClass classModelClass = new ClassModelClass(
                                    class_id, class_name
                            );

                            arrayListClass.add(classModelClass);
                        }

                        ClassSpinearAdapter adapter = new ClassSpinearAdapter(getContext(), R.layout.spiner_text
                                , arrayListClass);
                        spinner_Class.setAdapter(adapter);

                        Log.d("citylist", arrayListCity.toString());

                        spinner_Class.setSelection(-1, true);

                        spinner_Class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                try {

                                   ClassModelClass mystate = (ClassModelClass) parent.getSelectedItem();

                                    class_Id = mystate.getClass_id();
                                    class_Name = mystate.getClass_name();
                                    Log.d("R_Pincode", city_Id);

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
    public void GetFilterProduct(String city, String school, String cClass){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get Product Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.Filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")){

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        JSONArray jsonArray_product = new JSONArray(statusArray);

                        productDataModel1.clear();
                        productDataModel2.clear();
                        productDataModel3.clear();

                        if (jsonArray_product.length() != 0){

                            dialog.dismiss();

                            for (int i = 0; i < jsonArray_product.length(); i++) {

                                JSONObject jsonObject_product = jsonArray_product.getJSONObject(i);

                                String product_id = jsonObject_product.getString("product_id");
                                String product_name = jsonObject_product.getString("product_name");
                                String product_type = jsonObject_product.getString("product_type");
                                String pschool_id = jsonObject_product.getString("pschool_id");
                                String pclass_id = jsonObject_product.getString("pclass_id");
                                String description = jsonObject_product.getString("description");
                                String brands_id = jsonObject_product.getString("brands_id");
                                String primary_image = jsonObject_product.getString("primary_image");
                                String vendor_id = jsonObject_product.getString("vendor_id");
                                String pcity_id = jsonObject_product.getString("pcity_id");
                                String trendingg = jsonObject_product.getString("trendingg");
                                String today_dealing_date_time = jsonObject_product.getString("today_dealing_date_time");
                                String regular_price = jsonObject_product.getString("regular_price");
                                String sales_price = jsonObject_product.getString("sales_price");
                                String stock = jsonObject_product.getString("stock");
                                String category_1 = jsonObject_product.getString("category_1");
                                String status1 = jsonObject_product.getString("status");
                                String prodType = jsonObject_product.getString("prodType");
                                String created_date = jsonObject_product.getString("created_date");
                                String updated_date = jsonObject_product.getString("updated_date");

                                ProductFilterModel productFilterModel = new ProductFilterModel(
                                        product_id,product_name,product_type,pschool_id,pclass_id,description,brands_id,
                                        primary_image,vendor_id,pcity_id,trendingg,today_dealing_date_time,regular_price,sales_price,
                                        stock,category_1,status1,prodType,created_date,updated_date);

                                productDataModel3.add(productFilterModel);

                            }


                            if(productDataModel3.size() != 0){

                                filterProductAdapter = new FilterProductAdapter(productDataModel3,getActivity());
                                //gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                                productDetailsRecycler.setLayoutManager(gridLayoutManager);
                                productDetailsRecycler.setHasFixedSize(true);
                                productDetailsRecycler.setAdapter(filterProductAdapter);
                            }else{
                                Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                            }

                        }else{

                            Toast.makeText(getActivity(), "Product Details Not Found", Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("school", school);
                params.put("class", cClass);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


}
