package com.fatec.desenvolvimentomobile.rachai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fatec.desenvolvimentomobile.rachai.Model.Usuario;
import com.fatec.desenvolvimentomobile.rachai.R;

import java.util.List;

public class MotoristaAdapter extends ArrayAdapter<Usuario> {
    public MotoristaAdapter(Context context, List<Usuario> motoristas) {
        super(context, 0, motoristas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.motorista_adapter, parent, false);
        }

        Usuario motorista = getItem(position);

        TextView textNome = convertView.findViewById(R.id.text_nome);
        TextView textCurso = convertView.findViewById(R.id.text_curso);


        textNome.setText(motorista.getNome());
        textCurso.setText("Curso: " + motorista.getCurso());

        return convertView;
    }
}
