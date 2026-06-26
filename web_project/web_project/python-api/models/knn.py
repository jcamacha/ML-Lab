import numpy as np
import matplotlib.pyplot as plt

from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import accuracy_score

from utils.dataset_manager import (
    load_dataset
)

# =========================
# KNN
# =========================

def run_knn(
    dataset_name,
    k,
    new_point,
    session_dataset=None
):

    # LOAD DATASET
    if session_dataset is None:

        dataset = load_dataset(
        "knn",
        dataset_name
    )

    else:

        dataset = session_dataset

    X = np.array(
        dataset["points"]
    )

    y = np.array(
        dataset["labels"]
    )

    # MODEL
    model = KNeighborsClassifier(
        n_neighbors=k
    )

    model.fit(X, y)

    # TRAIN ACCURACY
    predictions = model.predict(X)

    accuracy = accuracy_score(
        y,
        predictions
    )

    # NEW POINT
    new_prediction = model.predict(
        [new_point]
    )[0]

    # VISUALIZATION
    plt.figure(figsize=(8,6))

    # EXISTING POINTS
    plt.scatter(
    X[:,0],
    X[:,1],
    c=y,
    cmap="viridis",
    s=100
)

    # NEW POINT
    plt.scatter(
    new_point[0],
    new_point[1],
    c="red",
    marker="x",
    s=250
)

    plt.title(
        f"KNN - {dataset_name}"
    )

    #plt.show()

    return {
        "model": "KNN",
        "dataset": dataset_name,
        "k": k,
        "accuracy": float(accuracy),
        "predicted_class": int(
            new_prediction
        )
    }