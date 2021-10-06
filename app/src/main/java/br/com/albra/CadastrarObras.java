package br.com.albra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CadastrarObras extends AppCompatActivity {

    private EditText nomeObra, linkObra, description, linkImage;
    private Button btn_cadastrarObra, image;
    private ImageButton btn_voltar;
    private ImageView imageI;
    String[] mensagem = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Campo de Imagem Vazio", "Tente Outro Link de Imagem"};
    String usuarioID;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_obras);

        Objects.requireNonNull(getSupportActionBar()).hide();
        InicarComponentes();

        btn_voltar.setOnClickListener(view -> {

            Intent i = new Intent(CadastrarObras.this, TelaPrincipal.class);
            startActivity(i);
            finish();

        });

        image.setOnClickListener(view -> {

            String img = linkImage.getText().toString();

            if (img.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, mensagem[2],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                selecionarFoto(img);
                image.setAlpha(0);
            }
        });

        btn_cadastrarObra.setOnClickListener(view -> {

            String NO = nomeObra.getText().toString();
            String LO = linkObra.getText().toString();
            String LI = image.getText().toString();
            String D = description.getText().toString();


            if(NO.isEmpty() || LO.isEmpty() || LI.isEmpty() || D.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, mensagem[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                SalvarObra();

                Snackbar snackbar = Snackbar.make(view, mensagem[1],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

                nomeObra.setText("");
                linkObra.setText("");
                linkImage.setText("");
                description.setText("");
                imageI.setImageDrawable(null);
                image.setAlpha(1);

            }
        });
    }

    private void selecionarFoto(String img) {

            Glide.with(this)
                    .load(img)
                    .error(R.drawable.ic_error)
                    .override(200,250)
                    .centerCrop()
                    .into(imageI);
    }

    private void SalvarObra() {

        String NO = nomeObra.getText().toString();
        String LO = linkObra.getText().toString();
        String LI = linkImage.getText().toString();
        String D = description.getText().toString();

        db = FirebaseFirestore.getInstance();

        Map<String,Object> obras = new HashMap<>();
        obras.put("NO", NO);
        obras.put("LO", LO);
        obras.put("LI", LI);
        obras.put("DES", D);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference collectionReference = db.collection("Obras Por Autor").document(usuarioID).collection("Obras");
        collectionReference.add(obras).addOnSuccessListener(unused -> Log.d("db","Sucesso ao salvar os dados"))
                .addOnFailureListener(e -> Log.d("db_erro","Erro ao salvar os dados" + e.toString()));

        CollectionReference cf = db.collection("Obras Gerais");
        cf.add(obras).addOnSuccessListener(unused -> Log.d("db","Sucesso ao salvar os dados"))
                .addOnFailureListener(e -> Log.d("db_erro","Erro ao salvar os dados" + e.toString()));

    }

    private void InicarComponentes(){
        nomeObra = findViewById(R.id.nomeObra);
        linkObra = findViewById(R.id.linkObra);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        btn_cadastrarObra = findViewById(R.id.btn_cadastrarObra);
        imageI = findViewById(R.id.imageI);
        linkImage = findViewById(R.id.linkImage);
        btn_voltar = findViewById(R.id.btn_voltar);
    }
}