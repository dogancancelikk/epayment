create table TRANSFER (ID bigint identity primary key,
			SENDER_ACCOUNT_ID bigint,
                        RECEIVER_ACCOUNT_ID bigint,
                        AMOUNT NUMERIC(19,2),
                        DATE TIMESTAMP);

