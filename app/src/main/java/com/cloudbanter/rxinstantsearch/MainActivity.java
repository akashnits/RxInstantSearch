package com.cloudbanter.rxinstantsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_local_search)
    Button btnLocalSearch;
    @BindView(R.id.btn_remote_search)
    Button btnRemoteSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.btn_local_search, R.id.btn_remote_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_local_search:
                startActivity(new Intent(MainActivity.this, LocalSearchActivity.class));
                break;
            case R.id.btn_remote_search:
                startActivity(new Intent(MainActivity.this, RemoteSearchActivity.class));
                break;
        }
    }
}
