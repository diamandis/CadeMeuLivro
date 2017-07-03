package br.com.senac.cademeulivro.activity.container;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.dao.ContainerDAO;
import br.com.senac.cademeulivro.dao.ContainerTiposDAO;
import br.com.senac.cademeulivro.helpers.DatabaseHelper;
import br.com.senac.cademeulivro.model.Container;
import br.com.senac.cademeulivro.model.ContainerTipos;
import br.com.senac.cademeulivro.util.classes.AlarmReceiver;
import br.com.senac.cademeulivro.util.classes.GerenciadorDeNotificacoes;


public class ContainerEditActivity extends AppCompatActivity {

    private EditText editNome,editLocal;
    private TextView tvIcone, ultimaModificacao;
    private ViewPager mViewPager;
    private List<ContainerTipos> mContainerTiposList;
    private SQLiteDatabase mDatabase;
    private ContainerDAO containerDAO;
    private ContainerTiposDAO containerTiposDAO;
    private ContainerTipos tipo;
    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_container_edit);

        mViewPager = (ViewPager) findViewById(R.id.cadastro_pager);
        editNome= (EditText) findViewById(R.id.editNomeCont);
        editLocal= (EditText) findViewById(R.id.editLocalCont);
        tvIcone= (TextView) findViewById(R.id.textIconeContEdit);
        ultimaModificacao= (TextView) findViewById(R.id.ultimaModificacao);

        mDatabase = DatabaseHelper.newInstance(this);
        containerTiposDAO = new ContainerTiposDAO(mDatabase);
        containerDAO = new ContainerDAO(mDatabase);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                tipo = mContainerTiposList.get(position);
                return ContainerFragment.newInstance(tipo);
            }

            @Override
            public int getCount() {
                return mContainerTiposList.size();
            }
        });

        Bundle params=getIntent().getExtras();

        if(params!=null) {
            container= (Container) params.getSerializable("container");
            preencheCampos(container);
        }else{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            ultimaModificacao.setText(getString(R.string.ultima_modif_container, df.format(new Date())));
        }


    }

    public void containerSalvar(View v){

        boolean edicao=true;

        if(container==null){
            edicao=false;
            container = new Container();
        }

        container.setNomeContainer(editNome.getText().toString());
        container.setLocalContainer(editLocal.getText().toString());
        container.setUltimaModificacao(new Date());
        //container.setIdBiblioteca(1); //user teste
        container.setContainerTipos(new ContainerTipos(mViewPager.getCurrentItem()+1)); //pager conta a partir do 0
        ContainerDAO containerDAO = new ContainerDAO(mDatabase);
        long result = 0;

        if(container.getIdContainer() !=null) {
            result = containerDAO.update(container);
        } else {
            result = containerDAO.insert(container);
        }
        if(result > 0) {
            if(edicao==false){
                GerenciadorDeNotificacoes notificacoes= new GerenciadorDeNotificacoes(ContainerEditActivity.this,
                        new Intent(ContainerEditActivity.this, AlarmReceiver.class), container.getNomeContainer());

                notificacoes.criarNotification((int) result);
            }
            finish();
        } else {
            Toast.makeText(this,"Erro", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void preencheCampos(Container container){

        mViewPager.setCurrentItem(container.getContainerTipos().get_id()-1 );
        editNome.setText(container.getNomeContainer());
        editLocal.setText(container.getLocalContainer());
        tvIcone.setText(container.getContainerTipos().getTipoIcone());
        ultimaModificacao.setText(String.valueOf(container.getUltimaModificacao()));
    }

    public void containerCancelar(View v){
        finish();
    }
}
