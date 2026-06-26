from fastapi import APIRouter

from models.knn import run_knn

from schemas.knn_schema import (
    KNNRequest
)

# =========================
# ROUTER
# =========================

router = APIRouter()

# =========================
# RUN KNN
# =========================

@router.post("/run")

def knn_endpoint(
    request: KNNRequest
):

    result = run_knn(
        dataset_name=request.dataset_name,
        k=request.k,
        new_point=request.new_point
    )

    return result