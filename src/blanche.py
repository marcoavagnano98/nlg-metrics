import wget
import os
import gdown
#import datasets
import nltk
import zipfile
import importlib.util
import sys
#file metrics import
#
BLEURT_MODEL_PATH="bleurt/checkpoint/bleurt-base-128"




#load all prerequisites for all metrics in library
def load(arg):
   
    metric=arg.lower()
    if metric == "feqa":
        check_dependencies("FEQA/requirements.txt")
        path_m="FEQA/bart_qg/checkpoint_best.pt"
        path_pt="FEQA/qa_models/pytorch_model.bin"
        if os.path.isfile(path_m):
            print("Already downloaded")
        else:
            print("Downloading models for generate questions...")
            url="https://drive.google.com/u/0/uc?export=download&confirm=Beop&id=1GFnimonLFgGal1LT6KRgMJZLbxmNJvxF"
            gdown.download(url,path_m,quiet=False)
        if os.path.isfile(path_pt):
            print("Already downloaded")
        else:
            print("Downloading models for generate answers...")
            url="https://drive.google.com/u/0/uc?export=download&confirm=8hcD&id=1pWMsSTTwcoX0l75bzNFjvSC7firawp9M"
            gdown.download(url,path_pt,quiet=False)
    if metric == "factcc":
        check_dependencies("factCC/requirements.txt")
        path="factCC/factcc-checkpoint.tar.gz"
        if not os.path.exists(path):
            print("Downloading model for factual evaluation...")
            url="https://storage.googleapis.com/sfr-factcc-data-research/factcc-checkpoint.tar.gz"
            wget.download(url,out=path)
    if metric=="meteor":
        NLTK_VERSION = nltk.__version__
        nltk.download('wordnet')
        if NLTK_VERSION >= "3.6.4":
            nltk.download("punkt")
    if metric=="bleurt-base":
        check_dependencies("bleurt/requirements.txt")
        BLEURT_MODEL_PATH="bleurt/bleurt-base-128/"
        if not os.path.isdir(BLEURT_MODEL_PATH):
            url="https://storage.googleapis.com/bleurt-oss/bleurt-base-128.zip"
            path="bleurt/bleurt-base-128.zip"
            wget.download(url,out=path)
            with zipfile.ZipFile(path,'r') as zip_ref:
                zip_ref.extractall("bleurt/")
            os.remove(path)
    if metric == "bertscore":
        check_dependencies("bert_score/requirements.txt")
    if metric=="questeval":
        check_dependencies("questeval/requirements.txt")


def check_dependencies(r_file):
    print("Checking for dependencies...")
    import subprocess
    reqs = subprocess.check_output([sys.executable, '-m', 'pip', 'freeze'])
    installed_packages = [r.decode().split('==')[0] for r in reqs.split()]
    filed=open(r_file,"r")
    content=filed.read()
    dep_list=content.split("\n")
    filed.close()
    for dep in dep_list:
        if not dep in installed_packages:
            script="pip install " + dep
            os.system(script)
    print("Dependencies installed!")

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

    """
    Rouge metric
    citation:
    @inproceedings{lin-2004-rouge,
      title = "{ROUGE}: A Package for Automatic Evaluation of Summaries",
      author = "Lin, Chin-Yew",
      booktitle = "Text Summarization Branches Out",
      month = jul,
      year = "2004",
      address = "Barcelona, Spain",
      publisher = "Association for Computational Linguistics",
      url = "https://www.aclweb.org/anthology/W04-1013",
      pages = "74--81",
    }
    
    """
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


    """ 
    FEQA metric
    citation:
    @inproceedings{durmus-etal-2020-feqa,
      title = "{FEQA}: A Question Answering Evaluation Framework for Faithfulness Assessment in Abstractive Summarization",
      author = "Durmus, Esin  and He, He  and Diab, Mona",
      booktitle = "Proceedings of the 58th Annual Meeting of the Association for Computational Linguistics",
      month = jul,
      year = "2020",
      address = "Online",
      publisher = "Association for Computational Linguistics",
      url = "https://www.aclweb.org/anthology/2020.acl-main.454",
      doi = "10.18653/v1/2020.acl-main.454",
      pages = "5055--5070",
      abstract = "Neural abstractive summarization models are prone to generate content inconsistent with the source document, i.e. unfaithful. Existing automatic metrics do not capture such mistakes effectively. We tackle the problem of evaluating faithfulness of a generated summary given its source document. We first collected human annotations of faithfulness for outputs from numerous models on two datasets. We find that current models exhibit a trade-off between abstractiveness and faithfulness: outputs with less word overlap with the source document are more likely to be unfaithful. Next, we propose an automatic question answering (QA) based metric for faithfulness, FEQA, which leverages recent advances in reading comprehension. Given question-answer pairs generated from the summary, a QA model extracts answers from the document; non-matched answers indicate unfaithful information in the summary. Among metrics based on word overlap, embedding similarity, and learned language understanding models, our QA-based metric has significantly higher correlation with human faithfulness scores, especially on highly abstractive summaries.",
    }

    """
