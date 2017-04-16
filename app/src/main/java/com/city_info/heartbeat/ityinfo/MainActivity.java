package com.city_info.heartbeat.ityinfo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.city_info.heartbeat.ityinfo.DB.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity в котором пользователь выбирает страну и получает список городов
 */
public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> countries;
    private List<String> countriesNull;

    private DBHelper dbHelper;
    private Button button_choice;
    private Spinner spinner;
    private TextView textTitle;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Обьявление View элементов
         */
        listview = (ListView) findViewById(R.id.listview);
        button_choice = (Button) findViewById(R.id.btnShowCities);
        spinner = (Spinner) findViewById(R.id.spinner);
        textTitle = (TextView) findViewById(R.id.textTitle);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout);
        countriesNull = new ArrayList<>();


        /**
         * Обработчик нажатия кнопки "SHOW CITIES"
        */

        button_choice.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        //Попытка отобразить список городов из БД, если он загружен в нее

        try{

        textTitle.setText(" ");
        countries = new ArrayList<>();
        dbHelper = new DBHelper(getApplicationContext(), spinner.getSelectedItem().toString());
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE, null, null, null, null, null, null);

        //Получение списка городов из таблицы БД
        if(cursor.moveToFirst()) {
            int city = cursor.getColumnIndex(DBHelper.KEY_CITY);

            do {
                countries.add(cursor.getString(city));
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter  =  new  ArrayAdapter<String>(
                getApplicationContext(), R.layout.my_list_layout, countries );

            // Отображение списка в ListView
        listview.setAdapter(adapter);
        } catch (Throwable t){

            Toast.makeText(getApplicationContext(),
                    "Ooops...wait a little more, download",
                    Toast.LENGTH_SHORT).show();

        }

    }
    });

        /**
         * Обработчик нажатия на элемент в ListView
         * После нажатия, переводит пользователя в CityInfoActivity
         * и передает название города, на который нажали
         */

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String city = ((TextView) view).getText().toString();
                Intent intent = new Intent(getApplicationContext(),CityInfoActivity.class);
                intent.putExtra("CITY", city);
                startActivity(intent);

            }
        });

    }

}
