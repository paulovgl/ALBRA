package br.com.albra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.xwray.groupie.GroupAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView cadastro;
    private EditText edEmail, edSenha;
    private Button login;
    private ProgressBar progressbar;
    String[] mensagem = {"Preencha todos os campos"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        IniciarComponentes();


        cadastro.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, TelaCadastro.class);
            startActivity(i);
        });

        login.setOnClickListener(view -> {

            String email = edEmail.getText().toString();
            String senha = edSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view,mensagem[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                AutenticarUsuario(view);
            }
        });
    }

    private void AutenticarUsuario(View view){

        String email = edEmail.getText().toString();
        String senha = edSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                progressbar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(this::TelaPrincipal,1000);
            }else{

                String erro;

                try {
                    throw Objects.requireNonNull(task.getException());
                }catch (Exception e){
                    erro = "Erro ao logar usuário";
                }

                Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
                TelaPrincipal();
        }

    }

    private void TelaPrincipal() {

        Intent i = new Intent(this, Loading.class);
        startActivity(i);
        finish();
/*
 FirebaseFirestore db = FirebaseFirestore.getInstance();
        String usuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuários").document(usuId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    String tipoDoUsuario = documentSnapshot.getString("perfil");

                    if (tipoDoUsuario.equals("Autor")){
                        Intent i = new Intent(MainActivity.this,TelaPrincipal.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(MainActivity.this,TelaPrincipalLeitor.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
 */
    }

    private void IniciarComponentes() {
        cadastro = findViewById(R.id.txt_cadastrar);
        login = findViewById(R.id.btn_login);
        progressbar = findViewById(R.id.carregar);
        edEmail = findViewById(R.id.ed_email);
        edSenha = findViewById(R.id.ed_senha);
    }
}
