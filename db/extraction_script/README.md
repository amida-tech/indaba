# Extract single client data from database

1. Open `staging.sql` in you favorite code editor.
2. Replace `1` in `SET @org_id = 1;` to desired organization id and save file.
3. Run myslq shell ...
```
mysql
```
4. ... and create `staging` database.
```
CREATE DATABASE staging
```
5. Run `staging.sql`
```
\. staging.sql
```
6. Script will create copy of database structure with data belonged to a single organization.
7. You may dump `staging` database and restore it later.
```
mysqldump staging > staging.sql
```

Staging database will be recreated each run of script.

## Extract single client file paths

1. Open `attachment.sql`.
2. Replace `1` in `SET @org_id = 1;` to desired organization id and save file.
3. Run myslq shell.
4. Run `attachment.sql`.
5. The script will create a file at /tmp/attachments.csv. Each line has the file path of a file uploaded by a user of the selected organization.
You can change this file by editing the `INTO OUTFILE` field defined in `attachment.sql`.

