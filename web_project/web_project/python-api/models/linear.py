import pandas as pd
import matplotlib.pyplot as plt

from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score

from utils.dataset_manager import (
    load_dataset
)

# =========================
# LINEAR REGRESSION
# =========================

def run_linear(
    dataset_name,
    predict_value,
    session_dataset=None
):

    # LOAD DATASET
    if session_dataset is None:

        dataset = load_dataset(
        "linear",
        dataset_name
    )

    else:

        dataset = session_dataset
    # CREATE DATAFRAME
    df = pd.DataFrame({
        "x": dataset["x"],
        "y": dataset["y"]
    })

    X = df[["x"]]

    y = df["y"]

    # MODEL
    model = LinearRegression()

    model.fit(X, y)

    # PREDICTIONS
    predictions = model.predict(X)

    # USER PREDICTION
    user_input = pd.DataFrame({
        "x": [predict_value]
    })

    user_prediction = model.predict(
        user_input
    )

    # METRICS
    mse = mean_squared_error(
        y,
        predictions
    )

    r2 = r2_score(
        y,
        predictions
    )

    # VISUALIZATION
    plt.figure(figsize=(8,6))

    plt.scatter(X, y)

    plt.plot(X, predictions)

    # USER POINT
    plt.scatter(
        predict_value,
        user_prediction[0]
    )

    plt.xlabel("X")

    plt.ylabel("Y")

    plt.title(
        f"Linear Regression - {dataset_name}"
    )

    #plt.show()

    return {
        "model": "Linear Regression",
        "dataset": dataset_name,
        "mse": float(mse),
        "r2": float(r2),
        "prediction": float(
            user_prediction[0]
        )
    }