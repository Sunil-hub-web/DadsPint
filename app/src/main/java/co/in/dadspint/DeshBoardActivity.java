package co.in.dadspint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DeshBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityDeshBoardBinding binding;

    public static TextView nav_MyOrder, text_name, nav_Profile, nav_MyAddress, nav_Home,
            nav_Logout, nav_Name, nav_MobileNo, text_ItemCount, nav_ContactUs,
            text_addressName, nav_Categogry;
    public static ImageView backimage, menu, image_search;
    SessionManager sessionManager;
    public static RelativeLayout realBack;

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_desh_board);

        binding = ActivityDeshBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(DeshBoardActivity.this);

        text_name = findViewById(R.id.text_AddressName);
        backimage = findViewById(R.id.backimage);
        menu = findViewById(R.id.menu);
        image_search = findViewById(R.id.image_search);
        realBack = findViewById(R.id.realBack);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.replace(R.id.framLayout, homeFragment);
        text_name.setText("Home");
        ft.addToBackStack(null);
        ft.commit();

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

        nav_Name.setText(sessionManager.getUSERNAME());
        nav_MobileNo.setText("+91 " + sessionManager.getMOBILENO());

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
                ft.commit();
                text_name.setTextSize(15);
                text_name.setText("Home Page");
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
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("Address Details");
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
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("My Order Details");

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
                ft.commit();
                text_name.setTextSize(15);
                text_name.setText("Home Page");
                menu.setVisibility(View.VISIBLE);
                backimage.setVisibility(View.GONE);
                image_search.setVisibility(View.VISIBLE);

                binding.bottomNavigation.setSelectedItemId(R.id.home);
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.home);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {

                    case R.id.myAccount:

                        selectedFragment = new PersonalInformation();

                        //loc.setVisibility(View.GONE);
                        //logo.setVisibility(View.GONE);
                        // search.setVisibility(View.GONE);
                        text_name.setTextSize(18);
                        text_name.setText("PersonalInformation");
                        //text_address.setVisibility(View.GONE);

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment).commit();

                        break;

                    case R.id.home:

                        selectedFragment = new HomeFragment();
                        //loc.setVisibility(View.VISIBLE);
                        text_name.setVisibility(View.VISIBLE);
                        //text_address.setVisibility(View.VISIBLE);
                        //logo.setVisibility(View.VISIBLE);
                        // search.setVisibility(View.VISIBLE);
                        text_name.setTextSize(15);
                        text_name.setText("Home Page");
                        menu.setVisibility(View.VISIBLE);
                        backimage.setVisibility(View.GONE);
                        image_search.setVisibility(View.VISIBLE);

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment, "HomeFragment").commit();

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

                        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, selectedFragment).commit();

                        break;

                    /*case R.id.wishlist:

                        return true;*/

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

        Toast.makeText(this, "Work in Progress", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        binding.MyDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {

        HomeFragment test = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");

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
        } else {

            text_name.setText("Home Page");
           /* HomePageActivity.fragmentManager.beginTransaction()
                    .replace(R.id.framLayout,new Homepage(),"HomeFragment").addToBackStack(null).commit();*/

            getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, new HomeFragment(), "HomeFragment").commit();
            realBack.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

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
}