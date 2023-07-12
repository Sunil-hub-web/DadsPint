package co.in.dadspint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import co.in.dadspint.databinding.ActivitySingleProductDescBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleProductDesc extends AppCompatActivity {

    ActivitySingleProductDescBinding binding;
    ArrayList<SingleProductModel> productDataModel = new ArrayList<>();
    ArrayList<SingleProductGallery> singleProductGalleries = new ArrayList<>();
    ArrayList<SingleProducuAttribute> singleProducuAttributes = new ArrayList<>();
    ArrayList<SingleProductVariations> singleProductVariations;
    TextView productName,product_decs,regular_price,tv_minus1,tv_count1,tv_plus;
    String product_name,regularprice,description,primary_image,productId,product_Name,userId,
            productid,quenty,quantity,price,varitionprice;
    public static String salesprice;
    public static TextView sales_price;
    ViewPager2 showImageViewPager2;
    TextView [] dots;
    SliderAdpter sliderAdpter;
    RecyclerView recyclerAttribute;
    AttributeVaritionAdapter attributeVaritionAdapter;
    SessionManager sessionManager;
    ImageView backimage;
    ArrayList<String> str_singleProductVariations = new ArrayList<>();
    HashMap<String,String> hash_singleProductVariations = new HashMap<>();
    int count_value;
    LinearLayout lin_addCart,lin_add_cart,lin_ByNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_single_product_desc);

        binding = ActivitySingleProductDescBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productName = findViewById(R.id.productName);
        product_decs = findViewById(R.id.product_decs);
        regular_price = findViewById(R.id.regular_price);
        sales_price = findViewById(R.id.sales_price);
        showImageViewPager2 = findViewById(R.id.showImageViewPager2);
        recyclerAttribute = findViewById(R.id.recyclerAttribute);
        tv_minus1 = findViewById(R.id.tv_minus1);
        tv_count1 = findViewById(R.id.tv_count1);
        tv_plus = findViewById(R.id.tv_plus);
        lin_addCart = findViewById(R.id.lin_addCart);
        lin_add_cart = findViewById(R.id.lin_add_cart);
        lin_ByNow = findViewById(R.id.lin_ByNow);
        //backimage = findViewById(R.id.backimage);

        productId = getIntent().getStringExtra("productId");
        product_Name = getIntent().getStringExtra("productName");

        sessionManager = new SessionManager(SingleProductDesc.this);
        userId = sessionManager.getUSERID();

        getSingleProduct(userId,productId);

        showImageViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                selectedIndicatorPosition(position);
                super.onPageSelected(position);
            }
        });

        /*binding.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });*/

        tv_minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout(false);

                quantity = tv_count1.getText().toString().trim();
                count_value = Integer.valueOf(tv_count1.getText().toString());
                varitionprice = attributeVaritionAdapter.getvaritionPrice();

                addToCart(userId,productid,quantity,varitionprice,salesprice);

            }
        });

        tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout(true);

                quantity = tv_count1.getText().toString().trim();
                count_value = Integer.valueOf(tv_count1.getText().toString());
                varitionprice = attributeVaritionAdapter.getvaritionPrice();

                addToCart(userId,productid,quantity,varitionprice,salesprice);

            }
        });

        lin_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sales_price.getText().toString().trim().equals("")){

                    Toast.makeText(SingleProductDesc.this, "Sales Price Not Found", Toast.LENGTH_SHORT).show();

                }else if (tv_count1.getText().toString().trim().equals("")){

                    Toast.makeText(SingleProductDesc.this, "Quentity Not Found", Toast.LENGTH_SHORT).show();

                }else{

                    quantity = tv_count1.getText().toString().trim();
                    String strprice = sales_price.getText().toString().trim();
                    varitionprice = attributeVaritionAdapter.getvaritionPrice();

                    addToCart(userId,productId,quantity,varitionprice,salesprice);
                }
            }
        });

        lin_ByNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeshBoardActivity.text_name.setVisibility(View.VISIBLE);

                CartFragment cartFragment = new CartFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
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
        });

    }
    public void getSingleProduct(String user_id, String product_id){

        ProgressDialog progressDialog = new ProgressDialog(SingleProductDesc.this);
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

                            Toast.makeText(SingleProductDesc.this, "Product Details Not Found", Toast.LENGTH_LONG).show();
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

                            sliderAdpter = new SliderAdpter(SingleProductDesc.this,singleProductGalleries);
                            showImageViewPager2.setAdapter(sliderAdpter);
                            int arraysize = singleProductGalleries.size();
                            dots = new TextView[arraysize];
                            dotsIndicator();

                        }else{

                            SingleProductGallery singleProductGallery = new SingleProductGallery(
                                    "","",primary_image
                            );

                            singleProductGalleries.add(singleProductGallery);

                            sliderAdpter = new SliderAdpter(SingleProductDesc.this,singleProductGalleries);
                            showImageViewPager2.setAdapter(sliderAdpter);
                            int arraysize = singleProductGalleries.size();
                            dots = new TextView[arraysize];
                            dotsIndicator();

                            Toast.makeText(SingleProductDesc.this, "Product Image Not Found", Toast.LENGTH_LONG).show();
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
                                    SingleProductDesc.this,LinearLayoutManager.VERTICAL,false
                            );

                            attributeVaritionAdapter = new AttributeVaritionAdapter(SingleProductDesc.this,singleProducuAttributes);
                            recyclerAttribute.setLayoutManager(linearLayoutManager);
                            recyclerAttribute.setHasFixedSize(true);
                            recyclerAttribute.setAdapter(attributeVaritionAdapter);

                        }else{

                            Toast.makeText(SingleProductDesc.this, "Attribute Not Found", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SingleProductDesc.this);
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

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(20);
            dots[i].setPadding(5, 0, 5, 0);
            dots[i].setLayoutParams(params);
            binding.dotsContainer.addView(dots[i]);
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

        ProgressDialog progressDialog = new ProgressDialog(SingleProductDesc.this);
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

                        Toast.makeText(SingleProductDesc.this, status_message, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(SingleProductDesc.this, ""+error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SingleProductDesc.this);
        requestQueue.add(stringRequest);

    }

}