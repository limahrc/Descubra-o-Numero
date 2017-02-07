package com.ericklima.descubraonumero;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class Facil extends AppCompatActivity {

    private static final int N_TENTATIVAS = 10;      //for debugging purpose only. Default value is 10.
    private static final int MINIMAL_SCORE = 500; //for debugging purpose only. Default value is 500.
    private static final int NUMBER_RANGE = 101;
    private static final int POINTS = 50;
    private static final int REMOVE_POINTS = 50;


    private int numLido,
            numGerado,
            tentativas=N_TENTATIVAS,
            pontuacao=0;

    private boolean isVictory;

    public String nomeJogador;

    Button botaoVerificar,
            Jerry;

    ImageView ivStatus;

    EditText campoNumero;

    TextView txtPontuacao,
             txtTentativas,
             txtCuidado;

    ListView lvNumJogados;

    Random randNum = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        MediaPlayer mInicio = MediaPlayer.create(this, R.raw.opening);
        mInicio.start();

        final AlertDialog.Builder systemMessage = new AlertDialog.Builder(this);
        final ArrayAdapter<String> adpNumJogados = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        final MediaPlayer sErro = MediaPlayer.create(Facil.this, R.raw.erro);
        final MediaPlayer sAcerto = MediaPlayer.create(Facil.this, R.raw.respostacorreta);
        final MediaPlayer sPerda = MediaPlayer.create(Facil.this, R.raw.gameover);
        final MediaPlayer sRestart = MediaPlayer.create(Facil.this, R.raw.success1);
        final MediaPlayer sJerry = MediaPlayer.create(Facil.this, R.raw.chaching);

        final Handler wait = new Handler();

        nomeJogador = getIntent().getStringExtra("nome");
        setTitle(nomeJogador + " - Fácil");


        numGerado = randNum.nextInt(NUMBER_RANGE);

        botaoVerificar = (Button) findViewById(R.id.botaoVerificar);
        Jerry = (Button) findViewById(R.id.Jerry);
        ivStatus = (ImageView) findViewById(R.id.ivStatus);
        campoNumero = (EditText) findViewById(R.id.campoNumero);
        txtPontuacao = (TextView) findViewById(R.id.txtPontuacao);
        txtCuidado = (TextView) findViewById(R.id.txtCuidado);
        txtTentativas = (TextView) findViewById(R.id.txtTentativas);
        lvNumJogados = (ListView) findViewById(R.id.lvNumJogados);

        txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));
        txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
        txtCuidado.setVisibility(View.INVISIBLE);
        lvNumJogados.setAdapter(adpNumJogados);

        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        msg.setCancelable(false);
        msg.setTitle("Introdução");
        msg.setMessage("Nesta modalidade você terá 10 tentativas para acertar o número secreto. " +
                "A cada acerto você ganha 50 pontos e a cada erro, perde 50 pontos.\n\nAo alcançar 500 pontos você poderá ir para a próxima dificuldade." +
                "\n\nO número secreto está entre 0 e 100! Boa sorte!");
        msg.setNeutralButton("Entendi!", null);
        msg.show();

        botaoVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_botaoVerificar(randNum, adpNumJogados, systemMessage, sAcerto, sErro, sPerda, sRestart);
            }
        });

        Jerry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_Jerry(sJerry);
            }
        });

    }

    public void onClick_botaoVerificar(final Random randNum, final ArrayAdapter<String> adpNumJogados,
                                       AlertDialog.Builder systemMessage, MediaPlayer sAcerto, MediaPlayer sErro,
                                       MediaPlayer sPerdeu, final MediaPlayer sRestart) {

        if(campoNumero.getText().length()>0){

            numLido = Integer.parseInt(campoNumero.getText().toString());

            if(numLido==numGerado){
                sAcerto.start();
                ivStatus.setImageResource(R.drawable.right);
                pontuacao+=POINTS;
                Toast.makeText(Facil.this, "Acertou! Parabéns!", Toast.LENGTH_SHORT).show();
                isVictory=true;
                clear(randNum, adpNumJogados, isVictory);
            }
            else if(numLido>numGerado && tentativas>=1){
                sErro.start();
                Toast.makeText(Facil.this, "Jogue um número menor!", Toast.LENGTH_SHORT).show();
                ivStatus.setImageResource(R.drawable.down);
                tentativas--;
                txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
                adpNumJogados.add(String.format(Locale.US, "%d", numLido));
                campoNumero.setText(null);
            }
            else if(numLido<numGerado && tentativas>=1){
                sErro.start();
                Toast.makeText(Facil.this, "Jogue um número maior!", Toast.LENGTH_SHORT).show();
                ivStatus.setImageResource(R.drawable.up);
                tentativas--;
                txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
                adpNumJogados.add(String.format(Locale.US, "%d", numLido));
                campoNumero.setText(null);

            }

            if(tentativas==0){
                sPerdeu.start();
                ivStatus.setImageResource(R.drawable.wrong);
                if(pontuacao>=POINTS) pontuacao-=REMOVE_POINTS;
                else pontuacao=0;
                systemMessage.setCancelable(false);
                systemMessage.setTitle("Você perdeu...");
                systemMessage.setMessage("Que pena, você perdeu...\nO número secreto era " + numGerado + "\n\nDeseja jogar novamente?");
                systemMessage.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sRestart.start();
                        isVictory=false;
                        clear(randNum, adpNumJogados, isVictory);
                    }
                });

                systemMessage.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Facil.this, Registro.class));
                        finish();
                    }
                });

                systemMessage.show();
            }

            if(pontuacao>=MINIMAL_SCORE){
                txtCuidado.setVisibility(View.VISIBLE);
                txtCuidado.setText("Você já pode seguir em frente.\nFale com o Jerry para avançar.");
            }

            if(tentativas==1) txtCuidado.setVisibility(View.VISIBLE);
        } else Toast.makeText(Facil.this, "O campo de número não deve ficar vazio", Toast.LENGTH_SHORT).show();
    }

    public void onClick_Jerry(MediaPlayer sJerry){
        sJerry.start();

        final CharSequence[] options = {"1. Avançar", "2. Voltar ao menu", "3. Sair do jogo"};

        final AlertDialog.Builder JerrySpeak = new AlertDialog.Builder(this);
        JerrySpeak.setCancelable(false);
        JerrySpeak.setTitle("Olá, o que você quer fazer?");
        JerrySpeak.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        if(pontuacao>=MINIMAL_SCORE){
                            Intent gotcha = new Intent(Facil.this, Moderado.class);
                            gotcha.putExtra("nome", nomeJogador);
                            gotcha.putExtra("score", String.valueOf(pontuacao));
                            startActivity(gotcha);
                            finish();
                            break;
                        }
                        else Toast.makeText(Facil.this, "Desculpe, você ainda não tem pontos suficientes para avançar", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        JerrySpeak.setTitle("Voltar ao menu");
                        JerrySpeak.setMessage("Tem certeza que deseja voltar ao menu, " + nomeJogador + "?\n\nSeus dados serão perdidos.");
                        JerrySpeak.setPositiveButton("Sim, tenho.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Facil.this, Registro.class));
                                finish();
                            }
                        });
                        JerrySpeak.show();
                        break;
                    case 2:
                        JerrySpeak.setTitle("Sair do jogo");
                        JerrySpeak.setMessage("Tem certeza que deseja sair do jogo, " + nomeJogador + "?\n\nSeus dados serão perdidos.");
                        JerrySpeak.setPositiveButton("Sim, tenho.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        JerrySpeak.show();
                        break;
                }
            }
        });

        JerrySpeak.setNeutralButton("Cancelar", null);
        JerrySpeak.show();
    }

    public void clear(Random randNum, ArrayAdapter<String> adpNumJogados, boolean isVictory){
        if(isVictory==false){
            pontuacao=0;
            ivStatus.setImageResource(R.drawable.loading);
            tentativas=N_TENTATIVAS;
            campoNumero.setText(null);
            numGerado = randNum.nextInt(NUMBER_RANGE);
            txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));
            txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
            txtCuidado.setVisibility(View.INVISIBLE);
            adpNumJogados.clear();
        } else {
            tentativas = N_TENTATIVAS;
            campoNumero.setText(null);
            numGerado = randNum.nextInt(NUMBER_RANGE);
            txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));
            txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
            txtCuidado.setVisibility(View.INVISIBLE);
            adpNumJogados.clear();
        }
    }

    public void onBackPressed(){
        AlertDialog.Builder BackMessage = new AlertDialog.Builder(this);
        BackMessage.setCancelable(false);
        BackMessage.setTitle("Sair do jogo");
        BackMessage.setMessage("Tem certeza que deseja sair do jogo, " + nomeJogador + "?\n\nSeus dados serão perdidos.");
        BackMessage.setPositiveButton("Sim, tenho.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        BackMessage.setNeutralButton("Cancelar", null);
        BackMessage.show();
    }
}


