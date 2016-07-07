package com.xiplus.oil_price;

import android.support.v7.app.ActionBarActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
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
public class MainActivity extends ActionBarActivity {

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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void run(View view) {
		ListView listView = (ListView) findViewById(R.id.result);
		ArrayList<String> values = new ArrayList<String>();
		boolean ok = true;
		double unit = 0;
		try {
			unit = Double.parseDouble(((EditText) findViewById(R.id.price)).getText().toString());
			if (unit <= 0) {
				values.add("油價不可為0");
				ok = false;
			}
		} catch (NullPointerException e) {
			values.add("油價錯誤");
			ok = false;
		} catch (NumberFormatException e) {
			values.add("未填寫油價");
			ok = false;
		}
		double discount = 0;
		try {
			discount = Double.parseDouble(((EditText) findViewById(R.id.discount)).getText().toString());
		} catch (NullPointerException e) {
		} catch (NumberFormatException e) {
		}
		double start_amount = 0;
		try {
			start_amount = Integer.parseInt(((EditText) findViewById(R.id.amount)).getText().toString());
		} catch (NullPointerException e) {
			values.add("數量錯誤");
			ok = false;
		} catch (NumberFormatException e) {
			values.add("未填寫數量");
			ok = false;
		}
		if (ok) {
			double want_save = 0;
			try {
				want_save = Double.parseDouble(((EditText) findViewById(R.id.save)).getText().toString());
			} catch (NullPointerException e) {
			} catch (NumberFormatException e) {
				if (discount == 0) {
					want_save = 0.4;
				} else {
					want_save = 0.8;
				}
			}
			double price_unit = unit * start_amount;
			double price_discount = discount * start_amount;
			unit /= 100;
			discount /= 100;
			Integer cnt = 0;
			for (double amount = start_amount; amount < start_amount + 5; amount += 0.01) {
				double price_origin = price_unit - price_discount;
				double price_now = new BigDecimal(price_unit).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue()
						- new BigDecimal(price_discount).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
				double save = price_origin - price_now;
				if (save >= want_save) {
					values.add(String.format("%.02f", amount) + " L 省" + String.format("%.03f", save) + "元  價格為" + price_now + "元");
					cnt++;
				}
				price_unit += unit;
				price_discount += discount;
			}
			if (cnt == 0) {
				values.add("查無結果，請降低至少省的金額");
			}
		}
		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, values);
		listView.setAdapter(adapter);
	}
}