def run_feqa(references=[],candidates=[]):
    load("feqa")
    # import nltk
    
    # nltk.download('stopwords')
    # nltk.download('punkt')
    from FEQA.feqa import FEQA
    scorer = FEQA(use_gpu=False)
    scorer.compute_score(references, candidates, aggregate=False)

    """
    FactCC metric
    citation:
    @article{kryscinskiFactCC2019,
      author    = {Wojciech Kry{\'s}ci{\'n}ski and Bryan McCann and Caiming Xiong and Richard Socher},
      title     = {Evaluating the Factual Consistency of Abstractive Text Summarization},
      journal   = {arXiv preprint arXiv:1910.12840},
      year      = {2019},
    }
    """
def run_factCC(data_path):
    """
    Parameters
    ----------
    data_path: path of json file containing id,text,claim for data evaluation

    """
    load("factCC")
    MODEL_NAME="bert-base-uncased"
    TASK_NAME="factcc_annotated"
    script="python3 factCC/modeling/run.py --task_name " + TASK_NAME + " --do_predict --eval_all_checkpoints \
            --do_lower_case --overwrite_cache --max_seq_length 512 --per_gpu_train_batch_size 12 \
            --model_type bert --model_name_or_path " + MODEL_NAME + " --data_dir " + data_path + " --output_dir factCC/"
   # print(script)
    os.system(script)
    # import subprocess
    # subprocess.run(["python3", "factCC/modeling/run.py",
    #                "-task_name"+ TASK_NAME,"--do_predict",
    #                "--eval_all_checkpoints","--do_lower_case",
    #                "--overwrite_cache","--max_seq_length 512",
    #                "--per_gpu_train_batch_size 12","--model_type bert",
    #                "--model_name_or_path " + MODEL_NAME, "--data_dir" + data_path,
    #                "--output_dir factCC/"], capture_output=True)

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
    """
    Meteor metric
    citation:
    @inproceedings{banerjee2005meteor,
      title={METEOR: An automatic metric for MT evaluation with improved correlation with human judgments},
      author={Banerjee, Satanjeev and Lavie, Alon},
      booktitle={Proceedings of the acl workshop on intrinsic and extrinsic evaluation measures for machine translation and/or summarization},
      pages={65--72},
      year={2005}
    }
    """
def run_meteor(references=[],candidates=[],alpha=0.9,beta=3,gamma=0.5):
    """
    Parameters
    ----------
    alpha:
    beta:
    gamma:
    
    """
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
    """
    Bleurt metric
    citation:
    @misc{sellam2020bleurt,
      title={BLEURT: Learning Robust Metrics for Text Generation}, 
      author={Thibault Sellam and Dipanjan Das and Ankur P. Parikh},
      year={2020},
      eprint={2004.04696},
      archivePrefix={arXiv},
      primaryClass={cs.CL}
    }
    
    """
    
def run_bleurt(references=[],candidates=[],metric="bleurt-base"):
    """
    Parameters
    ----------
    metric: specify which type of model

    """
    #load(metric)
    #checkpoint="bleurt/checkpoint/bleurt-base-128.zip"
    import bleurt.score as bs
    scorer = bs.BleurtScorer(BLEURT_MODEL_PATH)
    scores = scorer.score(references=references, candidates=candidates)
    assert type(scores) == list and len(scores) == 1
    print(scores)
    """ 
    BARTScore metric
    citation:
    @misc{yuan2021bartscore,
      title={BARTScore: Evaluating Generated Text as Text Generation}, 
      author={Weizhe Yuan and Graham Neubig and Pengfei Liu},
      year={2021},
      eprint={2106.11520},
      archivePrefix={arXiv},
      primaryClass={cs.CL}
    }

    """
def run_BARTScore(references=[],candidates=[]):
    #load("bascore")
    import BARTScore.bart_score as bt
    bart_scorer= bt.BARTScorer(device='cpu', checkpoint='facebook/bart-large-cnn')
    score_list=bart_scorer.score(references, candidates)
    print(score_list)
    """
    BERTScore metric
    @misc{zhang2020bertscore,
      title={BERTScore: Evaluating Text Generation with BERT}, 
      author={Tianyi Zhang and Varsha Kishore and Felix Wu and Kilian Q. Weinberger and Yoav Artzi},
      year={2020},
      eprint={1904.09675},
      archivePrefix={arXiv},
      primaryClass={cs.CL}
    }

    """

def run_BERTScore(references=[],candidates=[]):
    load("bertscore")
    from bert_score import BERTScorer
    scorer = BERTScorer(lang="en", rescale_with_baseline=True)
    P, R, F1 = scorer.score(candidates, references)
    print(F1)


    """
    QUESTEval metric 
    citation:
    @article{scialom2020QuestEval,
      title={QuestEval: Summarization Asks for Fact-based Evaluation},
      author={Scialom, Thomas and Dray, Paul-Alexis and Gallinari Patrick and Lamprier Sylvain and Piwowarski Benjamin and Staiano Jacopo and Wang Alex},
      journal={arXiv preprint arXiv:2103.12693},
      year={2021}
    }
    """
def run_questeval(sources,candidates,ref=[],task="summarization",do_weighter=False):
    """
    Parameters
    ----------
    task: there is many type of task from questeval: summarization, data2text

    """
    load("questeval")
    from questeval.questeval_metric import QuestEval
    questeval = QuestEval(no_cuda=True,task=task,do_weighter=do_weighter)
    score = questeval.corpus_questeval(candidates,sources,ref)
    print(score)