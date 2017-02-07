package com.ericklima.descubraonumero;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by Erick Lima on 28/01/2017.
 */

public class Moderado extends AppCompatActivity {

    private static final int N_TENTATIVAS = 7;      //for debugging purpose only. Default value is 7.
    private static final int MINIMAL_SCORE = 5000; //for debugging purpose only. Default value is 5000.
    private static final int NUMBER_RANGE = 201;
    private static final int POINTS = 500;
    private static final int REMOVE_POINTS = 100;
    private static final int DICA_ABSTRATA = 50;
    private static final int DICA_ESTATICA = 100;
    private static final int DICA_CONCRETA = 200;

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

    private int numLido,
            numGerado,
            tentativas = N_TENTATIVAS,
            pontuacao = 0;

    private boolean isVictory, isLoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        MediaPlayer mInicio = MediaPlayer.create(this, R.raw.moderado);
        mInicio.start();

        final AlertDialog.Builder systemMessage = new AlertDialog.Builder(this);
        final ArrayAdapter<String> adpNumJogados = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        final MediaPlayer sErro = MediaPlayer.create(Moderado.this, R.raw.erro);
        final MediaPlayer sAcerto = MediaPlayer.create(Moderado.this, R.raw.respostacorreta);
        final MediaPlayer sPerda = MediaPlayer.create(Moderado.this, R.raw.gameover);
        final MediaPlayer sRestart = MediaPlayer.create(Moderado.this, R.raw.success1);
        final MediaPlayer sJerry = MediaPlayer.create(Moderado.this, R.raw.chaching);

        final Handler wait = new Handler();

