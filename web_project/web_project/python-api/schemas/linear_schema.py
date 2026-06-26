from pydantic import BaseModel

# =========================
# LINEAR REQUEST
# =========================

class LinearRequest(BaseModel):

    dataset_name: str

    predict_value: float