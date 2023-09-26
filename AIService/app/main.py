import datetime
from fastapi import FastAPI, Response
from .routers import SummarizationRouter

from pykospacing import Spacing

from transformers import PreTrainedTokenizerFast
from transformers import BartForConditionalGeneration


def create_app() -> FastAPI:
    app = FastAPI()
    init_setting(app)
    init_router(app)
    return app


def init_setting(app: FastAPI):
    @app.on_event("startup")
    def startup_event():
        pass

    @app.on_event("shutdown")
    def shutdown_event():
        pass

    @app.get("/")
    async def index():
        """ELB check"""
        current_time = datetime.utcnow().strftime("%Y.%m.%d %H:%M:%S")
        msg = f"Notification API (UTC: {current_time})"
        return Response(msg)


def init_router(app: FastAPI):
    app.include_router(SummarizationRouter)


app = create_app()
