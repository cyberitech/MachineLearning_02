import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import matplotlib as mpl
infile="../../benchmark_results.csv"
df=pd.read_csv(infile)
df['efficiency']=df['score']/df['time-ns']



file_suffix = "comparison.png"
for problem in ["traveling-salesman","n-queens","count-ones","four-peaks"]:
    mean_score = df.query(f"problem == \"{problem}\"").groupby(['problem','algo'])['score'].mean().sort_values(ascending=True)
    mean_score.plot(ylabel='mean score',title=f'{problem} mean score by algo',figsize=(12,8))
    plt.savefig(f"images/{problem}-mean-score-{file_suffix}")
    plt.show()

    print(f"\n\n**Best Mean Score**\n{mean_score}")
    max_score = df.query(f"problem == \"{problem}\"").groupby(['problem','algo'])['score'].max().sort_values(ascending=True)
    max_score.plot(ylabel='max score', title=f'{problem} highest score by algo',figsize=(12,8))
    plt.savefig(f"images/{problem}-max-score-{file_suffix}")
    plt.show()
    print(f"\n\n**Best Max Score**\n{max_score}")
    mean_time = df.query(f"problem == \"{problem}\"").groupby(['problem','algo'])['time-ns'].mean().sort_values(ascending=True)
    mean_time.plot(ylabel='mean time', title=f'{problem} mean time by algo',figsize=(12,8))
    plt.savefig(f"images/{problem}-mean-time-{file_suffix}")
    plt.show()

    print(f"\n\n**Best Mean Speed**\n{mean_time}")
    min_time = df.query(f"problem == \"{problem}\"").groupby(['problem', 'algo'])['time-ns'].min().sort_values(
        ascending=True)
    min_time.plot(ylabel='min time', title=f'{problem} best time by algo',figsize=(12,8))
    plt.savefig(f"images/{problem}-max-time-{file_suffix}")
    plt.show()

    print(f"\n\n**Best Min Speed**\n{min_time}")
    mean_time = df.query(f"problem == \"{problem}\"").groupby(['problem','algo'])['efficiency'].mean().sort_values(ascending=False)
    mean_time.plot(ylabel='mean time', title=f'{problem} mean efficiency by algo',figsize=(12,8))
    plt.savefig(f"images/{problem}-mean-efficiency-{file_suffix}")
    plt.show()

    print(f"\n\n**Best Mean efficiency**\n{mean_time}")
    min_time = df.query(f"problem == \"{problem}\"").groupby(['problem', 'algo'])['efficiency'].max().sort_values(
        ascending=False)
    min_time.plot(ylabel='max efficiency', title=f'{problem} efficiency by algo',figsize=(12,8))
    plt.savefig(f"images/{problem}-max-efficiency-{file_suffix}")
    plt.show()

    print(f"\n\n**Best Min efficiency**\n{min_time}")




file_suffix="defaultparams.png"
#for problem in ["count-ones"]:
for problem in ["traveling-salesman","n-queens","count-ones","four-peaks"]:
    plt.close("all")
    for algo in ["rhc","genetic","mimic","annealing"]:
        fig, ((ax1,ax2),(ax3,ax4)) = plt.subplots(nrows=2, ncols=2,figsize=(12,12))
        fig.suptitle(f"Problem: {problem} Algorithm: {algo}")
        fig.set_size_inches(12, 12)
        zz=df.query(f"problem == \"{problem}\"").query(f"algo == \"{algo}\"")

        group1 = zz.groupby('train-iters')
        avg_x1 = group1['train-iters'].mean()
        avg_y1 = group1['score'].mean()
        ax1.scatter(x=avg_x1, y=avg_y1, color="b")

        ax1.set_title("Training Iterations vs Score")
        ax1.set_xlabel('training iterations')
        ax1.set_ylabel("score")

        group2 = zz.groupby('train-iters')
        avg_x2 = group2['time-ns'].mean()
        avg_y2 = group2['score'].mean()
        ax2.scatter(x=avg_x2, y=avg_y2, color="g")

        ax2.set_title("Training Time vs Score")
        ax2.set_xlabel('training time')
        ax2.set_ylabel("score")

        group3 = zz.groupby('problem-size')

        ax3.scatter(x=zz['problem-size'], y=zz['time-ns'], color="r")

        ax3.set_title("Problem Complexity vs Total Training Time")
        ax3.set_xlabel('complexity')
        ax3.set_ylabel("training time")

        group4 = zz.groupby('problem-size')
        ax4.scatter(x=zz['problem-size'], y=zz['efficiency'], color="m")

        ax4.set_title("Problem Complexity vs Training Efficiency\nEfficiency= score/train_time")
        ax4.set_xlabel('complexity')
        ax4.set_ylabel("efficiency")

        plt.show()
        fig.savefig(f"images/{problem}-{algo}-{file_suffix}")
