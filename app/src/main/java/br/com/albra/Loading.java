package br.com.albra;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual == null){
            Intent i = new Intent(Loading.this, MainActivity.class);
            startActivity(i);
            finish();
        }else {
            Tela();
        }

    }
    private void Tela() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String usuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuários").document(usuId);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                String tipoDoUsuario = documentSnapshot.getString("perfil");
                String autor = "Autor";

                assert tipoDoUsuario != null;
                if (tipoDoUsuario.equals(autor)) {
                    Intent i = new Intent(Loading.this, TelaPrincipal.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(Loading.this, TelaPrincipalLeitor.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}