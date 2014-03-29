package com.jcertif.android;

//cette classe pourra servir pour centraliser toutes les constantes et clés de l'application
public class Config {

	// TWITTER
	// Mes acces a l'api de twitter
	public static String TWITTER_CONSUMER_KEY = "e4NLMehoVS4qNgvDwjKl3A";
	public static String TWITTER_CONSUMER_SECRET = "RzxjpN5jmLAJvdeTCM2MTocyEHW4YmxHTkp04Rso";

	// Preference Constants for the token
	public static String PREFERENCE_JCERTIF_TWITTER = "jcertif_pref";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_twitter_token";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_twitter_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	public static final String TWITTER_CALLBACK_URL = "oauth://jcertif-app";

	// Twitter oauth urls
	public static final String URL_TWITTER_AUTH = "auth_url";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

}
