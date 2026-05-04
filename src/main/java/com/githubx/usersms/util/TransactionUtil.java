package com.githubx.usersms.util;


import com.githubx.usersms.model.Transaction;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

public class TransactionUtil {
    private TransactionUtil() {
    }

    public static Transaction crearTransaccion(HttpServletRequest request, String idUsuario) {
        return Transaction.builder().trFecha(new Date()).trHost(request.getRemoteHost()).trUserId(idUsuario).build();
    }

    public static Transaction crearTransaccion(HttpServletRequest request) {
        return Transaction.builder().trFecha(new Date()).trHost(request.getRemoteHost()).build();
    }
}
