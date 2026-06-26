# FRAGMENTO de: python-api/app.py (líneas 15-41)
# Servidor FastAPI principal que expone los microservicios de Machine Learning.

from fastapi import FastAPI
from routes.linear_routes import router as linear_router
from routes.knn_routes import router as knn_router
from routes.logistic_routes import router as logistic_router

app = FastAPI(title="ML Lab API", description="Servicios de Machine Learning para ML Lab")

# Registro de routers por algoritmo
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
