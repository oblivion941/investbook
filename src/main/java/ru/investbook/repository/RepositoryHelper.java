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

package ru.investbook.repository;

import org.hibernate.exception.ConstraintViolationException;

public class RepositoryHelper {

    /**
     * May return false positive result if NOT NULL column set by NULL
     */
    public static boolean isUniqIndexViolationException(Throwable t) {
        do {
            if (t instanceof ConstraintViolationException) {
                return true;
            }
        } while ((t = t.getCause()) != null);
        return false;
    }
}
