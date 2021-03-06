/*******************************************************************************
 * Copyright (c) 2017-2018 Aion foundation.
 *
 *     This file is part of the aion network project.
 *
 *     The aion network project is free software: you can redistribute it
 *     and/or modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation, either version 3 of
 *     the License, or any later version.
 *
 *     The aion network project is distributed in the hope that it will
 *     be useful, but WITHOUT ANY WARRANTY; without even the implied
 *     warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *     See the GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with the aion network project source files.
 *     If not, see <https://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Aion foundation.
 *     
 ******************************************************************************/

package org.aion.zero.impl.valid;

import org.aion.base.type.Hash256;
import org.aion.crypto.ISignature;
import org.aion.crypto.SignatureFac;
import org.aion.mcf.vm.types.DataWord;
import org.aion.zero.types.AionTransaction;
import org.aion.log.LogEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TXValidator {

    private static final Logger LOG = LoggerFactory.getLogger(LogEnum.TX.name());

    // TODO : MOVE ISignature to aionbase, and then use interface as an input
    public static boolean isValid(AionTransaction tx) {

        byte[] check = tx.getNonce();
        if (check == null || check.length > DataWord.BYTES) {
            LOG.error("invalid tx nonce!");
            return false;
        }

        check = tx.getTimeStamp();
        if (check == null || check.length > Long.BYTES) {
            LOG.error("invalid tx timestamp!");
            return false;
        }

        check = tx.getValue();
        if (check == null || check.length > DataWord.BYTES) {
            LOG.error("invalid tx value!");
            return false;
        }

        check = tx.getData();
        if (check == null) {
            LOG.error("invalid tx data!");
            return false;
        }

        long nrg = tx.getNrg();
        if (nrg < 0 || nrg > Long.MAX_VALUE) {
            LOG.error("invalid tx nrg!");
            return false;
        }

        nrg = tx.getNrgPrice();
        if (nrg < 0 || nrg > Long.MAX_VALUE) {
            LOG.error("invalid tx nrgprice!");
            return false;
        }

        byte[] hash = tx.getRawHash();
        if (hash == null || hash.length != Hash256.BYTES) {
            LOG.error("invalid tx raw hash!");
            return false;
        }

        ISignature sig = tx.getSignature();
        if (sig == null) {
            LOG.error("invalid tx signature!");
            return false;
        }

        try {
            return SignatureFac.verify(hash, sig);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
