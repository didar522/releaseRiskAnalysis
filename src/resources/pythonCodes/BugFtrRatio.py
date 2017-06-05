import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import sys
import os

dir_path = os.path.dirname(os.path.realpath(__file__))
p = os.path.abspath(os.path.join(dir_path, os.pardir))
#p = os.path.abspath(os.path.join(p, os.pardir))
#p = os.path.abspath(os.path.join(p, os.pardir))
# print(p)

input_file = p + '/InputOutput/BugFtrRatio.csv'
output_file = p + '/pythonGraphs/BugFtrRatio.png'

df = pd.read_csv(input_file)

plt.rcParams["font.family"] = "serif"

fig1 = df.plot(kind='bar', legend=True, fontsize=25, x=df.columns.tolist()[0], figsize=(47,25))
fig1.set_axisbelow(True)
fig1.yaxis.grid() 

fig2 = df.plot(kind='bar', fontsize=25, legend=True, x=df.columns.tolist()[0], figsize=(47,25))
fig2.set_axisbelow(True)
fig2.yaxis.grid() 


title = 'Bug Feature Resolved ratio'
plt.title(title, size=35, fontweight="bold")
plt.xlabel(df.columns.tolist()[0], size=25, fontweight="bold")

plt.ylabel('Bug and Feature resolved', size=25, fontweight="bold")

plt.savefig(output_file)