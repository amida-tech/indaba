SET @org_id = 7; -- Carter Foundation

USE indaba;

SELECT file_path FROM attachment a
JOIN user u ON a.user_id = u.id
WHERE u.organization_id = @org_id
INTO OUTFILE '/tmp/attachments.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';
