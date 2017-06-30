package br.com.senac.cademeulivro.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;

import java.util.List;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.activity.container.ContainerEditActivity;
import br.com.senac.cademeulivro.activity.container.ContainerListFragment;
import br.com.senac.cademeulivro.activity.obra.ObraDetalhadaEditActivity;
import br.com.senac.cademeulivro.activity.resultados.ResultadoScannerActivity;
import br.com.senac.cademeulivro.activity.tabs.tab_ObrasActivity;
import br.com.senac.cademeulivro.activity.tabs.tab_RecomendadosActivity;
import br.com.senac.cademeulivro.activity.tabs.tab_TagsActivity;
import br.com.senac.cademeulivro.activity.tag.TagEditActivity;
import br.com.senac.cademeulivro.model.Obra;
import br.com.senac.cademeulivro.util.classes.Scanner;
import br.com.senac.cademeulivro.util.constante.Constantes;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FloatingActionButton fab;
    private Scanner scanner;
    private List<Obra> lista;
    private int tabPosicao=0;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        mViewPager.setCurrentItem(tab.getPosition());
                        tabPosicao=tab.getPosition();
                    }
                });


        final String PREFS_NAME = "MyPrefsFile";


        //primeira vez que o app é aberto
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {

            //inicar activity de instruções
            //primeiroAcesso();
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
    }

    public void fabFuncao(View v){

        CharSequence opcoes[];
        Intent intent;

        switch (tabPosicao){

            case 0:
                intent=new Intent(MainActivity.this,ContainerEditActivity.class);
                startActivity(intent);

                break;
            case 1:
                opcoes = new CharSequence[] {"Manualmente", "Escanear ISBN"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Cadastrar");
                builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0){
                            Intent intent=new Intent(MainActivity.this, ObraDetalhadaEditActivity.class);
                            startActivity(intent);

                        }else{
                            //pega isbn via camera
                            Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
                            intent.setAction("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SAVE_HISTORY", false);
                            startActivityForResult(intent, Constantes.SCANNER_REQUEST);
                        }

                    }
                });
                builder.show();

                break;
            case 2:
                intent=new Intent(MainActivity.this, TagEditActivity.class);
                startActivity(intent);

                break;
            case 3:

                //recomendados
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:

                Dialog dialog= new Dialog(MainActivity.this);
                dialog.setTitle(getString(R.string.configuracoes));
                dialog.setContentView(R.layout.h_custom_dialog);
                dialog.show();

                final CheckBox notificacoes= (CheckBox) dialog.findViewById(R.id.checkboxNotificacoes);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(final DialogInterface d) {

                        //notificacoes.isChecked()
                    }
                });


                break;
            case R.id.action_logout:

                break;
            case R.id.action_search:

                //...
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    ContainerListFragment tabContainers=new ContainerListFragment();
                    return tabContainers;
                case 1:
                    tab_ObrasActivity tabObras=new tab_ObrasActivity();
                    return tabObras;
                case 2:
                    tab_TagsActivity tabTags=new tab_TagsActivity();
                    return tabTags;
                case 3:
                    tab_RecomendadosActivity tabRecomendados=new tab_RecomendadosActivity();
                    return tabRecomendados;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Containers";
                case 1:
                    return "Obras";
                case 2:
                    return "Tags";
                case 3:
                    return "Recomendados";
            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constantes.SCANNER_REQUEST) {
            if (resultCode == RESULT_OK) {

                //capturando o resultado do scanner
                String contents = data.getStringExtra("SCAN_RESULT");

                Intent intent=new Intent(this, ResultadoScannerActivity.class);
                intent.putExtra("isbn",contents);
                startActivity(intent);
                /*
                Intent intent=new Intent(this,ResultadoScannerActivity.class);
                intent.putExtra("lista",(ArrayList<Obra>)lista);
                startActivity(intent);
                */
            } else {
                Toast.makeText(this, getString(R.string.falha_leitura), Toast.LENGTH_SHORT).show();
            }
        }

    }



}
