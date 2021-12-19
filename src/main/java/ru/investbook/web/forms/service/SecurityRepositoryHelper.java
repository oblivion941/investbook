/*
 * InvestBook
 * Copyright (C) 2021  Vitalii Ananev <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.investbook.web.forms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.investbook.entity.SecurityEntity;
import ru.investbook.repository.SecurityRepository;
import ru.investbook.service.moex.MoexDerivativeCodeService;
import ru.investbook.web.forms.model.SecurityDescriptionModel;
import ru.investbook.web.forms.model.SecurityEventCashFlowModel;
import ru.investbook.web.forms.model.SecurityQuoteModel;
import ru.investbook.web.forms.model.SecurityType;
import ru.investbook.web.forms.model.TransactionModel;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SecurityRepositoryHelper {
    private final SecurityRepository securityRepository;
    private final MoexDerivativeCodeService moexDerivativeCodeService;

    /**
     * Generate securityId if needed, save security to DB, update securityId for model if needed
     * @return saved security id
     */
    public int saveAndFlushSecurity(SecurityDescriptionModel m) {
        int savedSecurityId = saveAndFlush(m.getSecurityId(), m.getSecurityIsin(), m.getSecurityName(), m.getSecurityType());
        m.setSecurityId(savedSecurityId);
        return savedSecurityId;
    }

    /**
     * Generate securityId if needed, save security to DB, update securityId for model if needed
     * @return saved security id
     */
    public int saveAndFlushSecurity(SecurityEventCashFlowModel m) {
        int savedSecurityId = saveAndFlush(m.getSecurityId(), m.getSecurityIsin(), m.getSecurityName(), m.getSecurityType());
        m.setSecurityId(savedSecurityId);
        return savedSecurityId;
    }

    /**
     * Generate securityId if needed, save security to DB, update securityId for model if needed
     * @return saved security id
     */
    public int saveAndFlushSecurity(SecurityQuoteModel m) {
        int savedSecurityId = saveAndFlush(m.getSecurityId(), m.getSecurityIsin(), m.getSecurityName(), m.getSecurityType());
        m.setSecurityId(savedSecurityId);
        return savedSecurityId;
    }

    /**
     * Generate securityId if needed, save security to DB, update securityId for model if needed
     * @return saved security id
     */
    public int saveAndFlushSecurity(TransactionModel m) {
        int savedSecurityId = saveAndFlush(m.getSecurityId(), m.getSecurityIsin(), m.getSecurityName(), m.getSecurityType());
        m.setSecurityId(savedSecurityId);
        return savedSecurityId;
    }

    /**
     * @return securityId from DB
     */
    private int saveAndFlush(Integer securityId, String isin, String securityName, SecurityType securityType) {
        SecurityEntity security = Optional.ofNullable(securityId)
                .flatMap(securityRepository::findById)
                .orElseGet(SecurityEntity::new);
        security.setType(securityType.toDbType());
        switch (securityType) {
            case SHARE, BOND -> {
                security.setIsin(isin);
                security.setName(securityName);
            }
            case DERIVATIVE, CURRENCY -> security.setTicker(moexDerivativeCodeService.convertDerivativeCode(securityName));
            case ASSET -> security.setName(securityName);
        }
        security = securityRepository.saveAndFlush(security);
        return security.getId();
    }
}
