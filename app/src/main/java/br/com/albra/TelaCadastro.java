package br.com.albra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TelaCadastro extends AppCompatActivity {

    private Button autorBtn,leitorBtn, cadastrarBtn;
    private EditText edNome, edEmail, edSenha;
    String[] mensagem = {"Preencha todos os campos", "Cadastro realizado com sucesso"};
    String usuarioID, escolhaPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        Objects.requireNonNull(getSupportActionBar()).hide();
        InicarComponentes();

        cadastrarBtn.setOnClickListener(view -> {

            String nome = edNome.getText().toString();
            String email = edEmail.getText().toString();
            String senha = edSenha.getText().toString();


            if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                Snackbar snackbar = Snackbar.make(view, mensagem[0],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                CadastrarUsuário(view);
            }
        });

        autorBtn.setOnClickListener(view -> {
            escolhaPerfil = autorBtn.getText().toString();
        });
        leitorBtn.setOnClickListener(view -> {
            escolhaPerfil = leitorBtn.getText().toString();
        });

    }

    private void CadastrarUsuário(View view) {

        String email = edEmail.getText().toString();
        String senha = edSenha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                SalvarUsuário();

                Snackbar snackbar = Snackbar.make(view, mensagem[1],Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

                Redirecionamento();

            }else{
                String erro;
                try {
                    throw task.getException();
                }catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Digite uma senha com no mínimo 6 caracteres";
                }catch (FirebaseAuthUserCollisionException e) {
                    erro = "Esta conta já foi cadastrada";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    erro = "Email Inválido";
                }catch (Exception e){
                    erro = "Erro ao cadastrar o usuário";
                }

                Snackbar snackbar = Snackbar.make(view, erro,Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    private void Redirecionamento() {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String usuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("Usuários").document(usuId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null){
                        String tipoDoUsuario = documentSnapshot.getString("perfil");

                        if (tipoDoUsuario.equals("Autor")){
                            Intent i = new Intent(TelaCadastro.this,TelaPrincipal.class);
                            startActivity(i);
                            finish();
                        }else{
                            Intent i = new Intent(TelaCadastro.this,TelaPrincipalLeitor.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            });
    }

    private void SalvarUsuário(){

        String nome = edNome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("perfil", escolhaPerfil);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentoReference = db.collection("Usuários").document(usuarioID);
        documentoReference.set(usuarios).addOnSuccessListener(unused -> {
                Log.d("db","Sucesso ao salvar os dados");
        }).addOnFailureListener(e -> {
                Log.d("db_erro","Erro ao salvar os dados" + e.toString());
        });
    }

    private void InicarComponentes(){
        autorBtn = findViewById(R.id.btn_autor);
        leitorBtn = findViewById(R.id.btn_leitor);
        cadastrarBtn = findViewById(R.id.btn_cadastrar);
        edNome = findViewById(R.id.ed_nome);
        edEmail = findViewById(R.id.ed_email);
        edSenha = findViewById(R.id.ed_senha);
    }
}