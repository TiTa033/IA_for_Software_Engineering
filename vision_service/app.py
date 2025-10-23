from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from PIL import Image
import io, os
import torch
from torchvision import models
from sklearn.neighbors import KNeighborsClassifier
import numpy as np

app = FastAPI(title="Material Vision API")

_backbone = models.mobilenet_v3_small(weights=models.MobileNet_V3_Small_Weights.DEFAULT)
_backbone.classifier = torch.nn.Identity()
_backbone.eval()
_preprocess = models.MobileNet_V3_Small_Weights.DEFAULT.transforms()

CLASSES, feats, labels = [], [], []

def _load_ref_images(root="ref_images"):
    CLASSES.clear(); feats.clear(); labels.clear()
    for cls in sorted(os.listdir(root)):
        d = os.path.join(root, cls)
        if not os.path.isdir(d): continue
        for f in os.listdir(d):
            if not f.lower().endswith((".jpg",".jpeg",".png")): continue
            try:
                img = Image.open(os.path.join(d, f)).convert("RGB")
                x = _preprocess(img).unsqueeze(0)
                with torch.no_grad():
                    z = _backbone(x).squeeze(0).numpy()
                feats.append(z); labels.append(cls)
            except Exception: pass
    if not feats: raise RuntimeError("Aucune image de référence.")
    CLASSES.extend(sorted(set(labels)))

_load_ref_images()
knn = KNeighborsClassifier(n_neighbors=min(3, len(set(labels))))
knn.fit(np.stack(feats), np.array(labels))

def _embed(img: Image.Image):
    x = _preprocess(img).unsqueeze(0)
    with torch.no_grad():
        return _backbone(x).squeeze(0).numpy()

@app.get("/health")
def health(): return {"status":"ok","classes":CLASSES,"num_refs":len(labels)}

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    try:
        img = Image.open(io.BytesIO(await file.read())).convert("RGB")
        z = _embed(img).reshape(1, -1)
        dist, idx = knn.kneighbors(z, n_neighbors=min(5, len(labels)), return_distance=True)
        score = {}
        for d, i in zip(dist[0], idx[0]):
            lab = labels[i]; score[lab] = score.get(lab,0.0) + 1.0/(d+1e-6)
        total = sum(score.values())
        probs = {k: float(v/total) for k,v in score.items()}
        top3 = sorted(probs.items(), key=lambda kv: kv[1], reverse=True)[:3]
        return {
            "top1": {"label": top3[0][0], "confidence": round(top3[0][1],3)},
            "top3": [{"label": l, "confidence": round(p,3)} for l,p in top3],
            "classes": CLASSES
        }
    except Exception as e:
        return JSONResponse({"error": str(e)}, status_code=400)

@app.post("/reload")
def reload_refs():
    _load_ref_images()
    knn.set_params(n_neighbors=min(3, len(set(labels))))
    knn.fit(np.stack(feats), np.array(labels))
    return {"reloaded": True, "classes": CLASSES, "num_refs": len(labels)}
