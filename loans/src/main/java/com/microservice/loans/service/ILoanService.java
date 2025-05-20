package com.microservice.loans.service;

import com.microservice.loans.dto.LoansDto;

public interface ILoanService {

    LoansDto createLoan(String mobileNumber);

    LoansDto fetchLoan(String mobileNumber);

    LoansDto updateLoan(LoansDto loansDto);

    void deleteLoan(String mobileNumber);
}
