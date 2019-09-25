package com.wl.bharatqr.util;
/**
 * 
 * @author ritesh.patil
 *
 */
public interface ReconConstants {
	
	String FILE_NAME_PREFIX = "RECON";
	String INPUT_DIR = "Input";
	String PROCESSED_DIR="Processed";
	String FAILED_DIR="Failed";
	String SUCCESS_DIR="Success";
	String VALIDATION_FAILED_DIR="Validation_failed";
	String XLSX_EXTENSION=".xlsx";
	String XLS_EXTENSION=".xls";
	String REFUND_SUCCESS_FILE="Refund-txn_detail_";
	String REFUND_FAILED_FILE="Refund-txn_failed_";
	String DATE_FORMAT_WITH_TIME="dd/MM/yyyy hh:mm:ss";
	String VALIDATION_FAILED_MSG="Hi SPOC, <br/> Please find attached Validation Failed file attached.";
	String VALIDATION_FAILED_SUBJECT="Validation Failed file";
	String SUCCESS_EXCECUTION_SUB="Recon Cron Job Excecution";
	String SUCCESS_EXCECUTION_MSG="Hi SPOC, <br/> Recon Cron Job Excecution has been done please go to SFTP location for download failed and success refund files. ";
	
	//constants added for Sbi Refund	
	String FILE_NAME_SBI_PREFIX = "SBI_BQRUPI_INB";
	String CSV_EXTENSION=".csv";
	
}
