package com.pizzai.shared.enums;

import java.util.EnumSet;
import java.util.Set;

public enum StatusPedido {
    AGUARDANDO,
    EM_PREPARO,
    PRONTO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    private static final Set<StatusPedido> FINALIZADOS = EnumSet.of(ENTREGUE, CANCELADO);

    public boolean isFinalizado() {
        return FINALIZADOS.contains(this);
    }

    public boolean podeTransicionarPara(StatusPedido novo) {
        if (this == CANCELADO) return false;
        if (this == ENTREGUE) return false;
        if (novo == CANCELADO) return true;
        return switch (this) {
            case AGUARDANDO -> novo == EM_PREPARO;
            case EM_PREPARO -> novo == PRONTO;
            case PRONTO -> novo == SAIU_PARA_ENTREGA;
            case SAIU_PARA_ENTREGA -> novo == ENTREGUE;
            default -> false;
        };
    }
}
