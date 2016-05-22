package com.bigcom.test.api;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bigcom.test.conf.Env;
import com.bigcom.test.pojo.Brand;
import com.bigcom.test.util.Utils;

public class TestApi extends Utils{
	int brandId = 0;

	@Test
	public void getAllBrands() throws Exception {
		try {
			log.info(S);
			String url = Env.HTTPS + Env.brands_url;
			HttpResponse response = execGet(url);
			JSONObject resp = getResponse(response);
			JSONArray brandObject = resp.getJSONObject("brands").getJSONArray("brand");
			AssertJUnit.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			AssertJUnit.assertEquals(1, resp.getJSONObject("brands").length());
			AssertJUnit.assertEquals(4, resp.getJSONObject("brands").getJSONArray("brand").length());
			AssertJUnit.assertEquals("X men brand", brandObject.getJSONObject(3).get("page_title"));
			AssertJUnit.assertEquals("Xmen", brandObject.getJSONObject(3).get("name"));
			AssertJUnit.assertEquals(38, brandObject.getJSONObject(3).getInt("id"));
			AssertJUnit.assertEquals("Sagaform", brandObject.getJSONObject(0).get("name"));
			AssertJUnit.assertEquals(35, brandObject.getJSONObject(0).getInt("id"));
			log.info(E);
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
			log.info(F);
			AssertJUnit.fail(e.getLocalizedMessage() + "\n" + e.getStackTrace());
		}
	}

	@Parameters({"brand_id"})
	@Test(dependsOnMethods = "getAllBrands")
	public void getOneBrand(@Optional("/38") String brand_id) throws Exception {
		try {
			log.info(S);
			String url = Env.HTTPS + Env.brands_url + brand_id;
			HttpResponse response = execGet(url);
			JSONObject resp = getResponse(response);
			AssertJUnit.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			AssertJUnit.assertEquals(38, resp.getJSONObject("brand").getInt("id"));
			AssertJUnit.assertEquals("X men brand", resp.getJSONObject("brand").getString("page_title"));
			AssertJUnit.assertEquals("Xmen", resp.getJSONObject("brand").getString("name"));
			log.info(E);
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
			log.info(F);
			AssertJUnit.fail(e.getLocalizedMessage() + "\n" + e.getStackTrace());
		}
	}

	@Test(dependsOnMethods = "getOneBrand")
	public void getBrandCount() throws Exception {
		try {
			log.info(S);
			String url = Env.HTTPS + Env.brands_url + Env.count_url;
			HttpResponse response = execGet(url);
			JSONObject resp = getResponse(response);
			AssertJUnit.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			AssertJUnit.assertEquals(4, resp.getJSONObject("brands").getInt("count"));
			log.info(E);
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
			log.info(F);
			AssertJUnit.fail(e.getLocalizedMessage() + "\n" + e.getStackTrace());
		}
	}

	@Parameters({"brandName", "brandPageTitle"})
	@Test(dependsOnMethods = "getBrandCount")
	public void createBrand(@Optional("Avengers") String brandName, @Optional("Marvel Comics Brand") String brandPageTitle) throws Exception {
		try {
			log.info(S);
			Brand newBrand = new Brand();
			newBrand.setName(brandName);
			newBrand.setPage_title(brandPageTitle);
			String body = objMap.writeValueAsString(newBrand);
			String url = Env.HTTPS + Env.brands_url;
			HttpResponse response = execPost(url, body);
			JSONObject resp = getResponse(response);
			brandId = resp.getJSONObject("brand").getInt("id");
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
			Assert.assertNotNull(resp.getJSONObject("brand").getInt("id"));
			Assert.assertEquals(newBrand.getPage_title(), resp.getJSONObject("brand").getString("page_title"));
			Assert.assertEquals(newBrand.getName(), resp.getJSONObject("brand").getString("name"));
			log.info(E);
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
			log.info(F);
			AssertJUnit.fail(e.getLocalizedMessage() + "\n" + e.getStackTrace());
		}
	}

	@Parameters({"updateBrandName", "updateBrandPageTitle"})
	@Test(dependsOnMethods = "createBrand")
	public void updateBrand(@Optional("Batman") String updateBrandName, @Optional("DC Comics Brand") String updateBrandPageTitle) throws Exception {
		try {
			log.info(S);
			Brand updateBrand = new Brand();
			updateBrand.setName(updateBrandName);
			updateBrand.setPage_title(updateBrandPageTitle);
			updateBrand.setId(brandId);
			String body = objMap.writeValueAsString(updateBrand);
			String url = Env.HTTPS + Env.brands_url + "/" + brandId;
			HttpResponse response = execPut(url, body);
			JSONObject resp = getResponse(response);
			Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
			Assert.assertEquals(updateBrand.getPage_title(), resp.getJSONObject("brand").getString("page_title"));
			Assert.assertEquals(updateBrand.getName(), resp.getJSONObject("brand").getString("name"));
			Assert.assertEquals(brandId, resp.getJSONObject("brand").getInt("id"));
			log.info(E);
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
			log.info(F);
			AssertJUnit.fail(e.getLocalizedMessage() + "\n" + e.getStackTrace());
		}
	}

	@Test(dependsOnMethods = "updateBrand")
	public void deleteBrand() throws Exception {
		try {
			log.info(S);
			String url = Env.HTTPS + Env.brands_url + "/" + brandId;
			HttpResponse response = execDelete(url);
			AssertJUnit.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT);
			log.info(E);
		} catch (Exception e) {
			log.info(e.getLocalizedMessage());
			e.printStackTrace();
			log.info(F);
			AssertJUnit.fail(e.getLocalizedMessage() + "\n" + e.getStackTrace());
		}
	}
}