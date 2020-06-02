package com.example.mytix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.mytix.database.DataHelper;
import com.example.mytix.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class Pesawat_page extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinMaskapai, spinDestinasi, spinDewasa, spinAnak;
    SessionManager session;
    String email;
    int id_book;
    public String sMaskapai , sDestinasi, sTanggal, sDewasa, sAnak;
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesawat_page);

        dbHelper = new DataHelper(Pesawat_page.this);
        db = dbHelper.getReadableDatabase();

        final String[] maskapai = {"Garuda Indonesia", "Lion Air", "Batik Air", "Air Asia", "Citilink"};
        final String[] destinasi = {"Jakarta", "Bali", "Surabaya", "Singapura", "Australia"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5", "6", "7"};
        final String[] anak = {"0", "1", "2", "3", "4", "5", "6", "7"};

        spinMaskapai = findViewById(R.id.maskapai);
        spinDestinasi = findViewById(R.id.destinasi);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterMaskapai = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, maskapai);
        adapterMaskapai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMaskapai.setAdapter(adapterMaskapai);

        ArrayAdapter<CharSequence> adapterDestinasi = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, destinasi);
        adapterDestinasi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDestinasi.setAdapter(adapterDestinasi);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);

        spinMaskapai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sMaskapai = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDestinasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDestinasi = parent.getItemAtPosition(position).toString();
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

        etTanggal = findViewById(R.id.tanggal_terbang);
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
                if (sMaskapai != null && sDestinasi != null && sTanggal != null && sDewasa != null) {
                    AlertDialog dialog = new AlertDialog.Builder(Pesawat_page.this)
                            .setTitle("Ingin booking Pesawat sekarang?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, dewasa, anak) VALUES ('" +
                                                sMaskapai + "','" +
                                                sDestinasi + "','" +
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
                                        Toast.makeText(Pesawat_page.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                        finish();
                                    } catch (Exception e) {
                                        Toast.makeText(Pesawat_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .create();
                    dialog.show();
                } else {
                    Toast.makeText(Pesawat_page.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbPswt);
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
        if (sMaskapai.equalsIgnoreCase("Garuda Indonesia") && sDestinasi.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 700000;
            hargaAnak = 70000;
        } else if (sMaskapai.equalsIgnoreCase("Garuda Indonesia") && sDestinasi.equalsIgnoreCase("Bali")) {
            hargaDewasa = 800000;
            hargaAnak = 80000;
        } else if (sMaskapai.equalsIgnoreCase("Garuda Indonesia") && sDestinasi.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 650000;
            hargaAnak = 65000;
        } else if (sMaskapai.equalsIgnoreCase("Garuda Indonesia") && sDestinasi.equalsIgnoreCase("Singapura")) {
            hargaDewasa = 1200000;
            hargaAnak = 120000;
        } else if (sMaskapai.equalsIgnoreCase("Garuda Indonesia") && sDestinasi.equalsIgnoreCase("Australia")) {
            hargaDewasa = 1500000;
            hargaAnak = 150000;
        } else if (sMaskapai.equalsIgnoreCase("Lion Air") && sDestinasi.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 600000;
            hargaAnak = 60000;
        } else if (sMaskapai.equalsIgnoreCase("Lion Air") && sDestinasi.equalsIgnoreCase("Bali")) {
            hargaDewasa = 760000;
            hargaAnak = 76000;
        } else if (sMaskapai.equalsIgnoreCase("Lion Air") && sDestinasi.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 864000;
            hargaAnak = 86400;
        } else if (sMaskapai.equalsIgnoreCase("Lion Air") && sDestinasi.equalsIgnoreCase("Singapura")) {
            hargaDewasa = 958000;
            hargaAnak = 95800;
        } else if (sMaskapai.equalsIgnoreCase("Lion Air") && sDestinasi.equalsIgnoreCase("Australia")) {
            hargaDewasa = 1125000;
            hargaAnak = 112500;
        } else if (sMaskapai.equalsIgnoreCase("Batik Air") && sDestinasi.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 750000;
            hargaAnak = 75000;
        } else if (sMaskapai.equalsIgnoreCase("Batik Air") && sDestinasi.equalsIgnoreCase("Bali")) {
            hargaDewasa = 820000;
            hargaAnak = 82000;
        } else if (sMaskapai.equalsIgnoreCase("Batik Air") && sDestinasi.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 780000;
            hargaAnak = 78000;
        } else if (sMaskapai.equalsIgnoreCase("Batik Air") && sDestinasi.equalsIgnoreCase("Singapura")) {
            hargaDewasa = 970000;
            hargaAnak = 97000;
        } else if (sMaskapai.equalsIgnoreCase("Batik Air") && sDestinasi.equalsIgnoreCase("Australia")) {
            hargaDewasa = 1080000;
            hargaAnak = 108000;
        } else if (sMaskapai.equalsIgnoreCase("Air Asia") && sDestinasi.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 730000;
            hargaAnak = 73000;
        } else if (sMaskapai.equalsIgnoreCase("Air Asia") && sDestinasi.equalsIgnoreCase("Bali")) {
            hargaDewasa = 835000;
            hargaAnak = 83500;
        } else if (sMaskapai.equalsIgnoreCase("Air Asia") && sDestinasi.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 770000;
            hargaAnak = 77000;
        } else if (sMaskapai.equalsIgnoreCase("Air Asia") && sDestinasi.equalsIgnoreCase("Singapura")) {
            hargaDewasa = 939000;
            hargaAnak = 93900;
        } else if (sMaskapai.equalsIgnoreCase("Air Asia") && sDestinasi.equalsIgnoreCase("Australia")) {
            hargaDewasa = 1210000;
            hargaAnak = 121000;
        } else if (sMaskapai.equalsIgnoreCase("Citilink") && sDestinasi.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 810000;
            hargaAnak = 81000;
        } else if (sMaskapai.equalsIgnoreCase("Citilink") && sDestinasi.equalsIgnoreCase("Bali")) {
            hargaDewasa = 860000;
            hargaAnak = 86000;
        } else if (sMaskapai.equalsIgnoreCase("Citilink") && sDestinasi.equalsIgnoreCase("Surabaya")) {
            hargaDewasa = 844000;
            hargaAnak = 84400;
        } else if (sMaskapai.equalsIgnoreCase("Citilink") && sDestinasi.equalsIgnoreCase("Singapura")) {
            hargaDewasa = 1360000;
            hargaAnak = 136000;
        } else if (sMaskapai.equalsIgnoreCase("Citilink") && sDestinasi.equalsIgnoreCase("Australia")) {
            hargaDewasa = 1420000;
            hargaAnak = 142000;
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
