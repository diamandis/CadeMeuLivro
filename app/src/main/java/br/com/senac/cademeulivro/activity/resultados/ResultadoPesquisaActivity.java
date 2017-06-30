package br.com.senac.cademeulivro.activity.resultados;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.senac.cademeulivro.R;


public class ResultadoPesquisaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_resultado_pesquisa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }//pesquisa por autor ou titulo

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
