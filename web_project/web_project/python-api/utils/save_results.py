import json
from pathlib import Path

# =========================
# SAVE FUNCTION
# =========================

def save_result(result):

    BASE_DIR = Path(__file__).resolve().parent.parent

    results_path = BASE_DIR / "results" / "results.json"

    # LOAD EXISTING RESULTS
    with open(results_path, "r") as file:

        results = json.load(file)

    # APPEND NEW RESULT
    results.append(result)

    # SAVE UPDATED RESULTS
    with open(results_path, "w") as file:

        json.dump(results, file, indent=4)

    print("\nResult saved successfully!")