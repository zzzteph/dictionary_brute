#!/usr/bin/env python
# -*- coding: utf-8 -*-

__author__ = "@tsma11"
__license__ = "MIT"

import argparse
from subprocess import Popen, PIPE
from glob import glob
import os
import re
import logging
from tabulate import tabulate
import csv


class Wrapcat(object):
	def __init__(self, args):
		self.__bin = args.bin
		self.__dict = args.d
		self.__Dict = args.D
		self.__ruleset = args.r
		self.__Ruleset = args.R
		self.__hashfile = args.hash
		self.__mode = str(args.mode)
		self.__result = []
		self.__output = args.output
		if self.__output:
			with open(self.__output, 'w') as f:
				writer = csv.writer(f, delimiter=',')
				writer.writerow(['Dictionary', 'Ruleset', 'K1', 'K2', 'Total variants'])

	def start(self):
		logging.info("started hashfile: %s, mode: %s" % (self.__hashfile, self.__mode))
		if self.__Dict:
			for dict in glob("%s*" % self.__Dict):
				self.__dict = dict
				if self.__Ruleset:
					for ruleset in glob("%s*" % self.__Ruleset):
						self.__ruleset = ruleset
						self.run_hashcat()
				else:
					self.run_hashcat()
		else:
			if self.__Ruleset:
				for ruleset in glob("%s*" % self.__Ruleset):
					self.__ruleset = ruleset
					self.run_hashcat()
			else:
				self.run_hashcat()

		table = tabulate(sorted(self.__result, key=lambda tup: tup[2], reverse=True), headers=['Dictionary', 'Ruleset', 'K1', 'K2', 'Total variants'])

		logging.info("finished hashfile: %s, mode: %s\n%s" % (self.__hashfile, self.__mode, table))

	def run_hashcat(self):
		logging.debug("started dict: %s, ruleset: %s" % (self.__dict, self.__ruleset))

		cmd = [	self.__bin, 
				"-m", self.__mode,
				self.__hashfile,  
				self.__dict,
				"-r", self.__ruleset,
				"--machine-readable",
				"--potfile-disable"]

		proc = Popen(cmd, stdin=PIPE, stdout=PIPE, stderr=PIPE)
		out, err = proc.communicate()
		if err:
			logging.error("cmd: %s, \nerr: %s" % (cmd, err))
		else:
			logging.debug("cmd: %s, \nout: %s" % (cmd, out))
			if re.search("\tPROGRESS\t\d+\t\d+\tRECHASH\t\d+\t\d+\t", out):
				variants, total_variants, recovered_hashes, all_hashes = re.findall("\tPROGRESS\t(\d+)\t(\d+)\tRECHASH\t(\d+)\t(\d+)\t", out)[-1]
				if variants != total_variants:
					logging.error("Hmm... Something wrong, cmd: %s, \nout: %s" % (cmd, out))
				else:
					k1 = (float(recovered_hashes) / float(all_hashes)) / float(total_variants)
					k2 = float(recovered_hashes) / float(all_hashes)
					self.__result.append((self.__dict, self.__ruleset, k1, k2, total_variants))

					logging.debug("started dict: %s, ruleset: %s, K1 = %s, K2 = %s, total variants = %s " % (self.__dict, self.__ruleset, k1, k2, total_variants))

					if self.__output:
						with open(self.__output, 'a') as f:
							writer = csv.writer(f, delimiter=',')
							writer.writerow((self.__dict, self.__ruleset, '{0:.16f}'.format(k1), '{0:.16f}'.format(k2), total_variants))


def main():
	help = """Hashcat wrapper"""
	epilog = """Explanation of results:
  K1 - (recoverd hashes / total hashes) / total variants
  K2 - recoverd hashes / total hashes
\nAuthor: %s""" % __author__
	parser = argparse.ArgumentParser(description=help, epilog=epilog, formatter_class=argparse.RawTextHelpFormatter)
	parser.add_argument("--bin", "-b", metavar='bin', help="hashcat binary", required=True)
	parser.add_argument("--hash", metavar='file', help="hashfile", required=True)
	parser.add_argument("--mode", "-m", metavar='mode', help="hash mode", type=int, required=True)
	parser.add_argument("-d", metavar='file', help="dictionary file", required=False)
	parser.add_argument("-D", metavar='directory', help="dictionaries directory", required=False)
	parser.add_argument("-r", metavar='file', help="ruleset file", required=False)
	parser.add_argument("-R", metavar='directory', help="rulesets directory", required=False)
	parser.add_argument("--debug", help="enable debug logging", action='store_true', required=False)
	parser.add_argument("--log", help="logging to file app.log", action='store_true', required=False)
	parser.add_argument("--output", "-o", metavar='file', help="output to csv file", required=False)
	args = parser.parse_args()

	if args.debug:
		level = logging.DEBUG
	else:
		level = logging.INFO

	logFormatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
	rootLogger = logging.getLogger()
	rootLogger.setLevel(level)

	if args.log:
		fileHandler = logging.FileHandler("app.log")
		fileHandler.setFormatter(logFormatter)
		rootLogger.addHandler(fileHandler)

	consoleHandler = logging.StreamHandler()
	consoleHandler.setFormatter(logFormatter)
	rootLogger.addHandler(consoleHandler)
	
	if not (args.d or args.D):
		parser.error('Dictionary file or directory is not defined')

	if not (args.r or args.R):
		parser.error('Ruleset file or directory is not defined')

	if args.d and not os.path.isfile(args.d):
		parser.error('%s is not a file' % args.d)

	if args.D and not os.path.isdir(args.D):
		parser.error('%s is not a directory' % args.D)

	if args.r and not os.path.isfile(args.r):
		parser.error('%s is not a file' % args.r)

	if args.R and not os.path.isdir(args.R):
		parser.error('%s is not a directory' % args.R)

	Wrapcat(args).start()

if __name__ == '__main__':
	main()