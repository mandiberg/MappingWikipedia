CREATE  TABLE ips ( ipkey INT NOT NULL AUTO_INCREMENT, ip_address VARCHAR(255) NULL, PRIMARY KEY (ipkey)) ENGINE = MyISAM DEFAULT CHARACTER SET = utf8mb4;

INSERT INTO ips (ip_address) select DISTINCT rev_user_text from revision where rev_user = 0;
ip_latitude
ip_longitude

ogr2ogr -f "MySQL" MYSQL:"testing,host=localhost,port=3306,user=root,password=Shinzizle.89" -nln "ipInfos" -a_srs "EPSG:4326" cb_2016_us_county_5m.shp -overwrite -addfields -fieldTypeToString All -lco ENGINE=MyISAM
