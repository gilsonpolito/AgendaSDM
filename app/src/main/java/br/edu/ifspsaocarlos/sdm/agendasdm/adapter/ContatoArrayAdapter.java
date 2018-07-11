package br.edu.ifspsaocarlos.sdm.agendasdm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class ContatoArrayAdapter extends ArrayAdapter<Contato> {
    private LayoutInflater inflater;

    public ContatoArrayAdapter(Context context, List<Contato> listaContatos) {
        super(context, android.R.layout.simple_list_item_1, listaContatos);
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class Holder{
        public TextView nomeContatoTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new Holder();
            holder.nomeContatoTextView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Contato contato = getItem(position);
        holder.nomeContatoTextView.setText(contato.getNome());
        return convertView;
    }
}
