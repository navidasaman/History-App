package com.example.assignment5project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            final EditText editText = findViewById(R.id.edit_text);

            @Override
            public void onClick(View view) {
                String myEditText = editText.getText().toString();
                Log.d("TAG", "Start SecondActivity" + myEditText);

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("name", myEditText);


                startActivity(intent);
                Log.d("TAG", "Start SecondActivity");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home_page:
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent1);
                Log.d("HOME", "Successfully launched home page");
                return true;
            case R.id.about_page:
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                Log.d("ABOUT", "About page");
                return true;
        }
        return super.onOptionsItemSelected(item);

    }



}

