package com.microservice.loans.mapper;

import com.microservice.loans.dto.LoansDto;
import com.microservice.loans.entity.Loans;

public class LoansMapper {

    public static Loans loansDtoToLoans(LoansDto loansDto) {
        Loans loan = new Loans();
        loan.setMobileNumber(loansDto.getMobileNumber());
        loan.setLoanNumber(loansDto.getLoanNumber());
        loan.setLoanType(loansDto.getLoanType());
        loan.setTotalLoan(loansDto.getTotalLoan());
        loan.setAmountPaid(loansDto.getAmountPaid());
        loan.setOutstandingAmount(loansDto.getOutstandingAmount());

        return loan;
    }

    public static LoansDto loansToLoansDto(Loans loans) {
        LoansDto loanDto = new LoansDto();
        loanDto.setMobileNumber(loans.getMobileNumber());
        loanDto.setLoanNumber(loans.getLoanNumber());
        loanDto.setLoanType(loans.getLoanType());
        loanDto.setTotalLoan(loans.getTotalLoan());
        loanDto.setAmountPaid(loans.getAmountPaid());
        loanDto.setOutstandingAmount(loans.getOutstandingAmount());

        return loanDto;
    }
}
