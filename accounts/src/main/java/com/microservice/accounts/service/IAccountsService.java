package com.microservice.accounts.service;

import com.microservice.accounts.dto.ExistingCustomerDto;
import com.microservice.accounts.dto.NewCustomerDto;

public interface IAccountsService {

    ExistingCustomerDto createAccount(NewCustomerDto newCustomerDto);

    ExistingCustomerDto fetchAccountDetails(String mobileNumber);

    boolean updateAccount(ExistingCustomerDto updateExistingCustomerDto);

    boolean deleteAccount(String mobileNumber);

    boolean updateCommunicationStatus(Long accountNumber);
}
