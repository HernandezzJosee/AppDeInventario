package com.example.sistemainventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> listausuarios;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Usuario usuario);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public UsuarioAdapter(List<Usuario> listaUsuarios) {
        this.listausuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listausuarios.get(position);
        holder.txtNombre.setText(usuario.getNombre());   // Nombre real
        holder.txtUsuario.setText(usuario.getUsuario()); // Usuario (login)
        holder.txtRol.setText(usuario.getRol());         // Rol

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(usuario);
        });
    }

    @Override
    public int getItemCount() {
        return listausuarios.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtUsuario, txtRol;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombreUsuario);
            txtUsuario = itemView.findViewById(R.id.txtUsuarioUsuario);
            txtRol = itemView.findViewById(R.id.txtRolUsuario);
        }
    }
}