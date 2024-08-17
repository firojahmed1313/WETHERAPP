package com.roj.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class wetherServlet
 */
public class wetherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public wetherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cityName = request.getParameter("city");
		System.out.println(cityName);
		String apiKey = "ec977ee89e477921191823c6fb86c842";
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
		System.out.println(apiUrl);
		//response.sendRedirect("index.jsp");
		//System.out.println("cityName");
		
		try {
			if(apiUrl.contains(" "))
				apiUrl = apiUrl.replace(" ", "%20");
			URL url = new URL(apiUrl);
			//System.out.println("responseContent1");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //System.out.println("responseContent2");
            InputStream inputStream = connection.getInputStream();
            //System.out.println("responseContent3");
            InputStreamReader reader = new InputStreamReader(inputStream);
            //System.out.println(reader);
            
            StringBuilder responseContent = new StringBuilder();
            
            Scanner scanner = new Scanner(reader);
            
            while (scanner.hasNext()) {
                responseContent.append(scanner.nextLine());
            }
            
            System.out.println(responseContent);
            scanner.close();
            
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
            //System.out.println(jsonObject);
            
            
          //Date & Time
            long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
            String date = new Date(dateTimestamp).toString();
            //tem
            double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            int temperatureCelsius = (int) (temperatureKelvin - 273.15);
           
            //Humidity
            int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
            
            //Wind Speed
            double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
            
            //Weather Condition
            String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").toString();
            
            
            request.setAttribute("date", date);
            request.setAttribute("city", cityName);
            request.setAttribute("temperature", temperatureCelsius);
            request.setAttribute("weatherCondition", weatherCondition); 
            request.setAttribute("humidity", humidity);    
            request.setAttribute("windSpeed", windSpeed);
            request.setAttribute("weatherData", responseContent.toString());
            
            
            connection.disconnect();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
