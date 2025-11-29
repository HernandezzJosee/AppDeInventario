package com.example.sistemainventario;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;


public class RegistroMovimiento extends AppCompatActivity {
    private Spinner spTipoMovimiento, spMaterial;
    private EditText etCantidad, etComentario;
    private Button btnRegistrarMovimiento, btnVolverMovimiento;
    private DatabaseHelper dbHelper; //Conexion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_movimiento);

        dbHelper = new DatabaseHelper(this);

        spTipoMovimiento = findViewById(R.id.spTipoMovimiento);
        spMaterial = findViewById(R.id.spMaterial);
        etCantidad = findViewById(R.id.etCantidad);
        etComentario = findViewById(R.id.etComentario);
        btnRegistrarMovimiento = findViewById(R.id.btnRegistrarMovimiento);
        btnVolverMovimiento = findViewById(R.id.btnVolverMovimiento);



        String[] tiposMovimiento = {"Entrada", "Salida", "Ajuste"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tiposMovimiento);
        spTipoMovimiento.setAdapter(adapterTipo);


        List<String> materiales = dbHelper.obtenerNombreMateriales();
        ArrayAdapter<String> adapterMaterial = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, materiales);
        spMaterial.setAdapter(adapterMaterial);


        btnRegistrarMovimiento.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                registrarMovimiento();
            }
        });


        btnVolverMovimiento.setOnClickListener(v -> finish());
    }

    private void registrarMovimiento() {
        String tipo = spTipoMovimiento.getSelectedItem().toString();
        String material = spMaterial.getSelectedItem().toString();
        String cantidadStr = etCantidad.getText().toString();
        String comentario = etComentario.getText().toString();


        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa la cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);


        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        boolean insertado = dbHelper.insertarMovimiento(tipo, material, cantidad, comentario, fecha);

        if (insertado) {
            Toast.makeText(this, "Movimiento registrado correctamente", Toast.LENGTH_SHORT).show();
            etCantidad.setText("");
            etComentario.setText("");
        } else {
            Toast.makeText(this, "Error al registrar el movimiento", Toast.LENGTH_SHORT).show();
        }
    }
}