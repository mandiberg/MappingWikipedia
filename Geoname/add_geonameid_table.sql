USE testing;
CREATE TABLE geocodes_to_FIPS (
		gid INT AUTO_INCREMENT,
		geonameid  INT NULL,
		name  VARCHAR(255) NULL,
		state VARCHAR(255) NULL,
		fips VARCHAR(255) NULL.
		PRIMARY KEY (geonameid)
);
INSERT INTO geocodes_to_FIPS (geonameid, name, state, fips)
VALUES ---add the values you have generated through python script here
