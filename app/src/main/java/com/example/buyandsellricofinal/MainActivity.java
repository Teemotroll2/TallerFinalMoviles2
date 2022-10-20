package com.example.buyandsellricofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String BaseDeDatosRico;

        EditText email = findViewById(R.id.MainEmail);
        EditText name = findViewById(R.id.MainName);
        EditText phone = findViewById(R.id.MainPhone);
        Button botonsave = findViewById(R.id.btnsave);
        Button botonsearch = findViewById(R.id.btnsearch);
        Button botonedit = findViewById(R.id.btnedit);
        Button botondelete = findViewById(R.id.btndelete);
        EditText TotalComiciones = findViewById(R.id.MainTotalComision);


        botonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSeller(email.getText().toString(), name.getText().toString(), phone.getText().toString(),TotalComiciones.getText().toString());
            }

        });

    }

        private void SaveSeller(String semail, String sname, String sphone, String sTotalComiciones) {
            // buscar la identificacion del cliente nuevo
            db.collection("Seller")
                    .whereEqualTo("email", semail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) { // no encontro el documento, se puede guardar xd ( ni idea)
                                    //GUARDAR LOS DATOS DEL CLIENTE AYUDAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                                    Map<String, Object> customer = new HashMap<>(); // Tabla cursor
                                    customer.put("email", semail);
                                    customer.put("name", sname);
                                    customer.put("phone", sphone);
                                    customer.put("TotalComitions", sTotalComiciones);

                                    db.collection("Seller")
                                            .add(customer)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                    Toast.makeText(getApplicationContext(), "Cliente agregado correctamente...",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Error! Cliente no se guardó...",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"ID de el cliente EXISTENTE ♥♥♥",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            /*  */
        }
    }
