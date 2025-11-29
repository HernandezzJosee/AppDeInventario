package com.example.sistemainventario;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class MenuPrincipalActivity extends AppCompatActivity {
    TextView tvBienvenida;
     private Button btnUsuarios, btnMateriales, btnMovimientos, btnReportes, btnCerrarSesion;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnMateriales = findViewById(R.id.btnMateriales);
        btnMovimientos = findViewById(R.id.btnMovimientos);
        btnReportes = findViewById(R.id.btnReportes);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

         SharedPreferences prefs = getSharedPreferences("Sesion", MODE_PRIVATE);
         String rol = prefs.getString("rol", "empleado");
         String usuario = prefs.getString("usuario", "desconocido");

         tvBienvenida.setText("Bienvenido al sistema de inventario (" + rol + ")");

         switch (rol.toLowerCase()) {
             case "admin":
                 // Administrador sin restricciones
                 btnUsuarios.setVisibility(Button.VISIBLE);
                 btnMateriales.setVisibility(Button.VISIBLE);
                 btnMovimientos.setVisibility(Button.VISIBLE);
                 btnReportes.setVisibility(Button.VISIBLE);
                 break;

             case "supervisor":
                 // Supervisor con una restriccion (no puede agregar o eliminar usuarios)
                 btnUsuarios.setVisibility(Button.GONE);
                 btnMateriales.setVisibility(Button.VISIBLE);
                 btnMovimientos.setVisibility(Button.VISIBLE);
                 btnReportes.setVisibility(Button.VISIBLE);
                 break;

             case "empleado":
                 // Empleado con 2 restricciones (solo puede ver movimientos y reportes)
                 btnUsuarios.setVisibility(Button.GONE);
                 btnMateriales.setVisibility(Button.GONE);
                 btnMovimientos.setVisibility(Button.VISIBLE);
                 btnReportes.setVisibility(Button.VISIBLE);
                 break;
         }


        btnUsuarios.setOnClickListener(v -> {
            startActivity(new Intent(this, GestionUsuariosActivity.class));
        });

        btnMateriales.setOnClickListener(v -> {
            startActivity(new Intent(this, GestionMaterialActivity.class));
        });

        btnMovimientos.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistroMovimiento.class));
        });

        btnReportes.setOnClickListener(v -> {
            startActivity(new Intent(this, ReporteActivity.class));
        });

         btnCerrarSesion.setOnClickListener(v -> {
             SharedPreferences.Editor editor = prefs.edit();
             editor.clear();
             editor.apply();

             Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(this, MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
             finish();
         });
    }
}