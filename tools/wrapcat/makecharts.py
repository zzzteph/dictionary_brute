# -*- coding: utf-8 -*-

import matplotlib.pyplot as plt
import csv
import argparse
import os
import time

def make_charts(file, dir):
	if not os.path.exists(dir):
		os.makedirs(dir)

	dicts = []
	with open(file, 'rb') as f:
		reader = csv.reader(f)
		next(reader, None)
		for row in reader:
			dicts.append(row[0])

	for dict in set(dicts):
		x_labels = []
		y_labels = []
		with open(file, 'rb') as f:
			reader = csv.reader(f)
			next(reader, None)
			for row in reader:
				if row[0] == dict:
					if float(row[2]) != 0:
						x_labels.append(float(row[2]))
						y_labels.append(os.path.basename(row[1]))
		
		if y_labels and x_labels:
			y = range(len(y_labels))
			plt.style.use('ggplot')
			plt.xlabel('K1 - (recovered hashes / total hashes) / total variants')
			plt.ylabel('Rulesets')
			plt.title('Results for %s' % dict)
			width = 0.3
			plt.barh(y, x_labels, width, align='center', alpha=0.4, color="blue")
			plt.yticks(y, y_labels)
			plt.savefig("%s/%s.png" % (dir, os.path.basename(dict)), bbox_inches='tight', dpi=100)
			plt.close() 

def main():
	parser = argparse.ArgumentParser()
	parser.add_argument("--file", "-f", metavar='file', help="csv file", required=True)
	parser.add_argument("--output", "-o", help="output dir", required=True)
	args = parser.parse_args()

	make_charts(args.file, args.output)

if __name__ == '__main__':
	main()