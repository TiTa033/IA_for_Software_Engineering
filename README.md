# Ridge-GWP AI Model - Carbon Footprint Prediction

FastAPI microservice for predicting packaging carbon footprint using Ridge regression.

## Quick Start

```bash
# Install dependencies
py -m pip install -r requirements.txt

# Run the service
py -m uvicorn main:app --host 0.0.0.0 --port 8000
```

Visit http://localhost:8000/docs for interactive API documentation.

## Model Details

- **Type**: Ridge Regression
- **Target**: GWP (Global Warming Potential in kg CO₂ equivalent)
- **Inputs**: weight (kg), length (cm), width (cm), height (cm), distance (km), material
- **Supported Materials**: cardboard, plastic, wood

## API Endpoints

### POST /predict
Predict GWP for packaging parameters.

**Request:**
```json
{
  "weight": 0.8,
  "length": 20,
  "width": 15,
  "height": 10,
  "distance_km": 450,
  "material": "cardboard"
}
```

**Response:**
```json
{
  "GWP": 3.694
}
```

### POST /optimize
Get optimal weight to minimize GWP.

**Request:** Same as `/predict`

**Response:**
```json
{
  "opti_weight": 150.0,
  "opti_GWP": 81.532
}
```

## Training the Model

1. Place your dataset as `cardboard-carbon-dataset.csv` in this directory
2. Run: `py train_gwp.py`
3. Model artifacts will be generated: `ridge_gwp.pkl` and `scaler.pkl`

## Testing

```bash
py -m pip install pytest
py -m pytest
```

## Docker

```bash
docker build -t ridge-gwp-service .
docker run -p 8000:8000 ridge-gwp-service
```

## Integration

This service is integrated into the main Angular + Spring Boot application:
- Spring Boot proxies requests at `/api/ridge-gwp/*`
- Angular frontend tests the model at http://localhost:4200/#/ridge-gwp

See the main project repository for full integration details.
