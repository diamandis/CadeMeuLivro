package br.com.senac.cademeulivro.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.dao.BitmapDAO;
import br.com.senac.cademeulivro.helpers.DatabaseHelper;
import br.com.senac.cademeulivro.model.BitmapCapa;
import br.com.senac.cademeulivro.model.Obra;
import br.com.senac.cademeulivro.util.classes.ImageUtils;


public class AdapterListViewObra extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Obra> itens;
    private Context context;


    public AdapterListViewObra(Context context, List<Obra> itens) {
        this.context = context;
        this.itens = itens;
        inflater=LayoutInflater.from(context);
    }

    public void setItens(List<Obra> itens) {
        this.itens = itens;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //Resgatar o item do ListView pelo position
        Obra item= (Obra) itens.get(position);

        //Resgatar o layout a ser preenchido
        view=inflater.inflate(R.layout.c_activity_item_lista_obras,null);

        //Resgatar os dois textviews e o imageview para insercao do conteudo
        TextView titulo=(TextView) view.findViewById(R.id.TextViewTituloLista);
        TextView autor=(TextView) view.findViewById(R.id.TextViewAutorLista);
        TextView editora=(TextView) view.findViewById(R.id.TextViewEditoraLista);
        ImageView capa=(ImageView) view.findViewById(R.id.ImageViewCapaLista);


        titulo.setText((item.getTitulo()!=null && item.getTitulo().length()>20) ? item.getTitulo().substring(0,20)+"..." : item.getTitulo());
        autor.setText((item.getAutor()!=null && item.getAutor().length()>20) ? item.getAutor().substring(0,20)+"..." : item.getAutor());
        editora.setText((item.getEditora()!=null && item.getEditora().length()>20) ? item.getEditora().substring(0,20)+"..." : item.getEditora());
        if(item.getCapaUrl() != null) {
            ImageUtils.loadCapa(item.getCapaUrl(),context,capa);
        } else if (item.getIdBitmap() != null && item.getIdBitmap() > 0){
            BitmapDAO dao = new BitmapDAO(DatabaseHelper.newInstance(context));
            BitmapCapa c = dao.getById(item.getIdBitmap());
            capa.setImageBitmap(c.getCapa());
        } else {
            capa.setImageResource(R.drawable.placeholder);
        }

        return view;
    }

}
