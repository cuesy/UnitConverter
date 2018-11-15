package org.feup.pedro.unitconverter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;

public class GetExchangeUnit implements Runnable {
    private String targetUnit;
    private String targetUnitJSON;
    private String sourceUnit;
    private String sourceUnitJSON;
    private Activity main;


    public GetExchangeUnit(Activity activity, String unit, String targetUnit) {
        main = activity;
        this.targetUnit = targetUnit;
        targetUnitJSON = checkUnits(targetUnit);
        sourceUnit = unit;
        sourceUnitJSON = checkUnits(unit);

        Thread thr = new Thread(this);
        thr.start();
        System.out.println("aqui");
    }

    private String addBrackets(String unit){
        String leftBracket="%5B";
        String rightBracket="%5D";
        return leftBracket+unit+rightBracket;
    }

    private String checkUnits(String unit){

        if(unit.equals("Ångstrom"))
            unit="Ao";

        else if(unit.equals("Smoot"))
            unit=addBrackets("smoot");

        else if(unit.equals("inch"))
            unit=addBrackets("in_i");

        else if (unit.equals("foot"))
            unit=addBrackets("ft_i");

        else if (unit.equals("yard"))
            unit=addBrackets("ya_i");

        else if(unit.equals("mile"))
            unit=addBrackets("mi_i");

        else if(unit.equals("nautical mile"))
            unit=addBrackets("nmi_i");

        else if (unit.equals("mil"))
            unit=addBrackets("mil_i");

        else if(unit.equals("rod"))
            unit=addBrackets("rd_us");

        else if(unit.equals("fathom"))
            unit=addBrackets("fth_us");

        else if(unit.equals("furlong"))
            unit=addBrackets("fur_us");

        else if(unit.equals("μm"))
            unit=addBrackets("um");

        else if(unit.equals("psi"))
            unit=addBrackets(unit);

        else if(unit.equals("atmosphere"))
            unit="atm";

        return unit;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }

    private String transform(String response) {
        String readableResult = null;

        try {
           // {"@ServerDateTime": "2018-11-13T22:33:29",
            // "WebServiceRequest": "http://ucum.nlm.nih.gov/ucum-service/v1/ucumtransform/from/m/to/cm",
            // "Response": {"SourceQuantity": "1.0","SourceUnit": "m", "TargetUnit": "cm", "ResultQuantity": "100.0"}}*/


            JSONObject json = new JSONObject(response);
            JSONObject jOrigin = json.getJSONObject("UCUMWebServiceResponse");
            JSONObject jResponse = jOrigin.getJSONObject("Response");

            String valSource=jResponse.getString("SourceQuantity");
            String valResult = jResponse.getString("ResultQuantity");

            readableResult=valSource+" "+ sourceUnit + " = " + valResult + " " + targetUnit;
        } catch (Exception e) {
            readableResult = "Error on returned string!\n" + response;
        }
        return readableResult;
    }


    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        System.out.println("run");
        try {
            url = new URL("https://ucum.nlm.nih.gov/ucum-service/v1/ucumtransform/from/" + sourceUnitJSON + "/to/" + targetUnitJSON);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setUseCaches(false);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {  // code 200 means OK
                String response = readStream(urlConnection.getInputStream());
                response = transform(response);
                ((Main)main).writeResult(response);
            } else
                ((Main) main).writeResult("Code: " + responseCode);
        } catch (Exception e) {
            ((Main) main).writeResult(e.toString());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

}
