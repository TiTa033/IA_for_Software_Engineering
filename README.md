# 🌿 GWP Prediction Platform

### *AI-Powered Web Application for Sustainable Packaging Analysis*

---

## 📖 Overview

This project is a **full-stack web platform** designed to **predict the Global Warming Potential (GWP)** of olive oil packaging using **AI**.
It integrates three main modules:

* 🧠 **Python AI Service** — Predicts GWP using a trained XGBoost model.
* ⚙️ **Spring Boot Backend** — Acts as the core API layer connecting the AI model and the database.
* 💻 **Angular Frontend** — Provides an intuitive dashboard for users to input packaging data and visualize results.

---

## 🏗️ Project Architecture

```
project-root/
│
├── backend-spring/             # Spring Boot REST API
│   ├── src/main/java/com/gwp/
│   │   ├── controller/         # REST Controllers
│   │   ├── service/            # Business logic
│   │   ├── model/              # Entity classes
│   │   └── repository/         # JPA repositories
│   └── application.properties  # Backend configuration
│
├── frontend-angular/           # Angular web app
│   ├── src/app/
│   │   ├── components/         # UI components
│   │   ├── services/           # HTTP services
│   │   └── models/             # TypeScript interfaces
│   └── environments/           # API URLs
│
├── ai-python/                  # AI prediction module
│   ├── predict_gwp.py          # Script for model inference
│   ├── xgb_model.pkl           # Trained XGBoost model
│   └── requirements.txt        # Python dependencies
│
└── README.md                   # You are here 🚀
```

---

## 🧩 How It Works

1. The **Angular frontend** collects packaging data (material, capacity, color, etc.) from the user.
2. The **Spring Boot backend** receives this data and forwards it to the **Python script**.
3. The **Python module** loads the trained XGBoost model and predicts the GWP value.
4. The backend returns the prediction result to the frontend, where it is displayed in the UI.

---

## 🔧 Installation & Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/gwp-prediction-platform.git
cd gwp-prediction-platform
```

---

### 2️⃣ Backend (Spring Boot) Setup

#### 📦 Requirements

* Java 17+
* Maven 3.8+
* Python 3.11+
* XGBoost & Scikit-learn installed

#### ⚙️ Steps

```bash
cd backend-spring
mvn clean install
mvn spring-boot:run
```

This runs the backend on **[http://localhost:8080](http://localhost:8080)**

The backend will automatically call the Python script for prediction when receiving a POST request at:

```
POST /api/ai/predict
```

Example payload:

```json
{
  "Material": "Glass",
  "Capacity (mL)": 500,
  "Packaging Weight (g)": 300,
  "Average Cost ($)": 0.8,
  "Shelf Life": 24,
  "Shape": "Cylindrical",
  "wall Thickness (mm)": 2,
  "OTR (cc/m²/day/mm)": 0.1,
  "WVTR (g/m²/day/mm)": 0.2,
  "Pour Spout": true,
  "Shipping Cost per Unit ($)": 0.3,
  "Biodegradability": "No",
  "Reusable": "Yes",
  "Color": "Green"
}
```

---

### 3️⃣ AI Module (Python)

#### 🧠 Requirements

Create a virtual environment and install dependencies:

```bash
cd ai-python
python -m venv venv
venv\Scripts\activate   # (Windows)
# or source venv/bin/activate (Linux/Mac)

pip install -r requirements.txt
```

#### 📜 predict_gwp.py

This script:

* Loads the trained `xgb_model.pkl`
* Reads JSON input (passed via the Spring Boot backend)
* Returns a single GWP prediction to stdout

Example manual test:

```bash
python predict_gwp.py "{\"Material\":\"Glass\",\"Capacity (mL)\":500,...}"
```

---

### 4️⃣ Frontend (Angular)

#### ⚙️ Requirements

* Node.js 18+
* Angular CLI 17+

#### 🚀 Run the App

```bash
cd frontend-angular
npm install
ng serve
```

Runs on **[http://localhost:4200](http://localhost:4200)**

The frontend communicates with the backend via:

```
http://localhost:8080/api/ai/predict
```

---

## 📊 Example Workflow

1. User opens the Angular app.
2. Fills out packaging characteristics.
3. Clicks **“Predict GWP”**.
4. The result appears as a numeric value with color-coded sustainability indicators (low/medium/high).

---

## 🧠 AI Model Details

* **Algorithm:** XGBoost Regressor
* **Training Data:** Custom dataset of packaging features and their environmental impact.
* **Output:** Predicted Global Warming Potential (GWP) value (in kg CO₂ eq).
* **Features Used:**

  * Material, Capacity (mL), Packaging Weight (g), Average Cost ($), Shelf Life, Shape
  * wall Thickness (mm), OTR, WVTR, Pour Spout, Shipping Cost per Unit ($), Biodegradability, Reusable, Color

---

## 🧪 API Endpoints Summary

| Method | Endpoint          | Description                               |
| ------ | ----------------- | ----------------------------------------- |
| `POST` | `/api/ai/predict` | Send packaging data to get GWP prediction |
| `GET`  | `/api/ai/test`    | Test connection with Python AI module     |

---

## 🧱 Technologies Used

| Layer     | Technology                          |
| --------- | ----------------------------------- |
| Frontend  | Angular, TypeScript, HTML, CSS      |
| Backend   | Spring Boot (Java), REST APIs       |
| AI Module | Python, XGBoost, Scikit-learn, JSON |
| Tools     | Maven, npm, Git, IntelliJ, VSCode   |

---

## ⚡ Troubleshooting

* **`JSONDecodeError`** → Make sure Spring sends JSON with **double quotes** (`"key": "value"`)
* **Model Load Warning** → Re-save the model using `model.save_model('xgb_model.json')` then reload
* **Permission issues on Windows** → Ensure Python is added to PATH and `.py` files are executable

---

## 🧑‍💻 Author

**Taha Amine Ben Tita**
🎓 Software Architecture Engineering Student
📧 [tahaamine.bentita@esprit.tn](mailto:tahaamine.bentita@esprit.tn)

---

## 📜 License

This project is open-source and licensed under the **MIT License**.

---

## 💚 Future Improvements

* ✅ Add visual analytics (charts) for prediction history
* ✅ Include model retraining pipeline
* ✅ Dockerize backend + AI for easier deployment
* ✅ Deploy full stack to AWS / Render

---
