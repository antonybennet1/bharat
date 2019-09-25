package com.wl.instamer.dao;

import com.wl.instamer.model.Detail;
import com.wl.instamer.model.MerchantOnboard;
import com.wl.instamer.model.MerchantOnboardResponseData;

public interface DetailDAO {
public void saveDetail(MerchantOnboard request, MerchantOnboardResponseData response);
public void saveContactDetail(MerchantOnboard request, MerchantOnboardResponseData response);
public void saveMobileDetail(MerchantOnboard request, MerchantOnboardResponseData response);
public void saveMerchantGroup(MerchantOnboard request, MerchantOnboardResponseData response);
public void saveMvisa(MerchantOnboard request, MerchantOnboardResponseData response);
public void updateDetails(Detail detail);
public boolean checkIfPhoneNumberAlreadyPresentInLoginTable(Long merMobileNumber, String bankCode);
public String createLogInInfo(long merchPhone, String tid, String bankCode);


}
