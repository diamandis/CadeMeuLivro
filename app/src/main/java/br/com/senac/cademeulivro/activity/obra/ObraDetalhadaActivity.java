package br.com.senac.cademeulivro.activity.obra;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.model.Obra;
import br.com.senac.cademeulivro.util.constante.Constantes;


public class ObraDetalhadaActivity extends AppCompatActivity {

    private Obra obra;
    private ImageView capa;
    private TextView tvTitulo, tvEditora, tvAutor, tvIsbn, tvAno, tvDescricao;
    private CheckBox emprestado;
    private Button botaoConcluir;
    private Bitmap imagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_obra_detalhada);
        setTitle("Informações");

        tvTitulo= (TextView) findViewById(R.id.TextViewTituloObra);
        tvAno= (TextView) findViewById(R.id.TextViewAnoObra);
        tvAutor= (TextView) findViewById(R.id.TextViewAutorObra);
        tvDescricao= (TextView) findViewById(R.id.TextViewDescricaoObra);
        tvEditora= (TextView) findViewById(R.id.TextViewEditoraObra);
        tvIsbn= (TextView) findViewById(R.id.TextViewIsbnObra);
        emprestado= (CheckBox) findViewById(R.id.CheckBoxEmprestadoObra);
        capa= (ImageView) findViewById(R.id.ImageViewCapaObra);
        botaoConcluir= (Button) findViewById(R.id.ButtonEditarObra);

        Bundle parametros = getIntent().getExtras();

        if (parametros!=null) {
            obra = (Obra) parametros.getSerializable("obra");
            imagem=(Bitmap) parametros.getParcelable("capa");
            capa.setImageBitmap(imagem);
            capa.setScaleX(1.5F);
            capa.setScaleY(1.5F);

            preencheCampos(obra);

            if (obra.getIdObra() == null) {
                botaoConcluir.setText("Adicionar");
            }
        }
    }

    public void obraDetalhadaEditar(View v){

        Intent intent=new Intent(this, ObraDetalhadaEditActivity.class);

        intent.putExtra("capa",imagem);

        obra.setCapa(null);
        intent.putExtra("obra",obra);

        startActivityForResult(intent, Constantes.CLOSE_REQUEST);
        //TODO fazer finish no activity for result
    }

    public void obraDetalhadaVoltar(View v){ finish(); }

    public void preencheCampos(Obra obra){

        tvTitulo.setText(obra.getTitulo());
        tvEditora.setText(obra.getEditora());
        tvIsbn.setText(obra.getIsbn());
        tvDescricao.setText(obra.getDescricao());
        tvAno.setText(String.valueOf(obra.getAnoPublicacao()));
        tvAutor.setText(obra.getAutor());
        emprestado.setChecked(obra.isEmprestado());
        //capa.setImageBitmap(obra.getCapa());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==Constantes.CLOSE_REQUEST) {
                if (resultCode == RESULT_OK) {
                    finish();
                }
        }
    }

}
