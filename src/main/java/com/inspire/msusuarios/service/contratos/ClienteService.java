package com.inspire.msusuarios.service.contratos;

import com.inspire.mscommon.criteria.modelCriteria.SearchRequest;
import com.inspire.mscommon.dto.response.EliminarResponse;
import com.inspire.mscommon.model.Transaccion;
import com.inspire.msusuarios.dto.request.ClienteRequest;
import com.inspire.msusuarios.model.usuarios.Cliente;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ClienteService {
    /**
     * Metodo para listar los clientes paginados.
     *
     * @param pagina   número de página a consultar
     * @param cantidad número de registros por página
     * @return Page<ClienteResponse> lista de clientes paginada
     */
    Page<Cliente> listarClientes(int pagina, int cantidad);

    /**
     * Metodo que obtiene un cliente por ID
     *
     * @param clienteId identificador de cliente
     * @return retorna un cliente
     */
    Cliente clienteUnico(String clienteId);

    /**
     * Metodo que crea cliente
     *
     * @param clienteRequest recibe un array de cliente request
     * @param transaction    recibe la información de la transacción
     * @return retorna un cliente responsé creado
     */
    Cliente crearCliente(@Valid ClienteRequest clienteRequest, Transaccion transaction);

    /**
     * Metodo que edita clientes
     *
     * @param clienteId      identificador del cliente
     * @param clienteRequest recibe un array de cliente request
     * @param transaccion    recibe la información de la transacción
     * @return retorna una lista con los clientes asignados
     */
    Cliente editarCliente(String clienteId, ClienteRequest clienteRequest, Transaccion transaccion);

    /**
     * Metodo que elimina cliente
     *
     * @param clienteId identificador del rol
     */
    EliminarResponse eliminarCliente(String clienteId);

    /**
     * Metodo que lista los clientes por criterios de busqueda
     *
     * @param searchRequest objeto que contiene los criterios de busqueda
     * @return retorna una lista con los clientes
     */
    Page<Cliente> listarSearchClientes(SearchRequest searchRequest);
}
