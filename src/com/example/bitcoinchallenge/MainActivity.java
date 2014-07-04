package com.example.bitcoinchallenge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView mTextView;
	private EditText mEditText;
	private Button mButton;
	private InputMethodManager imm;
	private final String URL = "http://api.bitcoincharts.com/v1/weighted_prices.json";
	private double mPrice;
	private final String CURRENCY_TYPE = "USD";
	private RadioButton mRadioButtonBuy;
	private RadioButton mRadioButtonSell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextView = (TextView) findViewById(R.id.textView1);
		mEditText = (EditText) findViewById(R.id.editText1);
		mButton = (Button) findViewById(R.id.button1);
		mRadioButtonBuy = (RadioButton) findViewById(R.id.choice1);
		mRadioButtonSell = (RadioButton) findViewById(R.id.choice2);
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void click(View view) {
		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
		if (mRadioButtonBuy.isChecked() == false && mRadioButtonSell.isChecked() == false) {
			Toast.makeText(getApplicationContext(), "Please select Buy or Sell", Toast.LENGTH_LONG).show();
			return;
		}
		new ParseJson().execute(URL);
	}

	private class ParseJson extends AsyncTask<String, Integer, Double> {

		@Override
		protected Double doInBackground(String... url) {
			for (int i = 0; i < url.length; i++) {
				JSONObject jsonObject = JsonParser.getJson(url[i]);
				try {
					JSONObject usd = jsonObject.getJSONObject(CURRENCY_TYPE);
					String price = usd.getString("7d");
					if (price != null) {
						return Double.parseDouble(price);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Double result) {
			mPrice = result;
			updateTextView();
		}

	}

	public void updateTextView() {
		double price = Integer.parseInt(mEditText.getText().toString()) * mPrice;
		String s = "Current Bitcoin price is: " + price + " per bitcoin" + "\n" + "\n";
		if (mRadioButtonBuy.isChecked()) {
			s += "Buy " + mEditText.getText().toString() + " bitcoins needs $" + price;
		} else if (mRadioButtonSell.isChecked()) {
			s += "Sell " + mEditText.getText().toString() + " bitcoins you get $" + price;
		}
		mTextView.setText(s);

	}

}
