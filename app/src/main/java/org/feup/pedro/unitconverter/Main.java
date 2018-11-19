package org.feup.pedro.unitconverter;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Main extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private Spinner sp_source;
    private Spinner sp_target;
    private GetExchangeUnit httpUnit;
    private TextView finalResult;
    private DrawerLayout drawer;

    //create a list of items for the spinner.
    private String[] lengths = new String[]{"Ångstrom", "Smoot", "inch", "foot", "yard", "mile", "nautical mile",
            "mil", "rod", "fathom", "furlong", "km", "m", "cm", "mm", "μm", "nm"};


    private String[] pressures= new String[] {"atmosphere", "kPa", "Pa", "psi" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("aquiaaaaaa");
        sp_source = findViewById(R.id.sp_source);
        sp_target = findViewById(R.id.sp_target);

        //to tell the app that we are going to use our toolbar as our actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.sidemenu_view);
        navigationView.setNavigationItemSelectedListener(this);

        //for the sidemenu button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lengths);
        //set the spinners adapter to the previously created one.
        sp_source.setAdapter(spinnerAdpater);
        sp_target.setAdapter(spinnerAdpater);

        //httpUnit= new GetExchangeUnit(this);

        //for our main activity start with lengths converter
        navigationView.setCheckedItem(R.id.nav_lengths);

        finalResult = (TextView) findViewById(R.id.tv_finalResult);

        ((Button) findViewById(R.id.convert)).setOnClickListener(this);

    }
   //when we pressed the back button by our navigation drawer we don't want to leave our activity imediatly
    public void onBackPressed() {
        //start because our drawer is on the left side
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        String unit = (String) sp_source.getSelectedItem();
        String targetUnit = (String) sp_target.getSelectedItem();
        httpUnit=new GetExchangeUnit(this, unit, targetUnit);

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

    /*@Override
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

    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lengths);
        ArrayAdapter<String> spinnerAdpaterP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pressures);
        switch (menuItem.getItemId()){
            case R.id.nav_lengths:
                sp_source.setAdapter(spinnerAdpater);
                sp_target.setAdapter(spinnerAdpater);
                break;
            case R.id.nav_pressures:
                sp_source.setAdapter(spinnerAdpaterP);
                sp_target.setAdapter(spinnerAdpaterP);
                break;

        }

        //to close our drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
