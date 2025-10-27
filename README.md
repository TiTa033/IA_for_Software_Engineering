# 🧠 Landsay

## 🌍 Description

**Landsay** is an AI-powered project designed to assess the **environmental impact** of olive oil packaging through advanced **machine learning** and **computer vision**.

In this project, a **machine learning model** using **XGBoost** predicts the **Global Warming Potential (GWP)** of olive oil packaging based on a variety of **material** and **environmental features**.  
The model is built and trained in **Python**, integrated into a **Spring Boot backend**, and deployed within a **web application** that displays existing packaging options and their sustainability characteristics.

The application also includes a **back-office interface** that allows administrators to:
- Add, update, and delete packaging data  
- Manage datasets dynamically  
- Provide a complete, interactive platform for sustainable packaging analysis  

### 🔍 Visual Material Recognition

One of the most important components of **Landsay** is its **visual recognition module**, which automatically identifies the **type of material** used in packaging (e.g., glass, plastic, aluminum, cardboard) from an **uploaded image**.  

This is achieved through a **computer vision model** (e.g., CNN or transfer learning via ResNet/VGG) trained on packaging images, enabling:
- Real-time detection and classification of packaging materials  
- Automatic feature extraction to enhance GWP prediction accuracy  
- Streamlined workflow between image analysis and environmental evaluation  

---

## ⚙️ Tech Stack

**Machine Learning & Computer Vision:**
- Python  
- XGBoost  
- TensorFlow / PyTorch (for visual recognition)  
- OpenCV  
- Pandas, NumPy, Scikit-learn  

**Backend:**
- Java / Spring Boot  
- RESTful API for communication with the ML model  

**Frontend:**
- Web application (HTML / CSS / JS or React/Angular)  
- Admin back-office for dataset management  

**Deployment:**
- ML model served as an API endpoint  
- Integrated into Spring Boot web service  
- Ready for deployment on Docker / cloud platform  

---

## 🚀 Installation & Setup

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/landsay.git
cd landsay
