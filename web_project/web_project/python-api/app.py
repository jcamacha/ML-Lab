from fastapi import FastAPI

from routes.linear_routes import (
    router as linear_router
)

from routes.knn_routes import (
    router as knn_router
)

from routes.logistic_routes import (
    router as logistic_router
)

# =========================
# FASTAPI APP
# =========================

app = FastAPI()

# =========================
# ROUTES
# =========================

app.include_router(
    linear_router,
    prefix="/linear",
    tags=["Linear Regression"]
)

app.include_router(
    knn_router,
    prefix="/knn",
    tags=["KNN"]
)

app.include_router(
    logistic_router,
    prefix="/logistic",
    tags=["Logistic Regression"]
)