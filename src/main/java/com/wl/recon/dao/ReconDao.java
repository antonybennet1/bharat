package com.wl.recon.dao;

import java.util.List;

import com.wl.recon.model.FileDataDTO;


public interface ReconDao {

	public List<FileDataDTO> getFileData(String bankCode,String fromDate, String toDate);
}
