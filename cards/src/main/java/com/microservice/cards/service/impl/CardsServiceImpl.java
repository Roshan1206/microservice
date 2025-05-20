package com.microservice.cards.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.microservice.cards.constant.CardsConstant;
import com.microservice.cards.dto.CardsDto;
import com.microservice.cards.entity.Cards;
import com.microservice.cards.exception.CardAlreadyExistException;
import com.microservice.cards.exception.ResourceNotFoundException;
import com.microservice.cards.mapper.CardMapper;
import com.microservice.cards.repository.CardsRepository;
import com.microservice.cards.service.ICardsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> card = cardsRepository.findByMobileNumber(mobileNumber);
        if (card.isPresent()) {
            throw new CardAlreadyExistException("Card already exist with the given mobile number : " + mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards card = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(mobileNumber));

        CardsDto cardsDto = CardMapper.cardToCardDto(card);
        return cardsDto;
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        boolean isUpdated = false;
        Cards card = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException(cardsDto.getCardNumber()));
        Cards updatedCard = CardMapper.cardDtoToCards(cardsDto, card);
        cardsRepository.save(updatedCard);
        isUpdated = true;
        return isUpdated;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        boolean isDeleted = false;
        Cards card = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(mobileNumber));
        cardsRepository.delete(card);
        isDeleted = true;
        return isDeleted;
    }

    private Cards createNewCard(String mobileNumber) {
        Long cardNumber = 1000000000000000L + new Random().nextLong(900000000000000L);

        Cards card = new Cards();
        card.setMobileNumber(mobileNumber);
        card.setCardNumber(Long.toString(cardNumber));
        card.setCardType(CardsConstant.CREDIT_CARD);
        card.setTotalAmount(CardsConstant.NEW_CARD_LIMIT);
        card.setAmountUsed(0);
        card.setAvailableAmount(CardsConstant.NEW_CARD_LIMIT);

        return card;
    }

}
