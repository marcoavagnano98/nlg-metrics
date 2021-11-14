import wget
import os
import gdown
#import datasets
import nltk
import zipfile
import importlib.util
import sys
import pathlib
import utils
import numpy as np
#file metrics import
#
from vars import *

def load(arg):
    """
    Parameters
    ----------
    arg: metric to load
    """
    metric=arg.lower()
    if metric == "feqa":
        check_dependencies("FEQA/requirements.txt")
        path_m="FEQA/bart_qg/checkpoints/checkpoint_best.pt"
        path_pt="FEQA/qa_models/squad1.0/pytorch_model.bin"
        if not os.path.isfile(path_m):
            print("Downloading models for generate questions...")
            url="https://drive.google.com/u/0/uc?export=download&confirm=Beop&id=1GFnimonLFgGal1LT6KRgMJZLbxmNJvxF"
            download_and_extract(url,path_m,unpackable=False, from_drive=True)
        if not os.path.isfile(path_pt):
            print("Downloading models for generate answers...")
            url="https://drive.google.com/u/0/uc?export=download&confirm=8hcD&id=1pWMsSTTwcoX0l75bzNFjvSC7firawp9M"
            download_and_extract(url,path_pt,unpackable=False, from_drive=True)
    if metric == "factcc":
        check_dependencies("factCC/requirements.txt")
        path="factCC/factcc-checkpoint.tar.gz"
        if not os.path.isdir(FACTCC_CHECKPOINT):
            print("Downloading model for factual evaluation...")
            url="https://storage.googleapis.com/sfr-factcc-data-research/factcc-checkpoint.tar.gz"
            download_and_extract(url,path,folder="factCC/")
    if metric=="meteor":
        NLTK_VERSION = nltk.__version__
        nltk.download('wordnet')
        if NLTK_VERSION >= "3.6.4":
            nltk.download("punkt")
    if metric=="bleurt-base":
        check_dependencies("bleurt/requirements.txt")
        if not os.path.isdir(BLEURT_MODEL_PATH):
            url="https://storage.googleapis.com/bleurt-oss/bleurt-base-128.zip"
            path="bleurt/bleurt-base-128.zip"
            download_and_extract(url,path,folder="bleurt/")
    if metric == "bertscore":
        check_dependencies("bert_score/requirements.txt")
    if metric=="questeval":
        check_dependencies("questeval/requirements.txt")
    if metric=='nubia':
        check_dependencies("nubia_score/requirements.txt")

def download_and_extract(url,storage_path,folder="./",unpackable=True,from_drive=False):
    """
    Parameters
    ---------ì
    url: file to download
    storage_path: path to save compressed file
    folder: folder to extract tar/zip
    unpackable: if True extract file
    from_drive: file downloaded from drive
    """
    import tarfile
    filename, file_extension = os.path.splitext(storage_path)
    
    if from_drive:
        gdown.download(url,storage_path,quiet=False)
    else:
        wget.download(url,out=storage_path)
    if unpackable:
        print("Extracting compressed model...")
        if file_extension == '.zip':
            with zipfile.ZipFile(storage_path,'r') as zip_ref:
                zip_ref.extractall(folder)
        else: 
            t_model=tarfile.open(storage_path)
            t_model.extractall(folder)
            t_model.close()
        os.remove(storage_path)



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

    """
    Bleu metric
    citation:
    @inproceedings{papineni-etal-2002-bleu,
      title = "{B}leu: a Method for Automatic Evaluation of Machine Translation",
      author = "Papineni, Kishore  and Roukos, Salim  and Ward, Todd  and Zhu, Wei-Jing",
      booktitle = "Proceedings of the 40th Annual Meeting of the Association for Computational Linguistics",
      month = jul,
      year = "2002",
      address = "Philadelphia, Pennsylvania, USA",
      publisher = "Association for Computational Linguistics",
      url = "https://aclanthology.org/P02-1040",
      doi = "10.3115/1073083.1073135",
      pages = "311--318",
    }

    description:
    Bleu is a metric born in 2002 to evaluate translation generate texts.
    It's an n-gram overlap algorithm between candidate phrase and set of references.
    Scores are calculated by counting each n-gram which occur both in candidate and references translations,then divides by the total number of words in
    the candidate translation.
    This type of computation is called precision.
    Bleu adopts a modified precision to prevent the high score given to the over-generation of "reasonable" words.
    It's calculated for individual translated segments—generally sentences—by.
    To extends this precison score to all corpus Bleu adds the n-gram counts for all the candidate sentences and divide by the number of candidate n-grams in the test corpus.
   
    task: summarization
    
    relavants aspects captured: adequancy and fluency

    """
