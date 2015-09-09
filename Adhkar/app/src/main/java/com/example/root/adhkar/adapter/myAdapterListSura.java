package com.example.root.adhkar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.root.adhkar.R;
import com.example.root.adhkar.classobject.Quran;

import java.util.ArrayList;

public class myAdapterListSura extends BaseAdapter {

	private ArrayList<Quran> qurans;
	private LayoutInflater myInflater;
	
	public myAdapterListSura(Context context, ArrayList<Quran> _produits)
	{
		this.myInflater = LayoutInflater.from(context);
		this.qurans = _produits;
	}
	
	@Override
	public int getCount() {
		return this.qurans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return this.qurans.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		TextView nbresura;
		TextView nbreayeh;
		TextView namesura;
		TextView pagestrat;
		TextView makki;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = myInflater.inflate(R.layout.listitem, null);
			holder = new ViewHolder();
			holder.makki = (TextView) convertView.findViewById(R.id.makki);
			holder.nbresura = (TextView) convertView.findViewById(R.id.num);
			holder.pagestrat = (TextView) convertView.findViewById(R.id.pagestar);
			holder.nbreayeh = (TextView) convertView.findViewById(R.id.nbreayeh);
			holder.namesura = (TextView) convertView.findViewById(R.id.namesura);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nbreayeh.setText(qurans.get(position).getNbreAyeh() + "");
		holder.makki.setText(qurans.get(position).getMakki() + "");
		holder.pagestrat.setText(qurans.get(position).getPagestart()+ "");
		holder.namesura.setText(qurans.get(position).getNamesura()+"");
		holder.nbresura.setText(qurans.get(position).getNbresura()+"");

		return convertView;
		
	}

}
