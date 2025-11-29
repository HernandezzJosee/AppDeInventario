package com.example.sistemainventario;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

public class ReporteActivity extends AppCompatActivity {

    private Spinner spFiltroTipo;
    private EditText etFecha;
    private Button btnGenerarReporte, btnVolver;
    private RecyclerView rvReportes;
    private DatabaseHelper dbHelper;
    private ReporteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        dbHelper = new DatabaseHelper(this);

        // referencias
        spFiltroTipo = findViewById(R.id.spFiltroTipo);
        etFecha = findViewById(R.id.etFecha);
        btnGenerarReporte = findViewById(R.id.btnGenerarReporte);
        btnVolver = findViewById(R.id.btnVolverReporte);
        rvReportes = findViewById(R.id.rvReportes);

        // RecyclerView
        rvReportes.setLayoutManager(new LinearLayoutManager(this));

        // Spinner tipos
        String[] tipos = {"Todos", "Entrada", "Salida"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFiltroTipo.setAdapter(adapterTipo);

        configurarSelectorDeFecha();


        btnGenerarReporte.setOnClickListener(v -> generarReporte());
        btnVolver.setOnClickListener(v -> finish());

        //cargar todo al abrir
        cargarTodosLosMovimientos();
    }

    private void configurarSelectorDeFecha() {
        Calendar calendario = Calendar.getInstance();

        etFecha.setFocusable(false);
        etFecha.setClickable(true);

        etFecha.setOnClickListener(v -> {
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    ReporteActivity.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        String fechaSeleccionada = year + "-" +
                                String.format("%02d", (month + 1)) + "-" +
                                String.format("%02d", dayOfMonth);
                        etFecha.setText(fechaSeleccionada);
                    },
                    año, mes, dia
            );

            datePicker.show();
        });
    }



    private void cargarTodosLosMovimientos() {
        Cursor cursor = dbHelper.obtenerMovimientos();
        List<Movimiento> lista = convertirCursorALista(cursor);

        adapter = new ReporteAdapter(lista);
        rvReportes.setAdapter(adapter);
    }


    private void generarReporte() {
        String tipoSeleccionado = spFiltroTipo.getSelectedItem().toString();
        String fechaIngresada = etFecha.getText().toString().trim();


        String tipoParaFiltro = tipoSeleccionado.equals("Todos") ? "" : tipoSeleccionado;

        Cursor cursor = dbHelper.filtrarMovimientos(tipoParaFiltro, fechaIngresada);
        List<Movimiento> listaFiltrada = convertirCursorALista(cursor);

        if (listaFiltrada.isEmpty()) {
            Toast.makeText(this, "No se encontraron registros con esos filtros", Toast.LENGTH_SHORT).show();
        }

        adapter = new ReporteAdapter(listaFiltrada);
        rvReportes.setAdapter(adapter);
    }

    private List<Movimiento> convertirCursorALista(Cursor cursor) {
        List<Movimiento> lista = new ArrayList<>();
        if (cursor == null) return lista;

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIPO));
                String material = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MATERIAL));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CANTIDAD));
                String comentario = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMENTARIO));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FECHA));

                Movimiento m = new Movimiento(id, tipo, material, cantidad, comentario, fecha);
                lista.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}