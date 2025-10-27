# README — Analyse du niveau de satisfaction des utilisateurs (Sentiment Analysis)

## 1. Présentation

Ce projet automatise l'analyse du niveau de satisfaction des utilisateurs à partir de leurs commentaires textuels. Il combine un micro-service Python (NLP via Hugging Face), un backend Spring Boot et une interface Angular pour afficher les résultats en temps réel (code couleur + indicateurs graphiques).

### Objectifs

* Classifier les commentaires utilisateur en POSITIVE / NEGATIVE / NEUTRAL.
* Retourner un score de confiance pour chaque prédiction.
* Intégrer les résultats dans le backend Spring Boot via un endpoint dédié.
* Afficher les résultats côté frontend Angular avec visualisations simples.

## 2. Architecture

* **Frontend** : Angular — interface utilisateur, envoi des commentaires et affichage des résultats.
* **Backend** : Spring Boot — expose l’API métier et consomme le micro-service NLP via `/api/sentiment/analyze`.
* **Micro-service NLP** : Python (FastAPI ou Flask) — prétraitement, appel au modèle Hugging Face (`distilbert-base-uncased-finetuned-sst-2-english`) et renvoi de la prédiction.
* **Base de données (optionnel)** : stocker commentaires, résultats et métriques.

Schéma (texte) :

```
Angular (UI)  <-->  Spring Boot (API)  <-->  Micro-service Python (NLP - Hugging Face)
```

## 3. Technologies utilisées

* Python 3.8+ (FastAPI / Flask)
* Transformers (Hugging Face)
* Torch (ou cpu-only torch)
* Spring Boot (Java)
* Angular (TypeScript)
* Docker (optionnel)
* Postgres / MongoDB (facultatif pour persistance)

## 4. Installation & Exécution (local)

### 4.1 Micro-service Python (NLP)

1. Créer un environnement virtuel :

```bash
python -m venv venv
source venv/bin/activate   # macOS / Linux
venv\Scripts\activate      # Windows
```

2. Installer les dépendances (exemple `requirements.txt`) :

```text
fastapi
uvicorn[standard]
transformers
torch
pydantic
requests
```

```bash
pip install -r requirements.txt
```

3. Exemple simple d’application FastAPI (`app.py`) :

```python
from fastapi import FastAPI
from pydantic import BaseModel
from transformers import pipeline

app = FastAPI()
classifier = pipeline("sentiment-analysis", model="distilbert-base-uncased-finetuned-sst-2-english")

class RequestItem(BaseModel):
    text: str

@app.post("/analyze")
def analyze(payload: RequestItem):
    results = classifier(payload.text, truncation=True)
    # results e.g. [{'label':'POSITIVE','score':0.96}]
    return {"text": payload.text, "predictions": results}
```

4. Lancer le micro-service :

```bash
uvicorn app:app --host 0.0.0.0 --port 8000
```

### 4.2 Backend Spring Boot

* Configurer dans `application.yml` l’URL du micro-service (par ex. `http://localhost:8000/analyze`).
* Endpoint exposé côté backend : `/api/sentiment/analyze` (gateway vers micro-service).
* Exemple simple d’appel HTTP depuis Spring (RestTemplate ou WebClient) pour poster le texte au micro-service et récupérer la réponse.

### 4.3 Frontend Angular

* Composant envoi de texte et affiche résultat en temps réel.
* Appel vers `SpringBoot/api/sentiment/analyze`.
* Affichage : pastille couleur (ex : vert = POSITIVE, rouge = NEGATIVE, gris = NEUTRAL) + score et graphique (barre / jauge).

## 5. API — Contract & Exemple d’utilisation

### Micro-service Python

* **POST** `/analyze`

  * Body JSON : `{ "text": "Votre commentaire ici" }`
  * Réponse :

    ```json
    {
      "text": "J'adore la nouvelle interface, elle est très intuitive.",
      "predictions": [
        {"label":"POSITIVE", "score":0.96}
      ]
    }
    ```

### Backend Spring Boot (gateway)

* **POST** `/api/sentiment/analyze`

  * Body JSON : `{ "text": "..." }`
  * Réponse : structure similaire, éventuellement enrichie avec meta (timestamp, idCommentaire, userId).

### Exemple `curl`

```bash
curl -X POST "http://localhost:8000/analyze" \
 -H "Content-Type: application/json" \
 -d '{"text":"J’adore la nouvelle interface, elle est très intuitive."}'
```

## 6. Modèle & Prompts

