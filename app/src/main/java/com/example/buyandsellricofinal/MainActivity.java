package com.example.buyandsellricofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] IdSeller = new String[1];
        EditText email = findViewById(R.id.MainEmail);
        EditText name = findViewById(R.id.MainName);
        EditText phone = findViewById(R.id.MainPhone);
        Button botonsave = findViewById(R.id.btnsave);
        Button botonsearch = findViewById(R.id.btnsearch);
        Button botonedit = findViewById(R.id.btnedit);
        Button botondelete = findViewById(R.id.btndelete);
        Button botonsales = findViewById(R.id.btnsales);
        EditText TotalComiciones = findViewById(R.id.MainTotalComision);


        //IR A LA OTRA PAGINA
        botonsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SellRico.class));
            }
        });

        //BOTON BORRAR
        botondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Confirmación de borrado
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setMessage("¿ Está seguro de eliminar el cliente con Id: " + email.getText().toString() + " ?");
                    alertDialogBuilder.setPositiveButton("Sí",
                            new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // Se eliminará el cliente con el id respectivo

                                    db.collection("Seller").document(IdSeller[0])
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this, "Cliente borrado correctamente...", Toast.LENGTH_SHORT).show();

                                                    //Limpiar las cajas de texto
                                                    email.setText("");
                                                    name.setText("");
                                                    phone.setText("");
                                                    TotalComiciones.setText("");
                                                    email.requestFocus(); //Enviar el foco al ident
                                                }
                                            })

                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });

                    alertDialogBuilder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

            }
        });


        //BOTON EDITARRRRRR
        botonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarCustomer(email.getText().toString(), name.getText().toString(), phone.getText().toString(), TotalComiciones.getText().toString());
            }

            //Boton Editar Seller
            private void editarCustomer(String semail, String sname, String sphone, String sTotalComiciones) {
                Map<String, Object> mcustomer = new HashMap<>();
                mcustomer.put("email", semail);
                mcustomer.put("name", sname);
                mcustomer.put("phone", sphone);
                mcustomer.put("TotalComiciones", sTotalComiciones);

                db.collection("Seller").document(IdSeller[0])
                        .set(mcustomer)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Cliente actualizado correctmente...",Toast.LENGTH_SHORT).show();

                                // Vaciar las cajas de texto
                                email.setText("");
                                name.setText("");
                                phone.setText("");
                                TotalComiciones.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
              }});
            }
        });

        //BOTON CLICK EN SAVE
        botonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSeller(email.getText().toString(), name.getText().toString(), phone.getText().toString(),TotalComiciones.getText().toString());
            }

            // LO QUE HACE EL BOTON SAVE
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
                                        //GUARDAR LOS DATOS DEL CLIENTE AYUDAA
                                        Map<String, Object> customer = new HashMap<>(); // Tabla cursor
                                        customer.put("email", semail);
                                        customer.put("name", sname);
                                        customer.put("phone", sphone);
                                        customer.put("TotalComiciones", sTotalComiciones);

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
        });

        //SE PONE EL CLICK EN EL BOTON BUSCAR
        botonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCustomer(email.getText().toString());
            }

            private void searchCustomer(String semail) {
                db.collection("Seller")
                        .whereEqualTo("email", semail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) { // Si encontró el documento
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            IdSeller[0] = document.getId();
                                            name.setText(document.getString("name"));
                                            phone.setText(document.getString("phone"));
                                            TotalComiciones.setText(document.getString("TotalComiciones"));
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"El Id del cliente no existe...",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }

        });



    }
}
