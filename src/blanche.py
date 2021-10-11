import wget
import os
import datasets
#file metrics import
#from FEQA.feqa import FEQA




#load all prerequisites for all metrics in library
def load(arg):
    metric=arg.lower()
    if metric == "rouge":
        print("Installing dependencies...")
        os.system("pip install rouge_score")
    if metric == "feqa":
        print("Downloading models for generating questions and answers...")
        p_model_url="https://drive.google.com/u/0/uc?export=download&confirm=Beop&id=1GFnimonLFgGal1LT6KRgMJZLbxmNJvxF"
        squad_url="https://drive.google.com/u/0/uc?export=download&confirm=8hcD&id=1pWMsSTTwcoX0l75bzNFjvSC7firawp9M"
        out_model="FEQA/bart_qg/best_pretrained.pt"
        out_squad="FEQA/qa_models/pytorch_model.bin"
        wget.download(p_model_url,out=out_model)
        gdown.download(squad_url,out=out_squad)
    if metric == "factcc":
        print("installing dependencies...")
        os.system("pip install -r factCC/requirements.txt")
        print("Downloading model for factual evaluation...")
        url="https://storage.googleapis.com/sfr-factcc-data-research/factcc-checkpoint.tar.gz"
        path="factCC/factcc-checkpoint.tar.gz"
        wget.download(url,out=path)
#evaluate metrics
def evaluate(arg, references=[], candidates=[],data_path=""):
    metric=arg.lower()
    if metric =="rouge":
        load("rouge")
        rouge=datasets.load_metric("rouge")
        results = rouge.compute(predictions=candidates, references=references)
        print(results["rouge2"])
    if metric == "factcc":
        load("factCC")
        MODEL_NAME="bert-base-uncased"
        TASK_NAME="factcc_annotated"
        script="python3 factCC/modeling/run.py --task_name " + TASK_NAME + " --do_predict --eval_all_checkpoints \
                --do_lower_case --overwrite_cache --max_seq_length 512 --per_gpu_train_batch_size 12 \
                --model_type bert --model_name_or_path " + MODEL_NAME + "--data_dir" + data_path + "--output_dir factCC/"
        os.system(script)
    if metric == "feqa":
        load("feqa")
        scorer = FEQA(use_gpu=False)
        scorer.compute_score(references, candidates, aggregate=False)


        






