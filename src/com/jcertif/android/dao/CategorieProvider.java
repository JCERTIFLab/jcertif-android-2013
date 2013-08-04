package com.jcertif.android.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;

import com.jcertif.android.model.Category;

public class CategorieProvider extends JCertifDb4oHelper<Category>{

	public CategorieProvider(Context ctx) {
		super(ctx);
	}

	public String[] getLabels() {
				
		List<Category> cats=getAll(Category.class);
		ArrayList<String> mlist= new ArrayList<String>();
		for(int i=0; i<cats.size();i++){
			Category ci=cats.get(i);
			mlist.add(ci.getLabel());
		}
		String[] labels=new String[mlist.size()];
		labels= mlist.toArray(labels);
		
		return labels;
	}
	
}
