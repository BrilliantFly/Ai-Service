# -*- coding: utf-8 -*-
"""
Know Python API
FastAPI Application
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import ai, other

app = FastAPI(
    title="Know Python API",
    description="AI and Data Processing Services",
    version="1.0.0"
)

# CORS配置
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 注册路由
app.include_router(ai.router, prefix="/ai", tags=["AI"])
app.include_router(other.router, prefix="/other", tags=["Other"])


@app.get("/")
def root():
    return {"message": "Know Python API", "version": "1.0.0"}


@app.get("/health")
def health_check():
    return {"status": "healthy"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=5000)
