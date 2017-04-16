package com.city_info.heartbeat.ityinfo;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.city_info.heartbeat.ityinfo.DB.DBHelper;
import com.city_info.heartbeat.ityinfo.DownloadDate.ApiService;
import com.city_info.heartbeat.ityinfo.DownloadDate.Client;
import com.city_info.heartbeat.ityinfo.DownloadDate.Countries;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Титульная Activity, которая встречает ползователя на старте приложения
 */

public class TitleActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "download_date";
    public static final String APP_PREFERENCES_CHECK = "download";


    private Button title_button_download;
    private Button title_button_continue;
    private TextView textProgress;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private SharedPreferences downloadsDate;
    private DBHelper dbHelper;

    private List<List<String>> city;

    //region Список всех стран
    private String[] listCounties = {"China", "Japan", "Thailand", "India", "Malaysia", "Republic of Korea", "Hong Kong",
            "Taiwan", "Philippine", "Australia", "Vietnam", "Russia", "France", "Germany", "Israel", "Sweden", "Italy",
            "Netherlands", "Greece", "Spain", "Austria", "United Kingdom", "Belgium", "United Arab Emirates", "Kazakhstan",
            "Portugal", "Saudi Arabia", "Denmark", "Slovenia", "Iran", "Norway", "Mexico", "Canada", "Syria", "Ukraine", "Cyprus",
            "Czech Republic", "Switzerland", "Iraq", "Turkey", "Romania", "Lebanon", "Hungary", "Georgia", "Brazil","United States", "Azerbaijan",
            "Palestine", "Republic of Lithuania", "Oman", "Slovakia", "Serbia", "Finland", "Iceland", "Republic of Moldova", "Bulgaria",
            "Macedonia", "Liechtenstein", "Jersey", "Poland", "Ireland", "Croatia", "Bosnia and Herzegovina", "Estonia", "Latvia",
            "Hashemite Kingdom of Jordan", "Kyrgyzstan", "Isle of Man","Libya", "Luxembourg", "Armenia", "British Virgin Islands",
            "Yemen", "Belarus", "Gibraltar", "Kenya", "Chile", "Qatar", "Kuwait", "Guadeloupe", "Martinique", "French Guiana",
            "Dominican Republic", "Guam", "U.S. Virgin Islands", "Puerto Rico", "Mongolia", "New Zealand", "Singapore", "Indonesia",
            "Nepal", "Papua New Guinea", "Pakistan", "Panama", "Costa Rica", "Peru", "Belize", "Nigeria", "Venezuela", "Bahamas",
            "Morocco", "Colombia", "Seychelles", "Barbados", "Egypt", "Argentina", "Brunei", "Bahrain", "Aruba", "Saint Lucia",
            "Bangladesh", "Tokelau", "Cambodia", "Macao", "Maldives", "Afghanistan", "New Caledonia", "Fiji", "Wallis and Futuna",
            "Albania", "Uzbekistan", "Montenegro", "North Korea", "Vatican City", "Antarctica", "Bermuda", "Ecuador", "South Africa",
            "Saint Kitts and Nevis", "Samoa", "Bolivia", "Guernsey", "Malta", "Tajikistan", "Zimbabwe", "Liberia", "Ghana", "Tanzania",
            "Zambia", "Madagascar", "Angola", "Namibia", "Ivory Coast", "Sudan", "Uganda", "Cameroon", "Malawi", "Gabon", "Mali",
            "Benin", "Chad", "Botswana", "Cape Verde", "Rwanda", "Republic of the Congo", "Mozambique", "Gambia", "Lesotho", "Mauritius",
            "Algeria", "Guinea", "Congo", "Swaziland", "Burkina Faso", "Sierra Leone", "Somalia", "Niger", "Central African Republic",
            "Togo", "Burundi", "Equatorial Guinea", "South Sudan", "Senegal", "Mauritania", "Djibouti", "Comoros", "Tunisia", "Mayotte",
            "Bhutan", "Greenland", "Kosovo", "Cayman Islands", "Jamaica", "Guatemala", "Marshall Islands", "Monaco",
            "Anguilla", "Grenada", "Paraguay", "Montserrat", "Turks and Caicos Islands", "Antigua and Barbuda", "Tuvalu",
            "French Polynesia", "Solomon Islands", "Vanuatu", "Suriname", "Cook Islands", "Kiribati", "Niue", "Tonga",
            "French Southern Territories", "Norfolk Island", "Turkmenistan", "Pitcairn Islands", "San Marino", "Faroe Islands",
            "Svalbard and Jan Mayen", "Cocos [Keeling] Islands", "Nauru", "South Georgia and the South Sandwich Islands",
            "U.S. Minor Outlying Islands", "Sint Maarten", "Guinea-Bissau", "Saint Martin", "Saint Vincent and the Grenadines",
            "Saint Pierre and Miquelon", "Dominica", "Falkland Islands", "Northern Mariana Islands", "East Timor", "Bonaire",
            "American Samoa", "Federated States of Micronesia", "Palau", "Guyana", "Honduras", "Nicaragua", "El Salvador", "Andorra",
            "Myanmar [Burma]", "Sri Lanka", "Haiti", "Trinidad and Tobago", "Laos", "Uruguay", "Eritrea", "Cuba", "Saint Helena",
            "Christmas Island", "Ethiopia"};

//endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_title);

        //Создание списка городов и его заполнение

        ArrayList<String> nameCountries = new ArrayList<String>();
        for (int i = 0; i < listCounties.length; i++) {
            nameCountries.add(listCounties[i]);
        }

        /**
         * Обьявления View элементов
         */

        title_button_continue = (Button) findViewById(R.id.title_button_continue);
        title_button_continue.setEnabled(false);

        title_button_download = (Button) findViewById(R.id.title_button_download);
        textProgress = (TextView) findViewById(R.id.textVProgress);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        downloadsDate = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        /**
         * Скрывание лишних View, если пользователь уже скачал данные
         */

        if(downloadsDate.getString(APP_PREFERENCES_CHECK,"").equals("complete")){

            title_button_download.setAlpha(0.0f);
            progressBar.setAlpha(0.0f);
            title_button_continue.setEnabled(true);
        }

        /**
         * Обработчик нажатия кнопки "Continue"
         */

        title_button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        /**
         *  Обработчик нажатия кнопки "Download date"
         */

        title_button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Проверка на наличие подключенного интернета

                if(InternetConnection.checkConnection(getApplicationContext())) {

                    title_button_download.setEnabled(false);
                    title_button_continue.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "You can view information about cities that have already downloaded",
                            Toast.LENGTH_LONG).show();
                    textProgress.setText("Wait...");

                    //Запуск потока для скачивания
                    new Thread(myThread).start();

                }else Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION",
                        Toast.LENGTH_LONG).show();


            }
        });
    }

    /**
     * Метод создания таблицы в базе данных SQL
     * На вход метода поступает название страны для названия таблицы и список городов
     */

    private void createDB(String countries, List<String> city){

        dbHelper = new DBHelper(this, countries);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();

        //Заполнение таблицы городами
        for(int i=0; i<city.size(); i++){
        contentValues.put(DBHelper.KEY_CITY, city.get(i));
        database.insert(DBHelper.TABLE, null, contentValues);}

    }


    /**
     * Отдельный поток для скачивания данных
     */

        private Runnable myThread = new Runnable() {
            @Override
            public void run() {

                //Запрос на получение городов

                ApiService apiService = Client.getApiService();
                Call<Countries> call = apiService.getJSON();

                call.enqueue(new Callback<Countries>() {
                    @Override
                    public void onResponse(Call<Countries> call, Response<Countries> response) {

                        //region Получение списков городов всех стран
                        city = new ArrayList<List<String>>();
                        city.add(response.body().getChina());
                        city.add(response.body().getJapan());
                        city.add(response.body().getThailand());
                        city.add(response.body().getIndia());
                        city.add(response.body().getMalaysia());
                        city.add(response.body().getRepublicOfKorea());
                        city.add(response.body().getHongKong());
                        city.add(response.body().getTaiwan());
                        city.add(response.body().getPhilippines());
                        city.add(response.body().getAustralia());
                        city.add(response.body().getVietnam());
                        city.add(response.body().getRussia());
                        city.add(response.body().getFrance());
                        city.add(response.body().getGermany());
                        city.add(response.body().getIsrael());
                        city.add(response.body().getSweden());
                        city.add(response.body().getItaly());
                        city.add(response.body().getNetherlands());
                        city.add(response.body().getGreece());
                        city.add(response.body().getSpain());
                        city.add(response.body().getAustria());
                        city.add(response.body().getUnitedKingdom());
                        city.add(response.body().getBelgium());
                        city.add(response.body().getUnitedArabEmirates());
                        city.add(response.body().getKazakhstan());
                        city.add(response.body().getPortugal());
                        city.add(response.body().getSaudiArabia());
                        city.add(response.body().getDenmark());
                        city.add(response.body().getSlovenia());
                        city.add(response.body().getIran());
                        city.add(response.body().getNorway());
                        city.add(response.body().getMexico());
                        city.add(response.body().getCanada());
                        city.add(response.body().getSyria());
                        city.add(response.body().getUkraine());
                        city.add(response.body().getCyprus());
                        city.add(response.body().getCzechRepublic());
                        city.add(response.body().getSwitzerland());
                        city.add(response.body().getIraq());
                        city.add(response.body().getTurkey());
                        city.add(response.body().getRomania());
                        city.add(response.body().getLebanon());
                        city.add(response.body().getHungary());
                        city.add(response.body().getGeorgia());
                        city.add(response.body().getBrazil());
                        city.add(response.body().getUnited_states());
                        city.add(response.body().getAzerbaijan());
                        city.add(response.body().getPalestine());
                        city.add(response.body().getRepublicOfLithuania());
                        city.add(response.body().getOman());
                        city.add(response.body().getSlovakia());
                        city.add(response.body().getSerbia());
                        city.add(response.body().getFinland());
                        city.add(response.body().getIceland());
                        city.add(response.body().getRepublicOfMoldova());
                        city.add(response.body().getBulgaria());
                        city.add(response.body().getMacedonia());
                        city.add(response.body().getLiechtenstein());
                        city.add(response.body().getJersey());
                        city.add(response.body().getPoland());
                        city.add(response.body().getIreland());
                        city.add(response.body().getCroatia());
                        city.add(response.body().getBosniaAndHerzegovina());
                        city.add(response.body().getEstonia());
                        city.add(response.body().getLatvia());
                        city.add(response.body().getHashemiteKingdomOfJordan());
                        city.add(response.body().getKyrgyzstan());
                        city.add(response.body().getIsleOfMan());
                        city.add(response.body().getLibya());
                        city.add(response.body().getLuxembourg());
                        city.add(response.body().getArmenia());
                        city.add(response.body().getBritishVirginIslands());
                        city.add(response.body().getYemen());
                        city.add(response.body().getBelarus());
                        city.add(response.body().getGibraltar());
                        city.add(response.body().getKenya());
                        city.add(response.body().getChile());
                        city.add(response.body().getQatar());
                        city.add(response.body().getKuwait());
                        city.add(response.body().getGuadeloupe());
                        city.add(response.body().getMartinique());
                        city.add(response.body().getFrenchGuiana());
                        city.add(response.body().getDominicanRepublic());
                        city.add(response.body().getGuam());
                        city.add(response.body().getUSVirginIslands());
                        city.add(response.body().getPuertoRico());
                        city.add(response.body().getMongolia());
                        city.add(response.body().getNewZealand());
                        city.add(response.body().getSingapore());
                        city.add(response.body().getIndonesia());
                        city.add(response.body().getNepal());
                        city.add(response.body().getPapuaNewGuinea());
                        city.add(response.body().getPakistan());
                        city.add(response.body().getPanama());
                        city.add(response.body().getCostaRica());
                        city.add(response.body().getPeru());
                        city.add(response.body().getBelize());
                        city.add(response.body().getNigeria());
                        city.add(response.body().getVenezuela());
                        city.add(response.body().getBahamas());
                        city.add(response.body().getMorocco());
                        city.add(response.body().getColombia());
                        city.add(response.body().getSeychelles());
                        city.add(response.body().getBarbados());
                        city.add(response.body().getEgypt());
                        city.add(response.body().getArgentina());
                        city.add(response.body().getBrunei());
                        city.add(response.body().getBahrain());
                        city.add(response.body().getAruba());
                        city.add(response.body().getSaintLucia());
                        city.add(response.body().getBangladesh());
                        city.add(response.body().getTokelau());
                        city.add(response.body().getCambodia());
                        city.add(response.body().getMacao());
                        city.add(response.body().getMaldives());
                        city.add(response.body().getAfghanistan());
                        city.add(response.body().getNewCaledonia());
                        city.add(response.body().getFiji());
                        city.add(response.body().getWallisAndFutuna());
                        city.add(response.body().getAlbania());
                        city.add(response.body().getUzbekistan());
                        city.add(response.body().getMontenegro());
                        city.add(response.body().getNorthKorea());
                        city.add(response.body().getVaticanCity());
                        city.add(response.body().getAntarctica());
                        city.add(response.body().getBermuda());
                        city.add(response.body().getEcuador());
                        city.add(response.body().getSouthAfrica());
                        city.add(response.body().getSaintKittsAndNevis());
                        city.add(response.body().getSamoa());
                        city.add(response.body().getBolivia());
                        city.add(response.body().getGuernsey());
                        city.add(response.body().getMalta());
                        city.add(response.body().getTajikistan());
                        city.add(response.body().getZimbabwe());
                        city.add(response.body().getLiberia());
                        city.add(response.body().getGhana());
                        city.add(response.body().getTanzania());
                        city.add(response.body().getZambia());
                        city.add(response.body().getMadagascar());
                        city.add(response.body().getAngola());
                        city.add(response.body().getNamibia());
                        city.add(response.body().getIvoryCoast());
                        city.add(response.body().getSudan());
                        city.add(response.body().getUganda());
                        city.add(response.body().getCameroon());
                        city.add(response.body().getMalawi());
                        city.add(response.body().getGabon());
                        city.add(response.body().getMali());
                        city.add(response.body().getBenin());
                        city.add(response.body().getChad());
                        city.add(response.body().getBotswana());
                        city.add(response.body().getCapeVerde());
                        city.add(response.body().getRwanda());
                        city.add(response.body().getRepublicOfTheCongo());
                        city.add(response.body().getMozambique());
                        city.add(response.body().getGambia());
                        city.add(response.body().getLesotho());
                        city.add(response.body().getMauritius());
                        city.add(response.body().getAlgeria());
                        city.add(response.body().getGuinea());
                        city.add(response.body().getCongo());
                        city.add(response.body().getSwaziland());
                        city.add(response.body().getBurkinaFaso());
                        city.add(response.body().getSierraLeone());
                        city.add(response.body().getSomalia());
                        city.add(response.body().getNiger());
                        city.add(response.body().getCentralAfricanRepublic());
                        city.add(response.body().getTogo());
                        city.add(response.body().getBurundi());
                        city.add(response.body().getEquatorialGuinea());
                        city.add(response.body().getSouthSudan());
                        city.add(response.body().getSenegal());
                        city.add(response.body().getMauritania());
                        city.add(response.body().getDjibouti());
                        city.add(response.body().getComoros());
                        city.add(response.body().getTunisia());
                        city.add(response.body().getMayotte());
                        city.add(response.body().getBhutan());
                        city.add(response.body().getGreenland());
                        city.add(response.body().getKosovo());
                        city.add(response.body().getCaymanIslands());
                        city.add(response.body().getJamaica());
                        city.add(response.body().getGuatemala());
                        city.add(response.body().getMarshallIslands());
                        city.add(response.body().getMonaco());
                        city.add(response.body().getAnguilla());
                        city.add(response.body().getGrenada());
                        city.add(response.body().getParaguay());
                        city.add(response.body().getMontserrat());
                        city.add(response.body().getTurksAndCaicosIslands());
                        city.add(response.body().getAntiguaAndBarbuda());
                        city.add(response.body().getTuvalu());
                        city.add(response.body().getFrenchPolynesia());
                        city.add(response.body().getSolomonIslands());
                        city.add(response.body().getVanuatu());
                        city.add(response.body().getSuriname());
                        city.add(response.body().getCookIslands());
                        city.add(response.body().getKiribati());
                        city.add(response.body().getNiue());
                        city.add(response.body().getTonga());
                        city.add(response.body().getFrenchSouthernTerritories());
                        city.add(response.body().getNorfolkIsland());
                        city.add(response.body().getTurkmenistan());
                        city.add(response.body().getPitcairnIslands());
                        city.add(response.body().getSanMarino());
                        city.add(response.body().getFaroeIslands());
                        city.add(response.body().getSvalbardAndJanMayen());
                        city.add(response.body().getCocosKeelingIslands());
                        city.add(response.body().getNauru());
                        city.add(response.body().getSouthGeorgiaAndTheSouthSandwichIslands());
                        city.add(response.body().getUSMinorOutlyingIslands());
                        city.add(response.body().getSintMaarten());
                        city.add(response.body().getGuineaBissau());
                        city.add(response.body().getSaintMartin());
                        city.add(response.body().getSaintVincentAndTheGrenadines());
                        city.add(response.body().getSaintPierreAndMiquelon());
                        city.add(response.body().getDominica());
                        city.add(response.body().getFalklandIslands());
                        city.add(response.body().getNorthernMarianaIslands());
                        city.add(response.body().getEastTimor());
                        city.add(response.body().getBonaire());
                        city.add(response.body().getAmericanSamoa());
                        city.add(response.body().getFederatedStatesOfMicronesia());
                        city.add(response.body().getPalau());
                        city.add(response.body().getGuyana());
                        city.add(response.body().getHonduras());
                        city.add(response.body().getNicaragua());
                        city.add(response.body().getElSalvador());
                        city.add(response.body().getAndorra());
                        city.add(response.body().getMyanmarBurma());
                        city.add(response.body().getSriLanka());
                        city.add(response.body().getHaiti());
                        city.add(response.body().getTrinidadAndTobago());
                        city.add(response.body().getLaos());
                        city.add(response.body().getUruguay());
                        city.add(response.body().getEritrea());
                        city.add(response.body().getCuba());
                        city.add(response.body().getSaintHelena());
                        city.add(response.body().getChristmasIsland());
                        city.add(response.body().getEthiopia());
                        //endregion

                        textProgress.setText("0 / 240 China");
                    }

                    @Override
                    public void onFailure(Call<Countries> call, Throwable t) {

                    }
                });

                progressStatus = 0;

                /**
                 * Заполнение БД полученными списками городов, и обновление View элементов в Activity,
                 * для отслеживания прогресса загрузки данных
                 */
                while (progressStatus < 240) {
                    try {

                        createDB(listCounties[progressStatus], city.get(progressStatus));
                        myHandle.sendMessage(myHandle.obtainMessage());

                    } catch (Throwable t) {

                    }
                }

                //По окончанию загрузки, оповестить об этом и
                // сохранения настроек для последующих запусков приложения

                textProgress.setText("Loading is complete");
                SharedPreferences.Editor editor = downloadsDate.edit();
                editor.putString(APP_PREFERENCES_CHECK,"complete");
                editor.apply();

            }

            //Динамическое обновление прогресса загрузки

            Handler myHandle = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    if(progressStatus != 240) {
                        progressStatus++;
                        progressBar.setProgress(progressStatus);
                        textProgress.setText(progressStatus + " / 240 " + listCounties[progressStatus]);
                    }
                }
            };
        };



}
