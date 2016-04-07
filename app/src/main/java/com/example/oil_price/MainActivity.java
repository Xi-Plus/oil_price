package com.example.oil_price;

import android.support.v7.app.ActionBarActivity;

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
		double amount = 0;
		try {
			amount = Integer.parseInt(((EditText) findViewById(R.id.amount)).getText().toString());
		} catch (NullPointerException e) {
			values.add("數量錯誤");
			ok = false;
		} catch (NumberFormatException e) {
			values.add("未填寫數量");
			ok = false;
		}
		if (ok) {
			double want_save = Double.parseDouble(((EditText) findViewById(R.id.save)).getText().toString());
			Integer cnt = 0;
			double price = unit * amount;
			unit /= 100;
			for (; cnt < 100; ) {
				double save = price - Math.floor(price);
				if (save < 0.5 && save >= want_save) {
					values.add(String.format("%.02f", amount) + " L 省" + String.format("%.03f", price - Math.floor(price)) + "元");
					cnt++;
				}
				price += unit;
				amount += 0.01;
			}
		}
		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, values);
		listView.setAdapter(adapter);
	}
}
