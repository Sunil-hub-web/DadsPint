package co.in.dadspint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class SubCategoryPage extends Fragment {

    String categoryID,categoryName;
    ArrayList<CategoryModelClass> allcategorymode = new ArrayList<>();
    CategoryListAdapter categoryListAdapter;
    LinearLayoutManager gridLayoutManager2;
    GridLayoutManager gridLayoutManager, gridLayoutManager1;
    RecyclerView subcategoryRecycler,productlistRecycler;
    ArrayList<ProductDataModel> productDataModel1 = new ArrayList<>();
    ArrayList<ProductDataModel> productDataModel2 = new ArrayList<>();
    SchoolUniformAdapter schoolUniformAdapter;
    SchoolAccessoriesAdapter schoolAccessoriesAdapter;
  //  TextView subcategorynotfound;
    LinearLayout cartempty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.subcategory_fragment,container,false);

        subcategoryRecycler = view.findViewById(R.id.subcategoryRecycler);
        productlistRecycler = view.findViewById(R.id.productlistRecycler);
     //   subcategorynotfound = view.findViewById(R.id.subcategorynotfound);
        cartempty = view.findViewById(R.id.cartempty);

        categoryID = getArguments().getString("categoryID");
        categoryName = getArguments().getString("categoryName");

     //   subcategorynotfound.setVisibility(View.GONE);

        Log.d("categoryiddada",categoryID);

        getSubCategoryDet(categoryID);

        return view;
    }

    public void getSubCategoryDet(String cat_id){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Get SubCategory Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.getsubcatagory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                Log.d("poductresponse", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")) {

                        allcategorymode.clear();
                        productDataModel1.clear();
                        productDataModel2.clear();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusobject = jsonObject_message.getString("status");

                        JSONObject jsonObject_statues = new JSONObject(statusobject);

                        String category = jsonObject_statues.getString("category");
                        String product_data = jsonObject_statues.getString("product_data");

                        JSONArray jsonArray_category = new JSONArray(category);
                        JSONArray jsonArray_product = new JSONArray(product_data);

                        if(jsonArray_category.length() == 0 & jsonArray_product.length() == 0 ){

                            subcategoryRecycler.setVisibility(View.GONE);
                            subcategoryRecycler.setVisibility(View.GONE);
                            cartempty.setVisibility(View.VISIBLE);

                        }else{

                            if (jsonArray_category.length() != 0){

                                for(int m= 0;m<jsonArray_category.length();m++){

                                    JSONObject jsonObject1_category = jsonArray_category.getJSONObject(m);

                                    String cat_id = jsonObject1_category.getString("cat_id");
                                    String cat_name = jsonObject1_category.getString("cat_name");
                                    String cat_img = jsonObject1_category.getString("cat_img");
                                    String status1 = jsonObject1_category.getString("status");

                                    CategoryModelClass categoryModelClass = new CategoryModelClass(
                                            cat_id,cat_name,cat_img,status1
                                    );

                                    allcategorymode.add(categoryModelClass);
                                }

                                categoryListAdapter = new CategoryListAdapter(allcategorymode,getActivity());
                                gridLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                subcategoryRecycler.setLayoutManager(gridLayoutManager2);
                                subcategoryRecycler.setHasFixedSize(true);
                                subcategoryRecycler.setAdapter(categoryListAdapter);

                               // subcategorynotfound.setVisibility(View.GONE);

                            }else {

                                Toast.makeText(getActivity(),"Category Not Found",Toast.LENGTH_SHORT).show();
                               // subcategorynotfound.setVisibility(View.VISIBLE);
                            }



                            if (jsonArray_product.length() == 0){

                                Toast.makeText(getActivity(),"Product Details Not Found",Toast.LENGTH_SHORT).show();
                                cartempty.setVisibility(View.VISIBLE);

                            }else {

                                cartempty.setVisibility(View.GONE);

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
                                    //   String prodType = jsonObject_product.getString("prodType");
                                    String description = jsonObject_product.getString("description");

                                    if (product_type.equals("1")) {

                                        ProductDataModel product_DataModel1 = new ProductDataModel(
                                                product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regular_price,
                                                sales_price, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description,"");

                                        productDataModel1.add(product_DataModel1);

                                    } else {

                                        ProductDataModel product_DataModel2 = new ProductDataModel(
                                                product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regular_price,
                                                sales_price, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description,"");

                                        productDataModel2.add(product_DataModel2);

                                    }
                                }

                                if(productDataModel1.size() != 0){

                                    schoolUniformAdapter = new SchoolUniformAdapter(productDataModel1,getActivity());
                                    gridLayoutManager1 = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                                    productlistRecycler.setLayoutManager(gridLayoutManager1);
                                    productlistRecycler.setHasFixedSize(true);
                                    productlistRecycler.setAdapter(schoolUniformAdapter);
                                }

                                if (productDataModel2.size() != 0){

                                    schoolAccessoriesAdapter = new SchoolAccessoriesAdapter(productDataModel2,getActivity());
                                    gridLayoutManager1 = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                                    //gridLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);;
                                    productlistRecycler.setLayoutManager(gridLayoutManager1);
                                    productlistRecycler.setHasFixedSize(true);
                                    productlistRecycler.setAdapter(schoolAccessoriesAdapter);
                                }
                            }
                        }

                    }else{

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        Toast.makeText(getActivity(), statusArray, Toast.LENGTH_SHORT).show();
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
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("cat_id",cat_id);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