        nomeJogador = getIntent().getStringExtra("nome");
        pontuacao = Integer.parseInt((getIntent().getStringExtra("score")));
        setTitle(nomeJogador + " - Moderado");


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
        msg.setMessage("Nesta modalidade você terá " + N_TENTATIVAS + " tentativas para acertar o número secreto. " +
                "A cada acerto você ganha " + POINTS + " pontos e a cada erro, perde " + REMOVE_POINTS + " pontos.\n\nAo alcançar " + MINIMAL_SCORE + " pontos você poderá ir para a próxima dificuldade." +
                "\n\nO número secreto está entre 0 e "+ (NUMBER_RANGE-1) +"! Boa sorte!");
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
                onClick_Jerry(sJerry, numGerado);
            }
        });

    }

    public void onClick_botaoVerificar(final Random randNum, final ArrayAdapter<String> adpNumJogados,
                                       AlertDialog.Builder systemMessage, MediaPlayer sAcerto, MediaPlayer sErro,
                                       MediaPlayer sPerdeu, final MediaPlayer sRestart) {

        if (campoNumero.getText().length() > 0) {

            numLido = Integer.parseInt(campoNumero.getText().toString());

            if (numLido == numGerado) {
                sAcerto.start();
                ivStatus.setImageResource(R.drawable.right);
                pontuacao += POINTS;
                Toast.makeText(Moderado.this, "Acertou! Parabéns!", Toast.LENGTH_SHORT).show();
                isVictory = true;
                isLoss = false;
                clear(randNum, adpNumJogados, isVictory, isLoss);
            } else if (numLido > numGerado && tentativas >= 1) {
                sErro.start();
                Toast.makeText(Moderado.this, "Jogue um número menor!", Toast.LENGTH_SHORT).show();
                ivStatus.setImageResource(R.drawable.down);
                tentativas--;
                txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
                adpNumJogados.add(String.format(Locale.US, "%d", numLido));
                campoNumero.setText(null);
            } else if (numLido < numGerado && tentativas >= 1) {
                sErro.start();
                Toast.makeText(Moderado.this, "Jogue um número maior!", Toast.LENGTH_SHORT).show();
                ivStatus.setImageResource(R.drawable.up);
                tentativas--;
                txtTentativas.setText((String.format(Locale.US, "%d", tentativas)));
                adpNumJogados.add(String.format(Locale.US, "%d", numLido));
                campoNumero.setText(null);

            }

            if (tentativas == 0) {
                sPerdeu.start();
                ivStatus.setImageResource(R.drawable.wrong);
                systemMessage.setCancelable(false);
                systemMessage.setTitle("Ah, não...");
                systemMessage.setMessage("Que pena, você perdeu...\nO número secreto era " + numGerado + ".\n\nDeseja jogar novamente?");
                systemMessage.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sRestart.start();
                        isVictory = false;
                        isLoss = true;
                        clear(randNum, adpNumJogados, isVictory, isLoss);
                    }
                });

                systemMessage.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Moderado.this, Registro.class));
                        finish();
                    }
                });

                systemMessage.show();
            }

            if (pontuacao >= MINIMAL_SCORE) {
                txtCuidado.setVisibility(View.VISIBLE);
                txtCuidado.setText("Você já pode seguir em frente.\nFale com o Jerry para avançar.");
            } else txtCuidado.setVisibility(View.INVISIBLE);

            if (tentativas == 1) txtCuidado.setVisibility(View.VISIBLE);
        } else
            Toast.makeText(Moderado.this, "O campo de número não deve ficar vazio", Toast.LENGTH_SHORT).show();
    }

    public void onClick_Jerry(MediaPlayer sJerry, final int numGerado) {
        sJerry.start();

        final CharSequence[] options = {"1. Avançar", "2. Voltar ao menu", "3. Comprar uma dica", "4. Sair do jogo"};
        final CharSequence[] optDica = {"Dica Abstrata - "+ DICA_ABSTRATA +" pontos", "Dica Concreta - "+ DICA_ESTATICA +" pontos", "Dica MASTER - "+ DICA_CONCRETA +" pontos"};

        final AlertDialog.Builder JerrySpeak = new AlertDialog.Builder(this);
        JerrySpeak.setCancelable(false);
        JerrySpeak.setTitle("Olá, o que você quer fazer?");
        JerrySpeak.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (pontuacao >= MINIMAL_SCORE) {
                            Intent gotcha = new Intent(Moderado.this, Dificil.class);
                            gotcha.putExtra("nome", nomeJogador);
                            startActivity(gotcha);
                            finish();
                            break;
                        } else
                            Toast.makeText(Moderado.this, "Desculpe, você ainda não tem pontos suficientes para avançar", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        JerrySpeak.setTitle("Voltar ao menu");
                        JerrySpeak.setMessage("Tem certeza que deseja voltar ao menu, " + nomeJogador + "?\n\nSeus dados serão perdidos.");
                        JerrySpeak.setPositiveButton("Sim, tenho.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Moderado.this, Registro.class));
                                finish();
                            }
                        });
                        JerrySpeak.show();
                        break;
                    case 3:
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
                    case 2:
                        JerrySpeak.setCancelable(false);
                        JerrySpeak.setTitle("Comprar uma dica");
                        JerrySpeak.setItems(optDica, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        if (pontuacao >= DICA_ABSTRATA) {
                                            if (numGerado % 2 == 0) {
                                                JerrySpeak.setCancelable(false);
                                                JerrySpeak.setTitle("Dica Abstrata");
                                                JerrySpeak.setMessage("O número secreto é ÍMPAR!");
                                                JerrySpeak.setPositiveButton("Entendi!", null);
                                                pontuacao -= DICA_ABSTRATA;
                                                txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));                                                JerrySpeak.show();
                                            } else {
                                                JerrySpeak.setCancelable(false);
                                                JerrySpeak.setTitle("Dica Abstrata");
                                                JerrySpeak.setMessage("O número secreto é PAR!");
                                                JerrySpeak.setPositiveButton("Entendi!", null);
                                                pontuacao -= DICA_ABSTRATA;
                                                txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));                                                JerrySpeak.show();
                                            }
                                        } else Toast.makeText(Moderado.this, "Você não tem pontos suficientes para comprar esta dica", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        String valor = String.valueOf(numGerado);

                                        if (pontuacao >= DICA_ESTATICA) {
                                            JerrySpeak.setCancelable(false);
                                            JerrySpeak.setTitle("Dica Concreta");
                                            if(valor.length()==2) JerrySpeak.setMessage("O número secreto termina com " + valor.substring(1,2) + "!");
                                            else if(valor.length()==3)  JerrySpeak.setMessage("O número secreto termina com " + valor.substring(2,3) + "!");
                                            JerrySpeak.setPositiveButton("Entendi!", null);
                                            pontuacao -= DICA_ESTATICA;
                                            txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));                                            JerrySpeak.show();
                                        } else Toast.makeText(Moderado.this, "Você não tem pontos suficientes para comprar esta dica", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        if(pontuacao>=DICA_CONCRETA) {
                                            JerrySpeak.setCancelable(false);
                                            JerrySpeak.setTitle("Dica MASTER");
                                            JerrySpeak.setMessage("O número secreto está entre " + (numGerado - 10) + " e " + (numGerado + 10) + "!");
                                            JerrySpeak.setPositiveButton("Entendi!", null);
                                            pontuacao -= DICA_CONCRETA;
                                            txtPontuacao.setText((String.format(Locale.US, "%d", pontuacao)));                                            JerrySpeak.show();
                                        } else Toast.makeText(Moderado.this, "Você não tem pontos suficientes para comprar esta dica", Toast.LENGTH_SHORT).show();
                                        break;
                                }
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

    public void clear(Random randNum, ArrayAdapter<String> adpNumJogados, boolean isVictory, boolean isLoss) {
        if (isVictory == false) {
            if(isLoss){
                if (pontuacao >= REMOVE_POINTS) pontuacao -= REMOVE_POINTS;
                else pontuacao = 0;
            } else pontuacao = 0;
            ivStatus.setImageResource(R.drawable.loading);
            tentativas = N_TENTATIVAS;
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
