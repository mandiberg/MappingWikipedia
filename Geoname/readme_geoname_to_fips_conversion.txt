Г1. Get US.txt from GeoNames.
|2. Rename the file to US.tsv.
|3. put US.tsv & fipscodes_states.csv & assign_fips_to_geoname.py in the same folder.
|4. run assign_fips_to_geoname.py:
|	open terminal and navigade to the folder that contains the files above by typing "cd path/to/directory/"
|	type "python assign_fips_to_geoname.py"
|5. you will get several files which are:
|	"geoname_to_fips_min.csv" --> this is the csv for successful fips
|	"geoname_to_fips_notFound.csv" --> this is the csv for not found fips
|	"geoname_table_sql.txt"  --> this is the sql ready values for not found fips
|															you will need to add sql queries to the top of the file and
|															save the file as geoname_table_sql.sql -- I already did that,
|															you can just skip these steps if you don't need to update
|	"geoname_table_sql_notfound.txt" --> this is the sql ready values for not found fips
|															you will need to add sql queries to the top of the file and
|															save the file as geoname_table_sql_notfound.sql -- I already did that,
|															you can just skip these steps if you don't need to update
|----> you don't need to go through these steps since I already have the sql queries ready:
						geoname_table_sql.sql
						geoname_table_sql_notfound.sql
						left_join.sql

If you already have the .sql files at hand you can start from here:
!important! remember to update the database name that you are using in the .sql files. (replace USE testing with USE yourdatabasename)
6. Open terminal: navigate to the folder that contains .sql Files
	type "cd path/to/directory/"
7. start mysql session in the terminal:
	type "mysql -u root -p"
	type your password
	now that you are in mysql
	type "\. geoname_table_sql.sql"
	then type "\. geoname_table_sql_notfound.sql"
	now that you have the geoname to fips lookup table ready we can join this table to our ipinfos table where the geonames match in both tables
	type "\. left_join.sql" ---> this part might take really long
