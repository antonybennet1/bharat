package com.wl.upi.dao;

import com.wl.upi.model.FileDataDTO;
import com.wl.upi.util.QueryConstant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("reconDao")
public class ReconDaoImpl
  implements ReconDao
{
  @Autowired
  private JdbcTemplate jdbcTemplate;
  private static final Logger logger = LoggerFactory.getLogger(ReconDaoImpl.class);
  
  public List<FileDataDTO> getFileData(String bankCode, String fromDate, String toDate)
  {
    logger.info("Inside the getFileData method");
    
    List<FileDataDTO> list = new ArrayList();
    try
    {
      String query = "";
      List<Map<String, Object>> rows = null;
      if (bankCode != null)
      {
        query = QueryConstant.GET_RECON_DATA;
        logger.info("query : " + query);
        logger.info("parameter : " + bankCode + "|" + fromDate + "|" + toDate);
        rows = this.jdbcTemplate.queryForList(query, new Object[] { bankCode, fromDate, toDate });
        if (rows.size() != 0) {
          for (Map<String, Object> row : rows)
          {
            FileDataDTO fileDTO = new FileDataDTO();
            fileDTO.setMid((String)row.get("merch_id"));
            fileDTO.setTid((String)row.get("TID"));
            fileDTO.setRrn((String)row.get("txn_ref_no"));
            fileDTO.setTxnId((String)row.get("gateway_trans_id"));
            fileDTO.setTxnDate((Timestamp)row.get("txn_date"));
            fileDTO.setBusDate((Timestamp)row.get("txn_date"));
            fileDTO.setTxnAmount(((Double)row.get("amount")).doubleValue());
            fileDTO.setSetlAmount(((Double)row.get("amount")).doubleValue());
            fileDTO.setMcc(((Integer)row.get("merchant_category_code")).intValue());
            fileDTO.setRespCode((String)row.get("response_code"));
            fileDTO.setChannelFlag("QR");
            list.add(fileDTO);
          }
        }
        return list;
      }
      logger.info("bankCode in else : " + bankCode);
      return null;
    }
    catch (Exception e)
    {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
    return null;
  }
}
