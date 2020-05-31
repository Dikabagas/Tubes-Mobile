package com.example.mytix;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mytix.database.DataHelper;
import com.example.mytix.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class Holiday_page extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    SessionManager session;

    SQLiteDatabase db;
    Spinner spinTempat, spinDewasa, spinAnak;

    String email;
    int id_book;
    public String sTempat, sTanggal, sDewasa, sAnak, asal="liburan";
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_page);

        dbHelper = new DataHelper(Holiday_page.this);
        db = dbHelper.getReadableDatabase();



        final String[] tempat = {"Dufan (Premium)", "Taman Nusa Bali", "Jatim Park 3", "Jatim Park 2", "Jatim Park 1", "Jungle Land Adventure Bogor"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5", "6", "7"};
        final String[] anak = {"0", "1", "2", "3", "4", "5", "6", "7"};


        spinTempat = findViewById(R.id.tmptlibur);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterTempat = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, tempat);
        adapterTempat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTempat.setAdapter(adapterTempat);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);



        spinTempat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sTempat = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDewasa = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAnak = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBook = findViewById(R.id.book);

        etTanggal = findViewById(R.id.tanggal_liburan);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        setDateTimeField();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perhitunganHarga();
                if (sTempat != null && sTanggal != null && sDewasa != null) {
                    AlertDialog dialog = new AlertDialog.Builder(Holiday_page.this)
                            .setTitle("Ingin booking kereta sekarang?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, dewasa, anak) VALUES ('" +
                                                asal+"','"+
                                                sTempat + "','" +
                                                sTanggal + "','" +
                                                sDewasa + "','" +
                                                sAnak + "');");
                                        cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                        cursor.moveToLast();
                                        if (cursor.getCount() > 0) {
                                            cursor.moveToPosition(0);
                                            id_book = cursor.getInt(0);
                                        }
                                        db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                email + "','" +
                                                id_book + "','" +
                                                hargaTotalDewasa + "','" +
                                                hargaTotalAnak + "','" +
                                                hargaTotal + "');");
                                        Toast.makeText(Holiday_page.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                        finish();
                                    } catch (Exception e) {
                                        Toast.makeText(Holiday_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .create();
                    dialog.show();
                }
                else {
                    Toast.makeText(Holiday_page.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.liburan);
        toolbar.setTitle("Form Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void perhitunganHarga() {
        if (sTempat.equalsIgnoreCase("Dufan (Premium)")) {
            hargaDewasa = 455000;
            hargaAnak = 455000;
        } else if (sTempat.equalsIgnoreCase("Taman Nusa Bali")) {
            hargaDewasa = 85000;
            hargaAnak = 85000;
        } else if (sTempat.equalsIgnoreCase("Jatim Park 3")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sTempat.equalsIgnoreCase("Jatim Park 2")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sTempat.equalsIgnoreCase("Jatim Park 1")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sTempat.equalsIgnoreCase("JungleLand Adventure Bogor")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        }

        jmlDewasa = Integer.parseInt(sDewasa);
        jmlAnak = Integer.parseInt(sAnak);

        hargaTotalDewasa = jmlDewasa * hargaDewasa;
        hargaTotalAnak = jmlAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}