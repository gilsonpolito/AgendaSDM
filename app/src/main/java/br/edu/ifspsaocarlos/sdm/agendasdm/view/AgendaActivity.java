package br.edu.ifspsaocarlos.sdm.agendasdm.view;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.agendasdm.R;
import br.edu.ifspsaocarlos.sdm.agendasdm.adapter.ContatoArrayAdapter;
import br.edu.ifspsaocarlos.sdm.agendasdm.data.ContatoDAO;
import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class AgendaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView contatosListView;
    private List<Contato> listaContatos;
    private ContatoArrayAdapter contatoArrayAdapter;

    public static final int ADICIONA_EDITA_CONTATO = 0;
    public static final int CONSULTA_CONTATO = 1;

    public static final String EXTRA_CODIGO_REQUISICAO = "EXTRA_CODIGO_REQUISICAO";
    public static final String EXTRA_CONTATO = "EXTRA_CONTATO";

    private ContatoDAO contatoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        contatoDAO = new ContatoDAO(this);
        contatoDAO.open();

        Intent intent = getIntent();
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String nomeProcurar = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setSubtitle("Resultado da pesquisa");
            listaContatos = contatoDAO.buscaContato(nomeProcurar);
        } else {
            listaContatos = contatoDAO.buscaTodosContatos();
        }
        contatoArrayAdapter = new ContatoArrayAdapter(this, listaContatos);

        contatosListView = (ListView) findViewById(R.id.lv_contatos);
        contatosListView.setAdapter(contatoArrayAdapter);
        contatosListView.setOnItemClickListener(this);

        registerForContextMenu(contatosListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_adicionar_contato:
                Intent editaContatoIntent = new Intent(this, DetalhesContatoActivity.class);
                editaContatoIntent.putExtra(EXTRA_CODIGO_REQUISICAO, ADICIONA_EDITA_CONTATO);
                startActivityForResult(editaContatoIntent, ADICIONA_EDITA_CONTATO);
                return true;
            case R.id.item_menu_sair:
                finish();
                return true;
            case R.id.item_menu_procurar:
                onSearchRequested();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_flutuante, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo infoItem = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Contato contato = contatoArrayAdapter.getItem(infoItem.position);
        switch (item.getItemId()) {
            case R.id.item_menu_editar_contato:
                Intent editaContatoIntent = new Intent(this, DetalhesContatoActivity.class);
                editaContatoIntent.putExtra(EXTRA_CODIGO_REQUISICAO, ADICIONA_EDITA_CONTATO);
                editaContatoIntent.putExtra(EXTRA_CONTATO, contato);
                startActivityForResult(editaContatoIntent, ADICIONA_EDITA_CONTATO);
                return true;
            case R.id.item_menu_remover_contato:
                //listaContatos.remove(contato);
                contatoDAO.deleteContato(contato);
                listaContatos = contatoDAO.buscaTodosContatos();
                contatoArrayAdapter.notifyDataSetChanged();
                Toast.makeText(this, R.string.contato_removido, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_menu_chamar_contato:
                Intent chamarContaoIntern = new Intent(Intent.ACTION_CALL);
                String numeroTelefone = "tel: " + contato.getFone();
                chamarContaoIntern.setData(Uri.parse(numeroTelefone));
                try {
                    startActivity(chamarContaoIntern);
                } catch (SecurityException se) {
                    Toast.makeText(this, R.string.sem_permissao_chamar_contato, Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Contato contato = contatoArrayAdapter.getItem(i);

        Intent consultaContatoIntent = new Intent(this, DetalhesContatoActivity.class);
        consultaContatoIntent.putExtra(EXTRA_CODIGO_REQUISICAO, CONSULTA_CONTATO);
        consultaContatoIntent.putExtra(EXTRA_CONTATO, contato);
        startActivity(consultaContatoIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADICIONA_EDITA_CONTATO) {
            if (resultCode == RESULT_OK) {
                Contato contato = (Contato) data.getSerializableExtra(EXTRA_CONTATO);
                if (contato.getId() == -1) {
                    //listaContatos.add(contato);
                    contatoDAO.createContato(contato);
                } else {
                   /* for(Contato contatoBusca : listaContatos){
                        if(contatoBusca.getId() == contato.getId()){
                            contatoBusca.setEmail(contato.getEmail());
                            contatoBusca.setNome(contato.getNome());
                            contatoBusca.setFone(contato.getFone());
                            break;
                        }*/
                    contatoDAO.updateContato(contato);
                }
                listaContatos = contatoDAO.buscaTodosContatos();
                contatoArrayAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(this, "Contato não adicionado/editado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contatoDAO.close();
    }
}
