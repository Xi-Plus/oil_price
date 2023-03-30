package com.xiplus.oil_price;

import android.support.v7.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

	public void run(View view) {
		ListView listView = findViewById(R.id.result);
		ArrayList<String> values = new ArrayList<>();
		boolean ok = true;
		int unit = 0;
		try {
			unit = (int) (10 * Double.parseDouble(((EditText) findViewById(R.id.price)).getText().toString()));
			if (unit <= 0) {
				values.add("油價不可為0");
				ok = false;
			}
			((EditText) findViewById(R.id.price)).setText(String.valueOf(unit / 10.0));
		} catch (NullPointerException e) {
			values.add("油價錯誤");
			ok = false;
		} catch (NumberFormatException e) {
			values.add("未填寫油價");
			ok = false;
		}

		int discount = 0;
		try {
			discount = (int) (10 * Double.parseDouble(((EditText) findViewById(R.id.discount)).getText().toString()));
			((EditText) findViewById(R.id.discount)).setText(String.valueOf(discount / 10.0));
		} catch (NullPointerException e) {
			// nothing to do
		} catch (NumberFormatException e) {
			// nothing to do
		}

		int start_amount = 0;
		try {
			start_amount = (int) (100 * Double.parseDouble(((EditText) findViewById(R.id.amount)).getText().toString()));
		} catch (NullPointerException e) {
			values.add("數量錯誤");
			ok = false;
		} catch (NumberFormatException e) {
			start_amount = 100;
		}
		((EditText) findViewById(R.id.amount)).setText(String.valueOf(start_amount / 100.0));

		if (ok) {
			double want_save = 0;
			try {
				want_save = Double.parseDouble(((EditText) findViewById(R.id.save)).getText().toString());
			} catch (NullPointerException e) {
				// nothing to do
			} catch (NumberFormatException e) {
				if (discount == 0) {
					want_save = 0.4;
				} else {
					want_save = 0.8;
				}
				((EditText) findViewById(R.id.save)).setText(String.valueOf(want_save));
			}

			int cnt = 0;
			for (int amount = start_amount; amount < start_amount + 500; amount += 1) {
				int price_unit = unit * amount;
				int price_discount = discount * amount;
				int price_origin = price_unit - price_discount;
				double price_now = new BigDecimal(price_unit / 1000.0).setScale(0, RoundingMode.HALF_UP).doubleValue()
						- new BigDecimal(price_discount / 1000.0).setScale(0, RoundingMode.HALF_UP).doubleValue();
				double save = price_origin / 1000.0 - price_now;
				if (save >= want_save) {
					values.add(String.format(Locale.TAIWAN, "%.02f L 省 %.03f 元  價格為 %.0f 元", amount / 100.0, save, price_now));
					cnt++;
				}
			}
			if (cnt == 0) {
				values.add("查無結果，請降低至少省的金額");
			}
		}
		ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
		listView.setAdapter(adapter);
	}
}
