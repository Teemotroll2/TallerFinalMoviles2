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

public class SellRico extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_rico);

        EditText salesemail = findViewById(R.id.etSalesEmail);
        EditText salesdate = findViewById(R.id.etSalesDate);
        EditText salesvalue = findViewById(R.id.etSalesValue);
        Button botonguardar = findViewById(R.id.btnguardarsales);


        //BOTON CLICK EN SAVE
        botonguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSeller(salesemail.getText().toString(), salesdate.getText().toString(), salesvalue.getText().toString());
            }


            // LO QUE HACE EL BOTON SAVE
            private void SaveSeller(String ssalesemail, String ssalesdate, String ssalesvalue) {
                // buscar la identificacion del cliente nuevo
                db.collection("Sales")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) { // no encontro el documento, se puede guardar xd ( ni idea)
                                        //GUARDAR LOS DATOS DEL CLIENTE AYUDAA
                                        Map<String, Object> customer = new HashMap<>(); // Tabla cursor
                                        customer.put("salesemail", ssalesemail);
                                        customer.put("salesdate", ssalesdate);
                                        customer.put("salesvalue", ssalesvalue);


                                        db.collection("Sales")
                                                .add(customer)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                        Toast.makeText(getApplicationContext(), "Cliente agregado correctamente...", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Error! Cliente no se guardó...", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "ID de el cliente EXISTENTE ♥♥♥", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }
}