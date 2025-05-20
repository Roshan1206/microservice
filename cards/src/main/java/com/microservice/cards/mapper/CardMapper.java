package com.microservice.cards.mapper;

import com.microservice.cards.dto.CardsDto;
import com.microservice.cards.entity.Cards;

public class CardMapper {
    public static Cards cardDtoToCards(CardsDto cardsDto, Cards card) {
        card.setMobileNumber(cardsDto.getMobileNumber());
        card.setCardNumber(cardsDto.getCardNumber());
        card.setCardType(cardsDto.getCardType());
        card.setAmountUsed(cardsDto.getAmountUsed());
        card.setAvailableAmount(cardsDto.getAvailableAmount());
        return card;
    }

    public static CardsDto cardToCardDto(Cards card) {
        return new CardsDto(card.getMobileNumber(), card.getCardNumber(), card.getCardType(), card.getTotalAmount(),
                card.getAvailableAmount(), card.getAmountUsed());
    }
}
