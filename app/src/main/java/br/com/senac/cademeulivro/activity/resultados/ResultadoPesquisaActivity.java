package br.com.senac.cademeulivro.activity.resultados;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.activity.obra.ObraDetalhadaActivity;
import br.com.senac.cademeulivro.dao.ObraDAO;
import br.com.senac.cademeulivro.helpers.DatabaseHelper;
import br.com.senac.cademeulivro.model.Obra;
import br.com.senac.cademeulivro.model.ObraPreview;
import br.com.senac.cademeulivro.util.adapter.AdapterListViewObra;
import br.com.senac.cademeulivro.util.adapter.AdapterListViewPesquisa;
import br.com.senac.cademeulivro.util.classes.ResultadoPesquisa;


public class ResultadoPesquisaActivity extends AppCompatActivity {

    private ListView listaResultAcervo, listaResultGoogle;
    private ObraDAO obraDao;
    private SQLiteDatabase mDatabase;
    private AdapterListViewObra adapterListViewAcervo;
    private AdapterListViewPesquisa adapterListViewGoogle;
    private boolean internoVisivel=false;
    private boolean externoVisivel=false;
    private List<Obra> listaExterna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_resultado_pesquisa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaResultAcervo= (ListView) findViewById(R.id.listaResultAcervo);
        listaResultGoogle= (ListView) findViewById(R.id.listaResultGoogle);

        mDatabase = new DatabaseHelper(getApplicationContext()).getWritableDatabase();
        obraDao = new ObraDAO(mDatabase);

        ResultadoPesquisa externa=new ResultadoPesquisa(this);
        Bundle parametros = getIntent().getExtras();

        //capturando resultado interno
        if (parametros != null) {

            List<ObraPreview> lista = obraDao.getByName(parametros.getString("data"));
            adapterListViewAcervo = new AdapterListViewObra(this, lista);
            listaResultAcervo.setAdapter(adapterListViewAcervo);
        }

        //capturando resultado externo
        listaExterna= externa.pesquisar(parametros.getString("data"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapterListViewGoogle = new AdapterListViewPesquisa(this, listaExterna);
        listaResultGoogle.setAdapter(adapterListViewGoogle);

        listaResultAcervo.setOnItemClickListener(cliqueCurtoAcervo());
        listaResultGoogle.setOnItemClickListener(cliqueCurtoGoogle());
    }

    public void expandirInterno(View v){

        if(internoVisivel==false){
            listaResultAcervo.setVisibility(View.VISIBLE);
            internoVisivel=true;
        }else{
            listaResultAcervo.setVisibility(View.GONE);
            internoVisivel=false;
        }
    }

    public void expandirExterno(View v) {

        if(externoVisivel==false){
            listaResultGoogle.setVisibility(View.VISIBLE);
            externoVisivel=true;
        }else{
            listaResultGoogle.setVisibility(View.GONE);
            externoVisivel=false;
        }
    }

    public AdapterView.OnItemClickListener cliqueCurtoAcervo() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ObraPreview obra = (ObraPreview) adapterListViewAcervo.getItem(position);
                Intent intent = new Intent(ResultadoPesquisaActivity.this, ObraDetalhadaActivity.class);
                intent.putExtra("id",obra.getIdObra());
                startActivity(intent);
            }
        };
    }

    public AdapterView.OnItemClickListener cliqueCurtoGoogle() {

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Obra obra = (Obra) adapterListViewGoogle.getItem(position);

                Intent intent = new Intent(ResultadoPesquisaActivity.this, ObraDetalhadaActivity.class);
                intent.putExtra("capa",obra.getCapa());
                obra.setCapa(null);
                intent.putExtra("obra",obra);
                startActivity(intent);
            }
        };
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
