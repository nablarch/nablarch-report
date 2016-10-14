DROP USER nablarch_report CASCADE
;
DROP USER nablarch_report_master CASCADE
;
CREATE USER nablarch_report
      IDENTIFIED BY nablarch_report
             DEFAULT tablespace USERS
            TEMPORARY tablespace TEMP
;
CREATE USER nablarch_report_master
      IDENTIFIED BY nablarch_report_master
             DEFAULT tablespace USERS
            TEMPORARY tablespace TEMP
;
GRANT DBA TO nablarch_report,nablarch_report_master
;
