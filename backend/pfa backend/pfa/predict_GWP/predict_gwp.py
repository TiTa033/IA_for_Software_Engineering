import pickle
import sys
import json
import xgboost as xgb
import numpy as np
import traceback
from sklearn.preprocessing import LabelEncoder

def safe_float(value, default=0.0):
    """Helper function to safely convert values to float or return default."""
    if value == '' or value is None:
        return default
    try:
        return float(value)
    except ValueError:
        return default


try:
    # Load the model
    model = pickle.load(open('C:/predict_GWP/xgb_model.pkl', 'rb'))

    # Read and parse the input JSON
    input_json = sys.stdin.read()
    input_data = json.loads(input_json)

    # Log received JSON keys for debugging
    print("Received JSON keys:", list(input_data.keys()), file=sys.stderr)

    # Fallback to check for OTR and WVTR keys
    otr_key = next((k for k in input_data if 'OTR' in k), None)
    wvtr_key = next((k for k in input_data if 'WVTR' in k), None)

    if not otr_key or not wvtr_key:
        raise ValueError("OTR or WVTR keys not found in JSON input.")

    # Initialize label encoders for categorical features
    label_encoder_material = LabelEncoder()
    label_encoder_shape = LabelEncoder()
    label_encoder_color = LabelEncoder()

    # Fit label encoders (example values; in production, ensure you're using training data)
    label_encoder_material.fit(['plastic', 'metal', 'glass', 'aluminum'])
    label_encoder_shape.fit(['cylinder', 'square', 'round', 'other'])
    label_encoder_color.fit(['green', 'transparent', 'brown', 'dark', 'light'])

    # Encode categorical variables (handling potential errors if the value is unseen)
    encoded_material = label_encoder_material.transform([input_data['Material']])[0]
    encoded_shape = label_encoder_shape.transform([input_data['Shape']])[0]
    encoded_color = label_encoder_color.transform([input_data['Color']])[0]

    # Extract features and convert to NumPy array, ensure no object dtype
    features = np.array([[ 
        encoded_material,  # Encoded 'Material'
        safe_float(input_data.get('Capacity (mL)', '')),  # Handle empty or invalid values
        safe_float(input_data.get('Packaging Weight (g)', '')),
        safe_float(input_data.get('Average Cost ($)', '')),
        safe_float(input_data.get('Shelf Life', '')),
        encoded_shape,  # Encoded 'Shape'
        safe_float(input_data.get('wall Thickness (mm)', '')),
        safe_float(input_data.get(otr_key, ''), 0),  # Handle missing OTR value
        safe_float(input_data.get(wvtr_key, ''), 0),  # Handle missing WVTR value
        safe_float(input_data.get('Pour Spout', '')),
        safe_float(input_data.get('Shipping Cost per Unit ($)', '')),
        safe_float(input_data.get('Biodegradability', '')),
        safe_float(input_data.get('Reusable', '')),
        encoded_color,  # Encoded 'Color'
        input_data.get('link', None),  # Add 'link' feature (default to None if missing)
        safe_float(input_data.get('Water Use Range(m3/L)', '')),
        safe_float(input_data.get('Fossil Energy Use Range(MJ/L)', ''))
    ]], dtype=np.float32)  # Ensure float32 for compatibility with XGBoost

    # Make the prediction
    prediction = model.predict(features)
    print(prediction[0])

except Exception as e:
    print(f"Error: {e}")
    print(traceback.format_exc(), file=sys.stderr)

