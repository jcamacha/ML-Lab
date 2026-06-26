import json
from pathlib import Path

# =========================
# BASE DIRECTORY
# =========================

BASE_DIR = Path(__file__).resolve().parent.parent

DATASETS_DIR = BASE_DIR / "datasets"

# =========================
# LOAD DATASET
# =========================

def load_dataset(model_type, dataset_name):

    dataset_path = (
        DATASETS_DIR /
        model_type /
        f"{dataset_name}.json"
    )

    with open(dataset_path, "r") as file:

        dataset = json.load(file)

    return dataset

# =========================
# SAVE DATASET
# =========================

def save_dataset(model_type, dataset_name, dataset):

    dataset_path = (
        DATASETS_DIR /
        model_type /
        f"{dataset_name}.json"
    )

    with open(dataset_path, "w") as file:

        json.dump(dataset, file, indent=4)

# =========================
# ADD POINT
# =========================

def add_point(
    model_type,
    dataset_name,
    point,
    label=None
):

    dataset = load_dataset(
        model_type,
        dataset_name
    )

    # LINEAR REGRESSION
    if model_type == "linear":

        dataset["x"].append(point[0])

        dataset["y"].append(point[1])

    # KNN / LOGISTIC
    else:

        dataset["points"].append(point)

        dataset["labels"].append(label)

    save_dataset(
        model_type,
        dataset_name,
        dataset
    )

    print("\nPoint added successfully!")

import copy

def clone_dataset(
    model_type,
    dataset_name
):

    dataset = load_dataset(
        model_type,
        dataset_name
    )

    return copy.deepcopy(dataset)

def add_point_temp(
    dataset,
    model_type,
    point,
    label=None
):

    if model_type == "linear":

        dataset["x"].append(point[0])

        dataset["y"].append(point[1])

    else:

        dataset["points"].append(point)

        dataset["labels"].append(label)

    return dataset