package com.example.sistemainventario;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private List<Material> listaMateriales;
    private Context context;
    private DatabaseHelper dbHelper;
    private Runnable recargar;

    public MaterialAdapter(Context context, List<Material> listaMateriales, DatabaseHelper dbHelper, Runnable recargar) {
        this.context = context;
        this.listaMateriales = listaMateriales;
        this.dbHelper = new DatabaseHelper(context);
        this.recargar = recargar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Material material = listaMateriales.get(position);
        holder.txtNombre.setText(material.getNombre());
        holder.txtDescripcion.setText(material.getDescripcion());
        holder.txtStockActual.setText(String.valueOf(material.getStockActual()));
        holder.txtStockMinimo.setText(String.valueOf(material.getStockMinimo()));

        holder.btnEliminarM.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Material")
                    .setMessage("¿Seguro que quieres eliminar este material y todos sus movimientos?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        boolean eliminado = dbHelper.eliminarMaterialConMovimientos(material.getId());
                        if (eliminado) {
                            Toast.makeText(context, "Material eliminado", Toast.LENGTH_SHORT).show();
                            recargar.run(); // recarga la lista completa desde la Activity
                        } else {
                            Toast.makeText(context, "Error al eliminar material", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaMateriales.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDescripcion, txtStockActual, txtStockMinimo;
        Button btnEliminarM;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreMaterial);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionMaterial);
            txtStockActual = itemView.findViewById(R.id.txtStockActualMaterial);
            txtStockMinimo = itemView.findViewById(R.id.txtStockMinimoMaterial);
            btnEliminarM = itemView.findViewById(R.id.btnEliminarM);
        }
    }
}