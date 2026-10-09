/*
 * Copyright (c) 2025 Alireza Khodakarami
 *
 * Licensed under the MIT, (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://opensource.org/license/mit
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dynamero.shared.enumerations;

import java.util.function.BooleanSupplier;

public enum IndeterminateBoolean
{
    TRUE,
    FALSE,
    INDETERMINATE;

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    public boolean isIndeterminate() {
        return this == INDETERMINATE;
    }

    public boolean evaluate(boolean condition) {
        return evaluate(() -> condition);
    }

    public boolean evaluate(BooleanSupplier condition) {
        if (isIndeterminate())
            return condition.getAsBoolean();

        return isTrue();
    }
}