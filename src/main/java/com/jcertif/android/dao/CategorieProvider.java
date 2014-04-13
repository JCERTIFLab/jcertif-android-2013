/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
