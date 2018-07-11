package br.edu.ifspsaocarlos.sdm.agendasdm.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.edu.ifspsaocarlos.sdm.agendasdm.R;
import br.edu.ifspsaocarlos.sdm.agendasdm.model.Contato;

public class DetalhesContatoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nomeEditText;
    private EditText foneEditText;
    private EditText emailEditText;
    private Button salvarButton;

    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_contato);

        nomeEditText = (EditText) findViewById(R.id.et_nome);
        foneEditText = (EditText) findViewById(R.id.et_telefone);
        emailEditText = (EditText) findViewById(R.id.et_email);
        salvarButton = (Button) findViewById(R.id.bt_salvar);
        salvarButton.setOnClickListener(this);

        Intent intent = getIntent();
        int requisicao = intent.getIntExtra(AgendaActivity.EXTRA_CODIGO_REQUISICAO, -1);
        if(requisicao == AgendaActivity.CONSULTA_CONTATO){
            nomeEditText.setEnabled(false);
            foneEditText.setEnabled(false);
            emailEditText.setEnabled(false);
            salvarButton.setVisibility(View.GONE);
        }
        contato = (Contato) intent.getSerializableExtra(AgendaActivity.EXTRA_CONTATO);
        if(contato != null){
            nomeEditText.setText(contato.getNome());
            foneEditText.setText(contato.getFone());
            emailEditText.setText(contato.getEmail());
        }
    }

    @Override
    public void onClick(View view) {
        if (contato == null){
            contato = new Contato();
        }

        contato.setNome(nomeEditText.getText().toString());
        contato.setFone(foneEditText.getText().toString());
        contato.setEmail(emailEditText.getText().toString());

        Intent resultadoIntent = new Intent();
        resultadoIntent.putExtra(AgendaActivity.EXTRA_CONTATO, contato);
        setResult(RESULT_OK, resultadoIntent);
        finish();
    }
}
