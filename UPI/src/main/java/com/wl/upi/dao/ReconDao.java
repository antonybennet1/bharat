package com.wl.upi.dao;

import com.wl.upi.model.FileDataDTO;
import java.util.List;

public abstract interface ReconDao
{
  public abstract List<FileDataDTO> getFileData(String paramString1, String paramString2, String paramString3);
}
