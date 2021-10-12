import wget
import os
#import datasets
import nltk
import zipfile
#file metrics import
#from FEQA.feqa import FEQA
BLEURT_MODEL_PATH="bleurt/checkpoint/bleurt-base-128"




#load all prerequisites for all metrics in library
def load(arg):
    metric=arg.lower()
    #if metric == "rouge":
     #   print("Installing dependencies...")
      #  os.system("pip install -r rouge/requirements.txt")
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
    if metric=="meteor":
        NLTK_VERSION = nltk.__version__
        nltk.download('wordnet')
        if NLTK_VERSION >= "3.6.4":
            nltk.download("punkt")
    if metric=="bleurt-base":
        os.system("pip install -r bleurt/requirements.txt")
        url="https://storage.googleapis.com/bleurt-oss/bleurt-base-128.zip"
        path="bleurt/bleurt-base-128.zip"

        wget.download(url,out=path)
        with zipfile.ZipFile(path,'r') as zip_ref:
            zip_ref.extractall("bleurt/")
        os.remove(path)
        BLEURT_MODEL_PATH="bleurt/bleurt-base-128"

#run metrics
def run_bleu(references=[], candidates=[]):
    import bleu.bleu_metric as bl
    results=bl.compute_bleu(references,candidates)
    (bleu, precisions, bp, ratio, translation_length, reference_length) =results
    print(bleu)


# coding=utf-8
# Copyright 2020 The HuggingFace Datasets Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
def run_rouge(rouge_types,references=[], candidates=[],stemmer_enable=False,use_aggregator=True):
    import rouge.rouge_scorer as rg
    import rouge.scoring
    #load("rouge")
    rouge_types = rouge_types
    scorer=rg.RougeScorer(rouge_types=rouge_types,use_stemmer=stemmer_enable)
    if use_aggregator:
        aggregator = rouge.scoring.BootstrapAggregator()
    else:
        scores=[]
    for ref, pred in zip(references, candidates):
        score = scorer.score(ref, pred)
        if use_aggregator:
            aggregator.add_scores(score)
        else:
            scores.append(score)

    if use_aggregator:
        result = aggregator.aggregate()
    else:
        result = {}
        for key in scores[0]:
            result[key] = list(score[key] for score in scores)

    return result



def run_feqa(references=[],candidates=[]):
    load("feqa")
    scorer = FEQA(use_gpu=False)
    scorer.compute_score(references, candidates, aggregate=False)

def run_factCC(data_path):
    load("factCC")
    MODEL_NAME="bert-base-uncased"
    TASK_NAME="factcc_annotated"
    script="python3 factCC/modeling/run.py --task_name " + TASK_NAME + " --do_predict --eval_all_checkpoints \
            --do_lower_case --overwrite_cache --max_seq_length 512 --per_gpu_train_batch_size 12 \
            --model_type bert --model_name_or_path " + MODEL_NAME + "--data_dir" + data_path + "--output_dir factCC/"

# coding=utf-8
# Copyright 2020 The HuggingFace Datasets Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
def run_meteor(references=[],candidates=[],alpha=0.9,beta=3,gamma=0.5):
    load("meteor")
    import numpy as np
    from nltk.translate import meteor_score  
    NLTK_VERSION = nltk.__version__
    if NLTK_VERSION >= "3.6.4":    
        from nltk import word_tokenize  
    if NLTK_VERSION >= "3.6.4":
        scores = [
            meteor_score.single_meteor_score(
                word_tokenize(ref), word_tokenize(pred), alpha=alpha, beta=beta, gamma=gamma
            )
            for ref, pred in zip(references, candidates)
        ]
    else:
        scores = [
            meteor_score.single_meteor_score(ref, pred, alpha=alpha, beta=beta, gamma=gamma)
            for ref, pred in zip(references, candidates)
        ]

    return {"meteor": np.mean(scores)}
    
def run_bleurt(references=[],candidates=[],metric="bleurt-base"):
    load(metric)
    #checkpoint="bleurt/checkpoint/bleurt-base-128.zip"
    import bleurt.score as bs
    scorer = bs.BleurtScorer(BLEURT_MODEL_PATH)
    scores = scorer.score(references=references, candidates=candidates)
    assert type(scores) == list and len(scores) == 1
    print(scores)
    





