package co.in.dadspint;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    SliderView imageSlider;
    List<SliderItem> sliderItemList = new ArrayList<>();
    private SliderAdapterExample adapter;
    ArrayList<ProductDataModel> productDataModel1 = new ArrayList<>();
    ArrayList<ProductDataModel> productDataModel2 = new ArrayList<>();
    ArrayList<BannerModelClass> bannerModel = new ArrayList<>();
    ArrayList<AllblogModel> allblogModel = new ArrayList<>();
    ArrayList<CategoryModelClass> allcategorymode = new ArrayList<>();
    SchoolUniformAdapter schoolUniformAdapter;
    SchoolAccessoriesAdapter schoolAccessoriesAdapter;
   // GridLayoutManager gridLayoutManager, gridLayoutManager1;
    LinearLayoutManager gridLayoutManager, gridLayoutManager1, gridLayoutManager2;
    RecyclerView schooluniformRecycler,schoolAccessRecycler,categorylist;
    CardView card_SchoolUniform,card_SchoolAccessories;
    SessionManager sessionManager;
    String userId;
    CategoryListAdapter categoryListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        imageSlider = view.findViewById(R.id.imageSlider);
        schooluniformRecycler = view.findViewById(R.id.schooluniformRecycler);
        schoolAccessRecycler = view.findViewById(R.id.schoolAccessRecycler);
        card_SchoolUniform = view.findViewById(R.id.card_SchoolUniform);
        card_SchoolAccessories = view.findViewById(R.id.card_SchoolAccessories);
        categorylist = view.findViewById(R.id.categorylist);

        sessionManager = new SessionManager(getActivity());
        userId = sessionManager.getUSERID();

        Log.d("ggvyvhvyc",userId);

        card_SchoolUniform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putString("YourKey", "SchoolUniform");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                DeshBoardActivity.menu.setVisibility(View.GONE);
                DeshBoardActivity.backimage.setVisibility(View.VISIBLE);

            }
        });

        card_SchoolAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putString("YourKey", "SchoolAccessories");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                DeshBoardActivity.menu.setVisibility(View.GONE);
                DeshBoardActivity.backimage.setVisibility(View.VISIBLE);
            }
        });

        imageSlider.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {

                Fragment fragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putString("YourKey", "SchoolUniform");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                DeshBoardActivity.menu.setVisibility(View.GONE);
                DeshBoardActivity.backimage.setVisibility(View.VISIBLE);
            }
        });

        sessionManager.setUSERID(userId);
        getHomeDetails(userId);

        return view;
    }
    public void getHomeDetails(String user_id) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.user_home, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                Log.d("poductresponse", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("200")){

                        allcategorymode.clear();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");

                        JSONObject jsonstatues = new JSONObject(statusArray);

                        String product_data = jsonstatues.getString("product_data");
                        String banner = jsonstatues.getString("banner");
                        String Allblog = jsonstatues.getString("Allblog");
                        String category_det = jsonstatues.getString("category");

                        JSONArray jsonArray_category = new JSONArray(category_det);

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
                            categorylist.setLayoutManager(gridLayoutManager2);
                            categorylist.setHasFixedSize(true);
                            categorylist.setAdapter(categoryListAdapter);
                        }

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

                                productDataModel2.add(product_DataModel1);

                            } else {

                                ProductDataModel product_DataModel2 = new ProductDataModel(
                                        product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regular_price,
                                        sales_price, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description,prodType);

                                productDataModel1.add(product_DataModel2);

                            }
                        }

                        if(productDataModel1.size() != 0){

                            schoolUniformAdapter = new SchoolUniformAdapter(productDataModel1,getActivity());
                            gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            schooluniformRecycler.setLayoutManager(gridLayoutManager);
                            schooluniformRecycler.setHasFixedSize(true);
                            schooluniformRecycler.setAdapter(schoolUniformAdapter);
                        }

                        if (productDataModel2.size() != 0){

                            schoolAccessoriesAdapter = new SchoolAccessoriesAdapter(productDataModel2,getActivity());
                            //   gridLayoutManager1 = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                            gridLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            schoolAccessRecycler.setLayoutManager(gridLayoutManager1);
                            schoolAccessRecycler.setHasFixedSize(true);
                            schoolAccessRecycler.setAdapter(schoolAccessoriesAdapter);
                        }


                        JSONArray jsonArray_banner = new JSONArray(banner);

                        for (int j=0;j<jsonArray_banner.length();j++){

                            JSONObject jsonObject_banner = jsonArray_banner.getJSONObject(j);

                            String banner_id = jsonObject_banner.getString("banner_id");
                            String banner_title = jsonObject_banner.getString("banner_title");
                            String type = jsonObject_banner.getString("type");
                            String orderby = jsonObject_banner.getString("orderby");
                            String image = jsonObject_banner.getString("image");

                            BannerModelClass bannerModelClass = new BannerModelClass(banner_id,banner_title,type,orderby,image);
                            bannerModel.add(bannerModelClass);
                        }

                        adapter = new SliderAdapterExample(getActivity(), bannerModel);
                        imageSlider.setSliderAdapter(adapter);
                        imageSlider.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        imageSlider.setIndicatorSelectedColor(Color.WHITE);
                        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                        imageSlider.setScrollTimeInSec(3);
                        imageSlider.setAutoCycle(true);
                        imageSlider.startAutoCycle();

                        JSONArray jsonArray_Allblog = new JSONArray(Allblog);

                        for (int k=0;k<jsonArray_Allblog.length();k++){

                            JSONObject jsonObject_Allblog = jsonArray_Allblog.getJSONObject(k);

                            String blog_id = jsonObject_Allblog.getString("blog_id");
                            String name = jsonObject_Allblog.getString("name");
                            String title = jsonObject_Allblog.getString("title");
                            String message = jsonObject_Allblog.getString("message");
                            String category = jsonObject_Allblog.getString("category");
                            String image = jsonObject_Allblog.getString("image");
                            String date = jsonObject_Allblog.getString("date");

                            AllblogModel allblogModel1 = new AllblogModel(
                                    blog_id,name,title,message,category,image,date
                            );

                            allblogModel.add(allblogModel1);
                        }

                    }else {

                        Toast.makeText(getActivity(), "Product Details Not Found", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
