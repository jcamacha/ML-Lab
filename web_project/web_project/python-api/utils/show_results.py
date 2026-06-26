import json
from pathlib import Path

# =========================
# SHOW RESULTS
# =========================

def show_results():

    BASE_DIR = Path(__file__).resolve().parent.parent

    results_path = BASE_DIR / "results" / "results.json"

    with open(results_path, "r") as file:

        results = json.load(file)

    print("\n=== EXPERIMENT HISTORY ===\n")

    if len(results) == 0:

        print("No experiments found.")

        return

    for i, result in enumerate(results, start=1):

        print(f"Experiment {i}")

        for key, value in result.items():

            print(f"{key}: {value}")

        print("\n-------------------\n")