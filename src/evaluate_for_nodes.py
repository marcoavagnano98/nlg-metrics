from vars import *
import os
def filter_by_node():

    PATH_F=os.path.join(PATH_D,"inputs_egv_paper.txt")
  
    with open(PATH_F, "r") as f:
        lines=f.readlines()
  #  count_nodes(lines)
    n_nodes=[]
    for line in lines:
        #square_occ=count_square(line)
        line=line.replace("]]]]]]","]").replace("]]]]]","]").replace("]]]]","]").replace("]]]","]").replace("]]","]")
        nodes=line.split("]")
        n_nodes.append(len(nodes) - 1) #cut \n character
    #print(len(lines[10].replace("[[","[").replace("]]]]]]","]").replace("]]]]]","]").replace("]]]]","]").replace("]]]","]").replace("]]","]").split("]")) - 1)
    PATH_O=os.path.join(PATH_D,"num_nodes_for_line.txt")
    with open(PATH_O,"a")as f:
        for node in n_nodes:
            f.write(str(node) + '\n')
        

    # print(len(f_list))
def count_square(line):
    from itertools import groupby
    groups = groupby(line)
    result = [(label, sum(1 for _ in group)) for label, group in groups]
    for r in result:
        if(r[0] == ']'):
            if max < r[1]:
                max=r[1]
    return max

def exec_metric_for_num_nodes(num_nodes=[1,2,3,4,5,6,7]):
    PATH_F=os.path.join(PATH_D,"num_nodes_for_line.txt")
    #METEOR
    aggregator=[]
    # with open("meteor/results.txt","r") as f:
    #     rouge1=f.readlines()
    with open("rouge/results-2.txt","r") as f:
        rouge2=f.readlines()
    with open("rouge/results-L.txt","r") as f:
        rougeL=f.readlines()
    with open("bleurt/results.txt","r") as f:
        bleurt=f.readlines()
    with open(PATH_F,"r") as f:
        lines=f.readlines()
    ind=0
    for nn in num_nodes:
        ex_index=[]
        ind=0
        for line in lines:
            if int(line)  == nn  and nn < 7:
                ex_index.append(ind)
            else:
                if nn >= 7:
                    if int(line) >= nn:
                        ex_index.append(ind)
            ind+=1
        print(calc_score("rouge2",ex_index,nn,rouge2))
        print(calc_score("rougeL",ex_index,nn,rougeL))
        print(calc_score("Bleurt",ex_index,nn,bleurt))
        print(calc_score("Bleu",ex_index,nn))

    
  


def calc_score(name,ex_index,num_nodes,lines=[]):
    scores=[]
    print(len(ex_index))
    if name == "Bleu":
        candidates=utils.load_preds("preds_egv_paper.txt")
        references=utils.load_preds("targets_egv_paper.txt")
        f_ref=[]
        f_cand=[]
    for c in ex_index:
        if name == "Bleu":
            f_ref.append(references[c])
            f_cand.append(candidates[c])
        else:
            scores.append(float(lines[c]))
    if name == "Bleu":
        return {name+"-" + str(num_nodes) : run_bleu(f_ref,f_cand)}
    return {name+"-" + str(num_nodes) : np.mean(scores)}
