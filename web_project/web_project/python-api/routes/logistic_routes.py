from fastapi import APIRouter

from models.logistic import run_logistic

from schemas.logistic_schema import (
    LogisticRequest
)

# =========================
# ROUTER
# =========================

router = APIRouter()

# =========================
# RUN LOGISTIC
# =========================

@router.post("/run")

def logistic_endpoint(
    request: LogisticRequest
):

    result = run_logistic(
        dataset_name=request.dataset_name,
        new_point=request.new_point
    )

    return result