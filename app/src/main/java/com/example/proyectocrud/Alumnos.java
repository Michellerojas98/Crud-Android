package com.example.proyectocrud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Alumnos extends AppCompatActivity {

    Spinner spSpinner;
    String[] comunas = new String[]{"Puente Alto", "Quinta Normal", "Maipu", "La Reina", "Las Condes", "La Florida"};

    EditText edtRut, edtNombre, edtDireccion, edtNota;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);

        edtRut = findViewById(R.id.edtRut);
        edtNombre = findViewById(R.id.edtNombre);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtNota = findViewById(R.id.edtNota);
        spSpinner = findViewById(R.id.spSpinner);
        lista = findViewById(R.id.listLista);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, comunas);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpinner.setAdapter(spinnerAdapter);
    }

    public void CargarLista() {
        DataHelper dh = new DataHelper(this, "alumno.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        Cursor c = bd.rawQuery("select rut, nombre, direccion, comuna, nota from alumno", null);

        if (c.moveToFirst()) {
            int i = 0;
            String[] arr = new String[c.getCount()];
            do {
                String linea = "||" + c.getInt(0) + "||" + c.getString(1) + "||" + c.getString(2) + "||" + c.getString(3) + "||" + c.getInt(4) + "||";
                arr[i] = linea;
                i++;
            } while (c.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<>
                    (this, android.R.layout.simple_expandable_list_item_1, arr);
            lista.setAdapter(adapter);
            bd.close();
        } else {
            // Manejar el caso donde no hay datos para mostrar.
        }
    }

    public void onClickAgregar(View view) {
        DataHelper dh = new DataHelper(this, "alumno.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("rut", edtRut.getText().toString());
        reg.put("nombre", edtNombre.getText().toString());
        reg.put("direccion", edtDireccion.getText().toString());
        reg.put("comuna", spSpinner.getSelectedItem().toString());
        reg.put("nota", edtNota.getText().toString());
        long resp = bd.insert("alumno", null, reg);
        bd.close();
        if (resp == -1) {
            Toast.makeText(this, "No se puede ingresar el alumno", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registro Agregado", Toast.LENGTH_LONG).show();
        }
        CargarLista();
        Limpiar();
    }

    public void onClickEliminar(View view) {
        DataHelper dh = new DataHelper(this, "alumno.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        String bRut = edtRut.getText().toString();
        long resp = bd.delete("alumno", "rut=?", new String[]{bRut});
        bd.close();
        if (resp == 0) {
            Toast.makeText(this, "No se puede eliminar el alumno", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registro Eliminado", Toast.LENGTH_LONG).show();
        }
        Limpiar();
        CargarLista();
    }

    public void onClickModificar(View view) {
        DataHelper dh = new DataHelper(this, "alumno.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("rut", edtRut.getText().toString());
        reg.put("nombre", edtNombre.getText().toString());
        reg.put("direccion", edtDireccion.getText().toString());
        reg.put("comuna", spSpinner.getSelectedItem().toString());
        reg.put("nota", edtNota.getText().toString());
        long resp = bd.update("alumno", reg, "rut=?", new String[]{edtRut.getText().toString()});
        bd.close();
        if (resp == 0) {
            Toast.makeText(this, "No se puede modificar el alumno", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Registro Modificado", Toast.LENGTH_LONG).show();
        }
        Limpiar();
        CargarLista();
    }

    public void Limpiar() {
        edtRut.setText("");
        edtNombre.setText("");
        edtDireccion.setText("");
        edtNota.setText("");
    }
}
