#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

with open(sys.argv[1], 'r') as f1:
	with open(sys.argv[2], 'r') as f2:
		same = set(f1).difference(f2)

with open('diff.txt', 'w') as outfile:
	for line in same:
		outfile.write(line)
