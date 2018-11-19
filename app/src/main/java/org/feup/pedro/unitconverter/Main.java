package org.feup.pedro.unitconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Main extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Spinner sp_source;
    private Spinner sp_target;
    private GetExchangeUnit httpUnit;
    private TextView finalResult;

    //create a list of items for the spinner.
    private String[] lengths = new String[]{"Ångstrom", "Smoot", "inch", "foot", "yard", "mile", "nautical mile",
            "mil", "rod", "fathom", "furlong", "km", "m", "cm", "mm", "μm", "nm"};


    private String[] pressures= new String[] {"atmosphere", "kPa", "Pa", "psi" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp_source = findViewById(R.id.sp_source);
        sp_target = findViewById(R.id.sp_target);


        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lengths);
        //set the spinners adapter to the previously created one.
        sp_source.setAdapter(spinnerAdpater);
        sp_target.setAdapter(spinnerAdpater);

        //httpUnit= new GetExchangeUnit(this);

        finalResult = (TextView) findViewById(R.id.tv_finalResult);

        ((Button) findViewById(R.id.convert)).setOnClickListener(this);
        ((ToggleButton) findViewById(R.id.toggleButton)).setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        String unit = (String) sp_source.getSelectedItem();
        String targetUnit = (String) sp_target.getSelectedItem();
        String quantity_string = ((EditText) findViewById(R.id.ed_quantity)).getText().toString();
        System.out.println(quantity_string.length());
        if(quantity_string.length() > 0) {
            double quantity = Double.parseDouble(quantity_string);
            httpUnit=new GetExchangeUnit(this, unit, targetUnit, quantity);
        }
        else{
            Toast.makeText(this, "No quantity to convert!", Toast.LENGTH_LONG).show();
        }



    }

    public void writeResult(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finalResult.setText(text);
                System.out.println("a222qui "+text);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lengths);
        ArrayAdapter<String> spinnerAdpaterP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pressures);

        if(isChecked){
            sp_source.setAdapter(spinnerAdpaterP);
            sp_target.setAdapter(spinnerAdpaterP);
            System.out.println("checked");
        }

        else{
            sp_source.setAdapter(spinnerAdpater);
            sp_target.setAdapter(spinnerAdpater);
            System.out.println("unchecked");
        }

    }
}