* Modèle principal utilisé : `distilbert-base-uncased-finetuned-sst-2-english`.

  * Avantages : rapide, léger, bien adapté à la classification de sentiments sur des textes courts/longs.
* Précautions : ce modèle est entraîné en anglais — pour des commentaires en français, prévoir soit un modèle francophone, soit une étape de traduction (avec pertes possibles) ou fine-tuning.
* Exemple de prompts / usages :

  * Classification simple : `Analyse ce commentaire et indique s’il est POSITIF, NÉGATIF ou NEUTRE. Fournis un score.`
  * Analyse contextuelle : gérer sarcasme/ironie (limites — parfois difficile).
  * Multi-aspects : demander le sentiment par aspect (interface, support, performance).

## 7. Exemples de résultats

* `"J’adore la nouvelle interface, elle est très intuitive."` → `POSITIVE` (0.96)
* `"Super… encore un bug qui bloque tout."` → `NEGATIVE` (0.81)
* `"Le support est rapide mais l’export des rapports est compliqué."` → `NEUTRAL` (0.80) + résumé court.

## 8. Tests & Qualité

* Écrire des tests unitaires pour le micro-service (pytest) qui valident la structure de sortie (label + score).
* Tests d’intégration : simuler appels du backend vers le micro-service avec des fixtures.
* Jeu de données : préparer un jeu d’exemples (positif, négatif, neutre, sarcasme) pour évaluer précision.
* Monitoring : logs de latence, taux d’erreur et histogramme des scores pour détecter dérive (drift) du modèle.

## 9. Optimisations et bonnes pratiques

* **Batching** : regrouper les requêtes pour améliorer le throughput si nécessaire.
* **Caching** : mettre en cache des prédictions pour textes répétés.
* **Timeouts** : côté backend, définir un timeout raisonnable sur l’appel vers le micro-service.
* **Fallback** : en cas d’indisponibilité du micro-service, renvoyer une réponse neutre ou indiquer "non disponible".
* **Asynchrone** : pour textes longs, effectuer traitement asynchrone et notifier via websocket / polling.
* **Audit & GDPR** : si conservation de commentaires, anonymiser les données sensibles, respecter la règlementation.

## 10. Déploiement (rapide)

* Dockeriser l’application Python et le backend Spring Boot.
* Exemple `Dockerfile` pour le micro-service Python :

```dockerfile
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
```

* Orchestration : Kubernetes / Docker Compose pour déployer le trio (frontend, backend, nlp).

## 11. Limitations rencontrées (et solutions)

* Sarcasme / ambiguïtés → amélioration via prompts contextuels, multi-aspect et fine-tuning sur dataset spécifique.
* Latence sur textes longs → découpage, asynchronisme, hardware GPU si nécessaire.
* Modèle anglophone vs français → utiliser modèle francophone pré-entraîné (ex : camembert, fr-BERT) ou traduire avant prédiction.

## 12. Débogage & FAQ

* **Le modèle renvoie toujours POSITIVE/NEGATIVE mais pas NEUTRAL** : vérifier seuils et mapping des labels ; SST-2 n’a pas de label NEUTRAL natif — il peut être nécessaire d’ajouter un post-traitement (p.ex. score proche de 0.5 → NEUTRAL) ou d’utiliser un dataset / modèle tri-class.
* **Scores inattendus** : vérifier prétraitement (lowercase, removal d’URLs) et max token truncation.
* **Problème de compatibilité JSON avec Spring** : vérifier DTOs, noms de champs et content-type header.

## 13. Structure du repo (suggestion)

```
/backend-springboot
  /src
  pom.xml
/frontend-angular
  package.json
  src/
/nlp-service
  app.py
  requirements.txt
  Dockerfile
/infra
  docker-compose.yml
/README.md
```

## 14. Contribution

* Fork → branch feature → PR.
* Ajouter tests unitaires et d’intégration pour toute feature.
* Documenter tout nouveau modèle / changement de paramètre.

## 15. Licence & Contact

* Licence : MIT (ou autre selon ton choix).
* Contact : `wiem.errouissi@esprit.tn` (ou autre adresse projet).

---

### Annexes : Prompts et exemples (réutilisables)

* Prompt classification simple :

  > "Analyse ce commentaire et indique s’il est POSITIF, NÉGATIF ou NEUTRE. Fournis un score de confiance."
* Prompt multi-aspect :

  > "Pour ce commentaire, indique le sentiment par aspect : interface, support, performance."
