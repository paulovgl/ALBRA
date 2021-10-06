package br.com.albra;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class TelaPrincipal extends AppCompatActivity {

    private ImageButton btnAdd;
    private Button btn_deslogar, btn_ObrasCadastradas, btn_leitorMode;
    private TextView tipoUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        Objects.requireNonNull(getSupportActionBar()).hide();
        InicarComponentes();

        btn_ObrasCadastradas.setOnClickListener(view -> {

            Intent i = new Intent(this, ObrasAutor.class);
            startActivity(i);
            finish();
        });

        btn_leitorMode.setOnClickListener(view -> {

            Intent i = new Intent(this, TelaPrincipalLeitor.class);
            startActivity(i);
            finish();
        });

        btn_deslogar.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, Loading.class);
            startActivity(i);
            finish();
        });

        btnAdd.setOnClickListener(view -> {
            Intent i = new Intent(this,CadastrarObras.class);
            startActivity(i);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String usuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuários").document(usuId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    String tipoDoUsuario = documentSnapshot.getString("perfil");

                    tipoUsu.setText("Você está logado como " + tipoDoUsuario);

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual == null){
            Intent i = new Intent(this, Loading.class);
            startActivity(i);
            finish();
        }
    }

    private void InicarComponentes(){
        btnAdd = findViewById(R.id.btn_add);
        btn_deslogar = findViewById(R.id.btn_deslogar);
        btn_leitorMode = findViewById(R.id.btn_leitorMode);
        btn_ObrasCadastradas = findViewById(R.id.btn_ObrasCadastradas);
        tipoUsu = findViewById(R.id.tipoUsu);
    }
}