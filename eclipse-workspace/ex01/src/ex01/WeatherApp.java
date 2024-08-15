package ex01;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherApp {
	//設定私有成員變數，用於 GUI 元件：一個框架、一個文本框、一個文本區域和一個按鈕。
    private JFrame frame;
    private JTextField cityTextField;
    private JTextArea resultTextArea;
    private JButton searchButton;
    //WeatherApp 的建構函數，用來初始化GUI
    public WeatherApp() {
    	//框架
        frame = new JFrame("天氣查詢應用");
        //文本框
        cityTextField = new JTextField();
        cityTextField.setBounds(10, 10, 200, 30);
        frame.add(cityTextField);
        //按鈕
        searchButton = new JButton("查詢天氣");
        searchButton.setBounds(220, 10, 100, 30);
        //處理按鈕點擊
        searchButton.addActionListener(new SearchButtonListener());
        frame.add(searchButton);
        //文本區域      
        resultTextArea = new JTextArea();
        resultTextArea.setBounds(10, 50, 310, 200);
        frame.add(resultTextArea);
        //設定框架的佈局管理器為 null
        frame.setLayout(null);
        //框架大小
        frame.setSize(350, 300);
        //關閉
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //顯示
        frame.setVisible(true);
    }
    //設定私有，實現 ActionListener 介面，用於處理按鈕點擊事件。
    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	//獲取用戶輸入的城市名稱
            String city = cityTextField.getText();
            //定義 API 密鑰
            String apiKey = "78c35360f51186c0c8099c0084b2a919";
            //構建 API 請求的 URL
            String apiUrl = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=zh_tw", city, apiKey);
            try {
            	//建立 URL 物件，表示 API 請求的 URL
                URL url = new URL(apiUrl);
                //打開一個 HTTP 連接，並將其轉換為 HttpURLConnection 類型
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //設定 HTTP 請求方法為 GET
                connection.setRequestMethod("GET");
                //獲取伺服器的回應碼，如果回應碼為 200
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                	
                	//創建 BufferedReader 來讀取伺服器的回應
                	//使用 connection.getInputStream() 獲取伺服器的回應輸入流，並將其包裝在 InputStreamReader 中，再包裝在 BufferedReader 中
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    //定義變數 line 用於存儲每次讀取的行
                    String line;
                    //創建 StringBuilder 來存儲完整的伺服器回應
                    StringBuilder response = new StringBuilder();
                    //使用 BufferedReader 的 readLine 方法逐行讀取伺服器的回應，直到返回 null（表示沒有更多的行）
                    //每次讀取的行都追加到 StringBuilder 中
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    //將伺服器的回應轉換為 JSON 物件
                    JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                    //從 JSON 物件中提取天氣描述和溫度資訊
                    String weatherDescription = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                    double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
                    //將城市名稱、天氣描述和溫度顯示在文本區域中
                    resultTextArea.setText(String.format("城市: %s\n天氣: %s\n溫度: %.2f°C", city, weatherDescription, temperature));
                } else {
                    resultTextArea.setText("查詢失敗：伺服器返回錯誤代碼：" + responseCode);
                }
            } catch (Exception ex) {
                resultTextArea.setText("查詢失敗：" + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new WeatherApp();
    }
}
