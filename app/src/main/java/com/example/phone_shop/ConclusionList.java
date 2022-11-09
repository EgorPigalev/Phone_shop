package com.example.phone_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConclusionList extends AppCompatActivity {

    private AdapterMask pAdapter;
    private List<Mask> listPhone = new ArrayList<>();

    ListView listView;
    ProgressBar pbLoading;
    EditText textSearch;
    TextView tvDeleteSearch;
    Spinner chingSearch, sorting, order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusion_list);

        pbLoading = findViewById(R.id.pbLoading);

        ListView ivProducts = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(ConclusionList.this, listPhone);
        ivProducts.setAdapter(pAdapter);


        tvDeleteSearch = findViewById(R.id.tvDeleteSearch);
        chingSearch = findViewById(R.id.chingSearch);
        sorting = findViewById(R.id.sorting);
        order = findViewById(R.id.order);
        listView = findViewById(R.id.lvData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Go((int)id);
            }
        });

        textSearch = findViewById(R.id.etSearch);

        textSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textSearch.setHint("");
            else
                textSearch.setHint(R.string.enter_value);
        });
        new GetPhones().execute();
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                new GetPhones().execute();
                if(textSearch.getText().length() != 0 || !sorting.getSelectedItem().toString().equals(""))
                {
                    tvDeleteSearch.setVisibility(View.VISIBLE);
                }
                else{
                    tvDeleteSearch.setVisibility(View.GONE);
                }
            }
        });
        sorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                new GetPhones().execute();
                if(!sorting.getSelectedItem().toString().equals(""))
                {
                    tvDeleteSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                new GetPhones().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        chingSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                new GetPhones().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public String fild(String str)
    {
        if(str.equals("Фирма")){
            return "manufacturer";
        }
        else if(str.equals("Модель")){
            return  "model";
        }
        else if(str.equals("Цвет"))
        {
            return "colour";
        }
        else if(str.equals("Цена")){
            return "price";
        }
        else{
            return null;
        }
    }

    public String order(String str){
        if(str.equals("Возрастание")){
            return "ascending";
        }
        else{
            return "descending";
        }
    }


    private class GetPhones extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String textChingSearch = fild(chingSearch.getSelectedItem().toString());
                String textSorting = fild(sorting.getSelectedItem().toString());
                String textOrder = order(order.getSelectedItem().toString());
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/Phones?fieldSearch=" + textChingSearch +"&textSearch=" + textSearch.getText() +"&fieldSort=" + textSorting +"&valueSort=" + textOrder);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                listPhone.clear();
                pAdapter.notifyDataSetInvalidated();
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempProduct = new Mask(
                            productJson.getInt("id_phone"),
                            productJson.getString("manufacturer"),
                            productJson.getString("model"),
                            productJson.getString("colour"),
                            (float) productJson.getDouble("price"),
                            productJson.getString("image")
                    );
                    listPhone.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
                pbLoading.setVisibility(View.GONE);
            }
            catch (Exception ignored)
            {

            }
        }
    }

    public void Go(int id)
    {
        Intent intent = new Intent(this, SingleEntryChange.class);
        Bundle b = new Bundle();
        b.putInt("key", id);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    public void Back(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void TextBlockVisibility(View view)
    {
        Button btnSearch = findViewById(R.id.btnSearch);
        TableLayout tlSearch = findViewById(R.id.tlSearch);
        if(tlSearch.getVisibility() == View.VISIBLE)
        {
            btnSearch.setText(R.string.filter_closed);
            tlSearch.setVisibility(View.GONE);
        }
        else
        {
            btnSearch.setText(R.string.filter_open);
            tlSearch.setVisibility(View.VISIBLE);
        }
    }

    public void deleteSearch(View v)
    {
        textSearch.setText("");
        chingSearch.setSelection(0);
        sorting.setSelection(0);
        order.setSelection(0);
        tvDeleteSearch.setVisibility(View.GONE);
    }
}