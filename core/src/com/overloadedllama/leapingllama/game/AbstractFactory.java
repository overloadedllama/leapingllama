package com.overloadedllama.leapingllama.game;

import com.overloadedllama.leapingllama.LlamaConstants;
import com.overloadedllama.leapingllama.LlamaNumericConstants;
import com.overloadedllama.leapingllama.llamautils.LlamaUtil;

public class AbstractFactory implements LlamaConstants, LlamaNumericConstants {
    final LlamaUtil llamaUtil;

    public AbstractFactory(LlamaUtil llamaUtil) {
        this.llamaUtil = llamaUtil;
    }
}
