package com.example.sistemainventario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventario.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MOVIMIENTOS = "movimientos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIPO = "tipo";
    public static final String COLUMN_MATERIAL = "material";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_COMENTARIO = "comentario";
    public static final String COLUMN_FECHA = "fecha";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_MOVIMIENTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIPO + " TEXT, "
                + COLUMN_MATERIAL + " TEXT, "
                + COLUMN_CANTIDAD + " INTEGER, "
                + COLUMN_COMENTARIO + " TEXT, "
                + COLUMN_FECHA + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_USUARIOS = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "usuario TEXT, " +
                "contrasena TEXT, " +
                "rol TEXT)";
        db.execSQL(CREATE_TABLE_USUARIOS);

        String CREATE_TABLE_MATERIALES = "CREATE TABLE materiales (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "descripcion TEXT, " +
                "stock_actual INTEGER, " +
                "stock_minimo INTEGER)";
        db.execSQL(CREATE_TABLE_MATERIALES);

        ContentValues adminValues = new ContentValues();
        adminValues.put("nombre", "Administrador");
        adminValues.put("usuario", "admin");
        adminValues.put("contrasena", "1234");
        adminValues.put("rol", "admin");
        db.insert("usuarios", null, adminValues);

        Log.d("DB_SETUP", "Base de datos creada y usuario admin insertado");
        Log.d("DB_SETUP", "Tablas creadas correctamente");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIMIENTOS);
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS materiales");
        onCreate(db);
    }

    public boolean insertarUsuario(String nombre, String usuario, String contrasena, String rol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("usuario", usuario);
        valores.put("contrasena", contrasena);
        valores.put("rol", rol);

        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", new String[]{usuario});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false;
        }
        long resultado = db.insert("usuarios", null, valores);
        cursor.close();
        db.close();
        return resultado != -1;
    }

    public boolean eliminarUsuarioPorId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("usuarios", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return filas > 0;
    }

    public Cursor verificarUsuario(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios WHERE usuario=? AND contrasena=?",
                new String[]{usuario, contrasena});
    }


    public int contarUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM usuarios", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nombre, usuario,contrasena, rol FROM usuarios", null);

        if (cursor.moveToFirst()) {
            do {
                lista.add(new Usuario(
                        cursor.getInt(0),        // id
                        cursor.getString(1),     // nombre
                        cursor.getString(2),     // usuario
                        cursor.getString(3),     // contraseña
                        cursor.getString(4)     // rol
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }


    public boolean insertarMaterial(String nombre, String descripcion, int stockActual, int stockMinimo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("descripcion", descripcion);
        valores.put("stock_actual", stockActual);
        valores.put("stock_minimo", stockMinimo);

        long resultado = db.insert("materiales", null, valores);
        db.close();
        return resultado != -1;

    }
    public List<String> obtenerNombreMateriales() {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM Materiales", null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
    public Cursor obtenerMateriales() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM materiales ORDER BY id DESC", null);
    }


    public boolean insertarMovimiento(String tipo, String material, int cantidad, String comentario, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_TIPO, tipo);
        valores.put(COLUMN_MATERIAL, material);
        valores.put(COLUMN_CANTIDAD, cantidad);
        valores.put(COLUMN_COMENTARIO, comentario);
        valores.put(COLUMN_FECHA, fecha);

        long resultado = db.insert(TABLE_MOVIMIENTOS, null, valores);
        db.close();
        return resultado != -1;
    }


    public Cursor obtenerMovimientos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MOVIMIENTOS + " ORDER BY id DESC", null);
    }
    public boolean eliminarMaterialConMovimientos(int materialId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre FROM materiales WHERE id = ?",
                new String[]{String.valueOf(materialId)});
        if (!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return false;
        }
        String nombreMaterial = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
        cursor.close();

        db.delete(TABLE_MOVIMIENTOS, COLUMN_MATERIAL + "=?", new String[]{nombreMaterial});

        int filasAfectadas = db.delete("materiales", "id=?", new String[]{String.valueOf(materialId)});

        db.close();
        return filasAfectadas > 0;
    }

    public Cursor filtrarMovimientos(String tipo, String fecha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIMIENTOS + " WHERE 1=1";

        if (!tipo.isEmpty()) query += " AND " + COLUMN_TIPO + " = '" + tipo + "'";
        if (!fecha.isEmpty()) query += " AND " + COLUMN_FECHA + " = '" + fecha + "'";

        return db.rawQuery(query, null);
    }



    public Cursor obtenerTodosLosMovimientos() {
        return obtenerMovimientos();
    }

    public Cursor obtenerMovimientosFiltrados(String tipo, String fecha) {
        return filtrarMovimientos(tipo, fecha);
    }
}
