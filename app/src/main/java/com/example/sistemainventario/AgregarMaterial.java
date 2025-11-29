package com.example.sistemainventario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarMaterial extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etStockActual, etStockMinimo;
    private Button btnGuardar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_material);

        etNombre = findViewById(R.id.etNombreMaterial);
        etDescripcion = findViewById(R.id.etDescripcionMaterial);
        etStockActual = findViewById(R.id.etStockActual);
        etStockMinimo = findViewById(R.id.etStockMinimo);
        btnGuardar = findViewById(R.id.btnGuardarMaterial);

        dbHelper = new DatabaseHelper(this);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String stockActualStr = etStockActual.getText().toString();
            String stockMinimoStr = etStockMinimo.getText().toString();

            if (nombre.isEmpty() || descripcion.isEmpty() || stockActualStr.isEmpty() || stockMinimoStr.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int stockActual = Integer.parseInt(stockActualStr);
            int stockMinimo = Integer.parseInt(stockMinimoStr);

            boolean exito = dbHelper.insertarMaterial(nombre, descripcion, stockActual, stockMinimo);
            if (exito) {
                Toast.makeText(this, "Material agregado correctamente ", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al agregar material", Toast.LENGTH_SHORT).show();
            }
        });
    }
}