import numpy as np
import matplotlib.pyplot as plt

from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score

from utils.dataset_manager import (
    load_dataset
)

# =========================
# LOGISTIC REGRESSION
# =========================

def run_logistic(
    dataset_name,
    new_point,
    session_dataset=None
):

    # LOAD DATASET
    if session_dataset is None:

        dataset = load_dataset(
        "logistic",
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
    model = LogisticRegression()

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

    # PROBABILITY
    probability = model.predict_proba(
        [new_point]
    )[0][1]

    # VISUALIZATION
    plt.figure(figsize=(8,6))

    plt.scatter(
        X[:,0],
        X[:,1],
        c=y
    )

    # NEW POINT
    plt.scatter(
        new_point[0],
        new_point[1],
        marker="x",
        s=200
    )

    # DECISION BOUNDARY
    x_values = np.linspace(
        X[:,0].min(),
        X[:,0].max(),
        100
    )

    w1 = model.coef_[0][0]

    w2 = model.coef_[0][1]

    b = model.intercept_[0]

    y_values = (
        -(w1 * x_values + b) / w2
    )

    plt.plot(
        x_values,
        y_values
    )

    plt.title(
        f"Logistic Regression - {dataset_name}"
    )

    #plt.show()

    return {
        "model": "Logistic Regression",
        "dataset": dataset_name,
        "accuracy": float(accuracy),
        "predicted_class": int(
            new_prediction
        ),
        "probability": float(
            probability
        )
    }