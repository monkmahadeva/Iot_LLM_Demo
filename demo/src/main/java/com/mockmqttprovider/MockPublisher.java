package com.mockmqttprovider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

enum State {
    HIGH, LOW, BALANCED
}

public class MockPublisher {

    public static void main(String[] args) throws InterruptedException, IOException, MqttException {
        Path configPath = Paths.get("src/main/resources/config.properties");
        State[] states = State.values();
        int stateIndex = 0;
        int cycleCount = 0;
        State currentState = State.BALANCED;
        String broker = "tcp://localhost:1883";
        String topic = "iot/deviceA/temperature";

        State state = State.HIGH;

        long lastModified = Files.getLastModifiedTime(configPath).toMillis();

        while (true) {
            long currentModified = Files.getLastModifiedTime(configPath).toMillis();
            Properties config = new Properties();

            if (currentModified > lastModified) {
                config = loadConfig(configPath); // ✅ load using helper method
                System.out.println("Reloaded config from file.");
                lastModified = currentModified;
            } else {
                config = loadConfig(configPath); // still load even if not modified, for simplicity
            }

            boolean shouldRotate = Boolean.parseBoolean(config.getProperty("shouldRotate", "true"));
            State fixedState = State.valueOf(config.getProperty("fixedState", "BALANCED"));

            if (shouldRotate) {
                if (cycleCount % 3 == 0) {
                    currentState = states[stateIndex];
                    System.out.println("Switching to state: " + currentState);
                    stateIndex = (stateIndex + 1) % states.length;
                }
            } else {
                currentState = fixedState;
                System.out.println("Fixed state mode: " + currentState);
            }

            double temperature = 0;
            int humidity = 0;
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.connect();

            Random random = new Random();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            switch (currentState) {
                case HIGH -> {
                    temperature = 30 + random.nextDouble() * 5;
                    humidity = 25 + random.nextInt(15);
                }
                case LOW -> {
                    temperature = 15 + random.nextDouble() * 5;
                    humidity = 60 + random.nextInt(20);
                }
                case BALANCED -> {
                    temperature = 23.5;//21 + random.nextDouble() * 3;
                    humidity = 50;//45 + random.nextInt(11);
                }
            }

            String payload = String.format(
                "{\"deviceId\":\"deviceA\",\"temperature\":%.2f,\"humidity\":%d,\"timestamp\":\"%s\"}",
                    temperature, humidity, LocalDateTime.now().format(formatter)
            );

            MqttMessage message = new MqttMessage(payload.getBytes());
            client.publish(topic, message);

            System.out.printf("Published: %.2f°C, %d%% Humidity\n", temperature, humidity);
            cycleCount++;
            Thread.sleep(5000);
            }
            }  

            public static void StartFileWatcher(Consumer<ConfigParams> onChange) throws IOException, InterruptedException {
            Path configPath = Paths.get("src/main/resources/config.properties");
            if (!Files.exists(configPath)) {
                throw new FileNotFoundException("Config file not found: " + configPath.toAbsolutePath());
            }

            Properties config = new Properties();
            long lastModified = Files.getLastModifiedTime(configPath).toMillis();

            while (true) {
            long currentModified = Files.getLastModifiedTime(configPath).toMillis();
            if (currentModified > lastModified) {
                try (InputStream input = Files.newInputStream(configPath)) {
                    config.clear();
                    config.load(input);
                    lastModified = currentModified;

                    boolean shouldRotate = Boolean.parseBoolean(config.getProperty("shouldRotate", "true"));
                    State fixedState = State.valueOf(config.getProperty("fixedState", "BALANCED"));

                    ConfigParams params = new ConfigParams(shouldRotate, fixedState);
                    onChange.accept(params);
                } catch (Exception e) {
                    System.err.println("Failed to reload config: " + e.getMessage());
                }
            }
            Thread.sleep(5000); // re-check every 5s
        }
    } 

    private record ConfigParams(
        boolean shouldRotate,
        State fixedState
    ) {}

    private static Properties loadConfig(Path configPath) throws IOException {
    Properties config = new Properties();
    try (InputStream input = Files.newInputStream(configPath)) {
        config.load(input);
    }
    return config;
} 
}

