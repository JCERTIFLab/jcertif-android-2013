package com.jcertif.android.dao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Query;
import com.jcertif.android.model.Contributor;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Speaker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Patrick Bashizi
 * 
 * @param <T>
 */
public abstract  class JCertifDb4oHelper<T> {

	private static ObjectContainer oc = null;
	private Context context;

	/**
	 * @param ctx
	 */

	public JCertifDb4oHelper(Context ctx) {
		context = ctx;

	}

	/**
	 * Create, open and close the database
	 */
	protected ObjectContainer db() {
		if (oc == null || oc.ext().isClosed()) {
			try {

				oc = new OpendDBTask().execute().get();

			} catch (InterruptedException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			} catch (ExecutionException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			}
		}
		return oc;
	}

	/**
	 * 
	 *
	 */
	class OpendDBTask extends AsyncTask<Void, Void, ObjectContainer> {

		@Override
		protected ObjectContainer doInBackground(Void... params) {
			try {

				oc = Db4oEmbedded.openFile(dbConfig(), db4oDBFullPath(context));

			} catch (DatabaseFileLockedException ie) {
				Log.e(JCertifDb4oHelper.class.getName(), ie.toString());

			} catch (Db4oIOException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			} catch (IncompatibleFileFormatException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			} catch (OldFormatException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			} catch (DatabaseReadOnlyException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			} catch (IOException e) {
				Log.e(JCertifDb4oHelper.class.getName(), e.toString());
			}

			return oc;
		}
	}

	/**
	 * Configure the behavior of the database
	 */

	private EmbeddedConfiguration dbConfig() throws IOException {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		configuration.common().objectClass(Session.class).objectField("id")
				.indexed(true);
		configuration.common().objectClass(Contributor.class).objectField("id")
		.indexed(true);
		configuration.common().objectClass(Session.class).cascadeOnUpdate(true);
		configuration.common().objectClass(Speaker.class).objectField("email")
				.indexed(true);
		
		configuration.common().objectClass(Speaker.class).cascadeOnUpdate(true);
		return configuration;
	}

	/**
	 * Returns the path for the database location
	 */

	private String db4oDBFullPath(Context ctx) {
		return ctx.getDir("data", 0) + "/" + "jcertif.db4o";
	}

	/**
	 * Generic operations
	 */

	public void store(T object) {
		db().store(object);
	}

	public T get(T prototype) {
		T result = null;
		ObjectSet<T> objSet = db().queryByExample(prototype);
		if (objSet.hasNext()) {
			result = objSet.next();
		}
		return result;
	}

	/**
	 * Get from T where fieldName==constraint
	 * 
	 * @param c
	 * @param fieldName
	 * @param constraint
	 * @return
	 */
	public T get(Class<T> c, String fieldName, String constraint) {
		Query q = db().query();
		q.constrain(c);
		q.descend(fieldName).constrain(constraint);
		ObjectSet<T> result = q.execute();
		if (result.hasNext()) {
			return result.next();
		}
		return null;
	}

	public List<T> getAll(Class<T> type) {
		return db().queryByExample(type);
	}

	public void deleteAll(Class<T> c) {
		ObjectSet<T> set = db().queryByExample(c);
		while (set.hasNext()) {
			db().delete(set.next());
		}
	}

	public void delete(T prototype) {
		T object = get(prototype);
		if (object != null) {
			db().delete(object);
		}
	}

	public void delete(Class<T> c, String fieldName, String constraint) {
		T object = get(c, fieldName, constraint);
		if (object != null) {
			db().delete(object);
		}
	}

	public int size(Class<T> type) {
		return getAll(type).size();
	}

	public void deleteDatabase() {
		// We dont actually delete the database we just wipe all objects in it
		ObjectSet<Object> results = db().query(Object.class);
		if ((results != null) && (results.size() > 0)) {
			for (Object o : results) {
				db().delete(o);
			}
			close();
			Toast.makeText(this.context, "Finished Deleting Database",
					Toast.LENGTH_SHORT).show();

		}
	}

/*	*//**
	 * Check if database is empty
	 *//*
	public boolean isDatabaseEmpy() {

	}
*/
	/**
	 * Closes the database
	 */

	public void close() {
		if (oc != null)
			oc.close();
	}
}
