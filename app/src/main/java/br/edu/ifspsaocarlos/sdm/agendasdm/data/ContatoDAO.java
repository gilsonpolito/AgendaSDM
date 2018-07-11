package br.edu.ifspsaocarlos.sdm.agendasdm.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class ContatoDAO {
    private Context contexto;
    private SharedPreferences database;
    private List<Contato> listaTodosContatos;

    private final String CHAVE_LISTA_CONTATOS = "LISTA_CONTATOS";

    public ContatoDAO(Context contexto) {
        this.contexto = contexto;
    }

    // Vamos simular o armazenamento em banco com um SharedPreferences
    public void open() {
        database = this.contexto.getSharedPreferences("br.edu.ifspsaocarlos.sdm.agendasdm.CONTATOS", Context.MODE_PRIVATE);
        listaTodosContatos = buscaTodosContatos();
    }
    public void close() {

    }

    public List<Contato> buscaTodosContatos() {
        String listaContatosString = database.getString(CHAVE_LISTA_CONTATOS, "");
        if (listaTodosContatos != null) {
            listaTodosContatos.clear();
        }
        else {
            listaTodosContatos = new ArrayList<>();
        }

        if (listaContatosString.length() > 0) {
            try {
                JSONArray listaContatoJSONArray = new JSONArray(listaContatosString);
                for (int index = 0; index < listaContatoJSONArray.length(); index++) {
                    JSONObject objetoJson = (JSONObject) listaContatoJSONArray.get(index);
                    Contato contato = new Contato(objetoJson.getLong("id"), objetoJson.getString("nome"), objetoJson.getString("fone"), objetoJson.getString("email"));

                    listaTodosContatos.add(contato);
                }
            }
            catch (JSONException jse){

            }

        }
        return listaTodosContatos;
    }

    public List<Contato> buscaContato(String nome) {
        List<Contato> listaContatosEncontrados = new ArrayList<>();

        for (Contato contato : listaTodosContatos) {
            if (contato.getNome().compareTo(nome) == 0) {
                listaContatosEncontrados.add(contato);
            }
        }
        return listaContatosEncontrados;
    }

    public Contato buscaContato(long id) {
        for (Contato contato : listaTodosContatos) {
            if (contato.getId() == id) {
                return contato;
            }
        }
        return null;
    }

    public void updateContato(Contato contatoAtualizado) {
        Contato contatoAntigo = buscaContato(contatoAtualizado.getId());
        contatoAntigo.setNome(contatoAtualizado.getNome());
        contatoAntigo.setFone(contatoAtualizado.getFone());
        contatoAntigo.setEmail(contatoAtualizado.getEmail());
        salvaAtualizaListaTodosContatos();
    }

    public void createContato(Contato novoContato) {
        if (listaTodosContatos.size() == 0) {
            novoContato.setId(0);
        }
        else {
            Contato ultimoContato = listaTodosContatos.get(listaTodosContatos.size() - 1);
            novoContato.setId(ultimoContato.getId() + 1);
        }
        listaTodosContatos.add(novoContato);
        salvaAtualizaListaTodosContatos();
    }

    public void deleteContato(Contato contatoRemover){
        for (Contato contato : listaTodosContatos) {
            if (contato.getId() == contatoRemover.getId()){
                listaTodosContatos.remove(contato);
                break;
            }
        }
        salvaAtualizaListaTodosContatos();
    }

    private void salvaAtualizaListaTodosContatos() {
        JSONArray listaContatoJSONArray = converteListaContatoJSONArray();
        SharedPreferences.Editor editor = database.edit();
        editor.putString(CHAVE_LISTA_CONTATOS, listaContatoJSONArray.toString());
        editor.commit();

        listaTodosContatos = buscaTodosContatos();
    }

    private JSONArray converteListaContatoJSONArray() {
        JSONArray jsonArray = new JSONArray();
        for (Contato contato : listaTodosContatos) {
            JSONObject contatoJSON = new JSONObject();
            try {
                contatoJSON.put("id", contato.getId());
                contatoJSON.put("nome", contato.getNome());
                contatoJSON.put("fone", contato.getFone());
                contatoJSON.put("email", contato.getEmail());
                jsonArray.put(contatoJSON);
            }
            catch (JSONException je) {

            }
        }
        return jsonArray;
    }
}


