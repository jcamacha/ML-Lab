from fastapi import APIRouter

from models.linear import run_linear

from schemas.linear_schema import (
    LinearRequest
)

# =========================
# ROUTER
# =========================

router = APIRouter()

# =========================
# RUN LINEAR
# =========================

@router.post("/run")

def linear_endpoint(
    request: LinearRequest
):

    result = run_linear(
        dataset_name=request.dataset_name,
        predict_value=request.predict_value
    )

    return result