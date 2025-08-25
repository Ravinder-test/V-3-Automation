package com.propertymonitor.utils;

import org.json.JSONObject;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.zip.GZIPInputStream;
import com.github.luben.zstd.ZstdInputStream; // Zstd library required

public class TokenLoginUtil {

    public static void loginAndInject(WebDriver driver,
                                      String baseURL,
                                      String projectSearchUrl,
                                      String apiUrl,
                                      String email,
                                      String password,
                                      String deviceId,
                                      String captchaTestToken) {
        try {
            System.out.println("🔄 Starting token-based login process...");
            System.out.println("📍 Base URL: " + baseURL);
            System.out.println("📍 API URL: " + apiUrl);

            // Build request payload
            JSONObject payload = new JSONObject();
            payload.put("email", email);
            payload.put("password", password);
            payload.put("captcha", captchaTestToken);

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(15))
                    // Headers exactly as seen in DevTools
                    .header("accept", "application/json, text/plain, */*")
                    .header("accept-language", "en")
                    .header("content-type", "application/json")
                    .header("origin", baseURL)
                    .header("referer", baseURL + "/")
                    .header("x-device-id", deviceId)
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36")
                    .header("sec-ch-ua", "\"Not;A=Brand\";v=\"99\", \"Google Chrome\";v=\"139\", \"Chromium\";v=\"139\"")
                    .header("sec-ch-ua-platform", "\"macOS\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            System.out.println("📡 HTTP Status: " + response.statusCode());
            if (response.statusCode() != 200) {
                throw new RuntimeException("❌ Token fetch failed! Status: " + response.statusCode());
            }

            // Detect compression
            String contentEncoding = response.headers().firstValue("content-encoding").orElse("");
            byte[] responseBytes = response.body();
            String jsonString;

            if ("zstd".equalsIgnoreCase(contentEncoding)) {
                try (InputStream bais = new ByteArrayInputStream(responseBytes);
                     ZstdInputStream zstdIn = new ZstdInputStream(bais)) {
                    jsonString = new String(zstdIn.readAllBytes());
                }
            } else if ("gzip".equalsIgnoreCase(contentEncoding)) {
                try (InputStream bais = new ByteArrayInputStream(responseBytes);
                     GZIPInputStream gzipIn = new GZIPInputStream(bais)) {
                    jsonString = new String(gzipIn.readAllBytes());
                }
            } else {
                jsonString = new String(responseBytes);
            }

            System.out.println("📄 Raw Decompressed API Response:\n" + jsonString);

            // Parse JSON
            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONObject data = jsonResponse.getJSONObject("data");

            String authToken = data.optString("token", "");
            String refreshToken = data.optString("refresh_token", "");
            String userId = String.valueOf(data.getJSONObject("user").optInt("id", -1));

            if (authToken.isEmpty()) {
                throw new RuntimeException("❌ Missing 'token' in API response.");
            }

            System.out.println("🔑 Token: " + authToken);
            System.out.println("🔑 Refresh Token: " + refreshToken);

            // Open base URL
            driver.get(baseURL);

            // Inject into localStorage if available
            JavascriptExecutor js = (JavascriptExecutor) driver;
            boolean usesLocalStorage = (Boolean) js.executeScript("return typeof window.localStorage !== 'undefined'");
            if (usesLocalStorage) {
                js.executeScript("window.localStorage.setItem('auth_token', arguments[0]);", authToken);
                js.executeScript("window.localStorage.setItem('refresh_token', arguments[0]);", refreshToken);
                js.executeScript("window.localStorage.setItem('device_id', arguments[0]);", deviceId);
                js.executeScript("window.localStorage.setItem('user_id', arguments[0]);", userId);
                System.out.println("✅ Tokens injected into localStorage.");
            } else {
                driver.manage().addCookie(new Cookie("auth_token", authToken));
                driver.manage().addCookie(new Cookie("refresh_token", refreshToken));
                driver.manage().addCookie(new Cookie("device_id", deviceId));
                driver.manage().addCookie(new Cookie("user_id", userId));
            }
            Thread.sleep(2000);  // Wait before navigation
            driver.navigate().to(projectSearchUrl);
            System.out.println("✅ Token-based login completed successfully.");

        } catch (Exception e) {
            System.out.println("❌ Login via token failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Login via token failed: " + e.getMessage(), e);
        }
    }
}