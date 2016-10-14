DROP TABLE REPORT_TEST_1 CASCADE CONSTRAINTS PURGE
/
CREATE TABLE REPORT_TEST_1
(
    col1                    CHAR(10) NOT NULL,
    col2                    VARCHAR2(10) NOT NULL,
    col3                    NUMBER(10) NOT NULL,
    col4                    NUMBER(10,2) NOT NULL,
    col5                    DATE NOT NULL,
    col6                    TIMESTAMP NOT NULL
)
/
