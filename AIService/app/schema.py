from pydantic import BaseModel
from typing import Optional


class SummarizeIn(BaseModel):
    name: Optional[str]
    text: str
