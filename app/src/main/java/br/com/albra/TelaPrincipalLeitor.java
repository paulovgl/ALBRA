package br.com.albra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.util.List;
import java.util.Objects;

public class TelaPrincipalLeitor extends AppCompatActivity {

    private RecyclerView rv;
    private Button deslogarBtn;
    private ImageButton btn_voltar;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal_leitor);

        Objects.requireNonNull(getSupportActionBar()).hide();
        IniciarComponentes();
        AutorMode();

        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fetchObras();

        deslogarBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, Loading.class);
            startActivity(i);
            finish();
        });

        btn_voltar.setOnClickListener(view -> {

            Intent i = new Intent(this, TelaPrincipal.class);
            startActivity(i);
            finish();
        });

    }

    private void AutorMode() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String usuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuários").document(usuId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    String tipoDoUsuario = documentSnapshot.getString("perfil");
                    String autor = "Autor";

                    assert tipoDoUsuario != null;
                    if (tipoDoUsuario.equals(autor)) {
                        deslogarBtn.setVisibility(View.GONE);
                        btn_voltar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void fetchObras() {


        CollectionReference cf = FirebaseFirestore.getInstance().collection("Obras Gerais");


        cf.addSnapshotListener((queryDocumentSnapshots, error) -> {

               if (queryDocumentSnapshots != null){
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                if (!docs.isEmpty()){

                    adapter.clear();
                }

                for (DocumentSnapshot doc: docs){
                    Obras obras = doc.toObject(Obras.class);
                    adapter.add(new ObrasItem(obras));
                }
            }
        });

    }

    private class ObrasItem extends Item<GroupieViewHolder> {

        private final Obras obras;

        private ObrasItem(Obras obras) {
            this.obras = obras;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

            TextView txtTitulo = viewHolder.itemView.findViewById(R.id.card_title);
            TextView txtDescription = viewHolder.itemView.findViewById(R.id.card_description);
            ImageView imgCard = viewHolder.itemView.findViewById(R.id.card_image);


            txtTitulo.setText(obras.getNO());
            txtDescription.setText(obras.getDES());

            Glide.with(TelaPrincipalLeitor.this)
                    .load(obras.getLI())
                    .override(200,250)
                    .centerCrop()
                    .into(imgCard);
        }

        @Override
        public int getLayout() {
            return R.layout.obra_card;
        }

    }

    private void IniciarComponentes() {
        rv = findViewById(R.id.recycleView);
        deslogarBtn = findViewById(R.id.deslogar_btn);
        btn_voltar = findViewById(R.id.btn_voltar);
    }
}