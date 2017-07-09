package br.com.senac.cademeulivro.activity.resultados;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.activity.obra.ObraDetalhadaActivity;
import br.com.senac.cademeulivro.model.Obra;
import br.com.senac.cademeulivro.util.classes.ISBNScanner;

public class ResultadoScannerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Obra> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_resultado_scanner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerScanner);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String isbn = getIntent().getStringExtra("isbn");
        //new FetchObrasTask().execute("9788575224540");
        new FetchObrasTask().execute(isbn);

    }

    private class ObraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView capa;
        private TextView titulo, autor, editora;
        private Obra mObra;

        public ObraHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titulo = (TextView) itemView.findViewById(R.id.TextViewTituloLista);
            autor = (TextView) itemView.findViewById(R.id.TextViewAutorLista);
            editora = (TextView) itemView.findViewById(R.id.TextViewEditoraLista);
            capa = (ImageView) itemView.findViewById(R.id.ImageViewCapaLista);
        }

        public void bind(Obra o) {
            mObra = o;
            Picasso.with(ResultadoScannerActivity.this)
                    .load(mObra.getCapaUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(capa);
            Bitmap bitmap = ((BitmapDrawable)capa.getDrawable()).getBitmap();
            mObra.setCapa(bitmap);
            titulo.setText(mObra.getTitulo());
            autor.setText(mObra.getAutor());
            editora.setText(mObra.getEditora());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ResultadoScannerActivity.this, ObraDetalhadaActivity.class);
            intent.putExtra("obra",mObra);
            startActivity(intent);
        }
    }


    private class ObraAdapter extends RecyclerView.Adapter<ObraHolder> {
        private List<Obra> itens;

        public ObraAdapter(List<Obra> itens) {
            this.itens = itens;
        }

        @Override
        public ObraHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ResultadoScannerActivity.this);
            View v = inflater.inflate(R.layout.c_activity_item_lista_obras, parent, false);

            return new ObraHolder(v);
        }

        @Override
        public void onBindViewHolder(ObraHolder holder, int position) {
            Obra o = itens.get(position);
            holder.bind(o);
        }

        @Override
        public int getItemCount() {
            return itens.size();
        }
    }


    private void setupAdapter() {
        mRecyclerView.setAdapter(new ObraAdapter(lista));
    }

    private class FetchObrasTask extends AsyncTask<String,Void,List<Obra>> {

        @Override
        protected List<Obra> doInBackground(String... params) {
            return new ISBNScanner(ResultadoScannerActivity.this).fetch(params[0]);
        }

        @Override
        protected void onPostExecute(List<Obra> obras) {
            lista = obras;
            setupAdapter();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
