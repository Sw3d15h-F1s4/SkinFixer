package dev.array21.skinfixer.apis;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import com.google.gson.Gson;

import dev.array21.skinfixer.SkinFixer;
import dev.array21.skinfixer.apis.gson.GetSkinResponse;
import dev.array21.skinfixer.apis.gson.GetUuidResponse;
import dev.array21.skinfixer.util.Triple;
import dev.array21.skinfixer.util.Utils;
import dev.array21.httplib.Http;
import dev.array21.httplib.Http.RequestMethod;
import dev.array21.httplib.Http.ResponseObject;

public class SkinFixerApi {
	
	public Triple<Boolean, String, String> getUuid(String playerName) {
		ResponseObject apiResponse;
		try {
			apiResponse = new Http().makeRequest(RequestMethod.GET, "https://skinfixer.k8s.array21.dev/player/" + playerName, null, null, null, null);
		} catch(IOException e) {
			SkinFixer.logWarn("An IOException occured while fetching a UUID from the SkinFixer API");
			SkinFixer.logWarn(Utils.getStackTrace(e));
			return new Triple<Boolean, String, String>(false, null, Utils.getStackTrace(e));
		}
		
		if(apiResponse.getResponseCode() != 200) {
			SkinFixer.logWarn("The SkinFixer API returned an unexpected result: " + apiResponse.getConnectionMessage());
			return new Triple<Boolean, String, String>(false, null, apiResponse.getConnectionMessage());
		}
		
		String uuid = new Gson().fromJson(apiResponse.getMessage(), GetUuidResponse.class).uuid;
		return new Triple<Boolean, String, String>(true, uuid, null);
	}
	
	public Triple<Boolean, GetSkinResponse, String> getSkin(String skinUrl, boolean slim) {
		String skinUrlBase64 = Base64.getEncoder().encodeToString(skinUrl.getBytes());
		
		ResponseObject apiResponse;
		try {
			apiResponse = new Http().makeRequest(RequestMethod.GET, "https://skinfixer.k8s.array21.dev/generate/url/" + skinUrlBase64, new HashMap<>(), null, null, null);
		} catch(IOException e) {
			SkinFixer.logWarn("An IOException occured while fetching a skin from the SkinFixer API");
			SkinFixer.logWarn(Utils.getStackTrace(e));
			return new Triple<Boolean, GetSkinResponse, String>(false, null, Utils.getStackTrace(e));
		}
		
		if(apiResponse.getResponseCode() != 200) {
			SkinFixer.logWarn("The SkinFixer API returned an unexpected result: " + apiResponse.getConnectionMessage());
			return new Triple<Boolean, GetSkinResponse, String>(false, null, apiResponse.getConnectionMessage());
		}
		
		final Gson gson = new Gson();
		return new Triple<Boolean, GetSkinResponse, String>(true, gson.fromJson(apiResponse.getMessage(), GetSkinResponse.class), null);
	}
	
	public Triple<Boolean, GetSkinResponse, String> getSkinOfPremiumPlayer(String uuid) {
		String uuidBase64 = Base64.getEncoder().encodeToString(uuid.getBytes());
		
		ResponseObject apiResponse;
		try {
			apiResponse = new Http().makeRequest(RequestMethod.GET, "https://skinfixer.k8s.array21.dev/generate/uuid/" + uuidBase64, new HashMap<>(), null, null, null);
		} catch(IOException e) {
			SkinFixer.logWarn("An IOException occured while fetching a skin from the SkinFixer API.");
			SkinFixer.logWarn(Utils.getStackTrace(e));
			return new Triple<Boolean, GetSkinResponse, String>(false, null, Utils.getStackTrace(e));
		}
		
		if(apiResponse.getResponseCode() != 200) {
			SkinFixer.logWarn("The SkinFixer API returned an unexpected result: " + apiResponse.getConnectionMessage());
			return new Triple<Boolean, GetSkinResponse, String>(false, null, apiResponse.getConnectionMessage());
		}
		
		final Gson gson = new Gson();
		return new Triple<Boolean, GetSkinResponse, String>(true, gson.fromJson(apiResponse.getMessage(), GetSkinResponse.class), null);
	}
}
