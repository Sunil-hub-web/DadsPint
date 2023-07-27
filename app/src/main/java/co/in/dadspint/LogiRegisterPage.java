package co.in.dadspint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import co.in.dadspint.R;
import co.in.dadspint.databinding.ActivityLogiRegisterPageBinding;

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

        binding.login.setTextColor(getResources().getColor(R.color.bluedrack));

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

        Fragment currentFragment = null;
        try {
            currentFragment = getSupportFragmentManager().findFragmentById(R.id.fram);
            Log.d("BackPressedFragment", currentFragment.toString());
        } catch (Exception e) {
            Log.d("BackPressedFragment", e.toString());
        }

        if (currentFragment instanceof LoginPageFragment) {

            this.finish();
            System.exit(0);

        } else if (currentFragment instanceof RegisterFragment) {

           startActivity(new Intent(getApplicationContext(),LogiRegisterPage.class));

        } else if (currentFragment instanceof ForgotPasswordFragment) {

            startActivity(new Intent(getApplicationContext(),LogiRegisterPage.class));

        } else if (currentFragment instanceof TermsConditionsFragment) {

            startActivity(new Intent(getApplicationContext(),LogiRegisterPage.class));

        }else{

            this.finish();
            System.exit(0);
        }
    }
}