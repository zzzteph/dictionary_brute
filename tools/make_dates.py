#!/usr/bin/env python
# -*- coding: utf-8 -*-

## possible months patterns
## dd_month
## 2digit_month
## ddmmyy_month
## dd_month_yy
## ddyy_month
## dd_month_spec_char
## month_yy
## month_yy_spec_char
## month_4digits
## month_ddmmyy

## 4digits_month
## 3digits_month
## spec_char_4digits_month
## 1digit_spec_char_month
## spec_char_yy_month
## spec_char_yyyy_month
## yyyy_spec_char_month
## dst_dnd_drd_dth_ddst_ddnd_ddrd_ddth_month


import sys
import datetime

def get_date(date):
	return datetime.datetime.strptime(date, "%d%m%y").date()

start_date = get_date(sys.argv[1])
if start_date.year >= 2000:
	start_date = start_date.replace(year=start_date.year-100)
end_date = get_date(sys.argv[2])

## ddmmyy_month
# step = datetime.timedelta(days=1)

# with open("ddmmyy_month.rule", "a") as f:
# 	while start_date < end_date:
# 		ddmmyy_month = " ".join(['^%s' % i for i in list(start_date.strftime('%d%m%y'))[::-1]])
# 		f.write("%s\n" % ddmmyy_month)
# 		start_date += step

## month_ddmmyy
step = datetime.timedelta(days=1)

with open("month_ddmmyy.rule", "a") as f:
	while start_date < end_date:
		month_ddmmyy = " ".join(['$%s' % i for i in list(start_date.strftime('%d%m%y'))])
		f.write("%s\n" % month_ddmmyy)
		start_date += step

## dd_month_yy
# result = []
# step = datetime.timedelta(days=1)

# while start_date < end_date:
# 	tmp = start_date.strftime('%d-%y').split("-")
# 	dd_month_yy = " ".join(['^%s' % i for i in list(tmp[0][::-1])] + ['$%s' % i for i in list(tmp[1])])
# 	result.append(ddyy_month)
# 	start_date += step

# with open("dd_month_yy.rule", "a") as f:
# 	for i in sorted(list(set(result))):
# 		f.write("%s\n" % i)
		
## ddyy_month
# result = []
# step = datetime.timedelta(days=1)

# while start_date < end_date:
# 	tmp = start_date.strftime('%d-%y').split("-")
# 	ddyy_month = " ".join(['^%s' % i for i in list(tmp[1][::-1])] + ['^%s' % i for i in list(tmp[0][::-1])])
# 	result.append(ddyy_month)
# 	start_date += step

# with open("ddyy_month.rule", "a") as f:
# 	for i in sorted(list(set(result))): 
# 		f.write("%s\n" % i)
