package co.in.dadspint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import co.in.dadspint.R;
import com.google.android.material.button.MaterialButton;

public class OrderSuccessFully extends AppCompatActivity {

    MaterialButton backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_fully);

        backToHome = findViewById(R.id.backToHome);

         getSupportActionBar().hide();
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window = OrderSuccessFully.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(OrderSuccessFully.this, R.color.white));

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderSuccessFully.this, DeshBoardActivity.class);
                startActivity(intent);
            }
        });
    }
}