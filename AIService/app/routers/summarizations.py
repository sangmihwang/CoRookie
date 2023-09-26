from fastapi import APIRouter, logger
from transformers import PreTrainedTokenizerFast, BartForConditionalGeneration
import torch
import time
from ..schema import SummarizeIn

router = APIRouter(
    prefix="/summarizations",
    tags=["summarizations"],
    responses={404: {"description": "Not found"}},
)

global tokenizer, model

tokenizer = PreTrainedTokenizerFast.from_pretrained("models/kobart-summarization/")
model = BartForConditionalGeneration.from_pretrained("models/kobart-summarization/")


@router.post("/")
async def summarize_text(request: SummarizeIn):
    start = time.time()

    text = request.text.replace("\n", " ")
    text = text.replace('"', "")
    summarized_text = ""
    
    raw_input_ids = tokenizer.encode(text)
    for i in range((int)(len(raw_input_ids)/512)+1):
        if((i+1)==(int)(len(raw_input_ids)/512)+1):
            summarized_text += summary(raw_input_ids[i*512 : ])
        else:
            summarized_text += summary(raw_input_ids[i*512 : (i+1)*512])


    return {
        "project_num": 1,
        "video_num": 1,
        "summarization_text": summarized_text,
        "model_duration": f"{time.time()-start:.4f} sec",
    }

def summary(raw_input_id):
    input_ids = [tokenizer.bos_token_id] + raw_input_id + [tokenizer.eos_token_id]

    summary_ids = model.generate(
            torch.tensor([input_ids]), num_beams=4, max_length=512, eos_token_id=1
        )
    
    return tokenizer.decode(summary_ids.squeeze().tolist(), skip_special_tokens=True)