import org.json.simple.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;
    private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private JLabel weatherConditionImage;
    private JLabel temperatureText;
    private JLabel weatherConditionDesc;
    private JLabel maxTemperatureText;
    private JLabel minTemperatureText;
    private JLabel humidityImage;
    private JLabel humidityText;
    private JLabel windSpeedImage;
    private JLabel windSpeedText;

    public WeatherAppGui() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        addGuiComponents();
    }

    private void addGuiComponents() {
        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(450, 650));
        setContentPane(layeredPane);

        // Background image
        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 450, 650);
        backgroundLabel.setIcon(new ImageIcon("src/assets/clear_sky.gif")); // Directly use ImageIcon for GIF
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Foreground components
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        layeredPane.add(searchTextField, JLayeredPane.PALETTE_LAYER);

        weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png", false));
        weatherConditionImage.setBounds(0, 105, 450, 217);
        layeredPane.add(weatherConditionImage, JLayeredPane.PALETTE_LAYER);

        temperatureText = new JLabel("-- C");
        temperatureText.setBounds(0, 320, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(temperatureText, JLayeredPane.PALETTE_LAYER);

        weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 380, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(weatherConditionDesc, JLayeredPane.PALETTE_LAYER);

        maxTemperatureText = new JLabel("Max Temp: -- C");
        maxTemperatureText.setBounds(0, 425, 450, 36);
        maxTemperatureText.setFont(new Font("Dialog", Font.PLAIN, 24));
        maxTemperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(maxTemperatureText, JLayeredPane.PALETTE_LAYER);

        minTemperatureText = new JLabel("Min Temp: -- C");
        minTemperatureText.setBounds(0, 455, 450, 36);
        minTemperatureText.setFont(new Font("Dialog", Font.PLAIN, 24));
        minTemperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(minTemperatureText, JLayeredPane.PALETTE_LAYER);

        humidityImage = new JLabel(loadImage("src/assets/humidity.png", false));
        humidityImage.setBounds(15, 490, 74, 66);
        layeredPane.add(humidityImage, JLayeredPane.PALETTE_LAYER);

        humidityText = new JLabel("<html><b>Humidity</b> -- </html>");
        humidityText.setBounds(90, 510, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        layeredPane.add(humidityText, JLayeredPane.PALETTE_LAYER);

        windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png", false));
        windSpeedImage.setBounds(220, 510, 74, 66);
        layeredPane.add(windSpeedImage, JLayeredPane.PALETTE_LAYER);

        windSpeedText = new JLabel("<html><b>Windspeed</b> --km/h</html>");
        windSpeedText.setBounds(310, 510, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        layeredPane.add(windSpeedText, JLayeredPane.PALETTE_LAYER);

        JButton searchButton = new JButton(loadImage("src/assets/search.png", false));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);
                System.out.println(weatherData);

                String weatherCondition = (String) weatherData.get("weather_condition");
                if (weatherCondition != null) {
                    switch (weatherCondition) {
                        case "Clear":
                            weatherConditionImage.setIcon(loadImage("src/assets/clear.png", false));
                            backgroundLabel.setIcon(new ImageIcon("src/assets/clear_sky.gif")); // Use ImageIcon for GIF
                            break;
                        case "Cloudy":
                            weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png", false));
                            backgroundLabel.setIcon(new ImageIcon("src/assets/cloudy_animate.gif")); // Use ImageIcon for GIF
                            break;
                        case "Rain":
                            weatherConditionImage.setIcon(loadImage("src/assets/rain.png", false));
                            backgroundLabel.setIcon(new ImageIcon("src/assets/rainy_animate.gif")); // Use ImageIcon for GIF
                            break;
                        case "Snow":
                            weatherConditionImage.setIcon(loadImage("src/assets/snow.png", false));
                            backgroundLabel.setIcon(new ImageIcon("src/assets/snow_background.gif")); // Use ImageIcon for GIF
                            break;
                    }

                    double temperature = (double) weatherData.get("temperature");
                    temperatureText.setText(temperature + " C");

                    weatherConditionDesc.setText(weatherCondition);

                    long humidity = (long) weatherData.get("humidity");
                    humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                    double windspeed = (double) weatherData.get("windspeed");
                    windSpeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");

                    double maxTemperature = (double) weatherData.get("max_temperature");
                    maxTemperatureText.setText("Max Temp: " + maxTemperature + " C");

                    double minTemperature = (double) weatherData.get("min_temperature");
                    minTemperatureText.setText("Min Temp: " + minTemperature + " C");

                    // Repaint and revalidate to ensure animations play
                    backgroundLabel.revalidate();
                    backgroundLabel.repaint();
                } else {
                    System.out.println("weather_condition key not found in the JSON response");
                }
            }
        });
        layeredPane.add(searchButton, JLayeredPane.PALETTE_LAYER);
    }

    private ImageIcon loadImage(String resourcePath, boolean scaleToFrame) {
        try {
            File file = new File(resourcePath);
            if (!file.exists()) {
                System.out.println("File not found: " + file.getAbsolutePath());
                return null;
            }

            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                System.out.println("Could not read image: " + resourcePath);
                return null;
            }

            ImageIcon imageIcon = new ImageIcon(image);

            if (scaleToFrame) {
                // Resize the image to fit the JFrame 
                Image scaledImage = imageIcon.getImage().getScaledInstance(450, 650, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                return imageIcon;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read image: " + resourcePath);
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGui gui = new WeatherAppGui();
            gui.setVisible(true);
        });
    }
}
