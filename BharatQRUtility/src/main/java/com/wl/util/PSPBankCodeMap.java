package com.wl.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


public class PSPBankCodeMap {

	private static HashMap<String, String> map = new HashMap<>();  
	static{
		map.put("00011","IDBIPSP");
		map.put("00004","CBIPSP");
		map.put("00031","AXISPSP");
		map.put("00006","BOIPSP");
		map.put("00075","SBIPSP");
		map.put("00045","YESPSP");
		map.put("00050","UBIPSP");
		map.put("00079","KOTAKPSP");
		map.put("00058","INDUSPSP");
		map.put("00003","CANARAPSP");
		map.put("00051","PNBPSP");
		map.put("00074","SIBPSP");
		map.put("00003","CANARAPSP");
		map.put("00049","OBCPSP");
		map.put("00041","DBCPSP");
		map.put("00001","BOBPSP");
		map.put("00055", "BANDHANPSP");
	}
	
	public static String getPspName(String bankCode)
	{
		return map.get(bankCode);
	}
	
	public static String getBankCode(String pspName)
	{
		Set<Entry<String, String>>  set = map.entrySet();
		for(Entry<String, String> ent : set)
		{
			if(pspName.equals(ent.getValue()))
				return ent.getKey();
		}
		return null;
	}
}
