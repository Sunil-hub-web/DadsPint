package com.example.schooluniformapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;

import com.example.schooluniformapp.databinding.ActivityLogiRegisterPageBinding;

public class LogiRegisterPage extends AppCompatActivity {

    ActivityLogiRegisterPageBinding binding;

    private UiModeManager uiModeManager;
    LoginPageFragment test;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_logi_register_page);

        binding = ActivityLogiRegisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(LogiRegisterPage.this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginPageFragment loginPageFragment = new LoginPageFragment();
        ft.replace(R.id.fram, loginPageFragment, "loginpage");
        ft.addToBackStack(null);
        ft.commit();

        binding.login.setTextColor(getResources().getColor(R.color.bluedrack));;

        test = (LoginPageFragment) getSupportFragmentManager().findFragmentByTag("loginpage");

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sessionManager.getlOGGEDIN()){

            startActivity(new Intent(LogiRegisterPage.this,DeshBoardActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /* Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);*/

        this.finish();
        System.exit(0);
    }
}