package com.ingenico.epayment.transfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.ingenico.epayment.transfer.model.Account;

@Component
public class TestHelper {

	public JSONObject constructAccount(String name, BigDecimal balance) {
		JSONObject accountBody = new JSONObject();

		try {
			if (null != name) {
				accountBody.put("name", name);
			}
			accountBody.put("balance", balance);

			return accountBody;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public JSONObject constructTransfer(Long senderID, Long receiverID, BigDecimal amount) {
		JSONObject transferBody = new JSONObject();

		try {
			if (null != senderID && null != receiverID && null != amount) {
				transferBody.put("senderID", senderID);
				transferBody.put("receiverID", receiverID);
				transferBody.put("amount", amount);
			}
			return transferBody;
		} catch (JSONException e) {
			return null;
		}
	}

	public JSONArray constructMultipleAccount(List<Account> accounts) {
		JSONArray accountArray = new JSONArray();
		for (Account accountDTO : accounts) {
			JSONObject accountBody = new JSONObject();

			try {
				if (null != accountDTO.getName()) {
					accountBody.put("name", accountDTO.getName());
				}
				accountBody.put("balance", accountDTO.getBalance());
				accountArray.put(accountBody);
			} catch (JSONException e) {

			}
		}
		return accountArray;
	}

	public HttpEntity getPostRequestHeaders(String jsonPostBody) {
		List<MediaType> acceptTypes = new ArrayList<MediaType>();
		acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);

		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		reqHeaders.setAccept(acceptTypes);

		return new HttpEntity<String>(jsonPostBody, reqHeaders);
	}

	public HttpEntity getRequestHeaders() {
		List<MediaType> acceptTypes = new ArrayList<MediaType>();
		acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);

		HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		reqHeaders.setAccept(acceptTypes);

		return new HttpEntity<String>("parameters", reqHeaders);
	}

	public String contactUrlHelper(String resourceUrl, String resourceId) {
		return resourceUrl + "/" + resourceId;
	}
}
