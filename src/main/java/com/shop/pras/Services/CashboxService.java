package com.shop.pras.Services;

import com.shop.pras.Models.Cashbox;
import com.shop.pras.Models.User;
import com.shop.pras.Repository.CashboxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashboxService {
    private final CashboxRepository cashboxRepository;

    public CashboxService(CashboxRepository cashboxRepository){
        this.cashboxRepository = cashboxRepository;
    }
    public List<Cashbox> getCashbox(String baseLogin) {
        return cashboxRepository.getCashbox(baseLogin);
    }
}
