package com.elyeproj.testingcoverage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainView {

    private EditText editNumberOne;
    private EditText editNumberTwo;
    private TextView textSum;
    private Button buttonSum;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        setupView();
    }

    private void setupView() {
        editNumberOne = findViewById(R.id.edt_number_one);
        editNumberTwo = findViewById(R.id.edt_number_two);
        textSum = findViewById(R.id.txt_sum);
        buttonSum = findViewById(R.id.btn_sum);

        buttonSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.add(editNumberOne.getText().toString(), editNumberTwo.getText().toString());
            }
        });
    }

    @Override
    public void showResult(String result) {
        textSum.setText(result);
    }

    @Override
    public String getIllegalResult() {
        return getString(R.string.illegal_result);
    }
}
