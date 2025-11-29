package com.example.sistemainventario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class AgregarUsuario extends AppCompatActivity {

    private EditText etNombre, etUsuario, etContrasena;
    private Spinner spRol;
    private Button btnGuardar, btnVolver;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        etNombre = findViewById(R.id.etNombre);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        spRol = findViewById(R.id.spRol);
        btnGuardar = findViewById(R.id.btnGuardarUsuario);
        btnVolver = findViewById(R.id.btnVolverUsuario);

        dbHelper = new DatabaseHelper(this);

        String[] roles = {"Administrador", "Supervisor", "Empleado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String usuario = etUsuario.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();
            String rol = spRol.getSelectedItem().toString();

            if (nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || rol.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean exito = dbHelper.insertarUsuario(nombre, usuario, contrasena, rol);

            if (exito) {
                Toast.makeText(this, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(v -> finish());

    }
}
