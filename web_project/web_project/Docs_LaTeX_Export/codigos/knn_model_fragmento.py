# FRAGMENTO de: python-api/models/knn.py (líneas 11-61)
# Implementación del clasificador K-Nearest Neighbors (KNN) con scikit-learn.

import numpy as np
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score
from utils.dataset_manager import load_dataset

def run_knn(dataset_name, k, new_point, session_dataset=None):
    # Cargar conjunto de datos
    if session_dataset is None:
        dataset = load_dataset("knn", dataset_name)
    else:
        dataset = session_dataset

    X = np.array(dataset["points"])
    y = np.array(dataset["labels"])

    # Inicializar y entrenar el modelo KNN
    model = KNeighborsClassifier(n_neighbors=k)
    model.fit(X, y)

    # Calcular exactitud del entrenamiento
    predictions = model.predict(X)
    accuracy = accuracy_score(y, predictions)

    # Predecir clase para un nuevo punto de consulta
    new_prediction = model.predict([new_point])[0]

    return {
        "model": "KNN",
        "dataset": dataset_name,
        "k": k,
        "accuracy": float(accuracy),
        "predicted_class": int(new_prediction)
    }
