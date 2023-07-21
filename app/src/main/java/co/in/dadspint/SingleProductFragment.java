package co.in.dadspint;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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

public class SingleProductFragment extends Fragment {

    ArrayList<SingleProductModel> productDataModel = new ArrayList<>();
    ArrayList<SingleProductGallery> singleProductGalleries = new ArrayList<>();
    ArrayList<SingleProducuAttribute> singleProducuAttributes = new ArrayList<>();
    ArrayList<SingleProductVariations> singleProductVariations;
    TextView productName,product_decs,regular_price,tv_minus1,tv_count1,tv_plus;
    String product_name,regularprice,description,primary_image,productId,product_Name,userId,
            quenty,quantity,price,varitionprice,str_singleProducuAttributes;
    public static String salesprice;
    public static TextView sales_price;
    ViewPager2 showImageViewPager2;
    TextView [] dots;
    SliderAdpter sliderAdpter;
    RecyclerView recyclerAttribute;
    AttributeVaritionAdapter attributeVaritionAdapter;
    SessionManager sessionManager;
    ImageView backimage,img_wishlist;
    ArrayList<String> str_singleProductVariations = new ArrayList<>();
    HashMap<String,String> hash_singleProductVariations = new HashMap<>();
    int count_value;
    LinearLayout lin_addCart,lin_add_cart,dots_container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_single_product_desc,container,false);

        productName = view.findViewById(R.id.productName);
        product_decs = view.findViewById(R.id.product_decs);
        regular_price = view.findViewById(R.id.regular_price);
        sales_price = view.findViewById(R.id.sales_price);
        showImageViewPager2 = view.findViewById(R.id.showImageViewPager2);
        recyclerAttribute = view.findViewById(R.id.recyclerAttribute);
        tv_minus1 = view.findViewById(R.id.tv_minus1);
        tv_count1 = view.findViewById(R.id.tv_count1);
        tv_plus = view.findViewById(R.id.tv_plus);
        lin_addCart = view.findViewById(R.id.lin_addCart);
        lin_add_cart = view.findViewById(R.id.lin_add_cart);
      //  lin_ByNow = view.findViewById(R.id.lin_ByNow);
       // backimage = view.findViewById(R.id.backimage);
        dots_container = view.findViewById(R.id.dots_container);
        img_wishlist = view.findViewById(R.id.img_wishlist);

        productId = getArguments().getString("productId");
        product_Name = getArguments().getString("productName");

       // productId = getIntent().getStringExtra("productId");
        //product_Name = getIntent().getStringExtra("productName");

        DeshBoardActivity.text_name.setTextSize(15);
        DeshBoardActivity.text_name.setText("Product Description");
        DeshBoardActivity.menu.setVisibility(View.GONE);
        DeshBoardActivity.backimage.setVisibility(View.VISIBLE);
        DeshBoardActivity.image_search.setVisibility(View.GONE);
        DeshBoardActivity.realBack.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAccent));

        sessionManager = new SessionManager(getContext());
        userId = sessionManager.getUSERID();

        getSingleProduct(userId,productId);

        showImageViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                selectedIndicatorPosition(position);
                super.onPageSelected(position);
            }
        });

        tv_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout(false);


                varitionprice = attributeVaritionAdapter.getvaritionPrice();

                if (str_singleProducuAttributes.equals("singleProducuAttributes")){

                    quantity = tv_count1.getText().toString().trim();
                    count_value = Integer.valueOf(tv_count1.getText().toString());

                    addToCart(userId,productId,quantity,"",salesprice);

                }else{

                    quantity = tv_count1.getText().toString().trim();
                    count_value = Integer.valueOf(tv_count1.getText().toString());

                    addToCart(userId,productId,quantity,varitionprice,salesprice);
                }




            }
        });

        tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout(true);

                if (str_singleProducuAttributes.equals("singleProducuAttributes")){

                    quantity = tv_count1.getText().toString().trim();
                    count_value = Integer.valueOf(tv_count1.getText().toString());
                    varitionprice = attributeVaritionAdapter.getvaritionPrice();

                    addToCart(userId,productId,quantity,"",salesprice);

                }else{

                    quantity = tv_count1.getText().toString().trim();
                    count_value = Integer.valueOf(tv_count1.getText().toString());
                    varitionprice = attributeVaritionAdapter.getvaritionPrice();

                    addToCart(userId,productId,quantity,varitionprice,salesprice);
                }



            }
        });

        lin_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sales_price.getText().toString().trim().equals("")){

                    Toast.makeText(getContext(), "Sales Price Not Found", Toast.LENGTH_SHORT).show();

                }else if (tv_count1.getText().toString().trim().equals("")){

                    Toast.makeText(getContext(), "Quentity Not Found", Toast.LENGTH_SHORT).show();

                }else{

                    //quantity = tv_count1.getText().toString().trim();
                    //String strprice = sales_price.getText().toString().trim();
                   // varitionprice = attributeVaritionAdapter.getvaritionPrice();

                   // addToCart(userId,productId,quantity,varitionprice,salesprice);

//                   Log.d("veritionprice",String.valueOf(attributeVaritionAdapter.getvaritionPrice()));

                    if (str_singleProducuAttributes.equals("singleProducuAttributes")){

                        quantity = tv_count1.getText().toString().trim();
                        count_value = Integer.valueOf(tv_count1.getText().toString());

                        addToCart(userId,productId,quantity,"",salesprice);

                        Log.d("veritionprice1",userId+", "+productId+", "+quantity+", "+salesprice);

                    }else{

                        quantity = tv_count1.getText().toString().trim();
                        count_value = Integer.valueOf(tv_count1.getText().toString());
                        varitionprice = attributeVaritionAdapter.getvaritionPrice();

                        addToCart(userId,productId,quantity,varitionprice,salesprice);

                        Log.d("veritionprice2",userId+", "+productId+", "+quantity+", "+varitionprice+", "+salesprice);
                    }

                }
            }
        });

        /*lin_ByNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeshBoardActivity.text_name.setVisibility(View.VISIBLE);

                CartFragment cartFragment = new CartFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, cartFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                DeshBoardActivity.text_name.setTextSize(15);
                DeshBoardActivity.text_name.setText("Cart Page");
                DeshBoardActivity.menu.setVisibility(View.GONE);
                DeshBoardActivity.backimage.setVisibility(View.VISIBLE);
                DeshBoardActivity.image_search.setVisibility(View.GONE);
            }
        });*/
        img_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addWishList(userId,productId);
            }
        });

        return view;
    }

    public void getSingleProduct(String user_id, String product_id){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Get Product Please Wait.....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.SingleProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    progressDialog.dismiss();

                    if(status.equals("200")) {

                        singleProducuAttributes.clear();
                        productDataModel.clear();
                        singleProductGalleries.clear();

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String statusArray = jsonObject_message.getString("status");
                        JSONObject jsonObject_statues = new JSONObject(statusArray);
                        String SingleProduct = jsonObject_statues.getString("SingleProduct");
                        String ProductGallery = jsonObject_statues.getString("ProductGallery");
                        String attribute = jsonObject_statues.getString("attribute");

                        JSONArray jsonArray_SingleProduct = new JSONArray(SingleProduct);

                        if (jsonArray_SingleProduct.length() != 0){

                            for (int i= 0;i<jsonArray_SingleProduct.length();i++){

                                JSONObject jsonObject_product = jsonArray_SingleProduct.getJSONObject(0);

                                String product_id = jsonObject_product.getString("product_id");
                                product_name = jsonObject_product.getString("product_name");
                                primary_image = jsonObject_product.getString("primary_image");
                                String vendor_id = jsonObject_product.getString("vendor_id");
                                String trendingg = jsonObject_product.getString("trendingg");
                                String today_dealing_date_time = jsonObject_product.getString("today_dealing_date_time");
                                String product_type = jsonObject_product.getString("product_type");
                                regularprice = jsonObject_product.getString("regular_price");
                                salesprice = jsonObject_product.getString("sales_price");
                                String stock = jsonObject_product.getString("stock");
                                String city_name = jsonObject_product.getString("city_name");
                                String city_id = jsonObject_product.getString("city_id");
                                String school_id = jsonObject_product.getString("school_id");
                                String school_name = jsonObject_product.getString("school_name");
                                String class_id = jsonObject_product.getString("class_id");
                                String class_name = jsonObject_product.getString("class_name");
                                String brands_id = jsonObject_product.getString("brands_id");
                                String brands_name = jsonObject_product.getString("brands_name");
                                description = jsonObject_product.getString("description");

                                SingleProductModel singleProductModel = new SingleProductModel(
                                        product_id, product_name, primary_image, vendor_id, trendingg, today_dealing_date_time, product_type, regularprice,
                                        salesprice, stock, city_name, city_id, school_id, school_name, class_id, class_name, brands_id, brands_name, description);

                                productDataModel.add(singleProductModel);

                            }

                            productName.setText(product_name);
                            regular_price.setText("Rs  "+regularprice);
                            sales_price.setText("Rs  "+salesprice);
                            product_decs.setText(Html.fromHtml(description));
                            regular_price.setPaintFlags(regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        }else{

                            Toast.makeText(getContext(), "Product Details Not Found", Toast.LENGTH_LONG).show();
                        }

                        JSONArray jsonArray_ProductGallery = new JSONArray(ProductGallery);

                        if (jsonArray_ProductGallery.length() !=0){

                            for (int j = 0;j<jsonArray_ProductGallery.length();j++){

                                JSONObject jsonObject_productGallery = jsonArray_ProductGallery.getJSONObject(j);

                                String gallery_id = jsonObject_productGallery.getString("gallery_id");
                                String product_id = jsonObject_productGallery.getString("product_id");
                                String image = jsonObject_productGallery.getString("image");

                                SingleProductGallery singleProductGallery = new SingleProductGallery(
                                        gallery_id,product_id,image
                                );

                                singleProductGalleries.add(singleProductGallery);
                            }

                            sliderAdpter = new SliderAdpter(getContext(),singleProductGalleries);
                            showImageViewPager2.setAdapter(sliderAdpter);
                            int arraysize = singleProductGalleries.size();
                            dots = new TextView[arraysize];
                            dotsIndicator();

                        }else{

                            SingleProductGallery singleProductGallery = new SingleProductGallery(
                                    "","",primary_image
                            );

                            singleProductGalleries.add(singleProductGallery);

                            sliderAdpter = new SliderAdpter(getContext(),singleProductGalleries);
                            showImageViewPager2.setAdapter(sliderAdpter);
                            int arraysize = singleProductGalleries.size();
                            dots = new TextView[arraysize];
                            dotsIndicator();

                           // Toast.makeText(getContext(), "Product Image Not Found", Toast.LENGTH_LONG).show();
                        }

                        JSONArray jsonArray_attribute = new JSONArray(attribute);

                        if (jsonArray_attribute.length() != 0){

                            for (int k=0;k<jsonArray_attribute.length();k++){

                                JSONObject jsonObject_attribute = jsonArray_attribute.getJSONObject(k);

                                String attribute_id = jsonObject_attribute.getString("attribute_id");
                                String attribute_name = jsonObject_attribute.getString("attribute_name");
                                String product_id = jsonObject_attribute.getString("product_id");
                                String variations = jsonObject_attribute.getString("variations");

                                singleProductVariations = new ArrayList<>();
                                str_singleProductVariations = new ArrayList<>();
                                hash_singleProductVariations = new HashMap<>();

                                singleProductVariations.clear();
                                str_singleProductVariations.clear();
                                hash_singleProductVariations.clear();

                                JSONArray jsonArray_variations = new JSONArray(variations);

                                for (int m=0;m<jsonArray_variations.length();m++){

                                    JSONObject jsonObject_variations = jsonArray_variations.getJSONObject(m);
                                    String variation_id = jsonObject_variations.getString("variation_id");
                                    String variation_name = jsonObject_variations.getString("variation_name");

                                    str_singleProductVariations.add(variation_name);
                                    hash_singleProductVariations.put(variation_name,variation_id);

                                    SingleProductVariations singleProductVariations1 = new SingleProductVariations(
                                            variation_id,variation_name
                                    );

                                    singleProductVariations.add(singleProductVariations1);
                                }

                                SingleProducuAttribute singleProducuAttribute = new SingleProducuAttribute(
                                        attribute_id,attribute_name,product_id,singleProductVariations,
                                        str_singleProductVariations,hash_singleProductVariations,salesprice
                                );

                                singleProducuAttributes.add(singleProducuAttribute);
                            }

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                    getContext(),LinearLayoutManager.VERTICAL,false
                            );

                            attributeVaritionAdapter = new AttributeVaritionAdapter(getContext(),singleProducuAttributes);
                            recyclerAttribute.setLayoutManager(linearLayoutManager);
                            recyclerAttribute.setHasFixedSize(true);
                            recyclerAttribute.setAdapter(attributeVaritionAdapter);

                            str_singleProducuAttributes = "";

                        }else{

                          //  Toast.makeText(getContext(), "Attribute Not Found", Toast.LENGTH_SHORT).show();

                            str_singleProducuAttributes = "singleProducuAttributes";

                        }

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user_id",user_id);
                params.put("product_id",product_id);
                Log.d("productdaa",user_id+",  "+product_id);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void selectedIndicatorPosition(int position) {


        for(int i=0;i<dots.length;i++){


            if(i==position){

                dots[i].setTextColor(getResources().getColor(R.color.blue600));

            }else{

                dots[i].setTextColor(getResources().getColor(R.color.primary));
            }
        }

    }
    private void dotsIndicator() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);

        for(int i=0;i<dots.length;i++){

            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(20);
            dots[i].setPadding(5, 0, 5, 0);
            dots[i].setLayoutParams(params);
            dots_container.addView(dots[i]);
        }
    }
    private void linearLayout(Boolean x) {
        int y = Integer.parseInt(tv_count1.getText().toString());
        if (x) {
            y++;
            tv_count1.setText(String.valueOf(y));
        } else {
            y--;
            if (y <= 0) {
                tv_count1.setText("1");
            } else {
                tv_count1.setText(String.valueOf(y));
            }
        }
    }
    public void addToCart(String user_id,String product_id,String qty,String variation_id,String price){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
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

                        Toast.makeText(getContext(), status_message, Toast.LENGTH_SHORT).show();

                        lin_addCart.setVisibility(View.GONE);
                        lin_add_cart.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
    public void addWishList(String userId,String ProductId){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Add To Cart Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.PostWishlist, new Response.Listener<String>() {
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
                Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("cust_id",userId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
