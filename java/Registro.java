package com.ericklima.descubraonumero;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    public String nomeJogador;
    public static int SCORE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Button botaoMan = (Button) findViewById(R.id.botaoMan);
        Button botaoWoman = (Button) findViewById(R.id.botaoWoman);
        Button botaoAvancar = (Button) findViewById(R.id.botaoAvancar);
        final Spinner spnDificuldade = (Spinner) findViewById(R.id.spnDificuldade);
        final EditText campoNome = (EditText) findViewById(R.id.campoNome);
        campoNome.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES); //Capitaliza a primeira letra
        ArrayAdapter adpDificuldades = ArrayAdapter.createFromResource(this, R.array.dificuldades, android.R.layout.simple_spinner_dropdown_item);
        spnDificuldade.setAdapter(adpDificuldades);

        final AlertDialog.Builder msg = new AlertDialog.Builder(this);

        botaoAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campoNome.getText().length()>0){
                    nomeJogador = campoNome.getText().toString();
                    msg.setTitle("Vamos prosseguir?");
                    msg.setMessage(campoNome.getText().toString() + ", você quer começar jogando no modo " + spnDificuldade.getSelectedItem().toString() + "?");
                    msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if ((spnDificuldade.getSelectedItem().toString()).equals("Fácil")) {
                                        Intent it = new Intent(Registro.this, Facil.class);
                                        it.putExtra("nome", nomeJogador);
                                        startActivity(it);
                                        finish();
                                    } else if ((spnDificuldade.getSelectedItem().toString()).equals("Moderado")) {
                                        Intent it = new Intent(Registro.this, Moderado.class);
                                        it.putExtra("nome", nomeJogador);
                                        it.putExtra("score", "0");
                                        startActivity(it);
                                        finish();
                                    } else {
                                        Intent it = new Intent(Registro.this, Dificil.class);
                                        it.putExtra("nome", nomeJogador);
                                        it.putExtra("score", String.valueOf(SCORE));
                                        startActivity(it);
                                        finish();
                                    }

                                }
                            });
                    msg.setNegativeButton("Corrigir", null);
                    msg.show();
                } else Toast.makeText(Registro.this, "O campo 'nome' não deve ficar vazio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem about = menu.add(0, 0, 0, "Sobre");
        about.setIcon(R.drawable.info);
        about.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                startActivity(new Intent(Registro.this, InfoScreen.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
