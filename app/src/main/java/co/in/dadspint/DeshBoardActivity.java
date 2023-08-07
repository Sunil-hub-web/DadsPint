package co.in.dadspint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.in.dadspint.R;
import co.in.dadspint.databinding.ActivityDeshBoardBinding;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeshBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityDeshBoardBinding binding;

    public static TextView nav_MyOrder, text_name, nav_Profile, nav_MyAddress, nav_Home,
            nav_Logout, nav_Name, nav_MobileNo, nav_Wallet, nav_ContactUs,nav_PrivacyPolicy,nav_TermsConditions;

    public static ImageView backimage, menu, image_search;
    SessionManager sessionManager;
    public static RelativeLayout realBack;
    private Boolean exit = false;
    String userId;
    public static BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_desh_board);

        binding = ActivityDeshBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        sessionManager = new SessionManager(DeshBoardActivity.this);

        text_name = findViewById(R.id.text_AddressName);
        backimage = findViewById(R.id.backimage);
        menu = findViewById(R.id.menu);
        image_search = findViewById(R.id.image_search);
        realBack = findViewById(R.id.realBack);

        realBack.setVisibility(View.VISIBLE);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.replace(R.id.framLayout, homeFragment);
        text_name.setText("");
        ft.addToBackStack(null);
        ft.commit();

       // text_name.setText("");
        //text_name.setVisibility(View.GONE);

        realBack.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        binding.navigationview.setNavigationItemSelectedListener(this);
        View header = binding.navigationview.getHeaderView(0);
        nav_Profile = header.findViewById(R.id.nav_Profile);
        //profile_image = header.findViewById(R.id.nav_profile_image);
        nav_Name = header.findViewById(R.id.nav_Name);
        nav_MobileNo = header.findViewById(R.id.nav_MobileNo);
        nav_Home = header.findViewById(R.id.nav_Home);
        nav_MyAddress = header.findViewById(R.id.nav_MyAddress);
        nav_MyOrder = header.findViewById(R.id.nav_MyOrder);
        nav_Logout = header.findViewById(R.id.nav_Logout);
        nav_Wallet = header.findViewById(R.id.nav_Wallet);
        nav_ContactUs = header.findViewById(R.id.nav_ContactUs);
        nav_PrivacyPolicy = header.findViewById(R.id.nav_PrivacyPolicy);
        nav_TermsConditions = header.findViewById(R.id.nav_TermsConditions);

        nav_Name.setText(sessionManager.getUSERNAME());
        nav_MobileNo.setText("+91 " + sessionManager.getMOBILENO());

        userId = sessionManager.getUSERID();
        cart_count(userId);

        nav_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                PersonalInformation personalInformation = new PersonalInformation();
                ft.replace(R.id.framLayout, personalInformation);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("PersonalInformation");


            }
        });

        nav_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if(test != null && test.isVisible()){

                    drawerLayout.closeDrawer(GravityCompat.START);
                    text_name.setVisibility(View.VISIBLE);

                }else{

                    drawerLayout.closeDrawer(GravityCompat.START);
                    //loc.setVisibility(View.VISIBLE);
                    //logo.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    //text_address.setVisibility(View.VISIBLE);
                    text_name.setVisibility(View.VISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Homepage homepage = new Homepage();
                    ft.replace(R.id.framLayout, homepage,"HomeFragment");
                    ft.commit();
                    text_name.setTextSize(15);
                    text_name.setText(addressDetails);

                }*/

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.VISIBLE);
                //logo.setVisibility(View.VISIBLE);
                // search.setVisibility(View.VISIBLE);
                //text_address.setVisibility(View.VISIBLE);
                text_name.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                HomeFragment homepage = new HomeFragment();
                ft.replace(R.id.framLayout, homepage, "HomeFragment");
                ft.addToBackStack(null);
                ft.commit();
                text_name.setTextSize(15);
                text_name.setText("");
                menu.setVisibility(View.VISIBLE);
                backimage.setVisibility(View.GONE);
                image_search.setVisibility(View.VISIBLE);

            }
        });

        nav_MyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Address_fragment address_fragment = new Address_fragment();
                ft.replace(R.id.framLayout, address_fragment);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("Address Details");
            }
        });

        nav_ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ContactUsFragment contactUsFragment = new ContactUsFragment();
                ft.replace(R.id.framLayout, contactUsFragment);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("Contact Us");
            }
        });

        nav_PrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
                ft.replace(R.id.framLayout, privacyPolicyFragment);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("Privacy Policy");
            }
        });

        nav_TermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                TermsConditionsFragment termsConditionsFragment = new TermsConditionsFragment();
                ft.replace(R.id.framLayout, termsConditionsFragment);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("Terms & Conditions");
            }
        });

        nav_MyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                MyOrderDet_Fragment myOrderDet_fragment = new MyOrderDet_Fragment();
                ft.replace(R.id.framLayout, myOrderDet_fragment);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("My Order Details");

            }
        });

        nav_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //text_address.setVisibility(View.GONE);
                //  search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Wallet_Fragment wallet_fragment = new Wallet_Fragment();
                ft.replace(R.id.framLayout, wallet_fragment);
                ft.addToBackStack(null);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("Wallet");
            }
        });

        nav_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout_Condition();
            }
        });

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                text_name.setVisibility(View.VISIBLE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                HomeFragment homepage = new HomeFragment();
                ft.replace(R.id.framLayout, homepage, "HomeFragment");
                ft.addToBackStack(null);
                ft.commit();
                text_name.setTextSize(15);
                text_name.setText("");
                menu.setVisibility(View.VISIBLE);
                backimage.setVisibility(View.GONE);
                image_search.setVisibility(View.VISIBLE);

                binding.bottomNavigation.setSelectedItemId(R.id.home);

               // onBackPressed();
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.home);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {

                    case R.id.myAccount:

                        selectedFragment = new MyOrderDet_Fragment();

                        //loc.setVisibility(View.GONE);
                        //logo.setVisibility(View.GONE);
                        // search.setVisibility(View.GONE);
                        text_name.setTextSize(18);
                        text_name.setText("My Order Details");
                        //text_address.setVisibility(View.GONE);

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment).addToBackStack(null).commit();

                        break;

                    case R.id.home:

                        selectedFragment = new HomeFragment();
                        //loc.setVisibility(View.VISIBLE);
                        text_name.setVisibility(View.VISIBLE);
                        //text_address.setVisibility(View.VISIBLE);
                        //logo.setVisibility(View.VISIBLE);
                        // search.setVisibility(View.VISIBLE);
                        text_name.setTextSize(15);
                        text_name.setText("");
                        menu.setVisibility(View.VISIBLE);
                        backimage.setVisibility(View.GONE);
                        image_search.setVisibility(View.VISIBLE);

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment, "HomeFragment").addToBackStack(null).commit();

                        break;

                    case R.id.cart:

                        selectedFragment = new CartFragment();
                        //loc.setVisibility(View.GONE);
                        //logo.setVisibility(View.GONE);
                        image_search.setVisibility(View.GONE);
                        text_name.setTextSize(18);
                        text_name.setText("My Cart");
                        //text_address.setVisibility(View.GONE);
                        menu.setVisibility(View.GONE);
                        backimage.setVisibility(View.VISIBLE);
                        image_search.setVisibility(View.GONE);

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment).addToBackStack(null).commit();

                        break;

                    case R.id.wishlist:

                        selectedFragment = new WishlistFragment();
                        //loc.setVisibility(View.GONE);
                        //logo.setVisibility(View.GONE);
                        image_search.setVisibility(View.GONE);
                        text_name.setTextSize(18);
                        text_name.setText("Wishlist");
                        //text_address.setVisibility(View.GONE);
                        menu.setVisibility(View.GONE);
                        backimage.setVisibility(View.VISIBLE);
                        image_search.setVisibility(View.GONE);

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment).addToBackStack(null).commit();

                        break;

                }
                //getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,selectedFragment).commit();

                return true;
            }
        });

    }
    public void Clickmenu(View view) {

        // open drawer
        openDrawer(binding.MyDrawer);
    }
    private static void openDrawer(DrawerLayout drawerLayout) {

        // opendrawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void Serachpage(View view){

        binding.MyDrawer.closeDrawer(GravityCompat.START);
        text_name.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SerachProductFragment serachProductFragment = new SerachProductFragment();
        ft.replace(R.id.framLayout,new SerachProductFragment());
        ft.addToBackStack(null);
        ft.commit();
        text_name.setTextSize(15);
        //text_name.setText("Home Page");
        menu.setVisibility(View.VISIBLE);
        backimage.setVisibility(View.GONE);
        image_search.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        binding.MyDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {

        HomeFragment test = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
        WebViewFragment fragmentInstance = (WebViewFragment) getSupportFragmentManager().findFragmentByTag("WebViewFragment");

      //  Log.d("fragmentInstance",fragmentInstance.toString());

        if (test != null && test.isVisible()) {

            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }, 4 * 1000);
            }
        } else if (fragmentInstance != null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, new CheckOut_Fragment(), "checkout").commit();
            realBack.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            realBack.setVisibility(View.VISIBLE);

        } else {

            text_name.setText("");

          //  HomeFragment.frag.beginTransaction().replace(R.id.framLayout,new Homepage(),"HomeFragment").addToBackStack(null).commit();

            getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, new HomeFragment(), "HomeFragment").commit();
            realBack.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            realBack.setVisibility(View.VISIBLE);

        }
    }
    public void logout_Condition() {

        //Show Your Another AlertDialog
        final Dialog dialog = new Dialog(DeshBoardActivity.this);
        dialog.setContentView(R.layout.condition_logout);
        dialog.setCancelable(false);
        Button btn_No = dialog.findViewById(R.id.no);
        TextView textView = dialog.findViewById(R.id.editText);
        Button btn_Yes = dialog.findViewById(R.id.yes);

        btn_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUser();

                dialog.dismiss();

                finish();
                //System.exit(1);

            }
        });
        btn_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.drawable.homecard_back1);

    }
    public void cart_count(String user_id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.cart_count, new Response.Listener<String>() {
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
                        String cart_count = jsonObject_message.getString("cart_count");
                        JSONObject jsonObject_cart_count = new JSONObject(cart_count);
                        String total_cart = jsonObject_cart_count.getString("total_cart");

                        int int_total_cart = Integer.parseInt(total_cart);

                        BadgeDrawable badge = binding.bottomNavigation.getOrCreateBadge(R.id.cart);//R.id.action_add is menu id
                        badge.setNumber(int_total_cart);
                        badge.setBackgroundColor(ContextCompat.getColor(DeshBoardActivity.this,R.color.bluedrack));

                    } else {

                        String error = jsonObject.getString("error");
                        String messages = jsonObject.getString("messages");
                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String cart_count = jsonObject_message.getString("cart_count");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DeshBoardActivity.this, "" + error, Toast.LENGTH_SHORT).show();

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

                Log.d("addressparameterlist",params.toString());

                return params;


            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DeshBoardActivity.this);
        requestQueue.add(stringRequest);
    }

/*    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framLayout);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
        super.onBackPressed();
    }*/
}