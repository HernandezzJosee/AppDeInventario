package com.example.sistemainventario;

import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    EditText editUsuario, editContrasena;
    Button btnIniciarSesion, btnCrearCuenta;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editUsuario = findViewById(R.id.editTextText);
        editContrasena = findViewById(R.id.editTextText2);
        btnIniciarSesion = findViewById(R.id.btn_iniciarsesion);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        dbHelper = new DatabaseHelper(this);

        if (dbHelper.contarUsuarios() == 0) {
            Toast.makeText(this, "Creando usuario administrador...", Toast.LENGTH_SHORT).show();
            dbHelper.insertarUsuario("Administrador", "admin", "1234", "admin");
        }

        btnCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, registro.class);
            startActivity(intent);
        });

        btnIniciarSesion.setOnClickListener(v -> {
            String usuario = editUsuario.getText().toString().trim();
            String contrasena = editContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
    }

            Cursor cursor = dbHelper.verificarUsuario(usuario, contrasena);

            if (cursor != null && cursor.moveToFirst()) {
                String rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"));
                cursor.close();

                SharedPreferences prefs = getSharedPreferences("Sesion", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("usuario", usuario);
                editor.putString("rol", rol);
                editor.apply();

                switch (rol.toLowerCase()) {
                    case "admin":
                        Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show();
                        break;
                    case "supervisor":
                        Toast.makeText(this, "Bienvenido Supervisor", Toast.LENGTH_SHORT).show();
                        break;
                    case "empleado":
                        Toast.makeText(this, "Bienvenido Empleado", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                        return;
                }

                Intent intent = new Intent(MainActivity.this, MenuPrincipalActivity.class);
                intent.putExtra("rol", rol);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }

            if (cursor != null) cursor.close();
        });
    }
}
