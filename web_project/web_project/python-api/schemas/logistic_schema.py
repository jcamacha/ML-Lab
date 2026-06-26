from pydantic import BaseModel

# =========================
# LOGISTIC REQUEST
# =========================

class LogisticRequest(BaseModel):

    dataset_name: str

    new_point: list