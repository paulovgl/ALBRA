package br.com.albra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import java.util.List;
import java.util.Objects;

public class ObrasAutor extends AppCompatActivity {

    private RecyclerView rv;
    private GroupAdapter adapter;
    private ImageButton btn_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obras_autor);

        Objects.requireNonNull(getSupportActionBar()).hide();
        IniciarComponentes();

        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fetchObrasAutor();

        btn_voltar.setOnClickListener(view -> {

            Intent i = new Intent(this, TelaPrincipal.class);
            startActivity(i);
            finish();
        });
    }

    private void fetchObrasAutor() {

        String autorLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference cf = FirebaseFirestore.getInstance().collection("/Obras Por Autor/" + autorLogado + "/Obras");

        cf.addSnapshotListener((queryDocumentSnapshots, error) -> {

            if (queryDocumentSnapshots != null){

                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();

                if (!docs.isEmpty()){

                    adapter.clear();
                }

                for (DocumentSnapshot doc: docs){
                    Obras obras = doc.toObject(Obras.class);
                    adapter.add(new ObrasItemAutor(obras));
                }

            }
        });
    }

    private class ObrasItemAutor extends Item<GroupieViewHolder> {

        private final Obras obras;

        private ObrasItemAutor(Obras obras) {
            this.obras = obras;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

            TextView txtTitulo = viewHolder.itemView.findViewById(R.id.card_title);
            TextView txtDescription = viewHolder.itemView.findViewById(R.id.card_description);
            ImageView imgCard = viewHolder.itemView.findViewById(R.id.card_image);


            txtTitulo.setText(obras.getNO());
            txtDescription.setText(obras.getDES());

            Glide.with(ObrasAutor.this)
                    .load(obras.getLI())
                    .into(imgCard);
        }

        @Override
        public int getLayout() {
            return R.layout.obra_card;
        }
    }

    private void IniciarComponentes() {
        rv = findViewById(R.id.recycleView);
        btn_voltar = findViewById(R.id.btn_voltar);
    }
}