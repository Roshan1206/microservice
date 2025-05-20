package com.microservice.loans.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.microservice.loans.constant.LoansConstant;
import com.microservice.loans.dto.LoansDto;
import com.microservice.loans.entity.Loans;
import com.microservice.loans.exception.LoanAlreadyExistsException;
import com.microservice.loans.exception.ResourceNotFoundException;
import com.microservice.loans.mapper.LoansMapper;
import com.microservice.loans.repository.LoansRepository;
import com.microservice.loans.service.ILoanService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private LoansRepository loansRepository;

    @Override
    public LoansDto createLoan(String mobileNumber) {
        Optional<Loans> existingLoan = loansRepository.findByMobileNumber(mobileNumber);
        if (existingLoan.isPresent()) {
            throw new LoanAlreadyExistsException(mobileNumber);
        }
        Loans loan = createNewLoan(mobileNumber);
        loansRepository.save(loan);
        return LoansMapper.loansToLoansDto(loan);
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loan = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(mobileNumber));
        return LoansMapper.loansToLoansDto(loan);
    }

    @Override
    public LoansDto updateLoan(LoansDto loansDto) {
        Loans loan = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException(loansDto.getLoanNumber()));

        loan.setLoanType(loansDto.getLoanType());
        loan.setMobileNumber(loansDto.getMobileNumber());
        loan.setAmountPaid(loansDto.getAmountPaid());
        loan.setOutstandingAmount(loansDto.getOutstandingAmount());
        loansRepository.save(loan);
        return LoansMapper.loansToLoansDto(loan);
    }

    @Override
    public void deleteLoan(String mobileNumber) {
        Loans loan = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(mobileNumber));
        loansRepository.delete(loan);
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans loan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        loan.setMobileNumber(mobileNumber);
        loan.setLoanNumber(Long.toString(randomLoanNumber));
        loan.setLoanType(LoansConstant.HOME_LOAN);
        loan.setTotalLoan(LoansConstant.NEW_LOAN_LIMIT);
        loan.setAmountPaid(0);
        loan.setOutstandingAmount(LoansConstant.NEW_LOAN_LIMIT);
        return loan;
    }
}
