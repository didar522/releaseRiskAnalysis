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

input_file = p + '/InputOutput/BugsIn7Days.csv'
output_file = p + '/pythonGraphs/BugsIn7Days.png'

df = pd.read_csv(input_file)

plt.rcParams["font.family"] = "serif"

fig = df.plot(kind='bar', fontsize=25, legend=False, x=df.columns.tolist()[0], figsize=(47,25))
fig.set_axisbelow(True)
fig.yaxis.grid() 

title = 'Bugs Reported within seven days'
plt.title(title, size=35, fontweight="bold")
plt.xlabel(df.columns.tolist()[0], size=25, fontweight="bold")

yaxis_label = df.columns.tolist()[1]
plt.ylabel(yaxis_label, size=25, fontweight="bold")

plt.savefig(output_file)