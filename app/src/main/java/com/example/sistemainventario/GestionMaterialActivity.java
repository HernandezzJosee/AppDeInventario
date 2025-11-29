package com.example.sistemainventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GestionMaterialActivity extends AppCompatActivity {

    Button btnAgregarMaterial, btnVolverMateriales;
    RecyclerView rvMateriales;
    DatabaseHelper dbHelper;
    List<Material> listaMateriales;
    MaterialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_material);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvMateriales = findViewById(R.id.rvMateriales);
        btnAgregarMaterial = findViewById(R.id.btnAgregarMaterial);
        btnVolverMateriales = findViewById(R.id.btnVolverMateriales);
        dbHelper = new DatabaseHelper(this);

        rvMateriales.setLayoutManager(new LinearLayoutManager(this));

        cargarMateriales();

        btnAgregarMaterial.setOnClickListener(v -> {
            Intent intent = new Intent(GestionMaterialActivity.this, AgregarMaterial.class);
            startActivity(intent);
        });

        btnVolverMateriales.setOnClickListener(v -> {
            Intent intent = new Intent(GestionMaterialActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void cargarMateriales() {
        listaMateriales = new ArrayList<>();
        Cursor cursor = dbHelper.obtenerMateriales();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                int stockActual = cursor.getInt(cursor.getColumnIndexOrThrow("stock_actual"));
                int stockMinimo = cursor.getInt(cursor.getColumnIndexOrThrow("stock_minimo"));

                listaMateriales.add(new Material(id, nombre, descripcion, stockActual, stockMinimo));
            } while (cursor.moveToNext());
        }

        cursor.close();

        adapter = new MaterialAdapter(this, listaMateriales, dbHelper, this::cargarMateriales);
        rvMateriales.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMateriales();
    }
}