def run_bleu(references=[], candidates=[]):
    import bleu.bleu_metric as bl
    if not candidates:
        candidates=utils.load_preds("bart_labels.txt")
    if not references:
        references=utils.load_preds("bart_preds.txt")
    ref_tokens=[]
    for ref in references:
        ref_tokens.append([ref.split()])
    pred_tokens=[]
    for pred in candidates:
        pred_tokens.append(pred.split())
# prefect match
    print(pred_tokens[0])
    with open("bleu/results.txt","a") as f:
        for r,p in zip(ref_tokens,pred_tokens):
            results=bl.compute_bleu(reference_corpus=r,translation_corpus=p)
            (bleu, precisions, bp, ratio, translation_length, reference_length) = results
            f.write(str(bleu))
            f.write("\n")

    return bleu
def test_bleu():
    refs=utils.load_preds("bart_labels.txt")
    cands=utils.load_preds("bart_preds.txt")
    from nltk.translate.bleu_score import sentence_bleu
    with open("bleu/results.txt","a") as f:
        for r,c in zip(refs,cands):
            f.write(str(sentence_bleu([r.split()],c.split(),weights=(1, 0, 0, 0))) + '\n')

   # return sentence_bleu("data/targets_egv_paper.txt","data/preds_egv_paper.txt")
    # 
    #
    #     for cand,ref in zip(cands,refs):
    #         bleu=list_bleu([ref], cand)
    #         f.write(str(bleu))
    #         f.write("\n")

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

    description:
    Rouge-N(N stands for n-gram length) is a n-gram overlap metric recall based which counts the maximum number of n-gram co-occuring 
    in candidate summary and a set of reference summaries.
    Unlike Bleu, the score is calculated dividing all co-occurence by number of reference n-gram.
    ROUGE-L(Longest Common Subsequence): LCS-based Fmeasure to estimate the similarity between two summaries (X,Y), where precision and recall are determined by dividing the longest
    subsequence in common LCS(X,Y) with length of candidate summary(precision) and reference summary(recall).

    task: summarization
    relavants aspects captured: adequancy and fluency

    Copyright 2020 The HuggingFace Datasets Authors.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    
    """
def run_rouge(references=[], candidates=[], rouge_types=["rougeL"], stemmer_enable=False, use_aggregator=True):
    """
    Parameters
    ----------
    references: list of references for each candidate
    candidates: list of texts to evaluate
    rouge_types: rouge1,rouge2, rougeL
    stemmer_enable: Bool indicating whether Porter stemmer should be used to strip word suffixes
    use_aggregator: return aggregate scores if True
    """
    import rouge.rouge_scorer as rg
    import rouge.scoring
    #load("rouge")
    method=0
    rouge_types = rouge_types
    if not candidates:
        candidates=utils.load_preds("bart_preds.txt")
    if not references:
        references=utils.load_preds("bart_labels.txt")
    scorer=rg.RougeScorer(rouge_types=rouge_types,use_stemmer=stemmer_enable)
    if use_aggregator:
        aggregator = rouge.scoring.BootstrapAggregator()
    else:
        scores=[]
    # if method == 3:
    #     counter=0
    #     app_list=[]
    #     scores=[]
    #     for ref, pred in zip(references, candidates):
    #         score=scorer.score(ref, pred)
    #         app_list.append(score["rougeL"].fmeasure)
    #         counter+=1
    #         if counter % 4 == 0 and not counter ==0:
    #             scores.append(utils.max_score(app_list,counter))
    #     import numpy as np
    #     print(np.mean(scores))
    #else:

    for ref, pred in zip(references, candidates):
        score = scorer.score(ref, pred)
        with open("rouge/bartL.txt","a") as f:
            f.write(str(score["rougeL"].fmeasure) + "\n")
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
    if use_aggregator:
        for rouge_type in rouge_types:
            score=result[rouge_type].mid.fmeasure
            print("[" + rouge_type + "] = " + str(score))

    # else:
    #     print(result)
    
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
    description:
    Given question-answer pairs generated from the summary, a QA model extracts answers from the document; non-matched answers indicate unfaithful information in the summary.
    BART model, fine-tuned on mask gold answers, is used to generate question from the summary sentences
    BERT-base instead fine-tuned on SQuAD-1.1 and SQuAD-2.0, generates answers from previous questions.
    The score is an F1 between genereted answers from the summary and those from the source text.

    task: question answering
    relevants aspects captured: faithfulness, factuality

    """
