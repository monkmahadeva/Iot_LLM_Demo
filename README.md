# 🤖 IoT + LLM Demo (SmartIOTApp + Java Publisher)
(Think... Sense... Suggest... An IoT & AI fusion that reads your environment and talks back...)
A full-stack prototype demonstrating real-time sensor data collection from an Android app, MQTT-based publishing from a Java simulator, and AI-based suggestion using a local LLaMA server.

---

## 📦 Project Structure

```
Iot_LLM_Demo/
│
├── SmartIOTApp/                  # Android app using Clean Architecture (MVVM + Hilt)
│   ├── domain/                   # UseCases + Interfaces (pure Kotlin)
│   ├── data/                     # MQTT/Room/Retrofit implementations
│   ├── core/di/                  # Hilt modules
│   └── presentation/             # Sensor screen with real-time chart + AI button
│
├── demo/                         # Java-based Mock MQTT Publisher
│   └── Publisher.java
│
└── README.md                     # Project overview and setup instructions
```

---

## 📲 How to Run the Android App

### Prerequisites:
- Android Studio Hedgehog or newer
- JDK 17
- Android device or emulator on same Wi-Fi as local server

### Steps:

1. Clone the repo:
   ```bash
   git clone https://github.com/monkmahadeva/Iot_LLM_Demo.git
   ```

2. Import the Android Module SmartIotApp in android studio
3. Open terminal and follow the steps
   * cd demo
   * mvn clean install
   * mvn clean compile exec:java

4. Finish Lama Server Setup and mosquitto broker installed as a service  

5. Run the app on device/emulator.

6. App will:
   - Subscribe to `iot/deviceA/temperature`
   - Show real-time chart
   - Tap “Ask AI” to fetch suggestions from LLaMA server

---

## 🧠 LLaMA Server Setup (Python)

1. Clone LLaMA backend repo (if any) or use mock server.

2. Run:
   ```bash
   This project uses a local inference server running llama-cpp-python to simulate an OpenAI-style API for AI suggestions.

   📥 Step 1: Download the Model
   Go to HuggingFace: https://huggingface.co/

   Download the file: mistral-7b-instruct-v0.2.Q4_K_M.gguf
   python3 -m venv venv

 
   source venv/bin/activate        # Mac/Linux
   OR
   .\venv\Scripts\activate         # Windows


   pip install llama-cpp-python

   python -m llama_cpp.server --model "<PathToModelFile>\mistral-7b-instruct-v0.2.Q4_K_M.gguf" --host 0.0.0.0 --port 8000
   ```

3. Ensure endpoint:
   ```
   POST http://<IpOfMachine>:8000/v1/chat/completions
   ```

4. Android app sends:
   ```json
   {
     "model": "llama-7b",
     "messages": [
       { "role": "user", "content": "Recent temperature data..." }
     ]
   }
   ```

---

## 🛰️ Java Mock Publisher

Used for simulating MQTT sensor messages:

### Example payload:

Publishes to topic `iot/deviceA/temperature`

:
```json
{
  "deviceId": "deviceA",
  "temperature": 23.5,
  "humidity": 50,
  "timestamp": "2025-06-07T13:51:28"
}
```

---

## 🧪 Future Enhancements (Feel free to collaborate)

- Dockerize LLM server
- BLE integration for real sensors
- Adapt POC to multiple devices handling connections and disconnections
- AI recommendation feedback loop
- Add more prompts for LLama (Open AI)

---

## 📅 Last Updated

June 07, 2025

---

## 👤 Author

Made with ❤️ by [@monkmahadeva](https://github.com/monkmahadeva)
