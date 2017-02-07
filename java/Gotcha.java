package com.ericklima.descubraonumero;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Gotcha extends AppCompatActivity {

    private String nomeJogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotcha);

        setTitle("Parabéns!");
        Button botaoJogar = (Button) findViewById(R.id.botaoJogar);
        Button botaoSair = (Button) findViewById(R.id.botaoSair);
        Button botaoSobre = (Button) findViewById(R.id.botaoSobre);
        TextView txtJogador = (TextView) findViewById(R.id.txtJogador);
        MediaPlayer gotcha = MediaPlayer.create(this, R.raw.gotcha);

        nomeJogador = getIntent().getStringExtra("nome");
        gotcha.start();

        txtJogador.setText(nomeJogador + "!");

        botaoJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Gotcha.this, Registro.class));
                finish();
            }
        });

        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botaoSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Gotcha.this, InfoScreen.class));
            }
        });
    }
}
