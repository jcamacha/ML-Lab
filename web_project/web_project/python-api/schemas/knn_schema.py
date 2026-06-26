from pydantic import BaseModel

# =========================
# KNN REQUEST
# =========================

class KNNRequest(BaseModel):

    dataset_name: str

    k: int

    new_point: list