def run_feqa(references,candidates):
    
    """ 
    Parameters
    ----------
    references: list of sources text for candidate
    candidates: list of texts to evaluate


    """
    load("feqa")
    feqa_downloads()
    from FEQA.feqa import FEQA
    scorer = FEQA(use_gpu=False)
    score=scorer.compute_score(references, candidates, aggregate=False)

    return score
def feqa_downloads():
    import benepar
    nltk.download('punkt')
    benepar.download('benepar_en2')
    nltk.download('stopwords')
    os.system("python -m spacy download en_core_web_sm")

    """
    FactCC metric
    citation:
    @article{kryscinskiFactCC2019,
      author    = {Wojciech Kry{\'s}ci{\'n}ski and Bryan McCann and Caiming Xiong and Richard Socher},
      title     = {Evaluating the Factual Consistency of Abstractive Text Summarization},
      journal   = {arXiv preprint arXiv:1910.12840},
      year      = {2019},
    }
    description:
    FactCC is one of the new metric model-based for factual evaluation of generated text.
    The authors propose using an artificial, weaklysupervised dataset for the task at hand which is made up of semantically and non-semantically altered claims for training BERT model.
    This type of perturbations help the model to distinguish non-factual claims.
    The source document and claim sentence were fed as input to the model and the two-way classification (CONSISTENT/INCONSISTENT) was done using a single-layer classifier based on the [CLS] token.
    
    relevants aspects captured: factuality
    """
