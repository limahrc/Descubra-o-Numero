package com.ericklima.descubraonumero;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoScreen extends AppCompatActivity {

    int cliques=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        setTitle("Sobre o app");

        Button botaoFace = (Button) findViewById(R.id.botaoFace);
        Button botaoInsta = (Button) findViewById(R.id.botaoInsta);
        Button botaoGit = (Button) findViewById(R.id.botaoGit);
        Button botaoLogo = (Button) findViewById(R.id.botaoLogo);


        botaoFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openFb = new Intent(android.content.Intent.ACTION_VIEW);
                openFb.setData(Uri.parse("https://www.facebook.com/limaerx"));
                startActivity(openFb);
            }
        });

        botaoInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openInsta = new Intent(android.content.Intent.ACTION_VIEW);
                openInsta.setData(Uri.parse("https://www.instagram.com/limaerx"));
                startActivity(openInsta);
            }
        });

        botaoGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGit = new Intent(android.content.Intent.ACTION_VIEW);
                openGit.setData(Uri.parse("https://github.com/limahrc"));
                startActivity(openGit);
            }
        });

        botaoLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliques++;
                if(cliques==5){
                    AlertDialog.Builder msg = new AlertDialog.Builder(InfoScreen.this);
                    msg.setTitle("Haha!");
                    msg.setMessage("Meus sinceros agradecimentos a\n\nAmanda Laranjeiras\nGezieli Costa" +
                            "\nVictor Cezari e \nKamilly Lima\n\nPor serem os primeiros testadores deste aplicativo! :)");
                    msg.setNeutralButton("Oh, yeah!", null);
                    msg.show();
                }

            }
        });
    }
}
