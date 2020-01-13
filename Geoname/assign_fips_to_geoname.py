import csv
outputFile = open("geoname_to_fips_min2.csv", "w")
outputNotFound = open("geoname_to_fips_notFound.csv","w")
outputSQL = open("geoname_table_sql.txt", "w")
outputSQLNotFound = open("geoname_table_sql_notfound.txt", "w")

csvWriter = csv.writer(outputFile)
csvWriterNotFound = csv.writer(outputNotFound)
found = 0
notFound = 0
mydict = {}
fips = ""
with open("fipscodes_states.csv") as inputFIPSstate:
	readerInputFIPS = csv.reader(inputFIPSstate)
	mydict = dict((rows[2], rows[1]) for rows in readerInputFIPS)
	print mydict

with open("US.tsv") as fd:
		firstline = True
		rd = csv.reader(fd, delimiter="\t", quotechar='"')
		for row in rd:
			info = list(row)
			# if firstline:
			# 	print info
			# 	firstline = False
			# print info
			country = info[8]
			state = info[10]
			countyFips = info[11]
			print country+", "+state+", "+countyFips
			if country.lower() in "us":
				if countyFips:
					if state:
						if state in mydict:
							fips = mydict[state]+countyFips
							found = found+1
							info.append(fips)
							csvWriter.writerow([info[0], info[1], state, fips])
							outputSQL.write('('+info[0]+',"'+ info[1]+'","'+ state+'","'+ fips+'"),\n')
						else:
							fips = "State didn't match"
							notFound = notFound+1
							info.append(fips)
							csvWriterNotFound.writerow(info)
							outputSQLNotFound.write('('+info[0]+',"'+ info[1]+'","'+ state+'","'+ fips+'"),\n')

					else:
						fips = "State not found"
						notFound = notFound+1
						info.append(fips)
						csvWriterNotFound.writerow(info)
						outputSQLNotFound.write('('+info[0]+',"'+ info[1]+'","0","'+ fips+'"),\n')

				else:
					fips = "No county fips found"
					notFound = notFound+1
					info.append(fips)
					csvWriterNotFound.writerow(info)
					outputSQLNotFound.write('('+info[0]+',"'+ info[1]+'","'+ state+'","00000"),\n')

			# print fips

print found # 2228226
print notFound # 9240
