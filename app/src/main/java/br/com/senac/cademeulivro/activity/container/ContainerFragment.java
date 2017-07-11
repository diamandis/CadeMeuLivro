package br.com.senac.cademeulivro.activity.container;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.model.ContainerTipos;
import br.com.senac.cademeulivro.util.classes.ImageUtils;

/**
 * Created by Didi on 5/23/2017.
 */

public class ContainerFragment extends Fragment {
    private ContainerTipos tipo;

    public static ContainerFragment newInstance(ContainerTipos c) {
        Bundle args = new Bundle();
        args.putSerializable("tipo", c);
        ContainerFragment fragment = new ContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tipo = (ContainerTipos) getArguments().getSerializable("tipo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.g_fragment_container,container, false);

        ImageView icone = (ImageView) v.findViewById(R.id.iconeContainer);
        TextView tipoNome = (TextView) v.findViewById(R.id.tipoContainer);
        icone.setImageResource(tipo.getTipoIcone());
        tipoNome.setText(tipo.getTipoNome());


        return v;
    }
}
