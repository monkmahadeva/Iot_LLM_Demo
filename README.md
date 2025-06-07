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

2. Open `SmartIOTApp` in Android Studio. Let Gradle sync complete.

3. Run the app on device/emulator.

4. App will:
   - Subscribe to `iot/deviceA/temperature`
   - Show real-time chart
   - Tap “Ask AI” to fetch suggestions from LLaMA server

---

## 🧠 LLaMA Server Setup (Python)

1. Clone LLaMA backend repo (if any) or use mock server.

2. Run:
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   pip install -r requirements.txt
   python main.py  # or: uvicorn main:app --reload
   ```

3. Ensure endpoint:
   ```
   POST http://192.168.1.5:8000/v1/chat/completions
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

### Run:
```bash
cd demo/
javac Publisher.java
java Publisher
```

Publishes to topic `iot/deviceA/temperature`

Example message:
```json
{
  "deviceId": "deviceA",
  "temperature": 23.5,
  "humidity": 50,
  "timestamp": "2025-06-07T13:51:28"
}
```

---

## 🧪 Future Enhancements

- Dockerize LLM server
- BLE integration for real sensors
- Cloud sync with Firebase or DynamoDB
- AI recommendation feedback loop

---

## 📅 Last Updated

June 07, 2025

---

## 👤 Author

Made with ❤️ by [@monkmahadeva](https://github.com/monkmahadeva)
