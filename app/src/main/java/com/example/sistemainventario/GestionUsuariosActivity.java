package com.example.sistemainventario;

import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GestionUsuariosActivity extends AppCompatActivity {
    Button btnAgregarUsuario, btnVolverUsuarios;
    RecyclerView rvUsuarios;
    DatabaseHelper dbHelper;

    List<Usuario> listaUsuarios;
    UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_usuarios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvUsuarios = findViewById(R.id.rvUsuarios);
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);
        btnVolverUsuarios = findViewById(R.id.btnVolverUsuarios);
        dbHelper = new DatabaseHelper(this);


        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        listaUsuarios = new ArrayList<>();
        cargarUsuarios();

        btnAgregarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(GestionUsuariosActivity.this, AgregarUsuario.class);
            startActivity(intent);
        });


        btnVolverUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(GestionUsuariosActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void cargarUsuarios(){
        listaUsuarios = dbHelper.obtenerUsuarios();

        if (listaUsuarios == null) {
            listaUsuarios = new ArrayList<>();
        }

        adapter = new UsuarioAdapter(listaUsuarios);
        rvUsuarios.setAdapter(adapter);

        adapter.setOnItemClickListener(usuario -> mostrarDialogoEliminar(usuario));
    }

    private void mostrarDialogoEliminar(Usuario usuario) {

        if (usuario.getUsuario().equalsIgnoreCase("admin")) {
            Toast.makeText(this, "No se puede eliminar al administrador principal", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("¿Seguro que deseas eliminar a " + usuario.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    DatabaseHelper db = new DatabaseHelper(GestionUsuariosActivity.this);
                    boolean eliminado = dbHelper.eliminarUsuarioPorId(usuario.getId());
                    if (eliminado) {
                        listaUsuarios.remove(usuario);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(GestionUsuariosActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GestionUsuariosActivity.this, "Error eliminando usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    };

    @Override
    protected void onResume() {
        super.onResume();
        cargarUsuarios();
    }
}