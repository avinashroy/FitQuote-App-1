package com.voyager.fitquote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startQuoteBtn = (Button) findViewById(R.id.get_quote_btn);
        startQuoteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_quote_btn: startQuoteWizard();
        }
    }

    private void startQuoteWizard() {
        Intent wizardScreen1Intent = new Intent(this, StartWizardActivity.class);
        startActivity(wizardScreen1Intent);
    }
}
