-- ALTER TABLE ipinfos
-- ADD COLUMN `geoname_location` VARCHAR(255) NULL AFTER `ip_geonameId`,
-- ADD COLUMN `geoname_state` VARCHAR(10) NULL AFTER `geoname_location`,
-- ADD COLUMN `geoname_fips` VARCHAR(10) NULL AFTER `geoname_state`;

UPDATE new_ipinfos
LEFT JOIN
geocodes_to_FIPS_Faster ON geocodes_to_FIPS_Faster.geonameid = new_ipinfos.ip_geonameId
SET
geoname_location = geocodes_to_FIPS_Faster.name,
geoname_state= geocodes_to_FIPS_Faster.state,
geoname_fips = geocodes_to_FIPS_Faster.fips;