def run_factCC(list):
    """
    Parameters
    ----------
    data_path: path of json file containing id,text,claim for data evaluation

    """
   # load("factCC")
    utils.jsonl_dumper('factcc',list) #create jsonl for factCC script
    MODEL_NAME="bert-base-uncased"
    TASK_NAME="factcc_annotated"
    script="python3 factCC/modeling/run.py --task_name " + TASK_NAME + " --do_eval --eval_all_checkpoints \
            --do_lower_case --overwrite_cache --max_seq_length 512 --per_gpu_train_batch_size 12 \
            --model_type bert --model_name_or_path " + MODEL_NAME + " --data_dir factCC/ --output_dir " + FACTCC_CHECKPOINT
   # print(script)
    os.system(script)



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
    description:
    Meteor was designed to explicitly address the weakness weaknesses of BLEU.
    It computes a score based on explicit word-to-word matches between the translation and a reference translation, in case of more references the best score is reported.
    Meteor defines an alignment between unigram,an unigram can only mapped with one unigram in the other string.
    IN the first phase a module lists all possible alignment between two string, then there are three type of module to map unigram.
    Exact module, exact match between unigram, porter stem  maps two unigrams if they are the same after they are stemmed using the Porter stemmer, wordnet synonymy maps two
    unigrams if they are synonyms of each other.
    Precision: is computed as the ratio of the number of unigrams in the system translation that are mapped
    Recall: is computed as the ratio of the number of unigrams in the system translation that are mapped
    Fmean: 10PR/(R+9PR)
    All the unigrams in the system translation that are mapped to unigrams in the reference translation are grouped into the fewest possible number of chunks, unigram in each chunk must be adiacent.
    The longer the n-grams, the fewer the chunks.
    Meteor computes a penalty as follow: 0.5*(#chunks/#unig_matches)^3
    Score = Fmean * (1 - Penalty)

    Copyright 2020 The HuggingFace Datasets Authors.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0

    """

def run_meteor(references=[],candidates=[],alpha=0.9,beta=3,gamma=0.5,method=1):
    """
    Parameters
    ----------
    alpha: Parameter for controlling relative weights of precision and recall. default: 0.9
    beta: Parameter for controlling shape of penalty as a function of fragmentation. default 3
    gamma: Relative weight assigned to fragmentation penalty. default 0.5
    
    Return
    ------
    Arithmetic mean of scores computed
    """
    load("meteor")
    import numpy as np
    from nltk.translate import meteor_score  
    NLTK_VERSION = nltk.__version__
    if NLTK_VERSION >= "3.6.4":    
        from nltk import word_tokenize  
    if not candidates:
        candidates=utils.load_preds("bart_preds.txt")
    if not references:
        references=utils.load_preds("bart_labels.txt")
    if NLTK_VERSION >= "3.6.4":
        scores_list=[]
        scores=[]
        if method==3:
            counter=0
            for ref, pred in zip(references, candidates):
          
                scores_list.append(meteor_score.single_meteor_score(
                                      word_tokenize(ref), word_tokenize(pred), alpha=alpha, beta=beta, gamma=gamma
                                   )
                )
                counter+=1
                if(counter % 4 == 0 and not counter==0):
                    scores.append(utils.max_score(scores_list,counter))
        else:
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
    # with open("meteor/bart.txt","a") as f:
    #     for element in scores:
    #         f.write(str(element))
    #         f.write("\n")
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
    
    description:
    BLEURT is a text-generation metric based on BERT which is pre-trained on artificial dataset made up of random perturbated Wikipedia sentences augmented with a diverse set of lexical and semantic-level supervision signals.
    Then BERT is fine-tuned on WMT Metric Shared Task.

    task: summarization, machine translation
    """
    
def run_bleurt(references=[],candidates=[],f_preds="preds.txt",h_labels="labels.txt",metric="bleurt-20"):
    """
    Parameters
    ----------
    references: list of sources text for candidate
    candidates: list of texts to evaluate
    metric: specify which type of model is used for evaluation

    """
    load(metric)
    #checkpoint="bleurt/checkpoint/bleurt-base-128.zip"
    if not candidates:
        candidates=utils.load_preds(f_preds)
    if not references:
        references=utils.load_preds(h_labels)
    import bleurt.score as bs
    import numpy as np
    scorer = bs.BleurtScorer(BLEURT_MODEL_PATH)
    #i=0
    all_scores=[]
    # with open("bleurt/bart.txt", "a") as f:
    for i in range(0,len(references)):
        scores = scorer.score(references=[references[i]], candidates=[candidates[i]])
        all_scores.append(scores)
        # f.write(str(scores))
        # f.write("\n")
        assert type(scores) == list and len(scores) == 1
        print(i)
    print(np.mean(all_scores))
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
    task: summarization, machine translation
    description:
    BARTScore argue for a formulation of evaluation of generated text as a text generation problem, directly evaluating text through the lens of its probability of being generated
    from or generating other textual inputs and outputs.
    This metric uses a pre-trained seq2seq model BART.
    There are no extra parameters beyond those used in pre-training itself, and it is an unsupervised metric that doesn’t require human judgments to train.
    BARTSCORE can better support evaluation of generated text from different perspectives (informativeness, coherence, factuality) by adjusting the input and the output of the text generation problem.
    BARTSCORE can be further enhanced by (i) providing textual prompts that bring the evaluation task closer to the pre-training task, orpdating the underlying model by fine-tuning BART based on downstream generation task.
    Faithfulness (s → h): from source document to hypothesis p(h|s, θ)
    Precision (r → h): from reference text to system-generated text p(h|r, θ)
    Recall (h → r): from system-generated text to reference text p(r|h, θ)
    F score (r ↔ h): Consider both directions and use the arithmetic average of Precision and Recall ones

    """
def run_BARTScore(references=[],candidates=[], device='cpu'):
    #load("bascore")
    import BARTScore.bart_score as bt
    import numpy as np
    if not candidates:
        candidates=utils.load_preds("preds_egv_paper.txt")
    if not references:
        references=utils.load_preds("targets_egv_paper.txt")
    bart_scorer= bt.BARTScorer(device=device, checkpoint='facebook/bart-large-cnn')
    score_list=np.mean(bart_scorer.score(references, candidates))
    
    # scores=norm(score_list)
    # print(scores)
    print(np.mean(score_list))
    return score_list
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
    description:
    Given a reference sentence x = <hx1, . . . , xki> and a candidate sentence xˆ = <hxˆ1, . . . , xˆli>, BERTScore uses a contextual embeddings to represent the tokens, and compute matching using cosine similarity
    The representation for each word piece is computed with a Transformer encoder by BERT model
    FBert: 2 * PBert * RBert / (PBert + RBert) 
    """
# def softmax(x):
#     import numpy as np
#     y = np.exp(x - np.max(x))
#     f_x = y / np.sum(np.exp(x))
#     return f_x    
# def norm(x):
#     import numpy as np
#     normalized_v = x/np.linalg.norm(x)
#     return 1+normalized_v 
def run_BERTScore(references=[],candidates=[]):
    load("bertscore")
    import bert_score
    if not candidates and not references:
        candidates=utils.load_preds("preds_egv_paper.txt")
        references=utils.load_preds("targets_egv_paper.txt")
    #scorer = BERTScorer(lang="en", rescale_with_baseline=True)
    P, R, F1 = bert_score.score(candidates,references,lang="en",rescale_with_baseline=True,verbose=True)
    # with open("results.txt","a") as fl:
    #     for f in F1:
    #         fl.write(str(f))
    #         fl.write("\n")
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
    description:
    QUESTEval is a framework for evaluating summarization system composed of QG and QA components.
    Question Answering: T5 model extracts answer from source document given document and a question to answer QA(r|T, q).
    It is crucial for the QA model to be able to predict when a question is unanswerable, QA component thus includes the unanswerable token (e).
    Question Generation: T5 model fine-tune to likelihood human questions.
    QUESTEval considers all the named entities and nouns from the source document as answers and qustions are generated via beam search
    A summary is deemed inconsistent with respect to its source text if, given a question, the answer differs when conditioned on summary or document.
    An answer could be expresses in some way, but F1 score consider only overlap from predicted answer and the corrisponding ground truth
    So QUESTEval uses QA confident of answerability (1- QA(e)) in this way the answerability is misured indipendently of the way the answer is expressed.
    Query weighter distinguish important question from anecdotal ones: W(q, D) denotes the probability that question is important for the document, given a source document
    D, each question q ∈ QG(D) is labeled as important if the corresponding human summary contains the answer.
   
    Precision: F1 for each answer generate from document and gold answer divided by number of pair question-answer set genereted by summary
    Recall: (W(q, D)(1 − QA(e|S, q)))/W(q, D) for each pairs answer-question in source document
    Score: 2*Prec*Rec/(Prec + Rec)

    relevants aspects captured: factuality, text relevance
    """
def run_questeval(references=[],candidates=[],sources=[],
                    f_preds="preds.txt",f_labels="labels.txt",task='text2text',do_weighter=False,no_cuda=True):
    """
    Parameters
    ----------
    references: list of references for each hypotesis
    candidates: list of claims to be evaluated
    sources: list of source documents for summarization task
    task: there is many type of task from questeval: summarization, text2text, data2text
    no_cuda: True use cpu, False use gpu, if available
    do_weighter: weight for summarization task

    """
    load("questeval")
    if not candidates and not references:
        candidates=utils.load_preds(f_preds)
        references=utils.load_preds(f_labels)
    from questeval.questeval_metric import QuestEval
    questeval = QuestEval(task=task, do_weighter=do_weighter,no_cuda=no_cuda)
    score = questeval.corpus_questeval(hypothesis=candidates,list_references=[references])
    return score

  
def file_mean(f_name="nubia/scores.txt",method=1):
    import numpy as np
    import utils
    with open(f_name,"r") as f:
        s_scores=f.readlines()
    f_list=[]
    for s_score in s_scores:
        f_list.append(float(s_score))

    if method == 1:
        return np.mean(f_list)

    if method==2:
        max_logs=[]
        for n in range(3,len(f_list),4):
            max_logs.append(float(f_list[n]))
        print(len(max_logs))
        return np.mean(max_logs)
    if method == 3:
        max_logs=[]
        l_size=len(f_list)
        for n in range(0,len(f_list)):
                if((n % 4 == 0 and not n==0) or n == (l_size-1)):
                    if n == (l_size - 1):
                        max_logs.append(utils.max_score(f_list,l_size))
                    else:
                        max_logs.append(utils.max_score(f_list,n))
        return np.mean(max_logs)



    
    # # with open(PATH_F, "r") as f:
    # #     lines=f.readlines()
    # max=0
    # for line in lines:
    #     groups = groupby(line)
    #     result = [(label, sum(1 for _ in group)) for label, group in groups]
    #     for r in result:
    #         if(r[0] == ']'):
    #            if max < r[1]:
    #                max=r[1]
    # print(max)
    # # for line in lines:
    #     line.count(']')

def run_nubia(ref=[], cands=[], _range=[]):
    load("nubia")
    #check_dependencies("nubia/requirements.txt")
    if not ref:
        ref=utils.load_preds("targets_egv_paper.txt")
    if not cands:
        cands=utils.load_preds("preds_egv_paper.txt")
    assert len(ref) == len(cands)
    scores=[]
    import nubia_score as ns
    nubia = ns.Nubia()
    if not _range:
        _range=[0,len(ref)]
    if len(_range) == 1:
        _range.append(len(ref))
    for i in range(_range[0],_range[1]):
        scores.append(nubia.score(ref[i],cands[i]))
        print(i)
    with open("nubia_score/results.txt", "a") as f:
        for score in scores:
            f.write(str(score))
            f.write("\n")
    return scores
def compute_repet():
    import numpy as np
    ref=utils.load_preds("targets_egv_paper.txt")
    cands=utils.load_preds("preds_egv_paper.txt")
    return count_rep(cands)
def count_rep(cands):
    corpus_score=[]
    index=0
    for  c in cands:
        index+=1

        token_c=c.split()
        #print(token_c)
        token_uc=list(dict.fromkeys(token_c)) #delete duplicates
        #print(token_uc)
        
        length_c=len(token_c)
        penalty=(len(token_c) - len(token_uc)) / length_c
        #print(length_c)
        occurrence=0
        precision=0
        with open("repts.txt", "a") as f:
            for t in token_uc:
                rep=token_c.count(t)
                if rep > 1:
                    occurrence+=rep
            score=occurrence/length_c
            f.write(str(score) + "\n")
            corpus_score.append(score)
    return np.mean(corpus_score)


    
import sys
from collections import defaultdict

def ngrams(tokens, n):
  ngram = []
  for token in tokens:
    if len(ngram) < n:
      ngram.append(token)
    else:
      yield ngram
      ngram.pop(0)
      ngram.append(token)
  if len(ngram) == n:
    yield ngram

def count_ngrams(tokens, n):
  counts = defaultdict(int)
  for ngram in ngrams(tokens, n):
    counts[' '.join(ngram)] += 1
  return counts

def rr(tokens, max_n=4, window_size=1000):
  if len(tokens) < max_n or len(tokens) < window_size:
    raise Exception('Too few tokens, change window_size or max_n')

  result = 1.0

  for n in range(1, max_n+1):
    numerator = 0.0
    denominator = 0.0

    for window in ngrams(tokens, window_size):
      ngram_counts = count_ngrams(window, n)
      singletons = [ngram for ngram, count in ngram_counts.items() if count == 1]
      numerator += len(ngram_counts) - len(singletons)
      denominator += len(ngram_counts)
    result *= numerator / denominator

  return pow(result, 1.0/max_n)
  

def test_ngrams():
    s="ciao come stai io bene tu".split()
    #rr(s)
    cands=utils.load_preds("preds_egv_paper.txt")
    stt=""
    for c in cands:
        stt+=c + " "
    print(len(stt))
    print(rr(stt.split()))
    print(count_rep(stt))